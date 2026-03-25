package one.oth3r.otterlib.mixin;

import com.mojang.blaze3d.platform.Window;
import one.oth3r.otterlib.client.screen.utl.ScreenUtl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class WindowFocusMixin {

    @Inject(method = "onFocus", at = @At("HEAD"))
    private void onWindowFocusChanged(long handle, boolean focused, CallbackInfo ci) {
        ScreenUtl.setFocused(focused);
    }
}
