package net.shirojr.revampedsmithery.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.shirojr.revampedsmithery.blockentity.SmithStationBlockEntity;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class SmithStationBlock extends BlockWithEntity {
    public static final EnumProperty<Part> PART = EnumProperty.of("smith_station_part", Part.class);

    public SmithStationBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Vec3d hitPos = hit.getPos();
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        if (!state.get(PART).equals(Part.FURNACE)) return null;
        return new SmithStationBlockEntity(pos, state);
    }

    public enum Part implements StringIdentifiable {
        FURNACE,
        CLAMPS;

        @Override
        public String asString() {
            return this.name();
        }
    }
}
