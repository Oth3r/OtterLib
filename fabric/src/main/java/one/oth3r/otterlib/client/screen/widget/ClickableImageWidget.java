package one.oth3r.otterlib.client.screen.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
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
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(image,
                this.getX(), this.getY(), 0.0f, 0.0f, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());

        float maxTime = 2.0f; // 2 second long animation

        if (canHover()) {
            hoverTime = Math.min(hoverTime + delta, maxTime);
        } else if (hoverTime > 0f) {
            hoverTime = Math.max(hoverTime - delta, 0f);
        }

        if (hoverTime > 0f) {
            int toolTipHeight = (int)((hoverTime / maxTime) * 20);
            int toolTipY = this.getTooltipY(toolTipHeight);

            context.drawTexture(hoverBackground,
                    getX(), toolTipY, 0.0F, 0.0F, this.getWidth(), toolTipHeight,
                    this.getWidth(), toolTipHeight);

            if (hoverTime >= maxTime) {
                assert hoverTxT != null; // cant be null because hovertime only goes up if hoverTxT is not null
                drawScrollableText(context, textRenderer, hoverTxT.b(),
                        getX() + 4, toolTipY, getX() + this.getWidth() - 4,
                        toolTipY + toolTipHeight, 0xFFFFFF);
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
