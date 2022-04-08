package com.github.reoseah.treehollows;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class TreeHollowsConfig {
    public static final Codec<TreeHollowsConfig> CODEC = RecordCodecBuilder.create( //
            instance -> instance.group( //
                            Codec.floatRange(0.0f, 1.0f).fieldOf("world_generation_chance").forGetter(config -> config.worldGenerationChance), //
                            Codec.floatRange(0.0f, 1.0f).fieldOf("growth_chance").forGetter(config -> config.growthChance)) //
                    .apply(instance, TreeHollowsConfig::new));

    private float worldGenerationChance;
    private float growthChance;

    public TreeHollowsConfig() {
        this(0.05F, 0.05F);
    }

    public TreeHollowsConfig(float worldGenerationChance, float growthChance) {
        this.worldGenerationChance = worldGenerationChance;
        this.growthChance = growthChance;
    }

    public static TreeHollowsConfig reload() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("tree-hollows.json");

        if (Files.exists(configPath)) {
            try (BufferedReader reader = Files.newBufferedReader(configPath)) {
                return CODEC.decode(JsonOps.INSTANCE, JsonParser.parseReader(reader)).result().map(Pair::getFirst).orElseThrow();
            } catch (Exception e) {
                TreeHollows.LOGGER.error("Error while reading config, using defaults", e);
                return new TreeHollowsConfig();
            }
        } else {
            TreeHollows.LOGGER.info("Missing config file, creating default");
            TreeHollowsConfig config = new TreeHollowsConfig();
            write(config);
            return config;
        }
    }

    public static void write(TreeHollowsConfig config) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("tree-hollows.json");
        try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
            JsonElement json = CODEC.encode(config, JsonOps.INSTANCE, new JsonObject()).result().orElseThrow();
            gson.toJson(json, gson.newJsonWriter(writer));
        } catch (Exception e) {
            TreeHollows.LOGGER.warn("Error while writing config", e);
        }
    }

    public float getWorldGenerationChance() {
        return this.worldGenerationChance;
    }

    public void setWorldGenerationChance(double value) {
        this.worldGenerationChance = Math.round(value * 100) / 100F;
    }

    public float getGrowthChance() {
        return this.growthChance;
    }

    public void setGrowthChance(double value) {
        this.growthChance = Math.round(value * 100) / 100F;
    }
}
