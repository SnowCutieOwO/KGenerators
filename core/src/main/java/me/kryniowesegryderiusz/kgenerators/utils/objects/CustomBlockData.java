package me.kryniowesegryderiusz.kgenerators.utils.objects;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.kryniowesegryderiusz.kgenerators.Main;
import me.kryniowesegryderiusz.kgenerators.utils.ItemUtils;
import me.kryniowesegryderiusz.kgenerators.xseries.XMaterial;

public class CustomBlockData {

	@Getter XMaterial xMaterial;
	
	public CustomBlockData(XMaterial xMaterial){
		this.xMaterial = xMaterial;
	}
	
	public void setBlock(Location location) {
		Main.getMultiVersion().getBlocksUtils().setBlock(location, xMaterial);
	}
	
	public ItemStack getItem() {
		return this.xMaterial.parseItem();
	}
	
	public String toString() {
		return "Material: " + this.xMaterial.name();
	}
	
	public static CustomBlockData load(String configString, String place) {
		if (configString.contains(":")) {
			String[] splitted = configString.split(":");
			if (splitted[0].contains("customhead"))
				return new HeadCustomBlockData(XMaterial.PLAYER_HEAD, splitted[1]);
		}
		
		return new CustomBlockData(ItemUtils.getXMaterial(configString, place+" CustomBlockData:", true));
		
	}
}
