# Moto-Taxi Rush

Jeu d'arcade en portrait : une moto-taxi sur une avenue de ville au crépuscule, trois
voies, un trafic qui se densifie et des passagers à ramasser.

Java 17 + libGDX 1.14.2. Cibles : Android 7.0+ (APK) et application de bureau.

## Documentation

Les quatre volumes sont disponibles en PDF dans `docs/pdf/` :

| Volume | Contenu |
|---|---|
| [Présentation et règles](docs/pdf/01-presentation-et-regles.pdf) | Concept, univers, commandes, règles, difficulté, écrans |
| [Architecture technique](docs/pdf/02-architecture-technique.pdf) | Modules, classes, boucle de jeu, rendu pseudo-3D |
| [Installation et compilation](docs/pdf/03-installation-et-build.pdf) | Prérequis, commandes Gradle, APK, dépannage |
| [Assets, tests et qualité](docs/pdf/04-assets-tests-qualite.pdf) | Origine des fichiers, 22 tests, recette manuelle, limites |

Les sources HTML et le script de génération sont dans `docs/src/` et
`tools/build_docs.py` : `python3 tools/build_docs.py` régénère les PDF.

## Commandes

```
./gradlew :core:test              # tests unitaires
./gradlew lwjgl3:run              # version bureau
./gradlew android:assembleDebug   # APK dans android/build/outputs/apk/debug/
```

Les crédits et licences des assets sont dans [ASSETS.md](ASSETS.md).
