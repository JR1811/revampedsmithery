package net.shirojr.revampedsmithery.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.shirojr.revampedsmithery.init.RevampedSmitheryItems;

import java.util.Locale;

public class RevampedSmitheryTranslationGenerator extends FabricLanguageProvider {
    public RevampedSmitheryTranslationGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        builder.add(RevampedSmitheryItems.HAMMER, getClean(getId(Registries.ITEM, RevampedSmitheryItems.HAMMER)));
    }

    private String getClean(Identifier identifier) {
        String[] words = identifier.getPath().split("_");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word.substring(0, 1).toUpperCase(Locale.ROOT)).append(word.substring(1));
        }
        return sb.toString();
    }

    @SuppressWarnings("SameParameterValue")
    private <S, T extends DefaultedRegistry<S>> Identifier getId(T registryType, S entry) {
        Identifier id = registryType.getId(entry);
        if (id.equals(registryType.getDefaultId())) throw new IllegalArgumentException("Couldn't find registry entry");
        return id;
    }
}
