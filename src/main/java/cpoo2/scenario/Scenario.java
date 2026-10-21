package cpoo2.scenario;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cpoo2.jeu.Couleur;
import cpoo2.jeu.Coup;
import cpoo2.jeu.Plateau;
import cpoo2.jeu.Position;

/**
 * Un scénario : une position de départ, l'équipe au trait, et des instructions. Il se lit dans
 * un texte (fourni : ce n'est pas l'objet de la séance), et se parcourt avec un visiteur.
 *
 * <pre>
 * # la première attaque bleue
 * départ officiel
 * bleus poussée c5-d4
 * rouges déplacement c2-c3
 * vérifier ballon en e3 et trait aux bleus
 * </pre>
 *
 * <p>Une ligne qui commence par {@code #} est un commentaire. La première ligne utile est
 * {@code départ officiel}, ou {@code départ} suivi d'une position en une ligne
 * ({@link Plateau#enLigne()}) et de l'équipe au trait.</p>
 *
 * <p>Avant le départ, un scénario peut porter un <b>en-tête</b> : des métadonnées, une par ligne,
 * {@code [Clé "valeur"]}, comme l'en-tête d'une partie d'échecs au format PGN. Le lecteur les
 * garde telles quelles, dans l'ordre ({@link #entete()}), sans les interpréter.</p>
 *
 * <pre>
 * [Règles "officielles"]
 * [Résultat "bleus"]
 * départ officiel
 * bleus déplacement e5-e4
 * </pre>
 */
public record Scenario(Plateau depart, Couleur trait, List<Instruction> instructions, Map<String, String> entete) {

    public Scenario {
        instructions = List.copyOf(instructions);
        entete = Collections.unmodifiableMap(new LinkedHashMap<>(entete));
    }

    /** Un scénario sans en-tête. */
    public Scenario(Plateau depart, Couleur trait, List<Instruction> instructions) {
        this(depart, trait, instructions, Map.of());
    }

    /** Le visiteur passe sur chaque instruction, dans l'ordre : c'est la structure d'objets du Visiteur. */
    public void parcourir(VisiteurDeScenario visiteur) {
        for (Instruction i : instructions) {
            i.accept(visiteur);
        }
    }

    // ---------- la lecture (fournie) ----------

    /**
     * Lit un scénario rangé dans les ressources du projet ({@code src/main/resources}), par exemple
     * {@code Scenario.charger("parties/entrainement.partie")}.
     */
    public static Scenario charger(String ressource) {
        try (InputStream in = Scenario.class.getClassLoader().getResourceAsStream(ressource)) {
            if (in == null) throw new IllegalArgumentException("ressource introuvable : " + ressource);
            return lire(new String(in.readAllBytes(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static final Pattern METADONNEE = Pattern.compile("\\[(\\S+)\\s+\"(.*)\"\\]");

    public static Scenario lire(String texte) {
        List<String> lignes = new ArrayList<>();
        for (String l : texte.split("\\R")) {
            l = l.strip();
            if (!l.isEmpty() && !l.startsWith("#")) lignes.add(l);
        }
        Map<String, String> entete = new LinkedHashMap<>();
        while (!lignes.isEmpty() && lignes.get(0).startsWith("[")) {
            String ligne = lignes.remove(0);
            Matcher m = METADONNEE.matcher(ligne);
            if (!m.matches()) throw new IllegalArgumentException("métadonnée attendue comme [Clé \"valeur\"] : " + ligne);
            entete.put(m.group(1), m.group(2));
        }
        if (lignes.isEmpty() || !lignes.get(0).startsWith("départ")) {
            throw new IllegalArgumentException("un scénario commence par « départ officiel »");
        }
        String[] d = lignes.get(0).split("\\s+");
        Plateau depart;
        Couleur trait;
        if (d.length == 2 && d[1].equals("officiel")) {
            depart = Plateau.officiel();
            trait = Couleur.BLEUS;
        } else if (d.length == 3) {
            depart = Plateau.depuisLigne(d[1]);
            trait = equipe(d[2]);
        } else {
            throw new IllegalArgumentException("départ illisible : " + lignes.get(0));
        }
        List<Instruction> instructions = new ArrayList<>();
        for (String l : lignes.subList(1, lignes.size())) {
            instructions.add(instruction(l));
        }
        return new Scenario(depart, trait, instructions, entete);
    }

    static Instruction instruction(String ligne) {
        String[] m = ligne.split("\\s+");
        if (m[0].equals("vérifier")) {
            return new Verifier(condition(Arrays.asList(m).subList(1, m.length)));
        }
        if (m.length != 3) throw new IllegalArgumentException("ligne illisible : " + ligne);
        Couleur equipe = equipe(m[0]);
        String[] cases = m[2].split("-");
        if (cases.length != 2) throw new IllegalArgumentException("coup illisible : " + ligne);
        Position de = Position.of(cases[0]);
        Position vers = Position.of(cases[1]);
        Instruction coup = switch (m[1]) {
            case "déplacement" -> new Deplacer(equipe, de, vers);
            case "poussée" -> new Pousser(equipe, de, vers);
            case "tacle" -> new Tacler(equipe, de, vers);
            case "saut" -> new Sauter(equipe, de, vers);
            default -> throw new IllegalArgumentException("sorte de coup inconnue : " + m[1]);
        };
        try {
            Coup.lire(m[1] + " " + m[2]);   // la géométrie se vérifie à la lecture : « déplacement e5-e3 » n'est pas un coup
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("coup illisible : " + ligne + " (" + e.getMessage() + ")");
        }
        return coup;
    }

    /** {@code condition := facteur ("et" facteur)*}, associatif à gauche. */
    static Condition condition(List<String> mots) {
        List<List<String>> facteurs = new ArrayList<>();
        List<String> courant = new ArrayList<>();
        for (String mot : mots) {
            if (mot.equals("et")) {
                facteurs.add(courant);
                courant = new ArrayList<>();
            } else {
                courant.add(mot);
            }
        }
        facteurs.add(courant);
        Condition c = facteur(facteurs.get(0));
        for (List<String> f : facteurs.subList(1, facteurs.size())) {
            c = new Et(c, facteur(f));
        }
        return c;
    }

    static Condition facteur(List<String> m) {
        if (!m.isEmpty() && m.get(0).equals("non")) return new Non(facteur(m.subList(1, m.size())));
        String t = String.join(" ", m);
        if (m.size() == 3 && t.startsWith("ballon en ")) return new BallonEn(Position.of(m.get(2)));
        if (m.size() == 3 && t.startsWith("trait aux ")) return new TraitAux(equipe(m.get(2)));
        if (m.size() == 2 && m.get(0).equals("vainqueur")) return new Vainqueur(equipe(m.get(1)));
        throw new IllegalArgumentException("condition illisible : " + t);
    }

    static Couleur equipe(String mot) {
        return switch (mot) {
            case "bleus" -> Couleur.BLEUS;
            case "rouges" -> Couleur.ROUGES;
            default -> throw new IllegalArgumentException("équipe attendue, bleus ou rouges : " + mot);
        };
    }

    static String nom(Couleur equipe) { return equipe == Couleur.BLEUS ? "bleus" : "rouges"; }
}
