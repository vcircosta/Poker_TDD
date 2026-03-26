# Texas Hold'em Poker Hand Evaluator - TDD Project

## 1. Présentation du Projet
Ce projet est un évaluateur et comparateur de mains de Poker (Texas Hold'em) développé en **Java 17** avec la méthodologie **TDD (Test-Driven Development)**. L'objectif est de déterminer la meilleure combinaison de 5 cartes pour chaque joueur parmi 7 disponibles et de désigner le(s) vainqueur(s).

## 2. Choix d'implémentation et Algorithme

### Sélection de la meilleure main (Best 5 out of 7)
Pour chaque joueur, le programme génère l'ensemble des **21 combinaisons** possibles de 5 cartes parmi les 7 à sa disposition (2 cartes privées + 5 cartes du board). Chaque combinaison est évaluée selon la hiérarchie standard, et seule la meilleure est conservée pour la comparaison finale.

### Ordre de tri du `chosen5` (Déterminisme)
Conformément à l'exigence 5.2 du sujet, les 5 cartes de la meilleure main sont retournées dans un ordre d'**importance décroissante** pour faciliter le tie-break:
* **Carré / Full / Brelan / Paire** : Les cartes formant la combinaison sont placées en tête, suivies des kickers par rang décroissant.
* **Quinte / Quinte Flush** : Triées du plus haut rang vers le plus bas.
    * *Cas particulier de la roue (A-2-3-4-5)* : Retournée dans l'ordre **5, 4, 3, 2, A** car le 5 est la carte de sommet.
* **Couleur / Hauteur** : Triées par rang décroissant.

### Validité des entrées
Nous avons choisi l'approche suivante concernant les données :
* **Hypothèse** : Le programme suppose qu'il n'y a **aucun doublon** dans les cartes fournies en entrée (jeu de 52 cartes standard).
* **Source de vérité** : Les catégories et priorités suivent strictement les règles de la page Wikipedia officielle.

## 3. Hiérarchie des mains (de la plus forte à la plus faible)
Le système gère l'intégralité des combinaisons :
1. Quinte Flush Royale
2. Four of a Kind (Carré)
3. Full House
4. Flush (Couleur)
5. Straight (Quinte) A-2-3-4-5 
6. Three of a Kind (Brelan)
7. Two Pair
8. One Pair
9. High Card

## 4. Tests et Couverture
Le projet a été développé de manière incrémentale. La suite de tests JUnit 5 couvre :
* La détection de chaque catégorie.
* Les règles de tie-break (départage par les kickers).
* La gestion des Split Pots (égalités parfaites).
* Le cas spécifique de la quinte "As-bas".

## 5. Lancement
### Prérequis
* Java 17
* Maven

### Commandes
* **Lancer les tests** : `mvn test`
* **Lancer une simulation aléatoire** : `mvn exec:java -Dexec.mainClass="poker.Main"`

---
*Projet réalisé par Elise LABARRERE et Valentin CIRCOSTA.*