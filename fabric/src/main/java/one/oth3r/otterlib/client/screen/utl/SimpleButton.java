package one.oth3r.otterlib.client.screen.utl;

import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import one.oth3r.otterlib.Assets;
import one.oth3r.otterlib.client.screen.SetClientScreen;
import one.oth3r.otterlib.client.screen.SetParentScreen;
import one.oth3r.otterlib.client.screen.UnderConstructionScreen;
import one.oth3r.otterlib.client.screen.widget.TextureButtonWidget;
import one.oth3r.otterlib.chat.CTxT;
import one.oth3r.otterlib.file.CustomFile;

import java.net.URI;

public abstract class SimpleButton {
    protected CTxT text;
    protected boolean hideText;
    protected int width;
    protected int height;
    protected CustomImage customImage;

    protected SimpleButton(CTxT text, boolean hideText, int width, int height, CustomImage customImage) {
        this.text = text;
        this.hideText = hideText;
        this.width = width;
        this.height = height;
        this.customImage = customImage;
    }

    public abstract <T extends Screen & SetClientScreen> TextureButtonWidget build(T openScreen);

    public static class Templates {
        public static Builder donate(CTxT text) {
            return new Builder(text)
                    .miniIcon(new CustomImage(Identifier.of(Assets.ID, "textures/gui/sprites/icon/donate.png"),15,15));
        }
        public static Builder warning(CTxT text) {
            return new Builder(text)
                    .miniIcon(new CustomImage(Identifier.of(Assets.ID, "textures/gui/sprites/icon/warning.png"),15,15));
        }
        public static Builder wiki(CTxT text) {
            return new Builder(text)
                    .miniIcon(new CustomImage(Identifier.of(Assets.ID, "textures/gui/sprites/icon/wiki.png"),15,15));
        }
        public static Builder done(CTxT text) {
            return new Builder(text).close();
        }
        public static <F extends CustomFile<F>> FileBuilder<F> fileEditor(CTxT text, F file) {
            return new Builder(text).editFile(file).size(250,30);
        }
        public static <F extends CustomFile<F>> FileBuilder<F> fileEditor(CTxT text, F file, CustomImage image) {
            return new Builder(text).editFile(file).size(250,30).image(image);
        }
    }

    public enum ButtonType {
        DISABLED,
        CLOSE,
        OPEN_SCREEN,
        OPEN_LINK,
        EDIT_FILE
    }

    /**
     * Builder CRTP
     */
    public abstract static class AbstractBuilder<T extends AbstractBuilder<T>> {
        protected CTxT text;
        boolean hideText = false;
        protected int width = 150, height = 20;
        protected CustomImage customImage;

        protected ButtonType buttonType;
        protected Screen screen;
        protected URI uri;

        protected AbstractBuilder(CTxT buttonText) {
            this.text = buttonText;
            buttonType = ButtonType.DISABLED;
        }

        protected abstract T self();

        /**
         * changes the settings of the builder to be optimal for a 20x20 mini button <br>
         * sets the size to 20x20, uses the image, and hides the button text
         * @param customImage a custom image, (15x15 preferred)
         */
        public T miniIcon(CustomImage customImage) {
            this.customImage = customImage;
            this.width = 20;
            this.height = 20;
            this.hideText = true;
            return self();
        }

        public T image(Identifier texture, int width, int height) {
            this.customImage = new CustomImage(texture, width, height);
            return self();
        }

        public T image(CustomImage customImage) {
            this.customImage = customImage;
            return self();
        }

        public T size(int width, int height) {
            this.width = width;
            this.height = height;
            return self();
        }

        public T hideText(boolean hideText) {
            this.hideText = hideText;
            return self();
        }

        public abstract SimpleButton build();
    }

    /**
     * Simple Button builder
     */
    public static class Builder extends AbstractBuilder<Builder> {

        public Builder(CTxT buttonText) {
            super(buttonText);
        }

