package com.yujibolt90.xrayalerts;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class XrayAlerts extends JavaPlugin implements Listener{
	
	Map<Player, Integer> playerDiamondCount = new HashMap<Player, Integer>();
	Map<Player, Integer> playerNetheriteCount = new HashMap<Player, Integer>();
	
	@Override
    public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("XrayAlerts has been enabled!");
    }
    
    @Override
    public void onDisable() {
    	getLogger().info("XrayAlerts has been disabled!");
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
    	
    	Material blockType = event.getBlock().getType();
    	
    	if (!(playerDiamondCount.containsKey(event.getPlayer())))
    		playerDiamondCount.put(event.getPlayer(),0);
    	
    	if (!(playerNetheriteCount.containsKey(event.getPlayer())))
    		playerNetheriteCount.put(event.getPlayer(),0);
    	
    	if (playerDiamondCount.get(event.getPlayer())==0|playerNetheriteCount.get(event.getPlayer())==0) {
    		runTimer(event.getPlayer());
    	}
    	
    	if (blockType.equals(Material.DIAMOND_ORE)) {
    		playerDiamondCount.put(event.getPlayer(),playerDiamondCount.get(event.getPlayer())+1);
    		getLogger().info(event.getPlayer().getName() + " has found diamond!");
    	}
    	
    	if (blockType.equals(Material.ANCIENT_DEBRIS)) {
    		playerNetheriteCount.put(event.getPlayer(),playerNetheriteCount.get(event.getPlayer())+1);
    		getLogger().info(event.getPlayer().getName() + " has found ancient debris!");
    	}
    }
    
    public void runTimer(Player p) {
    	CountdownTimer timer = new CountdownTimer(XrayAlerts.getPlugin(XrayAlerts.class),
	    	    60,
	    	    () -> {
	    	    },
	    	    () -> {
	    	        //Finish
	    	    	if (playerDiamondCount.containsKey(p)) {
		    	    	if (playerDiamondCount.get(p)>10) {
		    	    		for( Player player : Bukkit.getOnlinePlayers()) {
		    	        		if (player.isOp()) {
		    	        			player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + p.getPlayer().getName() + ChatColor.GOLD + ChatColor.BOLD + " has found " + ChatColor.BOLD + playerDiamondCount.get(p) + " Diamonds in the past " + ChatColor.RED + "" + ChatColor.BOLD + "60 seconds!");
		    	        		}
		    	        	}
		    	    	}
		    	    	if (playerDiamondCount.containsKey(p))
		    	    		playerDiamondCount.remove(p);
	    	    	}
	    	    	if (playerNetheriteCount.containsKey(p)) {
		    	    	if (playerNetheriteCount.get(p)>5) 
		    	    		for( Player player : Bukkit.getOnlinePlayers()) {
		    	        		if (player.isOp()) {
		    	        			player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + p.getPlayer().getName() + ChatColor.GOLD + ChatColor.BOLD + " has found " + ChatColor.BOLD + playerNetheriteCount.get(p) + " Ancient Debris in the past " + ChatColor.RED + "" + ChatColor.BOLD + "60 seconds!");
		    	        		}
		    	        	}
		    	    	if (playerNetheriteCount.containsKey(p))
		    	    		playerNetheriteCount.remove(p);
	    	    	}
	    	    },
	    	    (t) -> {
	    	    if (playerDiamondCount.containsKey(p)|playerNetheriteCount.containsKey(p)) {
	    	    	if (playerDiamondCount.get(p)>=20|playerNetheriteCount.get(p)>=10) {
	    	    		p.kickPlayer("[XrayAlerts] You were caught Xraying!");
	    	    		playerDiamondCount.put(p,0);
	    	    		playerNetheriteCount.put(p,0);
	    	    		}
	    	    	}
	    	    }
	    	 );	timer.scheduleTimer(); //start timer
    	}
}