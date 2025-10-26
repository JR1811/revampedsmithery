package net.shirojr.revampedsmithery;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.shirojr.revampedsmithery.datagen.RevampedSmitheryModelGenerator;
import net.shirojr.revampedsmithery.datagen.RevampedSmitheryTranslationGenerator;

public class RevampedSmitheryDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();

		pack.addProvider(RevampedSmitheryModelGenerator::new);
		pack.addProvider(RevampedSmitheryTranslationGenerator::new);
	}
}
