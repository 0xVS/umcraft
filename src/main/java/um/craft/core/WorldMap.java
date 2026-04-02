package um.craft.core;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

//speichert die einzelnen blöcke eines chunks
class chunk {
    String[][] chunkBlocks;

    public chunk(String[][] chunkBlocks) {
        this.chunkBlocks = chunkBlocks;
    }
}


public class WorldMap {

    final static String[] BIOMES = {"PLAINS", "OCEANS", "DESERT"};

    //größe des zu ladenden chunks in x und y Richtung
    final static int CHUNKSIZE = 16;

    //Anzahl an chunks die geladen werden auf normaler map
    final int MAPSIZE = 128;


    String[][] chunkMap = new String[MAPSIZE][MAPSIZE];

    //Methode zum Generieren des einzelnen chunks
    public void chunkLoader() {

        //hier das Prüfen welche Biom in dem chunk enthalten sind und in welche Richtung
        //die chunks kombinieren
        for (int i = 0; i < CHUNKSIZE; i++) {
            for (int j = 0; j < CHUNKSIZE; j++) {

            }

        }
    }

    public static void beginningChunkLoad(){
        String[][] chunk = new String[CHUNKSIZE][CHUNKSIZE];

        int randomizer = ThreadLocalRandom.current().nextInt(0, BIOMES.length);;

        while (BIOMES[randomizer].equals("OCEANS")){
            randomizer = ThreadLocalRandom.current().nextInt(0, BIOMES.length);
        }

        String startingChunk = BIOMES[randomizer];



        chunk chunkBlox = new chunk(chunk);
    }


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

    public static void main(String[] args){
        beginningChunkLoad();
    }
}
