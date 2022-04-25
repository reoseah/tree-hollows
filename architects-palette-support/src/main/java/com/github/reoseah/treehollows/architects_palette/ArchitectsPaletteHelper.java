package com.github.reoseah.treehollows.architects_palette;

import com.slomaxonical.architectspalette.registry.APBlocks;
import net.minecraft.block.Block;

public class ArchitectsPaletteHelper {
    public static boolean isTwistedLog(Block block) {
        return block == APBlocks.TWISTED_LOG;
    }

    public static Block getTwistedLog() {
        return APBlocks.TWISTED_LOG;
    }
}
