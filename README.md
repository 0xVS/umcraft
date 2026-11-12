# umcraft

umcraft ist ein Hobbyprojekt, um Game Development mit der Java LWJGL-Library zu lernen.

## Rechtliches & Lizenz

### Lizenz
Dieses Projekt steht unter der **[GLP-3.0 Lizenz](./LICENSE)**.

#### _Hinweis zu Minecraft_
Minecraft wird in diesem Projekt lediglich als Referenz für das Sandbox- und Block-Genre genannt. Dieses Projekt ist *kein* Minecraft-Klon, verwendet keine geschützten Spielinhalte (wie Texturen oder Sounds) und steht in keiner Verbindung zu Mojang Studios oder Microsoft. Es handelt sich um ein privates Lernprojekt ohne Anspruch auf Vollständigkeit oder Fehlerfreiheit.

## Aktueller Stand
- Fenster und OpenGL-3.2-Core-Kontext über GLFW/LWJGL
- First- und Third-Person Cam mit Maussteuerung
- Blockdefinitionen/Modelle werden aus JSON geladen
- Texturen werden aus PNG-Dateien geladen
- HUD mit Crosshair
- Weltgenerierung, Chunkbasiertes Rendering, Entities und Physik befinden sich im Aufbau

## Plan
- Weltgenerierung (Biome, Strukturen, ...)
- TextureAtlas für effizienteres Chunk Rendering
- Font Renderer
- Entities, Physik und Inventar


## Voraussetzungen

- JDK 17+
- Maven
- GPU/Treiber mit OpenGL 3.2

## Starten

1. Projekt in IntelliJ IDEA als Maven-Projekt öffnen.
2. `um.craft.core.Launcher` starten.
3. Alternativ nur bauen:
   ```bash
   mvn compile
 
Die Keybind-Einstellungen liegen in src/main/java/resources/keybinds.json,
Window- und Render-Einstellungen in src/main/java/resources/settings.json.

## Projektstruktur
 
| src/main/java/um/craft/core/  | Beschreibung |
| :--- | :--- |
| &nbsp;&nbsp;`window/` | Fenster + GLFW/OpenGL-Context |
| &nbsp;&nbsp;`input/` | Tastatur/Maus-Input |
| &nbsp;&nbsp;`logic/` | Gameloop + PlayerController |
| &nbsp;&nbsp;`render/` | Shader, Meshes, Texturen, HUD, Szene |
| &nbsp;&nbsp;`world/` | Chunks, Blöcke + Welt |
| &nbsp;&nbsp;`worldgen/` | Weltgenerierung (WIP) |
| &nbsp;&nbsp;`entity/` | Entity-Grundgerüst (WIP) |
| &nbsp;&nbsp;`util/` | Ressourcen- + Modell-Loader |

| src/main/java/resources/  | Beschreibung |
| :--- | :--- |
| &nbsp;&nbsp;`assets/` | Models, Shader + Texturen |
| &nbsp;&nbsp;`*json/` | Einstellungen + Keybinds |
