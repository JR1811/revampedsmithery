package net.shirojr.revampedsmithery;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RevampedSmithery implements ModInitializer {
	public static final String MOD_ID = "revampedsmithery";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Time to light up the furnace!");
	}

	public static Identifier getId(String path) {
		return Identifier.of(MOD_ID, path);
	}
}