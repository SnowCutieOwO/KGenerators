package me.kryniowesegryderiusz.kgenerators.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;

import me.kryniowesegryderiusz.kgenerators.Main;
import me.kryniowesegryderiusz.kgenerators.data.enums.DatabaseType;
import me.kryniowesegryderiusz.kgenerators.generators.generator.objects.GeneratorAction;
import me.kryniowesegryderiusz.kgenerators.generators.locations.handlers.enums.ActionType;
import me.kryniowesegryderiusz.kgenerators.generators.locations.handlers.enums.InteractionType;
import me.kryniowesegryderiusz.kgenerators.logger.Logger;
import me.kryniowesegryderiusz.kgenerators.settings.Settings;
import me.kryniowesegryderiusz.kgenerators.utils.immutable.Config;
import me.kryniowesegryderiusz.kgenerators.utils.immutable.ConfigManager;
import me.kryniowesegryderiusz.kgenerators.xseries.XUtils;

public class FilesConverter {
	
	public static void updateConfig(Settings settings)
	{
		File file = new File(Main.getInstance().getDataFolder(), "config.yml");
		Config config;
		try {
    		config = ConfigManager.getConfig("config.yml", (String) null, false, true);
			config.loadConfig();
		} catch (IOException | InvalidConfigurationException e) {
			Logger.error("FilesConverter: Cant load config. Disabling plugin.");
			Logger.error(e);
			Main.getInstance().getServer().getPluginManager().disablePlugin(Main.getInstance());
			return;
		}
		
		if (!config.contains("lang"))
		{
			add(file, "#KGenerators made by Kryniowesegryderiusz (Krynio_ on spigot)");
			add(file, "#https://www.spigotmc.org/resources/79246/");
			add(file, "#Need help? Join KGenerators discord: https://discord.gg/BZRjhpP4Ab");
			add(file, "");
			add(file, "#Choose lang file. Default possibilities are en, pl, ro, vi");
			add(file, "lang: " + settings.getLang());
			Logger.info("FilesConverter: Added lang settings to config file");
		}
		
		if (!config.contains("database"))
		{
			add(file, "#Database settings");
			add(file, "database:");
			add(file, "  #Possible options: SQLITE, MYSQL, YAML (not recommended for big servers)");
			add(file, "  #You can convert data from one database to another! More info here: https://github.com/Kryniowesegryderiusz/KGenerators/wiki/Converting-databases ");
			add(file, "  dbtype: YAML");
			add(file, "  #Settings for MYSQL");
			add(file, "  host: hostname");
			add(file, "  port: 3306");
			add(file, "  database: database");
			add(file, "  username: username");
			add(file, "  password: password");
			Logger.info("FilesConverter: Added database settings to config file");
			settings.setDbType(DatabaseType.YAML);
			Logger.warn("FilesConverter: Changed database type from default to YAML");
		}
		
		if (!config.contains("can-generate-instead"))
		{
			add(file, "");
			add(file, "#By default generator will generate block only on air. Here you can add blocks, which will be replaced by generated block.");
			add(file, "can-generate-instead:");
			for (ItemStack i : settings.getGeneratingWhitelist())
			{
				add(file, "  - " + i.getType().toString());
			}
			Logger.info("FilesConverter: Added can-generate-instead settings to config file");
		}
		
		if (!config.contains("disabled-worlds"))
		{
			add(file, "");
			add(file, "#List of worlds, where generators will not be enabled:");
			add(file, "disabled-worlds:");
			add(file, "- test_world");
			Logger.info("FilesConverter: Added disabled-worlds settings to config file");
		}
		
		if (!config.contains("pick-up-to-eq"))
		{
			add(file, "");
			add(file, "#Should generators be picked up directly to equipment?");
			add(file, "pick-up-to-eq: true");
			Logger.info("FilesConverter: Added pick-up-to-eq settings to config file");
		}
		
		if (!config.contains("generators-actionbar-messages"))
		{
			add(file, "");
			add(file, "#Should \"generators\" lang section be sent by actionbar instad of chat?");
			add(file, "generators-actionbar-messages: true");
			Logger.info("FilesConverter: Added generators-actionbar-messages settings to config file");
		}
		
		if (!config.contains("explosion-handler"))
		{
			add(file, "");
			add(file, "#How explosions should be handled, if there is generator inside explode area?");
			add(file, "#0 - cancel explosion");
			add(file, "#1 - drop generator");
			add(file, "#2 - remove generator");
			add(file, "explosion-handler: 0");
			Logger.info("FilesConverter: Added explosion-handler settings to config file");
		}
		
		if (!config.contains("actions"))
		{
			add(file, "");
			add(file, "#This configuration section is for configuring actions needed for particular features");
			add(file, "#Possible modes: BREAK (avaible only for pick-up), LEFT_CLICK, RIGHT_CLICK, NONE (ex. because of gui usage)");
			add(file, "#Item could be \"ANY\"");
			add(file, "#Sneak indicates if shift pressed is required");
			add(file, "actions:");
			add(file, "  #Action, which will be used for picking up generators");
			add(file, "  pick-up:");
			add(file, "    mode: " + settings.getActions().getGeneratorAction(ActionType.PICKUP).getInteraction().toString());
			if (settings.getActions().getGeneratorAction(ActionType.PICKUP).getItem() != null)
				add(file, "    item: " + settings.getActions().getGeneratorAction(ActionType.PICKUP).getItem().getType().toString());
			else
				add(file, "    item: ANY");
			add(file, "    sneak: " + String.valueOf(settings.getActions().getGeneratorAction(ActionType.PICKUP).isSneak()));
			add(file, "  #Action, which will be used for opening generator gui");
			add(file, "  open-gui:");
			add(file, "    mode: " + settings.getActions().getGeneratorAction(ActionType.OPENGUI).getInteraction().toString());
			if (settings.getActions().getGeneratorAction(ActionType.PICKUP).getItem() != null)
				add(file, "    item: " + settings.getActions().getGeneratorAction(ActionType.OPENGUI).getItem().getType().toString());
			else
				add(file, "    item: ANY");
			add(file, "    sneak: " + String.valueOf(settings.getActions().getGeneratorAction(ActionType.OPENGUI).isSneak()));
			add(file, "  #Action, which will be used for checking how much time left before regeneration");
			add(file, "  time-left-check:");
			add(file, "    mode: " + settings.getActions().getGeneratorAction(ActionType.TIMELEFT).getInteraction().toString());
			if (settings.getActions().getGeneratorAction(ActionType.TIMELEFT).getItem() != null)
				add(file, "    item: " + settings.getActions().getGeneratorAction(ActionType.PICKUP).getItem().getType().toString());
			else
				add(file, "    item: ANY");
			add(file, "    sneak: " + String.valueOf(settings.getActions().getGeneratorAction(ActionType.TIMELEFT).isSneak()));
			Logger.info("FilesConverter: Added actions settings to config file");
		}
		
		if (!config.contains("sounds"))
		{
			add(file, "");
			add(file, "#Here you can choose which sounds should be played on each action.");
			add(file, "#ALWAYS use names from https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html");
			add(file, "sounds:");
			add(file, "  place:");
			add(file, "    #Type sound name or \"NONE\" to disable sound");
			add(file, "    sound: BLOCK_ANVIL_LAND");
			add(file, "    #Volume should be a number between 0.0 and 10.0 (1.0 is normal)");
			add(file, "    volume: 0.2");
			add(file, "    #Volume should be a number between 0.0 and 2.0 (0.0 is normal)");
			add(file, "    pitch: 0.0");
			add(file, "  pick-up:");
			add(file, "    sound: ENTITY_BAT_TAKEOFF");
			add(file, "    volume: 0.3");
			add(file, "    pitch: 0.0");
			add(file, "  upgrade:");
			add(file, "    sound: ENTITY_PLAYER_LEVELUP");
			add(file, "    volume: 0.3");
			add(file, "    pitch: 0.0");
			Logger.info("FilesConverter: Added sounds settings to config file");
		}
		
		if (!config.contains("intervals"))
		{
			add(file, "");
			add(file, "#These settings are related to performance and its not recommended to set them too low");
			add(file, "#Values are presented in ticks (20 ticks = 1 second)");
			add(file, "#Changed values need server restart to affect plugin functioning");
			add(file, "intervals:");
			add(file, "  #How often should generators check if they should regenerate?");
			add(file, "  #All generator delays should be a multiple of this number");
			add(file, "  generation-check: 10");
			add(file, "  #How often should holograms be updated?");
			add(file, "  hologram-update: 20");
			add(file, "  #How often should generators guis be updated?");
			add(file, "  #Set -1 to not update GUI");
			add(file, "  gui-update: 20");
			Logger.info("FilesConverter: Added intervals settings to config file");
		}
	}
	
	private static void add(File file, String string)
	{
		FilesUtils.addToFile(file, string);
	}	
}
