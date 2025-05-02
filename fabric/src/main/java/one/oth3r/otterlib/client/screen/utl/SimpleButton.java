package one.oth3r.fabricTest.client.screen.utl;

import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import one.oth3r.fabricTest.FabricTest;
import one.oth3r.fabricTest.client.screen.SetClientScreen;
import one.oth3r.fabricTest.client.screen.widget.TextureButtonWidget;
import one.oth3r.otterlib.chat.CTxT;

import java.net.URI;

public abstract class SimpleButton {
    protected CTxT text;
    protected int width;
    protected int height;
    protected CustomImage customImage;

    protected SimpleButton(CTxT text, int width, int height, CustomImage customImage) {
        this.text = text;
        this.width = width;
        this.height = height;
        this.customImage = customImage;
    }

    public abstract <T extends Screen & SetClientScreen> TextureButtonWidget build(T openScreen);

    public static class Templates {
        public static Builder donate(CTxT text) {
            return new Builder(text)
                    .miniIcon(new CustomImage(Identifier.of(FabricTest.MOD_ID, "donate"),15,15));
        }
        public static Builder warning(CTxT text) {
            return new Builder(text)
                    .miniIcon(new CustomImage(Identifier.of(FabricTest.MOD_ID, "issues"),15,15));
        }
        public static Builder wiki(CTxT text) {
            return new Builder(text)
                    .miniIcon(new CustomImage(Identifier.of(FabricTest.MOD_ID, "wiki_icon"),15,15));
        }
        public static Builder done(CTxT text) {
            return new Builder(text).close();
        }
    }

    public static class Builder {
        public enum ButtonType {
            DISABLED,
            CLOSE,
            OPEN_SCREEN,
            OPEN_LINK
        }

        protected CTxT text;
        protected int width = 150;
        protected int height = 20;
        protected CustomImage customImage;

        protected ButtonType buttonType;
        protected Screen screen;
        protected URI uri;

        public Builder(CTxT buttonText) {
            this.text = buttonText;
            buttonType = ButtonType.DISABLED;
        }

        public Builder openScreen(Screen screen) {
            this.screen = screen;
            buttonType = ButtonType.OPEN_SCREEN;
            return this;
        }

        public Builder openLink(URI uri) {
            this.uri = uri;
            buttonType = ButtonType.OPEN_LINK;
            return this;
        }

        public Builder openLink(String url) {
            this.uri = URI.create(url);
            buttonType = ButtonType.OPEN_LINK;
            return this;
        }

        public Builder close() {
            buttonType = ButtonType.CLOSE;
            return this;
        }

        public Builder miniIcon(CustomImage customImage) {
            this.customImage = customImage;
            this.width = 20;
            this.height = 20;
            return this;
        }

        public Builder image(Identifier texture, int width, int height) {
            this.customImage = new CustomImage(texture, width, height);
            return this;
        }

        public Builder image(CustomImage customImage) {
            this.customImage = customImage;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public SimpleButton build() {
            return switch (buttonType) {
                case CLOSE -> new Close(text,width,height,customImage);
                case OPEN_SCREEN -> new OpenScreen(text,width,height,customImage,screen);
                case OPEN_LINK -> new OpenLink(text,width,height,customImage,uri);
                default -> new Disabled(text,width,height,customImage);
            };
        }
    }

    public static class Close extends SimpleButton {

        protected Close(CTxT text, int width, int height, CustomImage customImage) {
            super(text, width, height, customImage);
        }

        @Override
        public TextureButtonWidget build(Screen openScreen) {
            TextureButtonWidget.Builder builder = new TextureButtonWidget.Builder(text.b()).size(width,height)
                    .onPress(b -> openScreen.close());
            if (customImage != null) return builder.texture(customImage).hideText(true).build();
            return builder.build();
        }
    }

    public static class Disabled extends SimpleButton {

        protected Disabled(CTxT text, int width, int height, CustomImage customImage) {
            super(text, width, height, customImage);
        }

        @Override
        public TextureButtonWidget build(Screen openScreen) {
            TextureButtonWidget.Builder builder = new TextureButtonWidget.Builder(text.b()).size(width,height).disabled(true);
            if (customImage != null) return builder.texture(customImage).hideText(true).build();
            return builder.build();
        }
    }

    public static class OpenLink extends SimpleButton {
        protected URI uri;

        public OpenLink(CTxT text, int width, int height, CustomImage customImage, URI uri) {
            super(text, width, height, customImage);
            this.uri = uri;
        }

        @Override
        public TextureButtonWidget build(Screen openScreen) {
            TextureButtonWidget.Builder builder = new TextureButtonWidget.Builder(text.b(),ConfirmLinkScreen.opening(openScreen,uri)).size(width,height);
            if (customImage != null) return builder.texture(customImage).hideText(true).build();
            return builder.build();
        }

    }

    public static class OpenScreen extends SimpleButton {
        protected Screen screen;

        public OpenScreen(CTxT text, int width, int height, CustomImage customImage, Screen screen) {
            super(text, width, height, customImage);
            this.screen = screen;
        }

        @Override
        public <T extends Screen & SetClientScreen> TextureButtonWidget build(T openScreen) {
            TextureButtonWidget.Builder builder = new TextureButtonWidget.Builder(text.b()).size(width,height)
                    .onPress(btn -> openScreen.setScreen(screen));
            if (customImage != null) return builder.texture(customImage).hideText(true).build();
            return builder.build();
        }
    }
}
