package io.github.caelummc.caelum.block;

import io.github.caelummc.caelum.Uplands;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SkymossBlock extends Block implements Fertilizable {
    protected static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 12, 16);

    public SkymossBlock(Settings settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.fullCube();
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }

    @Override
    @SuppressWarnings("deprecation")
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 0.2F;
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
            if (world.isAir(ipos) && random.nextBoolean() && Uplands.Blocks.SKYMOSS_STALKS.getDefaultState().canPlaceAt(world, ipos)) {
                world.setBlockState(ipos, Uplands.Blocks.SKYMOSS_STALKS.getDefaultState().with(SkymossStalksBlock.AGE, random.nextInt(2)));
            }
        }
    }
}
