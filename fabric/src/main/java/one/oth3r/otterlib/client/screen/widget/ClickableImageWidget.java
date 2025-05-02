package one.oth3r.otterlib.client.screen.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ClickableImageWidget extends ButtonWidget {
    private final Identifier hoverBackground = Identifier.ofVanilla("textures/gui/sprites/widget/button_disabled.png");

    private final Identifier image;
    private final Text hoverText;
    private final TextRenderer textRenderer;

    private long hoverTime = 0;

    public ClickableImageWidget(int x, int y, int width, int height, Text text, TextRenderer textRenderer, Identifier image, PressAction onPress, Text hoverText) {
        super(x, y, width, height, text, onPress, Supplier::get);
        this.image = image;
        this.hoverText = hoverText;
        this.textRenderer = textRenderer;

        // disable button functionality if there is no press action
        if (onPress == null) this.active = false;
    }

    private int getTooltipY(int toolTipHeight) {
        return this.getY() + this.getHeight() - toolTipHeight;
    }

    private boolean canHover() {
        return this.hovered && this.hoverText != null;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(RenderLayer::getGuiTextured, image,
                this.getX(), this.getY(), 0.0f, 0.0f, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
        int toolTipHeight;
        if (canHover()) {
            this.hoverTime++;
            toolTipHeight = 20;
            if (this.hoverTime <= 40) {
                toolTipHeight = (int) (this.hoverTime/2);
            }
            int toolTipY = this.getTooltipY(toolTipHeight);

            context.drawTexture(RenderLayer::getGuiTextured, hoverBackground, getX(), toolTipY,0.0F, 0.0F, this.getWidth(), toolTipHeight, this.getWidth(), toolTipHeight);

            // only show the text when animation is done
            if (this.hoverTime > 40) drawScrollableText(context,textRenderer,hoverText,getX()+4,toolTipY,getX()+this.getWidth()-4,toolTipY+toolTipHeight,16777215);
        } else if (this.hoverTime != 0) {
            if (this.hoverTime > 40) this.hoverTime = 40;
            toolTipHeight = (int) (this.hoverTime/2);
            int toolTipY = this.getTooltipY(toolTipHeight);

            context.drawTexture(RenderLayer::getGuiTextured, hoverBackground, getX(), toolTipY,0.0F, 0.0F, this.getWidth(), toolTipHeight, this.getWidth(), toolTipHeight);
            this.hoverTime--;
        }
    }

    public static class Builder {
        private final Text text;
        private Text hoverText = null;
        private final TextRenderer textRenderer;
        @Nullable
        private PressAction onPress = null;
        private final Identifier image;
        private int width;
        private int height;

        private int x = 0;
        private int y = 0;

        @Nullable
        ButtonWidget.NarrationSupplier narrationSupplier;

        public Builder(Text text, TextRenderer textRenderer, Identifier image, int width, int height) {
            this.text = text;
            this.textRenderer = textRenderer;
            this.image = image;
            this.width = width;
            this.height = height;
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

        public Builder onHover(Text hoverText) {
            this.hoverText = hoverText;
            return this;
        }

        public Builder dimensions(int width, int height) {
            this.width = width;
            this.height = height;
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
            return new ClickableImageWidget(x,y,width,height,text,textRenderer,image,onPress,hoverText);
        }
    }
}
