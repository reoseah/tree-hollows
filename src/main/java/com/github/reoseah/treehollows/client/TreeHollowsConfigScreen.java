package com.github.reoseah.treehollows.client;

import com.github.reoseah.treehollows.TreeHollows;
import com.github.reoseah.treehollows.TreeHollowsConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Collections;
import java.util.List;
import java.util.function.DoubleConsumer;

@Environment(EnvType.CLIENT)
public class TreeHollowsConfigScreen extends Screen {
    private static final Text TITLE = new TranslatableText("options.treehollows.title");
    private static final Text WORLD_GEN_CHANCE = new TranslatableText("options.treehollows.world_gen_chance");
    private static final Text GROWTH_CHANCE = new TranslatableText("options.treehollows.growth_chance");

    protected final Screen previous;
    private ElementListWidget<?> list;

    public TreeHollowsConfigScreen(Screen previous) {
        super(TITLE);
        this.previous = previous;
    }

    @Override
    public void removed() {
        TreeHollowsConfig.write(TreeHollows.config);
    }

    @Override
    public void close() {
        this.client.setScreen(this.previous);
    }

    @Override
    protected void init() {
        this.list = new ElementListWidget(this.client, this.width, this.height, 32, this.height - 32, 25) {
            {
                this.addEntry(new FullWidthEntry(new PercentageSliderWidget(TreeHollows.config.getWorldGenerationChance(), TreeHollows.config::setWorldGenerationChance, WORLD_GEN_CHANCE, width)));
                this.addEntry(new FullWidthEntry(new PercentageSliderWidget(TreeHollows.config.getGrowthChance(), TreeHollows.config::setGrowthChance, GROWTH_CHANCE, width)));
            }

            @Override
            public int getRowWidth() {
                return 400;
            }

            @Override
            protected int getScrollbarPositionX() {
                return super.getScrollbarPositionX() + 32;
            }
        };

        this.addSelectableChild(this.list);
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, button -> this.client.setScreen(this.previous)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.list.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private static class PercentageSliderWidget extends SliderWidget {
        protected final DoubleConsumer consumer;
        protected final Text label;

        public PercentageSliderWidget(double value, DoubleConsumer consumer, Text label, int width) {
            this(value, consumer, label, width / 2 - 155, 0, 310, 20);
        }

        public PercentageSliderWidget(double value, DoubleConsumer consumer, Text label, int x, int y, int width, int height) {
            super(x, y, width, height, LiteralText.EMPTY, value);
            this.consumer = consumer;
            this.label = label;
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            this.setMessage(new TranslatableText("options.percent_value", this.label, Math.round(this.value * 100)));
        }

        @Override
        protected void applyValue() {
            this.consumer.accept(this.value);
        }
    }

    private static class FullWidthEntry extends ElementListWidget.Entry<FullWidthEntry> {
        protected final ClickableWidget widget;

        private FullWidthEntry(ClickableWidget widget) {
            this.widget = widget;
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.widget.y = y;
            this.widget.render(matrices, mouseX, mouseY, tickDelta);
        }

        @Override
        public List<? extends Element> children() {
            return Collections.singletonList(this.widget);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return Collections.singletonList(this.widget);
        }
    }
}
