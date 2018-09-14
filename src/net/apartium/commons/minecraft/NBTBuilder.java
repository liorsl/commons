package net.apartium.commons.minecraft;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import net.apartium.commons.minecraft.NBTUtils.NBTTagInteracter;

public class NBTBuilder {
	
	@Getter
	NBTTagInteracter
			tag;
	
	public NBTBuilder(NBTTagInteracter tag) {
		this.tag = tag;
		
	}
	
	public NBTBuilder() {
		this(new NBTTagInteracter(NBTUtils.createNBTTag()));
	}
	
	public NBTBuilder(ItemStack item) {
		this(new NBTTagInteracter(item));
	}
	
	public NBTBuilder(Object tag) {
		this(new NBTTagInteracter(tag));
	}
	
	public NBTBuilder addAll(Map<String, Object> map) {
		for (Entry<String, Object> entry : map.entrySet()) 
			tag.set(entry.getKey(), entry.getValue());
		
		return this;
	}
	
	public NBTBuilder set(String key, Object value) {
		tag.set(key, value);
		return this;
	}
		
	public NBTBuilder apply(ItemStack itemStack) {
		try {
			NBTUtils.setNBTTag(itemStack, this);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return this;
	}
	
}
