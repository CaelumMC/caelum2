package io.github.caelummc.caelum.block;

import io.github.caelummc.caelum.Caelum;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.light.ChunkLightProvider;

public class MossyAerrackBlock extends Block implements Fertilizable {
    public MossyAerrackBlock(Settings settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockState above = world.getBlockState(pos.up());
        if (ChunkLightProvider.getRealisticOpacity(world, state, pos, above, pos.up(), Direction.UP, above.getOpacity(world, pos.up())) >= world.getMaxLightLevel() || world.getLightLevel(pos.up()) <= 1) {
            world.setBlockState(pos, Caelum.Blocks.AERRACK.getDefaultState());
        }
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        for (BlockPos ipos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            if (world.getBlockState(ipos).isOf(Blocks.WATER)) {
                world.setBlockState(ipos, this.getDefaultState(), Block.NOTIFY_ALL);
            }
        }
        for (BlockPos ipos : BlockPos.iterate(pos.add(-2, -2, -2), pos.add(2, 2, 2))) {
            if (world.isAir(ipos) && random.nextInt(4) == 0 && Caelum.Blocks.SKYMOSS_STALKS.getDefaultState().canPlaceAt(world, ipos)) {
                world.setBlockState(ipos, Caelum.Blocks.SKYMOSS_STALKS.getDefaultState().with(SkymossStalksBlock.AGE, random.nextInt(4) == 0 ? 1 : 0));
            }
        }
    }
}
