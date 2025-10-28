package net.shirojr.revampedsmithery.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.shirojr.revampedsmithery.blockentity.SmithStationBlockEntity;
import net.shirojr.revampedsmithery.init.RevampedSmitheryBlockEntities;
import net.shirojr.revampedsmithery.init.RevampedSmitheryBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;

@SuppressWarnings("deprecation")
public class SmithStationBlock extends BlockWithEntity {
    public static final EnumProperty<Part> PART = EnumProperty.of("smith_station_part", Part.class);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public SmithStationBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
                this.getDefaultState()
                        .with(PART, Part.getOrigin())
                        .with(FACING, Direction.NORTH)
        );
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        if (!state.get(PART).isOrigin()) return null;
        return new SmithStationBlockEntity(pos, state);
    }

    public @Nullable SmithStationBlockEntity getBlockEntity(WorldView world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof SmithStationBlockEntity blockEntity) return blockEntity;
        BlockState state = world.getBlockState(pos);
        if (!state.contains(PART) || !state.contains(FACING)) return null;
        BlockPos originPos = state.get(PART).getPosOfOrigin(pos, state.get(FACING));
        if (!(world.getBlockEntity(originPos) instanceof SmithStationBlockEntity smithStationBlockEntity)) return null;
        return smithStationBlockEntity;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(PART, FACING);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public long getRenderingSeed(BlockState state, BlockPos pos) {
        BlockPos originPos = state.get(PART).getPosOfOrigin(pos, state.get(FACING));
        return MathHelper.hashCode(originPos);
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, RevampedSmitheryBlockEntities.SMITH_STATION, (world1, pos, state1, blockEntity) -> {
            // ticking is done by CCA component
        });
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!state.contains(PART)) return ActionResult.PASS;
        if (hit == null) return ActionResult.PASS;
        SmithStationBlockEntity blockEntity = getBlockEntity(world, pos);
        if (blockEntity == null) return ActionResult.PASS;
        ItemStack stack = player.getStackInHand(hand);
        return blockEntity.attemptInteraction(stack, player, hit);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        for (Part other : state.get(PART).getOthers()) {
            if (!other.canPlace(world, pos)) return false;
        }
        return super.canPlaceAt(state, world, pos);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState placementState = super.getPlacementState(ctx);
        if (placementState == null) return null;
        return placementState.with(FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!(world instanceof ServerWorld serverWorld)) return;
        Direction facing = state.get(FACING);
        Part part = state.get(PART);
        for (Part other : part.getOthers()) {
            BlockPos offsetPos = other.getPosFromOrigin(pos, facing);
            serverWorld.setBlockState(offsetPos, state.with(PART, other), Block.NOTIFY_ALL);
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.isClient() || !player.isCreative()) return;
        Part.breakStructure(world, pos);
        super.onBreak(world, pos, state, player);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(this) && !newState.isOf(this)) {
            if (!Part.isStructureValid(world, pos)) {
                Part.breakStructure(world, pos);
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    public enum Part implements StringIdentifiable {
        PEDAL(false, (pos, direction) -> pos),
        FENCES(false, (pos, direction) -> pos.offset(direction.rotateYCounterclockwise())),
        FURNACE(true, (pos, direction) -> pos.down()),
        CLAMPS(true, (pos, direction) -> pos.down().offset(direction.rotateYCounterclockwise()));

        private final BiFunction<BlockPos, Direction, BlockPos> originPos;
        private final boolean isTop;

        Part(boolean isTop, BiFunction<BlockPos, Direction, BlockPos> originPos) {
            this.isTop = isTop;
            this.originPos = originPos;
        }

        public boolean isTop() {
            return isTop;
        }

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        public boolean isOrigin() {
            return this.equals(PEDAL);
        }

        public static Part getOrigin() {
            return PEDAL;
        }

        @SuppressWarnings("unused")
        public Part getNext() {
            int ordinal = this.ordinal() + 1;
            if (ordinal >= Part.values().length) {
                ordinal = 0;
            }
            return Part.values()[ordinal];
        }

        public List<Part> getOthers() {
            List<Part> result = new ArrayList<>();
            for (Part entry : Part.values()) {
                if (this.ordinal() == entry.ordinal()) continue;
                result.add(entry);
            }
            return result;
        }

        @Override
        public String asString() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        BlockPos getPosOfOrigin(BlockPos pos, Direction direction) {
            if (direction.getAxis().isVertical()) {
                throw new UnsupportedOperationException("Smith Station block only supports horizontal directions");
            }
            return this.originPos.apply(pos, direction);
        }

        BlockPos getPosFromOrigin(BlockPos originPos, Direction direction) {
            if (direction.getAxis().isVertical()) {
                throw new UnsupportedOperationException("Smith Station block only supports horizontal directions");
            }
            BlockPos offset = this.originPos.apply(BlockPos.ORIGIN, direction);
            return originPos.subtract(offset);
        }

        public boolean canPlace(WorldView world, BlockPos pos) {
            if (!world.getBlockState(pos).isReplaceable()) return false;
            BlockPos posBelow = pos.down();
            BlockState stateBelow = world.getBlockState(posBelow);
            if (!isTop()) {
                return stateBelow.isSideSolidFullSquare(world, posBelow, Direction.UP);
            }
            return true;
        }

        public static void breakStructure(World world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            if (!state.isOf(RevampedSmitheryBlocks.SMITH_STATION)) return;
            Part part = state.get(PART);
            Direction facing = state.get(FACING);
            BlockPos originPos = part.getPosOfOrigin(pos, facing);

            for (Part other : part.getOthers()) {
                BlockPos otherPos = other.getPosFromOrigin(originPos, facing);
                int breakStatusFlag = Block.NOTIFY_ALL;
                if (!other.isOrigin()) {
                    breakStatusFlag = breakStatusFlag | Block.SKIP_DROPS;
                }
                world.setBlockState(otherPos, Blocks.AIR.getDefaultState(), breakStatusFlag);
            }
        }

        public static boolean isStructureValid(World world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            if (!state.isOf(RevampedSmitheryBlocks.SMITH_STATION)) return false;
            Part part = state.get(PART);
            Direction facing = state.get(FACING);
            BlockPos originPos = part.getPosOfOrigin(pos, facing);
            for (Part other : getOrigin().getOthers()) {
                BlockPos otherPos = other.getPosFromOrigin(originPos, facing);
                BlockState otherState = world.getBlockState(otherPos);
                if (!otherState.contains(PART)) return false;
                if (!otherState.get(PART).equals(other)) return false;
            }
            return true;
        }
    }
}
