package one.oth3r.otterlib.client.screen.widget;

import net.minecraft.client.font.DrawnTextConsumer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.util.Identifier;
import one.oth3r.otterlib.chat.CTxT;
import one.oth3r.otterlib.client.screen.utl.CustomImage;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ClickableImageWidget extends ButtonWidget {
    private final Identifier hoverBackground = Identifier.ofVanilla("textures/gui/sprites/widget/button_disabled.png");

    private final Identifier image;
    private final CTxT hoverTxT;
    private final TextRenderer textRenderer;

    private float hoverTime = 0;
    private final float MAX_TIME = 2.0f;

    /**
     * creates a clickable image widget
     * @param x the x position of the widget
     * @param y the x position of the widget
     * @param width the width of the image
     * @param height the height of the image
     * @param text the button text (hidden)
     * @param textRenderer the screen's text renderer
     * @param image the image identifier
     * @param onPress the press action of the widget
     * @param hoverTxT the text to display when hovering over the widget
     */
    public ClickableImageWidget(int x, int y, int width, int height, CTxT text, TextRenderer textRenderer, Identifier image, PressAction onPress, CTxT hoverTxT) {
        super(x, y, width, height, text.b(), onPress, Supplier::get);
        this.image = image;
        this.hoverTxT = hoverTxT;
        this.textRenderer = textRenderer;

        // disable button functionality if there is no press action
        if (onPress == null) this.active = false;
    }

    /**
     * creates a clickable image widget using a {@link CustomImage}
     * @param x the x position of the widget
     * @param y the y position of the widget
     * @param text the button text (hidden)
     * @param textRenderer the screen's text renderer
     * @param customImage the image to display
     * @param onPress the press action of the widget
     * @param hoverTxT the text to display when hovering over the widget
     */
    public ClickableImageWidget(int x, int y, CTxT text, TextRenderer textRenderer, CustomImage customImage, PressAction onPress, CTxT hoverTxT) {
        super(x, y, customImage.getWidth(), customImage.getHeight(), text.b(), onPress, Supplier::get);
        this.image = customImage.getImage();
        this.hoverTxT = hoverTxT;
        this.textRenderer = textRenderer;

        // disable button functionality if there is no press action
        if (onPress == null) this.active = false;
    }

    private int getTooltipY(int toolTipHeight) {
        return this.getY() + this.getHeight() - toolTipHeight;
    }

    private boolean canHover() {
        return this.hovered && this.hoverTxT != null;
    }

    @Override
    protected void drawIcon(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, image,
                this.getX(), this.getY(), 0.0f, 0.0f, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());

        if (canHover()) {
            hoverTime = Math.min(hoverTime + delta, MAX_TIME);
        } else if (hoverTime > 0f) {
            hoverTime = Math.max(hoverTime - delta, 0f);
        }

        if (hoverTime > 0f) {
            int toolTipHeight = (int)((hoverTime / MAX_TIME) * 20);
            int toolTipY = this.getTooltipY(toolTipHeight);

            context.drawTexture(RenderPipelines.GUI_TEXTURED, hoverBackground,
                    getX(), toolTipY, 0.0F, 0.0F, this.getWidth(), toolTipHeight,
                    this.getWidth(), toolTipHeight);
            if (hoverTime >= MAX_TIME) {
                assert hoverTxT != null; // cant be null because hovertime only goes up if hoverTxT is not null
                int padding = 4;
                int textX = getX() + padding;
                int textY = toolTipY + (toolTipHeight - textRenderer.fontHeight) / 2;
                context.drawText(textRenderer, hoverTxT.b(), textX, textY, 0xFFFFFF, false);
                context.getHoverListener(this, DrawContext.HoverType.NONE)
                        .text(hoverTxT.b(),textX, getX()+width-padding, toolTipY, getY()+this.getHeight());
            }
        }
    }

    public static class Builder {
        private final CTxT text;
        private CTxT hoverText = null;
        private final TextRenderer textRenderer;
        @Nullable
        private PressAction onPress = null;
        private final CustomImage image;

        private int x = 0;
        private int y = 0;

        @Nullable
        ButtonWidget.NarrationSupplier narrationSupplier;

        public Builder(CTxT text, TextRenderer textRenderer, CustomImage customImage) {
            this.text = text;
            this.textRenderer = textRenderer;
            this.image = customImage;
        }

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder onPress(PressAction onPress) {
            this.onPress = onPress;
            return this;
        }

        public Builder onHover(CTxT hoverText) {
            this.hoverText = hoverText;
            return this;
        }

        public Builder narration(NarrationSupplier narrationSupplier) {
            this.narrationSupplier = narrationSupplier;
            return this;
        }

        public ClickableImageWidget build() {
            if (this.image == null) {
                throw new IllegalStateException("Sprite not set");
            }
            return new ClickableImageWidget(x,y,text,textRenderer,image,onPress,hoverText);
        }
    }
}
