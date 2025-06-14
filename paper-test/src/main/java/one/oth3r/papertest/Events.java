package one.oth3r.papertest;

import one.oth3r.otterlib.chat.CTxT;
import one.oth3r.otterlib.chat.Rainbow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.awt.*;

public class Events implements Listener {
    @EventHandler
    public static void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(new CTxT("Hello").color(Color.BLUE).bold(true).strikethrough(true)
                .append(new CTxT("World!!!!!!!!!").rainbow(new Rainbow(true)).underline(true).italic(true)).b());
    }
}