        @Override
        protected Builder self() {
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

        // switch to a screen builder
        public <S extends Screen & SetParentScreen> ScreenBuilder<S> openScreen(S screen) {
            return new ScreenBuilder<>(this, screen);
        }

        // switch to a file builder
        public <F extends CustomFile<F>> FileBuilder<F> editFile(F file) {
            return new FileBuilder<>(this, file);
        }

        public Builder close() {
            buttonType = ButtonType.CLOSE;
            return this;
        }

        public SimpleButton build() {
            return switch (buttonType) {
                case CLOSE -> new Close(text,hideText,width,height,customImage);
                case OPEN_LINK -> new OpenLink(text,hideText,width,height,customImage,uri);
                default -> new Disabled(text,hideText,width,height,customImage);
            };
        }
    }

    public static class ScreenBuilder <S extends Screen & SetParentScreen> extends AbstractBuilder<ScreenBuilder<S>> {
        protected S screen;

        protected ScreenBuilder(Builder base, S screen) {
            super(base.text);

            this.width = base.width;
            this.height = base.height;
            this.customImage = base.customImage;
            this.screen = screen;
        }

        @Override
        protected ScreenBuilder<S> self() {
            return this;
        }

        @Override
        public SimpleButton build() {
            return new OpenScreen<>(text,hideText,width,height,customImage,screen);
        }
    }

    /**
     * Simple Button Builder for Files
     */
    public static class FileBuilder<F extends CustomFile<F>> extends AbstractBuilder<FileBuilder<F>> {
        protected final F file;

        private FileBuilder(Builder base, F file) {
            super(base.text);

            this.width = base.width;
            this.height = base.height;
            this.customImage = base.customImage;
            this.file = file;
        }

        @Override
        protected FileBuilder<F> self() {
            return this;
        }

        @Override
        public SimpleButton build() {
            return new OpenScreen<>(text, hideText, width, height, customImage,
                    new UnderConstructionScreen<>(file));
        }
    }

    public static class Close extends SimpleButton {

        protected Close(CTxT text, boolean hideText, int width, int height, CustomImage customImage) {
            super(text, hideText, width, height, customImage);
        }

        @Override
        public TextureButtonWidget build(Screen openScreen) {
            TextureButtonWidget.Builder builder = new TextureButtonWidget.Builder(text.b()).size(width,height)
                    .onPress(b -> openScreen.close()).hideText(hideText);
            if (customImage != null) return builder.texture(customImage).build();
            return builder.build();
        }
    }

    public static class Disabled extends SimpleButton {

        protected Disabled(CTxT text, boolean hideText, int width, int height, CustomImage customImage) {
            super(text, hideText, width, height, customImage);
        }

        @Override
        public TextureButtonWidget build(Screen openScreen) {
            TextureButtonWidget.Builder builder = new TextureButtonWidget.Builder(text.b()).size(width,height).disabled(true).hideText(hideText);
            if (customImage != null) return builder.texture(customImage).build();
            return builder.build();
        }
    }

    public static class OpenLink extends SimpleButton {
        protected URI uri;

        public OpenLink(CTxT text, boolean hideText, int width, int height, CustomImage customImage, URI uri) {
            super(text, hideText, width, height, customImage);
            this.uri = uri;
        }

        @Override
        public TextureButtonWidget build(Screen openScreen) {
            TextureButtonWidget.Builder builder = new TextureButtonWidget.Builder(text.b(),ConfirmLinkScreen.opening(openScreen,uri.toString()))
                    .size(width,height).hideText(hideText);
            if (customImage != null) return builder.texture(customImage).build();
            return builder.build();
        }

    }

    public static class OpenScreen <S extends Screen & SetParentScreen> extends SimpleButton {
        protected S screen;

        public OpenScreen(CTxT text, boolean hideText, int width, int height, CustomImage customImage, S screen) {
            super(text, hideText, width, height, customImage);
            this.screen = screen;
        }

        @Override
        public <T extends Screen & SetClientScreen> TextureButtonWidget build(T openScreen) {
            screen.setParentScreen(openScreen);

            TextureButtonWidget.Builder builder = new TextureButtonWidget.Builder(text.b()).size(width,height)
                    .onPress(btn -> openScreen.setScreen(screen)).hideText(hideText);
            if (customImage != null) return builder.texture(customImage).build();
            return builder.build();
        }
    }
}
