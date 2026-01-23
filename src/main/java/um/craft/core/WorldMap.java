package um.craft.core;

public class WorldMap {

    //größe des zu ladenden chunks in x und y Richtung
    final int CHUNKSIZE = 16;

    //Anzahl an chunks die geladen werden auf normaler map
    final int MAPSIZE = 128;

    //speichert die einzelnen blöcke eines chunks
    String[][] chunk = new String[CHUNKSIZE][CHUNKSIZE];

    //Methode zum Generieren des einzelnen chunks
    public void chunkLoader() {

        //hier das Prüfen welche Biom in dem chunk enthalten sind und in welche Richtung
        //die chunks kombinieren
        for (int i = 0; i < chunk.length; i++) {
            for (int j = 0; j < chunk[i].length; j++) {

            }
            
        }
    }

    final String[] BIOMES = {"PLAINS", "OCEANS", "DESERT"};

    //soll ermitteln welches Biom in dem Chunk enthalten ist
    //ein Main Biom und eventuell neben Biom für einen möglichen Übergang
    public String biomeFinder() {

        return BIOMES[1];
    }

    //soll ermitteln, welches Biom neben dem ausgewählten chunk liegt
    public String nearBiomes() {

        return BIOMES[1];
    }

    //gibt die Richtung an, in der der ermittelte chunk an den ausgewählten anliegt
    public String direction() {

        return "west";
    }

}
