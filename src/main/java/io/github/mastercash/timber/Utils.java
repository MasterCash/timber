package io.github.mastercash.timber;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Queue;

import static org.bukkit.Bukkit.getServer;

public class Utils {
    public static CoreProtectAPI CoreProtect = null;

    public static CoreProtectAPI getCoreProtect() {
        Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");

        if (plugin == null || !(plugin instanceof CoreProtect)) {
            getServer().getConsoleSender().sendMessage("[Timber] No CoreProtect");
            return null;
        }

        CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();

        if (CoreProtect.isEnabled() == false) {
            getServer().getConsoleSender().sendMessage("[Timber] No CoreProtectAPI");
            return null;
        }
        if (CoreProtect.APIVersion() < 6) {
            getServer().getConsoleSender().sendMessage("[Timber] No CoreProtectAPI v6. Is: " + CoreProtect.APIVersion());
            return null;
        }

        return CoreProtect;
    }
    public static boolean isAxe(ItemStack tool) {
        boolean isAxe = tool.getType().name().endsWith("_AXE");
        return isAxe;
    }

    public static boolean isLogBlock(Block block) {
        boolean isLog = block.getType().name().endsWith("_LOG");
        return isLog;
    }

    public static void addNeighbors(Queue<Block> blocks, Block block) {
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
