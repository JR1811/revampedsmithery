package net.shirojr.revampedsmithery.init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.shirojr.revampedsmithery.RevampedSmithery;
import net.shirojr.revampedsmithery.blockentity.SmithStationBlockEntity;

public interface RevampedSmitheryBlockEntities {
    BlockEntityType<SmithStationBlockEntity> SMITH_STATION = register(
            "smith_station", SmithStationBlockEntity::new, RevampedSmitheryBlocks.SMITH_STATION
    );

    @SuppressWarnings("SameParameterValue")
    private static <T extends BlockEntity> BlockEntityType<T> register(String name,
                                                                       FabricBlockEntityTypeBuilder.Factory<? extends T> factory,
                                                                       Block... blocks) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(RevampedSmithery.MOD_ID, name),
                FabricBlockEntityTypeBuilder.<T>create(factory, blocks).build());
    }

    static void initialize() {
        // static initialisation
    }
}
