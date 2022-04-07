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

    public static void reload() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("tree-hollows.json");

        if (Files.exists(configPath)) {
            try (BufferedReader reader = Files.newBufferedReader(configPath)) {
                TreeHollows.config = CODEC.decode(JsonOps.INSTANCE, JsonParser.parseReader(reader)).result().map(Pair::getFirst).orElse(new TreeHollowsConfig());
            } catch (Exception e) {
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
            JsonElement json = CODEC.encode(TreeHollows.config, JsonOps.INSTANCE, new JsonObject()).result().get();
            gson.toJson(json, gson.newJsonWriter(writer));
        } catch (Exception e) {
            TreeHollows.LOGGER.warn("Error while writing config", e);
        }
    }

    public float getWorldGenerationChance() {
        return this.worldGenerationChance;
    }

    public void setWorldGenerationChance(float value) {
        this.worldGenerationChance = value;
    }

    public float getGrowthChance() {
        return this.growthChance;
    }

    public void setGrowthChance(float value) {
        this.growthChance = value;
    }
}
