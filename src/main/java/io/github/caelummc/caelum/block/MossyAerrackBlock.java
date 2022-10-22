package io.github.caelummc.caelum.block;

import io.github.caelummc.caelum.Uplands;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.chunk.light.ChunkLightProvider;

public class MossyAerrackBlock extends Block {
    public MossyAerrackBlock(Settings settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockState above = world.getBlockState(pos.up());
        if (ChunkLightProvider.getRealisticOpacity(world, state, pos, above, pos.up(), Direction.UP, above.getOpacity(world, pos.up())) >= world.getMaxLightLevel() || world.getLightLevel(pos.up()) <= 1) {
            world.setBlockState(pos, Uplands.Blocks.AERRACK.getDefaultState());
        }
    }
}
