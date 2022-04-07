package me.kryniowesegryderiusz.kgenerators.classes;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import java.util.ArrayList;
import me.kryniowesegryderiusz.kgenerators.Main;
import me.kryniowesegryderiusz.kgenerators.api.interfaces.IHologramProvider;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class HolographicDisplaysProvider implements IHologramProvider {
	public void createHologram(Location location, ArrayList<String> lines) {
		Hologram hologram = HologramsAPI.createHologram((Plugin)Main.getInstance(), location);
		for (String s : lines)
			hologram.appendTextLine(s); 
	}
	
	public void updateHologramLine(Location location, int lineNr, String line) {
		for (Hologram hologram : HologramsAPI.getHolograms((Plugin)Main.getInstance())) {
			if (hologram.getLocation().equals(location)) {
				hologram.removeLine(lineNr);
				hologram.insertTextLine(lineNr, line);
			} 
		} 
	}
	
	public void removeHologram(Location location) {
		for (Hologram hologram : HologramsAPI.getHolograms((Plugin)Main.getInstance())) {
			if (hologram.getLocation().equals(location))
				hologram.delete(); 
		} 
	}
}
