package net.shirojr.revampedsmithery.init;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.shirojr.revampedsmithery.RevampedSmithery;
import net.shirojr.revampedsmithery.item.HammerItem;

import java.util.ArrayList;
import java.util.List;

public interface RevampedSmitheryItems {
    List<Item> ALL_ITEMS = new ArrayList<>();

    HammerItem HAMMER = register("hammer", new HammerItem(new Item.Settings().maxCount(1).maxDamage(420)));

    @SuppressWarnings("SameParameterValue")
    private static <T extends Item> T register(String name, T entry) {
        T registeredEntry = Registry.register(Registries.ITEM, RevampedSmithery.getId(name), entry);
        ALL_ITEMS.add(registeredEntry);
        return registeredEntry;
    }

    static void initialize() {
        // static initialisation
    }
}
