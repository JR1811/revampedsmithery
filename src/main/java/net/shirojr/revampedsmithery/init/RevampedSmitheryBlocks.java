package net.shirojr.revampedsmithery.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.shirojr.revampedsmithery.RevampedSmithery;
import net.shirojr.revampedsmithery.block.SmithStationBlock;

import java.util.ArrayList;
import java.util.List;

public interface RevampedSmitheryBlocks {
    List<Block> ALL_BLOCKS = new ArrayList<>();

    SmithStationBlock SMITH_STATION = register("smith_station", new SmithStationBlock(
            AbstractBlock.Settings.create().strength(4).ticksRandomly()
    ), true, List.of(RevampedSmitheryItems.ALL_ITEMS));


    @SuppressWarnings("SameParameterValue")
    private static <T extends Block> T register(String name, T entry, boolean registerDefaultItem, List<List<Item>> itemLists) {
        Identifier id = RevampedSmithery.getId(name);
        T registeredEntry = Registry.register(Registries.BLOCK, id, entry);
        if (registerDefaultItem) {
            BlockItem registeredItemEntry = Registry.register(Registries.ITEM, id, new BlockItem(registeredEntry, new Item.Settings()));
            for (List<Item> list : itemLists) {
                list.add(registeredItemEntry);
            }
        }
        ALL_BLOCKS.add(registeredEntry);
        return registeredEntry;
    }

    static void initialize() {
        // static initialisation
    }
}
