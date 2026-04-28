package com.codex.composer.api.v1.block;

import com.codex.composer.api.v1.block.entity.AbstractPlushieBlockEntity;
import com.codex.composer.internal.registry.ModStatistics;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//? if minecraft: >=1.21
import net.minecraft.state.property.EnumProperty;

//? if minecraft: <=1.21 {
/*import net.minecraft.world.WorldAccess;
import net.minecraft.state.property.DirectionProperty;
*///? } else {
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import net.minecraft.util.math.random.Random;
//? }

//? if minecraft: <=1.20.4 {
/*import net.minecraft.util.Hand;
@SuppressWarnings("deprecation")
*///? }
public abstract class AbstractPlushieBlock extends BlockWithEntity implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final /*? if minecraft: <=1.20.6 { *//*DirectionProperty*//*? } else {*/ EnumProperty<Direction> /*?}*/ FACING = Properties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = createCuboidShape(3.0, 0.0, 3.0, 13.0, 15.0, 13.0);

    public AbstractPlushieBlock(Settings settings) {
        super(settings);
    }

    protected abstract void playSound(World world, BlockState state, BlockPos pos, PlayerEntity player);
    protected abstract BlockEntityType<AbstractPlushieBlockEntity> getType();
    protected Identifier boopStat() {
        return ModStatistics.PLUSH_BOOP;
    }

    @Override
    //? if minecraft: <=1.20.4 {
    /*public ActionResult onUse(BlockState state, @NotNull World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
     *///? } else {
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        //?}
        if (!world.isClient/*? if minecraft: >=1.21.9 { *//*()*//*?}*/) {
            playSound(world, state, pos, player);

            if (world.getBlockEntity(pos) instanceof AbstractPlushieBlockEntity plushie) plushie.squish(1);
            player.incrementStat(boopStat());
        }
        return ActionResult.SUCCESS;
    }

    public BlockRenderType getRenderType(BlockState state) {
        //? if minecraft: <=1.20.6 {
        /*return BlockRenderType.ENTITYBLOCK_ANIMATED;
         *///? } else {
        return BlockRenderType.INVISIBLE;
        //?}
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (!world.isClient/*? if minecraft: >=1.21.9 { *//*()*//*?}*/) {
            if (world.getBlockEntity(pos) instanceof AbstractPlushieBlockEntity plushie) plushie.squish(24);
        }
    }

    @Override
    protected void spawnBreakParticles(World world, PlayerEntity player, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) instanceof AbstractPlushieBlockEntity plushie) plushie.squish(4);
        super.spawnBreakParticles(world, player, pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return /*? if minecraft: <=1.20.1 { *//*checkType*//*? } else {*/validateTicker/*?}*/(type, getType(), AbstractPlushieBlockEntity::tick);
    }

    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(WATERLOGGED, fluidState.isOf(Fluids.WATER));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
            //? if minecraft: <=1.21 {
    /*public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
    *///? } else {
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (state.get(WATERLOGGED)) tickView.scheduleBlockTick(pos, Blocks.WATER, Fluids.WATER.getTickRate(world));
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }
    //?}


    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
}
