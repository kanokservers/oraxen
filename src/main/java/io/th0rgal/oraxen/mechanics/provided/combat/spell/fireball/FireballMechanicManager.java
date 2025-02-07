package io.th0rgal.oraxen.mechanics.provided.combat.spell.fireball;

import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.mechanics.MechanicFactory;
import io.th0rgal.oraxen.utils.timers.Timer;
import io.th0rgal.protectionlib.ProtectionLib;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class FireballMechanicManager implements Listener {

    private final MechanicFactory factory;

    public FireballMechanicManager(MechanicFactory factory) {
        this.factory = factory;
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        String itemID = OraxenItems.getIdByItem(item);
        FireballMechanic mechanic = (FireballMechanic) factory.getMechanic(item);
        Block block = event.getClickedBlock();
        Location location = block != null ? block.getLocation() : player.getLocation();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.useInteractedBlock() == Event.Result.ALLOW) return;
        if (event.useItemInHand() == Event.Result.DENY) return;
        if (!ProtectionLib.canUse(player, location)) return;
        if (factory.isNotImplementedIn(itemID)) return;
        if (mechanic == null) return;

        Timer playerTimer = mechanic.getTimer(player);

        if (!playerTimer.isFinished()) {
            mechanic.getTimer(player).sendToPlayer(player);
            return;
        }

        playerTimer.reset();

        Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setYield((float) mechanic.getYield());
        fireball.setDirection(fireball.getDirection().multiply(mechanic.getSpeed()));

        mechanic.removeCharge(item);
    }
}
