package one.oth3r.spigottest;

import one.oth3r.otterlib.chat.CTxT;
import one.oth3r.otterlib.chat.Rainbow;
import one.oth3r.otterlib.chat.Wrapper;
import one.oth3r.otterlib.chat.hover.HoverAction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.awt.*;

public class Events implements Listener {
    @EventHandler
    public static void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.spigot().sendMessage(new CTxT()
                .append(new CTxT("Hello").color(Color.BLUE).bold(true).strikethrough(true).wrapper())
                .append(new CTxT("World!!!!!!!!!").rainbow(new Rainbow(true)).underline(true).italic(true)
                        .hover(HoverAction.of(new CTxT("man this is hovering rn").bold())))
                .append(new CTxT("button").hover(HoverAction.of(new CTxT("hovering it up"))).color(Color.BLUE).wrapper(new Wrapper<>(new CTxT("{").color(Color.GREEN).bold(), new CTxT("}").italic())))
                .b());
    }
}
