package io.github.caelummc.caelum.block;

import io.github.caelummc.caelum.Uplands;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class AerrackBlock extends Block implements Fertilizable {
    public AerrackBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        if (!world.getBlockState(pos.up()).isTranslucent(world, pos)) {
            return false;
        }
        for (BlockPos ipos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            BlockState istate = world.getBlockState(ipos);
            if (istate.getBlock() == Uplands.Blocks.MOSSY_AERRACK || istate.getBlock() == Uplands.Blocks.SKYMOSS) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        world.setBlockState(pos, Uplands.Blocks.MOSSY_AERRACK.getDefaultState(), 3);
    }
}
