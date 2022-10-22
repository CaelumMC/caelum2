package io.github.caelummc.caelum.block;

import io.github.caelummc.caelum.Caelum;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SkymossStalksBlock extends PlantBlock implements Fertilizable {
    protected static final VoxelShape SHAPE_STAGE0 = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 3.0D, 15.0D);
    protected static final VoxelShape SHAPE_STAGE1 = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 5.0D, 15.0D);
    public static final IntProperty AGE = Properties.AGE_1;

    public SkymossStalksBlock(Settings settings) {
        super(settings.offsetType(OffsetType.XZ).ticksRandomly());
        this.setDefaultState(this.getDefaultState().with(AGE, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.getBlock() == Caelum.Blocks.SKYMOSS
                || floor.getBlock() == Caelum.Blocks.MOSSY_AERRACK;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Vec3d vec3d = state.getModelOffset(world, pos);
        VoxelShape shape = state.get(AGE) == 0 ? SHAPE_STAGE0 : SHAPE_STAGE1;
        return shape.offset(vec3d.x, vec3d.y, vec3d.z);
    }


    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(AGE) == 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBaseLightLevel(pos, 0) >= 9 && state.get(AGE) == 0) {
            int neighbors = this.countGrownNeighbors(world, pos);
            if (neighbors < 5 && random.nextInt(10 + 5 * neighbors * neighbors) == 0) {
                world.setBlockState(pos, state.with(AGE, 1));
            }
        }
    }

    private int countGrownNeighbors(ServerWorld world, BlockPos pos) {
        int count = 0;
        for (BlockPos ipos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            BlockState istate = world.getBlockState(ipos);
            if (istate.getBlock() == this && istate.get(AGE) == 1) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return state.get(AGE) == 0;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return state.get(AGE) == 0;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state.with(AGE, 1));
    }
}
