package one.oth3r.fabricTest.mixin;

import net.minecraft.client.MinecraftClient;
import one.oth3r.fabricTest.FabricTest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class WindowFocusMixin {


    @Inject(method = "onWindowFocusChanged", at = @At("HEAD"))
    private void onWindowFocusChanged(boolean focused, CallbackInfo ci) {
        FabricTest.setIsFocused(focused);
    }
}
