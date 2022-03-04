package com.github.reoseah.treehollows;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TreeHollowsConfig {
	public static final Codec<TreeHollowsConfig> CODEC = RecordCodecBuilder.create( //
			instance -> instance.group( //
							Codec.floatRange(0.0f, 1.0f).fieldOf("chance").forGetter(config -> config.chance)) //
					.apply(instance, TreeHollowsConfig::new));

	public static TreeHollowsConfig instance = new TreeHollowsConfig();

	private final float chance;

	public TreeHollowsConfig() {
		this(0.05F);
	}

	public TreeHollowsConfig(float chance) {
		this.chance = chance;
	}

	public static void reload() {
		Path configPath = FabricLoader.getInstance().getConfigDir().resolve("tree-hollows.json");

		if (Files.exists(configPath)) {
			try (BufferedReader reader = Files.newBufferedReader(configPath)) {
//				instance = gson.fromJson(reader, TreeHollowsConfig.class);
				instance = CODEC.decode(JsonOps.INSTANCE, JsonParser.parseReader(reader)).result().map(Pair::getFirst).orElse(new TreeHollowsConfig());
			} catch (IOException e) {
				TreeHollows.LOGGER.warn("Error while reading config, using defaults", e);
			}
		} else {
			TreeHollows.LOGGER.info("Missing config file, creating default");
			write();
		}
	}

	public static void write() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Path configPath = FabricLoader.getInstance().getConfigDir().resolve("tree-hollows.json");
		try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
			gson.toJson(instance, writer);
		} catch (IOException e) {
			TreeHollows.LOGGER.warn("Error while writing config", e);
		}
	}

	public float getChance() {
		return this.chance;
	}
}
