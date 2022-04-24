package me.kryniowesegryderiusz.kgenerators.generators.recipes;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import me.kryniowesegryderiusz.kgenerators.Main;
import me.kryniowesegryderiusz.kgenerators.generators.generator.objects.Generator;
import me.kryniowesegryderiusz.kgenerators.generators.recipes.objects.Recipe;
import me.kryniowesegryderiusz.kgenerators.logger.Logger;
import me.kryniowesegryderiusz.kgenerators.utils.immutable.Config;
import me.kryniowesegryderiusz.kgenerators.utils.immutable.ConfigManager;

public class RecipesManager {
	
	private HashMap<Generator, Recipe> recipes = new HashMap<Generator, Recipe>();
	
	public RecipesManager(){
		Config file;

    	try {
    		file = ConfigManager.getConfig("recipes.yml", (String) null, true, false);
    		file.loadConfig();
		} catch (IOException | InvalidConfigurationException e) {
			Logger.error("Recipes file: Cant load recipes config. Disabling plugin.");
			Logger.error(e);
			Main.getInstance().getServer().getPluginManager().disablePlugin(Main.getInstance());
			return;
		}
    	
    	for(String generatorID: file.getConfigurationSection("").getKeys(false)) {
			new Recipe(this, file, generatorID);
    	}
    	
    	Logger.info("Recipes file: Loaded " + this.recipes.size() + " recipes");
	}
	
	public void add(Generator generator, Recipe recipe)
	{
		recipes.put(generator, recipe);
	}
	
	public Recipe get(Generator generator)
	{
		if (recipes.containsKey(generator))
			return recipes.get(generator);
		else
		{
			for (org.bukkit.inventory.Recipe bukkitRecipe : Main.getInstance().getServer().getRecipesFor(generator.getGeneratorItem()))
			{
				if (bukkitRecipe.getResult().equals(generator.getGeneratorItem()))
				{
					if (bukkitRecipe instanceof ShapedRecipe)
						return new Recipe(generator, (ShapedRecipe) bukkitRecipe);
					else if (bukkitRecipe instanceof ShapelessRecipe)
						return new Recipe(generator, (ShapelessRecipe) bukkitRecipe);
				}
			}
			return null;
		}
	}
}