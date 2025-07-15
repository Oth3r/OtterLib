package one.oth3r.fabricTest;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import one.oth3r.otterlib.chat.CTxT;
import one.oth3r.otterlib.chat.Rainbow;
import one.oth3r.otterlib.registry.CustomFileReg;

import java.awt.*;

public class FabricTest implements ModInitializer {
    public static final String MOD_ID = "fabric-test";

    @Override
    public void onInitialize() {
        CustomFileReg.registerFile(MOD_ID, new CustomFileReg.FileEntry(TestFile.ID, new TestFile(), true, true));

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            handler.player.sendMessage(new CTxT("Hello").color(Color.BLUE).bold(true).strikethrough(true)
                    .append(new CTxT("World!!!!!!!!!").rainbow(new Rainbow(true)).underline(true).italic(true)).b());
            });
    }

}
