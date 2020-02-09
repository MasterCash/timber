package io.github.mastercash.timber.events;

import io.github.mastercash.timber.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;



public class LogBreakEvent implements Listener {

    // EventHandler for player breaking a log.
    @EventHandler
    public void onPlayerBreakLog(BlockBreakEvent event) {
        //Variables
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack tool;
        Queue<Block> blockQueue = new LinkedList<Block>();
        int maxDurability;
        int durability;

        // Initial Checks
        if(player == null) return;
        else tool = player.getInventory().getItemInMainHand();

        player.sendMessage("Running timber");

        if(!Utils.isAxe(tool)) return;
        if(!Utils.isLogBlock(block)) return;
        if (!player.isSneaking()) return;
        // Everything is in order.

        player.sendMessage(("Attempting to break stuff"));
        // Get the durability of the current tool.
        durability = ((Damageable) tool.getItemMeta()).getDamage();

        // Add the starting block to the queue
        blockQueue.add(block);

        // Get the Max Durability of the current tool.
        maxDurability = tool.getType().getMaxDurability();


        // Keep Looping through all the blocks till there are none left to check
        // or durability has run out.
        while(!blockQueue.isEmpty() && maxDurability - durability > 0) {
            // Get a block from the queue.
            Block temp = blockQueue.remove();
            // ItemMeta for the tool.
            Damageable meta;

            //Deal with overlap.
            if(!Utils.isLogBlock(temp)) continue;

            // Add Neighbors to check.
            Utils.addNeighbors(blockQueue, temp);


            if (Utils.CoreProtect != null) {
                Utils.CoreProtect.logRemoval(player.getName(), temp.getLocation(), temp.getType(), temp.getBlockData());
            }
            temp.breakNaturally(tool);

            // Update the durability of the tool.
            // Get current ItemMeta of tool.
            meta = (Damageable) tool.getItemMeta();

            // Set new durability.
            // If unbreaking enchantment, chance that it doesn't take damage.
            if(new Random().nextInt(100) < (100/(tool.getEnchantmentLevel(Enchantment.DURABILITY) + 1))) {
                meta.setDamage(meta.getDamage() + 1);
                tool.setItemMeta((ItemMeta) meta);
            }

            durability = ((Damageable) tool.getItemMeta()).getDamage();
        }

    }
}
