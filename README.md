# CPOO2, séance 4 : un lecteur de parties

*Le code de la séance 4 de **CPOO2** (INSA Rennes, 4INFO, 2026-2027) : un
lecteur de parties de ChessBall, à écrire en suivant le sujet, distribué à
part.*

Java 21 et Maven, rien d'autre : JUnit 5 est la seule dépendance.

```bash
git clone https://github.com/acherm/cpoo2-s04-lecteur.git
cd cpoo2-s04-lecteur
mvn test                    # rouge au départ : c'est l'énoncé
mvn -q compile exec:java    # le lecteur, sur les six parties de la boîte
mvn -q compile exec:java -Dexec.args="entrainement --chercher ballon en e4"
```

Tant qu'une question n'est pas écrite, le lecteur l'affiche à sa place :
`pas encore écrit (TODO S04 Qn)`.

## Ce qu'il y a dedans

```text
src/main/java/cpoo2/
  lecteur/LecteurDeParties     ← le lecteur, étape par étape (Q2, Q3, Q5)
  scenario/                    ← une partie lue : ses instructions, ses conditions, ses visiteurs
    Deplacer, Pousser, Tacler, Sauter, Verifier ← Q1 : accept
    Executeur                  ← Q1 : rejouer
    Statistiques               ← Q3 : résumer, sans jouer
    BallonEn, TraitAux, Vainqueur, Et, Non ← Q4 : les conditions s'évaluent
    Afficheur                  ← fourni : le modèle des Q1 et Q3
    Scenario, Instruction, Condition, VisiteurDeScenario, Exemples ← fournis
    SurchargeDemo              ← à exécuter (Q8)
  motifs/LecteurParMotifs      ← Q10 : le même lecteur, sans visiteur
  jeu/                         ← fourni : la partie, son plateau, ses coups
src/main/resources/parties/    ← six parties enregistrées
lib/                           ← l'arbitre du ChessBall de la boîte, compilé : Maven le trouve seul
rendus/s04-lecteur.puml        ← Q6 : le point de départ de votre diagramme (PlantUML Studio)
rendus/s04.md                  ← vos réponses (Q6 à Q11), à compléter pour le rendu
```

## Les tests

| Suite | Questions | Tests | Rouges au départ |
|---|---|---|---|
| `cpoo2.scenario.ScenarioTest` | fourni | 5 | 0 |
| `cpoo2.scenario.VisiteursTest` | Q1, Q3 | 5 | 5 |
| `cpoo2.scenario.InterpreteurTest` | Q4 (et Q1) | 5 | 5 |
| `cpoo2.lecteur.LecteurDePartiesTest` | Q2, Q3, Q5 | 5 | 5 |
| `cpoo2.motifs.MotifsTest` | Q10 | 5 | 5 |

```bash
mvn test -Dtest=VisiteursTest                          # une seule suite
mvn test -Dmaven.test.failure.ignore=true              # rouge, mais le tableau complet
mvn -q compile exec:java -Dexec.mainClass=cpoo2.motifs.LecteurParMotifs   # Q10
java -cp target/classes cpoo2.scenario.SurchargeDemo                     # Q8
```

## Le rendu

Dans votre propre dépôt, sur le GitLab de l'INSA : la dernière section du
sujet, « Le rendu », donne les commandes.
