package com.github.reoseah.treehollows;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class TreeHollowsConfig {
    public static ForgeConfigSpec COMMON_CONFIG;

    public static final ForgeConfigSpec.DoubleValue WORLD_GENERATION_CHANCE;
    public static final ForgeConfigSpec.DoubleValue GROWTH_CHANCE;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        WORLD_GENERATION_CHANCE = COMMON_BUILDER.comment("How often (in percentage) should Tree Hollows spawn on trees during world generation? Set it to 0.0 to disable this.").defineInRange("worldGenerationChance", 0.05, 0.0, 1.0);
        GROWTH_CHANCE = COMMON_BUILDER.comment("How often (in percentage) should Tree Hollows spawn on trees when grown from a sapling? Set it to 0.0 to disable this.").defineInRange("growthChance", 0.05, 0.0, 1.0);

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) { }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading configEvent) { }
}
