package um.craft.core.WorldGen;

import java.util.concurrent.ThreadLocalRandom;

//speichert die einzelnen blöcke eines chunks
class Chunk {
    //ein Chunk besteht aus den Blöcken auf der x y und z koordinate
    //x und z sind hier jeweils 16 blöcke und y 384, da dies die höhe in mc wiedergibt
    String[][][] chunkBlocks;

    public Chunk(String[][][] chunkBlocks) {
        this.chunkBlocks = chunkBlocks;
    }
}


public class WorldMap {
    //Werte für die Generierung eines Chunks
    private static final int CHUNK_HEIGHT = 384;
    private static final int CHUNK_WIDTH = 16;
    private static final int SEA_LEVEL = 62;

    enum BIOME {
        OCEANS,
        SEA,
        PLAINS,
        BIRCH_FOREST,
        FOREST,
        SWAMP,
        BEACH,
        DESERT,
        TAIGA,
        DARK_FOREST,
        JUNGLE,
        SAVANNA,
        CHERRY_BLOSSOM;

        public static BIOME randomBiome(){
            BIOME[] biomes = values();
            for (int i = 0; i < biomes.length; i++) {
                System.out.println(biomes[i]);
            }
            int randomizer = ThreadLocalRandom.current().nextInt(2, biomes.length);
            return biomes[randomizer];
        }
    }

    //Anzahl an chunks die geladen werden auf normaler map
    final int MAPSIZE = 128;


    String[][] chunkMap = new String[MAPSIZE][MAPSIZE];

    //Methode zum Generieren des einzelnen chunks
    public void chunkLoader() {

        //hier das Prüfen welche Biom in dem chunk enthalten sind und in welche Richtung
        //die chunks kombinieren
        for (int i = 0; i < CHUNK_WIDTH; i++) {
            for (int j = 0; j < CHUNK_WIDTH; j++) {

            }

        }
    }

    public static Chunk beginningChunkLoad(){
        String[][][] tempchunk = new String[CHUNK_WIDTH][CHUNK_HEIGHT][CHUNK_WIDTH];

        BIOME startingBiome = BIOME.randomBiome();

        while (startingBiome.equals(BIOME.OCEANS)){
            startingBiome = BIOME.randomBiome();
        }

        System.out.println("[LOG] ---- startingChunk: " + startingBiome);

        //hier werden die Blöcke des startenden Chunks geladen


        return new Chunk(tempchunk);
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
        Chunk startingChunk = beginningChunkLoad();
    }
}
