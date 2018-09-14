package net.apartium.commons.reflect;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import net.apartium.commons.Validate;
import net.apartium.commons.bukkit.PluginUtils;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class ReflectionSearchUtils {

	private ReflectionSearchUtils() {}
	
	public static List<String> findPackageNamesStartingWith(String prefix) {
		Validate.notEmpty(prefix, "prefix +-");

		List<String> result = new ArrayList<>();
		for (Package p : Package.getPackages()) {
			if (p.getName().startsWith(prefix)) {
				result.add(p.getName());
			}
		}
		return result;
	}

	/**
	 * Scans all classes accessible from the context class loader which belong to
	 * the given package and subpackages.
	 *
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Class<?>[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		Validate.notEmpty(packageName, "packageName +-");

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		ClassLoader bukkitClassLoaders[] = PluginUtils.getAllPluginClassLoaders();

		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);

		List<File> dirs = new ArrayList<>();

		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}

		for (ClassLoader loader : bukkitClassLoaders) {
			Enumeration<URL> bResources = loader.getResources(path);

			while (bResources.hasMoreElements()) {
				URL resource = bResources.nextElement();
				dirs.add(new File(resource.getFile()));
			}

		}

		List<Class<?>> classes = new ArrayList<>();
		for (File directory : dirs)
			classes.addAll(findClasses(directory, packageName));

		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * Recursive method used to find all classes in a given directory and subdirs.
	 *
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	public static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
		Validate.notNull(directory, "directory +-");
		Validate.notEmpty(packageName, "packageName +-");

		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists())
			return classes;

		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(
						Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

}
