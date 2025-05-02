package one.oth3r.fabricTest;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import one.oth3r.otterlib.chat.CTxT;
import one.oth3r.otterlib.chat.Rainbow;

import java.awt.*;

public class FabricTest implements ModInitializer {
    public static final String MOD_ID = "fabric-test";

    public static final TestFile testFile = new TestFile();

    @Override
    public void onInitialize() {
        testFile.load();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            handler.player.sendMessage(new CTxT("Hello").color(Color.BLUE).bold(true).strikethrough(true)
                    .append(new CTxT("World!!!!!!!!!").rainbow(new Rainbow(true)).underline(true).italic(true)).b());
            });
    }

}
