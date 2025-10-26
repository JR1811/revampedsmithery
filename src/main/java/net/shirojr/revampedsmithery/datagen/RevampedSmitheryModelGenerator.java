package net.shirojr.revampedsmithery.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.shirojr.revampedsmithery.init.RevampedSmitheryItems;

public class RevampedSmitheryModelGenerator extends FabricModelProvider {
    public RevampedSmitheryModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(RevampedSmitheryItems.HAMMER, Models.GENERATED);
    }
}
