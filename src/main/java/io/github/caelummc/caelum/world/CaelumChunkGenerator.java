package io.github.caelummc.caelum.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.caelummc.caelum.Caelum;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;

public class CaelumChunkGenerator extends ChunkGenerator {
    public static final Codec<CaelumChunkGenerator> CODEC = RecordCodecBuilder
            .create(instance -> CaelumChunkGenerator.createStructureSetRegistryGetter(instance)
                    .and(// instance.group(
                            BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource))
                    //)
                    .apply(instance, instance.stable(CaelumChunkGenerator::new)));


    protected final OctavePerlinNoiseSampler noise;
    protected final long seed = 0;// FIXME use world seed!

    public CaelumChunkGenerator(Registry<StructureSet> structureSetRegistry, BiomeSource source) {
        super(structureSetRegistry, Optional.of(RegistryEntryList.of()), source);

        Random random = new LocalRandom(this.seed);

        this.noise = OctavePerlinNoiseSampler.create(random, IntStream.range(0, 4));
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public void carve(ChunkRegion region, long seed, NoiseConfig config, BiomeAccess biomes, StructureAccessor structures, Chunk chunk, GenerationStep.Carver step) {

    }

    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig config, Chunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;

        // TODO testing
        ChunkPos islandChunkPos = IslandPlacement.locateIsland(0, chunkPos.getCenterAtY(128), 4);
        if (islandChunkPos == null) {
            return;
        }
        ChunkRandom islandRandom = new ChunkRandom(new LocalRandom(this.seed));
        islandRandom.setRegionSeed(this.seed, islandChunkPos.x, islandChunkPos.z, 0xCAE1151E);
        BlockPos islandPos = islandChunkPos.getCenterAtY(128);

//        final int size = 65;
//        IslandBuilder builder = new IslandBuilder(size);
//        builder.generate(new java.util.Random(islandRandom.nextLong()));
//
//        BlockPos.Mutable pos = new BlockPos.Mutable();
//        for (int ix = 0; ix < size; ix++) {
//            for (int iy = 0; iy < size; iy++) {
//                for (int iz = 0; iz < size; iz++) {
//                    int x = islandPos.getX() - size / 2 + ix;
//                    int y = islandPos.getY() - size / 2 + iy;
//                    int z = islandPos.getZ() - size / 2 + iz;
//                    if (x >= chunkX * 16 && x < chunkX * 16 + 16
//                            && z >= chunkZ * 16 && z < chunkZ * 16 + 16) {
//                        pos.set(x, y, z);
//
//                    }
//                }
//            }
//        }

        final int horizontalCellSize = 4;
        final int verticalCellSize = 4;
        final int horizontalCellCount = 16 / horizontalCellSize;
        final int verticalCellCount = this.getWorldHeight() / verticalCellSize;
        final double horizontalNoiseScale = 1 / 64D;
        final double verticalNoiseScale = 1 / 48D;


        for (int cellX = 0; cellX < horizontalCellCount; cellX++) {
            for (int cellZ = 0; cellZ < horizontalCellCount; cellZ++) {
                for (int cellY = 0; cellY < verticalCellCount; cellY++) {
                    int cellX0 = (chunkX * 16 + cellX * horizontalCellSize);
                    int cellZ0 = (chunkZ * 16 + cellZ * horizontalCellSize);
                    int cellY0 = cellY * verticalCellSize;

                    int cellX1 = (chunkX * 16 + (cellX + 1) * horizontalCellSize);
                    int cellZ1 = (chunkZ * 16 + (cellZ + 1) * horizontalCellSize);
                    int cellY1 = (cellY + 1) * verticalCellSize;

                    double[] cornersNoise = {
                            this.getSample(cellX0, cellY0, cellZ0, horizontalNoiseScale, verticalNoiseScale),
                            this.getSample(cellX1, cellY0, cellZ0, horizontalNoiseScale, verticalNoiseScale),
                            this.getSample(cellX0, cellY1, cellZ0, horizontalNoiseScale, verticalNoiseScale),
                            this.getSample(cellX1, cellY1, cellZ0, horizontalNoiseScale, verticalNoiseScale),
                            this.getSample(cellX0, cellY0, cellZ1, horizontalNoiseScale, verticalNoiseScale),
                            this.getSample(cellX1, cellY0, cellZ1, horizontalNoiseScale, verticalNoiseScale),
                            this.getSample(cellX0, cellY1, cellZ1, horizontalNoiseScale, verticalNoiseScale),
                            this.getSample(cellX1, cellY1, cellZ1, horizontalNoiseScale, verticalNoiseScale),
                    };

                    boolean emptyCell = true;
                    for (int i = 0; i < 8; i++) {
                        if (cornersNoise[i] > 0) {
                            emptyCell = false;
                            break;
                        }
                    }
                    if (emptyCell) {
                        continue;
                    }

                    for (int ix = 0; ix < horizontalCellSize; ix++) {
                        for (int iz = 0; iz < horizontalCellSize; iz++) {
                            for (int iy = 0; iy < verticalCellSize; iy++) {
                                double blockNoise = MathHelper.lerp3(((float) ix) / horizontalCellSize, ((float) iy) / verticalCellSize, ((float) iz) / horizontalCellSize,
                                        cornersNoise[0], cornersNoise[1], cornersNoise[2], cornersNoise[3],
                                        cornersNoise[4], cornersNoise[5], cornersNoise[6], cornersNoise[7]);

                                if (blockNoise > 0) {
                                    int x = chunkX * 16 + cellX * horizontalCellSize + ix;
                                    int y = cellY * verticalCellSize + iy;
                                    int z = chunkZ * 16 + cellZ * horizontalCellSize + iz;
                                    pos.set(x, y, z);

                                    chunk.setBlockState(pos, Caelum.Blocks.AERRACK.getDefaultState(), false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private double getSample(int x, int y, int z, double horizontalNoiseScale, double verticalNoiseScale) {
        double noise = this.noise.sample(x * horizontalNoiseScale, y * verticalNoiseScale, z * horizontalNoiseScale) + 0.25F;

        BlockPos island = IslandPlacement.locateIsland(0, new BlockPos(x, y, z), 4).getCenterAtY(128);

        int distSq = (island.getX() - x) * (island.getX() - x) + (island.getZ() - z) * (island.getZ() - z);
        noise -= MathHelper.clamp(distSq - 32 * 32, 0, 1024F) / 1024F;

        final int centerY = 128;
        float distForCenter = Math.abs(y - centerY);
        noise -= Math.max(0, distForCenter - 32) / 32;

        return noise;
    }

    @Override
    public void populateEntities(ChunkRegion region) {

    }

    @Override
    public int getWorldHeight() {
        return 256;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, NoiseConfig config, StructureAccessor structures, Chunk chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getMinimumY() {
        return 0;
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig config) {
        return world.getBottomY();
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig config) {
        BlockState[] states = new BlockState[256];
        Arrays.fill(states, Blocks.AIR.getDefaultState());
        return new VerticalBlockSample(0, states);
    }

    @Override
    public void getDebugHudText(List<String> text, NoiseConfig config, BlockPos pos) {

    }
}
