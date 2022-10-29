package io.github.caelummc.caelum.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.LocalRandom;

public class IslandPlacement {
    public static ChunkPos locateIsland(long seed, BlockPos from, int radius) {
        int chunkX = from.getX() >> 4;
        int chunkZ = from.getZ() >> 4;
        ChunkRandom random = new ChunkRandom(new LocalRandom(seed));

        for (int i = 0; i <= radius; i++) {
            for (int dx = -i; dx <= i; dx++) {
                boolean isXEdge = dx == -i || dx == i;

                for (int dz = -i; dz <= i; dz++) {
                    boolean isZEdge = dz == -i || dz == i;
                    if (isXEdge || isZEdge) {
                        ChunkPos chunkPos = searchInRegion(random, seed, chunkX, chunkZ, dx, dz);
                        if (chunkPos != null) {
                            return chunkPos;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static ChunkPos searchInRegion(ChunkRandom random, long seed, int centerX, int centerZ, int offsetX, int offsetZ) {
        final int regionSize = 40;
        final int separation = 10;

        int x = centerX + regionSize * offsetX;
        int z = centerZ + regionSize * offsetZ;
        int regionX = (x < 0 ? x - regionSize + 1 : x) / regionSize;
        int regionZ = (z < 0 ? z - regionSize + 1 : z) / regionSize;
        random.setRegionSeed(seed, regionX, regionZ, 0xCAE1CAE1);
        int startX = regionX * regionSize + (random.nextInt(regionSize - separation) + random.nextInt(regionSize - separation)) / 2;
        int startZ = regionZ * regionSize + (random.nextInt(regionSize - separation) + random.nextInt(regionSize - separation)) / 2;

        boolean hasIsland = random.nextInt(4) != 0;
        if (hasIsland) {
            return new ChunkPos(startX, startZ);
        }
        return null;
    }
}