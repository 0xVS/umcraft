package um.craft.core.WorldGen;

import java.util.concurrent.ThreadLocalRandom;

//speichert die einzelnen blöcke eines chunks
class chunk {
    String[][] chunkBlocks;

    public chunk(String[][] chunkBlocks) {
        this.chunkBlocks = chunkBlocks;
    }
}


public class WorldMap {

    enum BIOME {
        PLAINS,
        OCEANS,
        DESERT,
        TAIGA,
        JUNGLE,
        CHERRY_BLOSSOM;

        public static BIOME randomBiome(){
            BIOME[] biomes = values();
            int randomizer = ThreadLocalRandom.current().nextInt(0, biomes.length);
            return biomes[randomizer];
        }
    }


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

    public static chunk beginningChunkLoad(){
        String[][] tempchunk = new String[CHUNKSIZE][CHUNKSIZE];

        BIOME startingBiome = BIOME.randomBiome();

        while (startingBiome.equals(BIOME.OCEANS)){
            startingBiome = BIOME.randomBiome();
        }

        System.out.println("[LOG] ---- startingChunk: " + startingBiome);

        //hier werden die Blöcke des startenden Chunks geladen


        return new chunk(tempchunk);
    }


    //soll ermitteln welches Biom in dem Chunk enthalten ist
    //ein Main Biom und eventuell neben Biom für einen möglichen Übergang
    public String biomeFinder() {

        return BIOME.randomBiome().toString();
    }

    //soll ermitteln, welches Biom neben dem ausgewählten chunk liegt
    public String nearBiomes() {

        return BIOME.randomBiome().toString();
    }

    //gibt die Richtung an, in der der ermittelte chunk an den ausgewählten anliegt
    public String direction() {

        return "west";
    }


    //zum Testen später static entfernen überall und als eine Instanz den Aufbau der startwelt bauen
    public static void main(String[] args){
        chunk startingChunk = beginningChunkLoad();
    }
}
