package net.shirojr.revampedsmithery.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.shirojr.revampedsmithery.block.SmithStationBlock;
import net.shirojr.revampedsmithery.init.RevampedSmitheryBlocks;
import net.shirojr.revampedsmithery.init.RevampedSmitheryItems;

import java.util.Optional;

public class RevampedSmitheryModelGenerator extends FabricModelProvider {
    public RevampedSmitheryModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(RevampedSmitheryBlocks.SMITH_STATION)
                        .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates())
                        .coordinate(BlockStateVariantMap.create(SmithStationBlock.PART)
                                .register(part -> switch (part) {
                                    case PEDAL, FENCES ->
                                            BlockStateVariant.create().put(VariantSettings.MODEL, registerDynamicItemBlockModel(generator, part, Blocks.DEEPSLATE));
                                    case FURNACE ->
                                            BlockStateVariant.create().put(VariantSettings.MODEL, registerDynamicItemBlockModel(generator, part, Blocks.BRICKS));
                                    case CLAMPS ->
                                            BlockStateVariant.create().put(VariantSettings.MODEL, registerDynamicItemBlockModel(generator, part, Blocks.SMOOTH_STONE));
                                })
                        )
        );
    }

    private static Identifier registerDynamicItemBlockModel(BlockStateModelGenerator generator, SmithStationBlock.Part part, Block particle) {
        TextureMap textureMap = new TextureMap();
        textureMap.put(TextureKey.PARTICLE, TextureMap.getId(particle));

        Model model = new Model(
                Optional.ofNullable(Identifier.of("minecraft", "builtin/entity")),
                Optional.empty(),
                TextureKey.PARTICLE
        );
        Identifier blockId = Registries.BLOCK.getId(RevampedSmitheryBlocks.SMITH_STATION);
        return model.upload(Identifier.of(blockId.getNamespace(), blockId.getPath() + "_%s".formatted(part.asString())), textureMap, generator.modelCollector);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(RevampedSmitheryItems.HAMMER, Models.GENERATED);
    }
}
