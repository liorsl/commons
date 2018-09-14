package net.apartium.commons.bukkit.item;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;

public class ItemIndex {
	
	Material[][] arrmor;
	Material[][] tools;
	
	public ItemIndex(Material[][] arrmor, Material[][] tools) {
		Validate.notEmpty(arrmor, "arrmor +-");
		Validate.notEmpty(tools, "tools +-");

		this.arrmor = arrmor;
		this.tools = tools;
		
	}
	
}
