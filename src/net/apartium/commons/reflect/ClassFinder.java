package net.apartium.commons.reflect;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import net.apartium.commons.Validate;

public class ClassFinder {
	
	@Getter
	final ClassLoader
			classLoader;
	
	Set<ClassInfo>
			set = new HashSet<>();
	
	public ClassFinder(ClassLoader classLoader) { 
		Validate.notNull(classLoader, "classLoader +-");
		this.classLoader = classLoader;
		
		loadResources();
		
	}
	
	void loadResources() {
		
	}
	
	public class ClassInfo {
		
	}
}
