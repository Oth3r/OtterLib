package one.oth3r.otterlib.chat;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rainbow {
    protected boolean enabled = false;
    protected float position = 0;
    protected float stepSize = 5;
    protected float brightness = 1;
    protected float saturation = 1;

    public Rainbow(boolean enabled, float position, float stepSize, float brightness, float saturation) {
        this.enabled = enabled;
        this.position = position;
        this.stepSize = stepSize;
        this.brightness = brightness;
        this.saturation = saturation;
    }

    public Rainbow(float position, float stepSize) {
        this.enabled = true;
        this.position = position;
        this.stepSize = stepSize;
    }

    public Rainbow() {}

    public Rainbow(Rainbow rainbow) {
        this.enabled = rainbow.enabled;
        this.position = rainbow.position;
        this.stepSize = rainbow.stepSize;
        this.brightness = rainbow.brightness;
        this.saturation = rainbow.saturation;
    }

    public Rainbow(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public float getPosition() {
        return position;
    }

    public void setPosition(float position) {
        this.position = position;
    }

    public float getStepSize() {
        return stepSize;
    }

    public void setStepSize(float stepSize) {
        this.stepSize = stepSize;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    /**
     * makes a string rainbow using the variables in the Rainbow class
     * @return a colorized rainbow string as a CTxT ArrayList<>
     */
    @SuppressWarnings("unchecked")
    public <T extends LoaderText<T>> ArrayList<LoaderText<T>> colorize(String target) {
        // if not enabled, don't send a string
        if (!enabled) return new ArrayList<>(List.of(new LoaderText<>(target)));

        // get the hue as the position
        float hue = position;

        // create the TxT to add too
        ArrayList<LoaderText<T>> rainbow = new ArrayList<>();

        // loop for the text length
        for (int i = 0; i < target.codePointCount(0, target.length()); i++) {
            // if empty, skip
            if (target.charAt(i) == ' ') {
                rainbow.add((LoaderText<T>) new LoaderText<>(" ").rainbow(null));
                continue;
            }

            // get the color from the hue, sat and brightness
            Color color = Color.getHSBColor(hue / 360.0f, saturation, brightness);

            // set a null rainbow so it doesn't get overwritten when copy changed methods runs
            T letter = (T) new LoaderText<>(Character.toString(target.codePointAt(i)))
                    .color(color).rainbow(null);
            rainbow.add(letter);

            // bump the hue
            hue = (hue+stepSize) % 360f;
        }
        // update the position to the new ending point
        position = hue;
        return rainbow;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Rainbow rainbow = (Rainbow) o;
        return enabled == rainbow.enabled && Float.compare(position, rainbow.position) == 0 && Float.compare(stepSize, rainbow.stepSize) == 0 && Float.compare(brightness, rainbow.brightness) == 0 && Float.compare(saturation, rainbow.saturation) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, position, stepSize, brightness, saturation);
    }
}
