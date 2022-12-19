package com.github.reoseah.treehollows.client;

import com.github.reoseah.treehollows.TreeHollowsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.DoubleConsumer;

@Environment(EnvType.CLIENT)
public class TreeHollowsConfigScreen extends Screen {
    private static final Component TITLE = Component.translatable("options.treehollows");
    private static final Component WORLD_GEN_CHANCE = Component.translatable("options.treehollows.world_gen_chance");
    private static final Component GROWTH_CHANCE = Component.translatable("options.treehollows.growth_chance");

    protected final Screen previous;
    private ContainerObjectSelectionList<?> list;

    public TreeHollowsConfigScreen(Screen previous) {
        super(TITLE);
        this.previous = previous;
    }

    @Override
    public void removed() {
        TreeHollowsConfig.writeToFile();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.previous);
    }

    @Override
    protected void init() {
        this.list = new ContainerObjectSelectionList(this.minecraft, this.width, this.height, 32, this.height - 32, 25) {
            {
                this.addEntry(new FullWidthEntry(new PercentageSliderWidget(TreeHollowsConfig.getWorldGenerationChance(), TreeHollowsConfig::setWorldGenerationChance, WORLD_GEN_CHANCE, width)));
                this.addEntry(new FullWidthEntry(new PercentageSliderWidget(TreeHollowsConfig.getGrowthChance(), TreeHollowsConfig::setGrowthChance, GROWTH_CHANCE, width)));
            }

            @Override
            public int getRowWidth() {
                return 400;
            }

            @Override
            protected int getScrollbarPosition() {
                return super.getScrollbarPosition() + 32;
            }
        };

        this.addWidget(this.list);
        this.addRenderableWidget(new Button.Builder(CommonComponents.GUI_DONE, button -> this.minecraft.setScreen(this.previous)).pos(this.width / 2 - 100, this.height - 27).size(200, 20).build());
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.list.render(matrices, mouseX, mouseY, delta);
        drawCenteredString(matrices, this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private static class PercentageSliderWidget extends AbstractSliderButton {
        protected final DoubleConsumer consumer;
        protected final Component label;

        public PercentageSliderWidget(double value, DoubleConsumer consumer, Component label, int width) {
            this(value, consumer, label, width / 2 - 155, 0, 310, 20);
        }

        public PercentageSliderWidget(double value, DoubleConsumer consumer, Component label, int x, int y, int width, int height) {
            super(x, y, width, height, Component.empty(), value);
            this.consumer = consumer;
            this.label = label;
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            this.setMessage(Component.translatable("options.percent_value", this.label, Math.round(this.value * 100)));
        }

        @Override
        protected void applyValue() {
            this.consumer.accept(this.value);
        }
    }

    private static class FullWidthEntry extends ContainerObjectSelectionList.Entry<FullWidthEntry> {
        protected final AbstractWidget widget;

        private FullWidthEntry(AbstractWidget widget) {
            this.widget = widget;
        }

        @Override
        public void render(PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.widget.setY(y);
            this.widget.render(matrices, mouseX, mouseY, tickDelta);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.singletonList(this.widget);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return Collections.singletonList(this.widget);
        }
    }
}
