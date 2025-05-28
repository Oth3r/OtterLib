package one.oth3r.otterlib.client.screen.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import one.oth3r.otterlib.client.screen.utl.CustomImage;
import org.jetbrains.annotations.Nullable;

public class TextureButtonWidget extends ButtonWidget {
    //todo add texture offset
    protected final Identifier texture;
    protected final int textureWidth;
    protected final int textureHeight;
    protected final boolean tooltip;

    TextureButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress,
                        Identifier texture, int textureWidth, int textureHeight,
                        @Nullable ButtonWidget.NarrationSupplier narrationSupplier,
                        boolean tooltip, boolean disabled) {
        super(x, y, width, height, message, onPress, narrationSupplier == null ? DEFAULT_NARRATION_SUPPLIER : narrationSupplier);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.texture = texture;
        this.tooltip = tooltip;
        if (tooltip) setTooltip(Tooltip.of(message));

        this.active = !disabled;
    }

    public void setDisabled(boolean disabled) {
        this.active = !disabled;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);

        if (texture == null) return;

        int x = this.getX() + this.getWidth() / 2 - this.textureWidth / 2;
        int y = this.getY() + this.getHeight() / 2 - this.textureHeight / 2;
        int color = this.active ? -1 : -6908266;
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, this.texture, x, y, this.textureWidth, this.textureHeight, color);
    }

    @Override
    public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
        if (!this.tooltip) super.drawMessage(context, textRenderer, color);
    }

    /**
     * makes a new textured button widget that is 20x20, that takes an image that is 15x15
     */
    public static Builder createIconButton(Text buttonText, PressAction pressAction, Identifier texture) {
        return new Builder(buttonText,pressAction).hideText(true).size(20,20)
                .texture(texture,15,15);
    }


    public static class Builder {
        private final Text text;
        private PressAction onPress = null;
        private boolean hideText;

        private int x;
        private int y;

        private int width = 150;
        private int height = 20;

        private boolean disabled = false;

        @Nullable
        private Identifier texture;
        private int textureWidth;
        private int textureHeight;

        @Nullable
        ButtonWidget.NarrationSupplier narrationSupplier;

        /**
         * crates a builder with an enabled button with a press action
         * @param text the button text
         * @param onPress the button's press action
         */
        public Builder(Text text, PressAction onPress) {
            this.text = text;
            this.onPress = onPress;
        }

        /**
         * creates a builder with just the button text (be sure to make it disabled or add a pressAction!)
         * @param text the button text
         */
        public Builder(Text text) {
            this.text = text;
        }

        /**
         * sets the button's press action
         * @param onPress the press action
         */
        public Builder onPress(PressAction onPress) {
            this.onPress = onPress;
            return this;
        }

        /**
         * sets the disabled state of the button
         * @param disabled if the button should be disabled or not
         */
        public Builder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        /**
         * sets weather the text should be hidden or not (for image only)
         * @param hideText if the button text should be hidden or not
         */
        public Builder hideText(boolean hideText) {
            this.hideText = hideText;
            return this;
        }

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder dimensions(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;

            this.width = width;
            this.height = height;
            return this;
        }

        public Builder texture(Identifier texture, int width, int height) {
            this.texture = texture;
            this.textureWidth = width;
            this.textureHeight = height;
            return this;
        }

        public Builder texture(CustomImage customImage) {
            this.texture = customImage.getImage();
            this.textureWidth = customImage.getWidth();
            this.textureHeight = customImage.getHeight();
            return this;
        }

        public Builder narration(NarrationSupplier narrationSupplier) {
            this.narrationSupplier = narrationSupplier;
            return this;
        }

        public TextureButtonWidget build() {
            return new TextureButtonWidget(x,y,width,height,text,onPress,texture,textureWidth,textureHeight,narrationSupplier,hideText,disabled);
        }
    }
}
