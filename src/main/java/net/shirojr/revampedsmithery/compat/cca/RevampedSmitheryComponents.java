package net.shirojr.revampedsmithery.compat.cca;

import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.shirojr.revampedsmithery.blockentity.SmithStationBlockEntity;
import net.shirojr.revampedsmithery.compat.cca.custom.SmithStationDataComponent;

public class RevampedSmitheryComponents implements BlockComponentInitializer {
    public static final ComponentKey<SmithStationDataComponent> SMITHING_STATION_DATA =
            ComponentRegistry.getOrCreate(SmithStationDataComponent.KEY, SmithStationDataComponent.class);

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(SmithStationBlockEntity.class, SMITHING_STATION_DATA, SmithStationDataComponent::new);
    }
}
