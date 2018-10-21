package io.github.mastercash.timber.events;

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

        if(!isAxe(tool)) return;
        if(!isLogBlock(block)) return;
        if(!isBlockBelowDirt(block)) return;

        // Everything is in order.

        // Add the starting block to the queue
        blockQueue.add(block);

        // Get the Max Durability of the current tool.
        maxDurability = tool.getType().getMaxDurability();

        // Get the durability of the current tool.
        durability = ((Damageable) tool.getItemMeta()).getDamage();

        // Keep Looping through all the blocks till there are none left to check
        // or durability has run out.
        while(!blockQueue.isEmpty() && maxDurability - durability > 0) {
            // Get a block from the queue.
            Block temp = blockQueue.remove();
            // ItemMeta for the tool.
            Damageable meta;

            // Add Neighbors to check.
            addNeighbors(blockQueue, block);

            // Break the current block.
            temp.breakNaturally(tool);

            // Update the durability of the tool.
            // Get current ItemMeta of tool.
            meta = (Damageable) tool.getItemMeta();

            // Set new durability.
            // If unbreaking enchantment, chance that it doesn't take damage.
            if(new Random().nextInt(100) < (100/(tool.getEnchantmentLevel(Enchantment.DURABILITY) - 1))) {
                meta.setDamage(meta.getDamage() - 1);
                tool.setItemMeta((ItemMeta) meta);
            }

            durability = ((Damageable) tool.getItemMeta()).getDamage();
        }



    }


    // Helper Functions
    // TODO: move these to a separate helper class.

    // Check to see if the item is an axe.
    // TODO: set this up as a setting in config.yml to allow changes.
    private boolean isAxe(ItemStack tool) {
        switch (tool.getType()) {
            case DIAMOND_AXE:
            case GOLDEN_AXE:
            case IRON_AXE:
            case STONE_AXE:
            case WOODEN_AXE:
                return true;
            default:
                return false;
        }
    }

    // TODO: set this up as a setting in config.yml to allow changes.
    private boolean isLogBlock(Block block) {
        switch (block.getType()) {
            case ACACIA_LOG:
            case BIRCH_LOG:
            case DARK_OAK_LOG:
            case JUNGLE_LOG:
            case OAK_LOG:
            case SPRUCE_LOG:
            case STRIPPED_ACACIA_LOG:
            case STRIPPED_BIRCH_LOG:
            case STRIPPED_DARK_OAK_LOG:
            case STRIPPED_JUNGLE_LOG:
            case STRIPPED_OAK_LOG:
            case STRIPPED_SPRUCE_LOG:
                return true;
            default:
                return false;
        }
    }


    // Check to see if the block below is a dirt block, this is to try
    // and stop breaking everything but trees.
    // TODO: set this up as a setting in config.yml to allow changes.
    private boolean isBlockBelowDirt(Block block) {
        Block below = block.getRelative(BlockFace.DOWN);

        switch (below.getType()) {
            case DIRT:
            case GRASS_BLOCK:
            case COARSE_DIRT:
                return true;
            default:
                return false;
        }
    }

    // Add the Neighbors of a block to the queue
    // TODO: make more efficient, remove overlap.
    private void addNeighbors(Queue<Block> blocks, Block block) {
        // Get block above so we can check the upper layer too for upper Diagonals.
        Block above = block.getRelative(BlockFace.UP);

        for(BlockFace face : BlockFace.values()) {
            // Don't want lower blocks as we start from the bottom up.
            if(face.getModY() < 0) continue;

            // Remove the blocks that are neither diagonal or adjacent.
            if(Math.abs(face.getModX()) > 1) continue;
            if(Math.abs(face.getModY()) > 1) continue;
            if(Math.abs(face.getModZ()) > 1) continue;

            // Add any block that is a log to the queue around the current block
            if(isLogBlock(block.getRelative(face))) {
                blocks.add(block.getRelative(face));
            }

            if(isLogBlock(above.getRelative(face))) {
                blocks.add(above.getRelative(face));
            }
        }
    }
}
