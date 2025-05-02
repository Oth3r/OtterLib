package one.oth3r.fabricTest.client.screen.utl;

import net.minecraft.util.Identifier;

public class CustomImage {
    protected final Identifier image;
    protected final int width;
    protected final int height;

    public CustomImage(Identifier image, int width, int height) {
        this.image = image;
        this.width = width;
        this.height = height;
    }

    public Identifier getImage() {
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
