# umcraft

umcraft ist ein kleines Hobbyprojekt, um Game Development mit der Java LWJGL-Library zulernen.



## Rechtliches & Lizenz

### Lizenz
Dieses Projekt steht unter der **[GLP-3.0 Lizenz](./LICENSE)**.

#### Hinweis zu Minecraft
> Minecraft wird in diesem Projekt lediglich als Referenz für das Sandbox- und Block-Genre genannt. Dieses Projekt ist *kein* Minecraft-Klon, verwendet keine geschützten Spielinhalte (wie Texturen oder Sounds) und steht in keiner Verbindung zu Mojang Studios oder Microsoft. Es handelt sich um ein privates Lernprojekt ohne Anspruch auf Vollständigkeit oder Fehlerfreiheit.

## Aktueller Stand

- Fenster und OpenGL-3.2-Core-Kontext über GLFW/LWJGL
- First- und Third-Person-Kamera mit Maussteuerung

- Blockdefinitionen/Modelle werden aus JSON geladen
- Texturen werden aus PNG-Dateien geladen
- HUD mit Crosshair
- Weltgenerierung, Chunkbasiertes Rendering (16 × 64 × 16 Blöcke pro Chunk), Entities und Physik befinden sich im Aufbau

## Voraussetzungen

- JDK 17 oder neuer
- Maven (oder IntelliJ IDEA)
- GPU/Treiber mit OpenGL 3.2 Core

## Starten

1. Projekt in IntelliJ IDEA als Maven-Projekt öffnen.
2. `um.craft.core.Launcher` starten.
3. Alternativ nur bauen:
   ```bash
   mvn compile
 
Die Keybind-Einstellungen liegen in src/main/java/resources/keybinds.json,
Fenster- und Render-Einstellungen in src/main/java/resources/settings.json.

## Projektstruktur
 
 <!-- | Datei / Paket | Beschreibung | -->
| src/main/java/um/craft/core/  | Beschreibung |
| :--- | :--- |
| &nbsp;&nbsp;`window/` | Fenster und GLFW/OpenGL-Context |
| &nbsp;&nbsp;`input/` | Tastatur- und Maus-Input |
| &nbsp;&nbsp;`logic/` | Gameloop und PlayerController |
| &nbsp;&nbsp;`render/` | Shader, Meshes, Texturen, HUD, Szene |
| &nbsp;&nbsp;`world/` | Chunks, Blöcke und Welt |
| &nbsp;&nbsp;`worldgen/` | Weltgenerierung (WIP) |
| &nbsp;&nbsp;`entity/` | Entity-Grundgerüst (WIP) |
| &nbsp;&nbsp;`util/` | Ressourcen- und Modell-Loader |

| src/main/java/resources/  | Beschreibung |
| :--- | :--- |
| &nbsp;&nbsp;`assets/` | Models, Shader und Texturen |
| &nbsp;&nbsp;`*json/` | Einstellungen und Keybinds |


src/main/java/resources/
  assets/         Modelle, Shader und Texturen
  *.json          Einstellungen und Keybinds

## Plan
- Weltgenerierung mit Biomen
- TextureAtlas für effizienteres Chunk Rendering
- Entities, Physik und Inventar
