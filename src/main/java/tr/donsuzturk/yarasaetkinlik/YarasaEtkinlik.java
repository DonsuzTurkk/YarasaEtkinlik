package tr.donsuzturk.yarasaetkinlik;

import org.bukkit.*;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class YarasaEtkinlik extends JavaPlugin implements Listener {

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveConfig();
        getLogger().info("Eklenti aktif edildi!");
    }

    @EventHandler
    public void yarasaOldurme(final EntityDeathEvent event) {
        if (event.getEntity() instanceof Bat) {
            event.getDrops().clear();
            event.setDroppedExp(0);
            event.getDrops().add(new ItemStack(Material.valueOf(getConfig().getString("Odul")), getConfig().getInt("Miktar")));
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    final Firework f = (Firework)event.getEntity().getWorld().spawn(event.getEntity().getLocation(), (Class)Firework.class);
                    final FireworkMeta fm = f.getFireworkMeta();
                    fm.addEffect(FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.STAR).withColor(Color.AQUA).withColor(Color.RED).withFade(Color.WHITE).build());
                    fm.setPower(0);
                    f.setFireworkMeta(fm);
                }
            }, 20L);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void alinca(final PlayerPickupItemEvent event) {
        final Item yeredus = event.getItem();
        if (event.getItem().getItemStack().getType() == Material.valueOf(getConfig().getString("Odul"))) {
            event.getPlayer().giveExpLevels(1);
            event.getPlayer().setHealth(20.0);
            event.getPlayer().setFoodLevel(20);
            event.getPlayer().getWorld().playEffect(event.getPlayer().getLocation(), Effect.MOBSPAWNER_FLAMES, 3);
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 3));
            yeredus.remove();
        }
    }
}
