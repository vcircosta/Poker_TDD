# Poker TDD

Poker Texas Hold'em simplifié en Java, développé en TDD.

## Structure

```
src/
├── main/java/poker/
│   ├── Carte.java         # Valeur (2→A) + Couleur (♥♦♣♠)
│   ├── Combinaison.java   # Détection et comparaison des mains
│   ├── Partie.java        # Déroulement d'une partie
│   └── Main.java          # Point d'entrée
└── test/java/poker/
    ├── CarteTest.java
    ├── CombinaisonTest.java
    └── PartieTest.java
```

## Règles implémentées

9 combinaisons (de la plus haute à la plus basse) :

| Rang | Combinaison   |
|------|---------------|
| 9    | Quinte flush  |
| 8    | Carré         |
| 7    | Full          |
| 6    | Couleur       |
| 5    | Quinte        |
| 4    | Brelan        |
| 3    | Double paire  |
| 2    | Paire         |
| 1    | Carte haute   |

Chaque joueur reçoit 2 cartes + 5 cartes communes = meilleure main de 5 parmi 7.

## Lancer le projet

**Compiler**
```bash
javac -d out src/main/java/poker/*.java
```

**Jouer** (3 joueurs par défaut, ou passer un nombre en argument)
```bash
java -cp out poker.Main
java -cp out poker.Main 4
```

**Lancer les tests** (nécessite `lib/junit-standalone.jar`)
```bash
javac -cp lib/junit-standalone.jar src/main/java/poker/*.java src/test/java/poker/*.java -d out
java -jar lib/junit-standalone.jar --class-path out --scan-class-path
```

Télécharger le jar JUnit si absent :
```bash
curl -sL "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar" -o lib/junit-standalone.jar
```
