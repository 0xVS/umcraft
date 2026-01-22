package um.craft.core;

public class WorldMap {

    //größe des zu ladenen chunks in x und y Richtung
    final int CHUNKSIZE = 16;

    //anzahl an chunks die geladen werden auf normaler map
    final int MAPSIZE = 128;

    //speichert die einzelnen blöcke eines chunks
    String[][] chunk = new String[CHUNKSIZE][CHUNKSIZE];

    //Methode zum generieren des einzelnen chunks
    public void chunkLoader() {

        //hier das prüfen welche Biome in dem chunk enthalten sind und in welche Richtung
        //die chunks kombinieren
        for (int i = 0; i < chunk.length; i++) {
            for (int j = 0; j < chunk[i].length; j++) {

            }
            
        }
    }

    final String[] BIOMES = {"PLAINS", "OCEANS", "DESERT"};

    public String biomeFinder() {

        return BIOMES[1];
    }
}
