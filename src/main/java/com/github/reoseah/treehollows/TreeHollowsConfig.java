package com.github.reoseah.treehollows;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class TreeHollowsConfig {
    public static final Logger LOGGER = LoggerFactory.getLogger("tree-hollows");
    public static final Codec<TreeHollowsConfig> CODEC = RecordCodecBuilder.create( //
            instance -> instance.group( //
                            Codec.floatRange(0.0f, 1.0f).fieldOf("world_generation_chance").orElse(0.05F).forGetter(config -> config.worldGenerationChance), //
                            Codec.floatRange(0.0f, 1.0f).fieldOf("growth_chance").orElse(0.05F).forGetter(config -> config.growthChance)) //
                    .apply(instance, TreeHollowsConfig::new));

    private static TreeHollowsConfig instance;

    private float worldGenerationChance;
    private float growthChance;

    public TreeHollowsConfig() {
        this(0.05F, 0.05F);
    }

    public TreeHollowsConfig(float worldGenerationChance, float growthChance) {
        this.worldGenerationChance = worldGenerationChance;
        this.growthChance = growthChance;
    }

    public static TreeHollowsConfig getInstance() {
        if (instance == null) {
            reload();
        }
        return instance;
    }

    public static void reload() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("tree-hollows.json");

        if (Files.exists(configPath)) {
            try (BufferedReader reader = Files.newBufferedReader(configPath)) {
                instance = CODEC.decode(JsonOps.INSTANCE, JsonParser.parseReader(reader)).result().map(Pair::getFirst).orElseThrow();
            } catch (Exception e) {
                LOGGER.error("Error while reading config, using defaults", e);
                instance = new TreeHollowsConfig();
            }
        } else {
            LOGGER.info("Missing config file, creating default");
            instance = new TreeHollowsConfig();
            writeToFile();
        }
    }

    public static void writeToFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("tree-hollows.json");
        try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
            JsonElement json = CODEC.encode(instance, JsonOps.INSTANCE, new JsonObject()).result().orElseThrow();
            gson.toJson(json, gson.newJsonWriter(writer));
        } catch (Exception e) {
            LOGGER.warn("Error while writing config", e);
        }
    }

    public static float getWorldGenerationChance() {
        return getInstance().worldGenerationChance;
    }

    public static void setWorldGenerationChance(double value) {
        getInstance().worldGenerationChance = Math.round(value * 100) / 100F;
    }

    public static float getGrowthChance() {
        return getInstance().growthChance;
    }

    public static void setGrowthChance(double value) {
        getInstance().growthChance = Math.round(value * 100) / 100F;
    }
}
