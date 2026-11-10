# Assets - Moto-Taxi Rush

| Fichier | Origine | Licence |
|---|---|---|
| `lwjgl3/src/main/resources/libgdx128.png`, `libgdx64.png`, `libgdx32.png`, `libgdx16.png` | Icones de fenetre libGDX (generees par gdx-liftoff) | Apache License 2.0 |
| `lwjgl3/src/main/java/cm/motorush/lwjgl3/StartupHelper.java` | gdx-liftoff (auteur : damios) | Apache License 2.0 |
| `assets/sprites/bike.png` | Cree pour ce projet avec `tools/generate_assets.py` | CC0 1.0 (domaine public) |
| `assets/sprites/car.png` | Cree pour ce projet avec `tools/generate_assets.py` | CC0 1.0 (domaine public) |
| `assets/sprites/truck.png` | Cree pour ce projet avec `tools/generate_assets.py` | CC0 1.0 (domaine public) |
| `assets/sprites/pothole.png` | Cree pour ce projet avec `tools/generate_assets.py` | CC0 1.0 (domaine public) |
| `assets/sprites/passenger.png` | Cree pour ce projet avec `tools/generate_assets.py` | CC0 1.0 (domaine public) |
| `assets/sprites/heart.png` | Cree pour ce projet avec `tools/generate_assets.py` | CC0 1.0 (domaine public) |
| `assets/sounds/crash.wav` | Synthetise pour ce projet avec `tools/generate_assets.py` | CC0 1.0 (domaine public) |
| `assets/sounds/pickup.wav` | Synthetise pour ce projet avec `tools/generate_assets.py` | CC0 1.0 (domaine public) |
| `assets/music/theme.wav` | Synthetise pour ce projet avec `tools/generate_assets.py` | CC0 1.0 (domaine public) |
| `assets/sprites/portrait.png` | Medaillon du menu, deduit de `tools/portrait_source.jpg` par `tools/make_portrait.py` | Photo personnelle de l'auteur, tous droits reserves |

Tous les assets du jeu sont originaux : aucun fichier tiers n'est importe.
La photo `tools/portrait_source.jpg` n'est utilisee que pour generer le
medaillon et n'est pas embarquee dans le jeu.
Le script `tools/generate_assets.py` (Python 3 + Pillow) reproduit les images et
les sons a l'identique. Pour les regenerer : `python3 tools/generate_assets.py`.
Le medaillon photo se regenere avec `python3 tools/make_portrait.py`.
