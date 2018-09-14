package net.apartium.commons.bukkit.world;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import net.apartium.commons.Validate;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class LocationUtils {

	private LocationUtils() {}
	/**
	 * Checking if the specified locations have the same XYZ.
	 * 
	 * @param loc1
	 *            the first location.
	 * @param loc2
	 *            the second location.
	 * @return if the location have the same XYZ.
	 */
	public static boolean sameXYZ(Location loc1, Location loc2, Location... locations) {
		Validate.notNull(loc1, "loc1 +-");
		Validate.notNull(loc2, "loc2 +-");
		Validate.notEmpty(locations, "location +-");

		if (loc1 == null || loc2 == null)
			return false;

		double x = loc1.getX();
		double y = loc1.getY();
		double z = loc1.getZ();

		if (loc2.getX() != x || loc2.getY() != y || loc2.getZ() != z)
			return false;

		for (Location loc : locations)
			if (loc.getX() != x || loc.getY() != y || loc.getZ() != z)
				return false;

		return true;
	}

	/**
	 * Checking if the locations is in the same world
	 * 
	 * @param loc1
	 *            the first location
	 * @param loc2
	 *            the second location
	 * @return true if those locations are in the same world, else false
	 */
	public static boolean sameWorld(Location loc1, Location loc2) {
		Validate.notNull(loc1, "loc1 +-");
		Validate.notNull(loc2, "loc2 +-");

		if (loc1 == null || loc2 == null)
			return false;

		return loc1.getWorld().equals(loc2.getWorld());
	}

	/**
	 * Paste non relative map
	 * 
	 * @param blocks
	 *            the map containing the blocks
	 */
	@SuppressWarnings("deprecation")
	public static void pasteRaw(Map<Location, Entry<Material, Byte>> blocks) {
		for (Entry<Location, Entry<Material, Byte>> entry : blocks.entrySet()) {
			Block block = entry.getKey().getBlock();
			block.setType(entry.getValue().getKey());
			block.setData(entry.getValue().getValue());

		}
	}

	/**
	 * Get all the locations inside two poses
	 * 
	 * @param pos1
	 *            the first position
	 * @param pos2
	 *            the second position
	 * @return the locations between those positions, null if they are not at the
	 *         same world
	 */
	public static Set<Location> getLocationsBetween(Location pos1, Location pos2) {
		Validate.notNull(pos1, "pos1 +-");
		Validate.notNull(pos2, "pos2 +-");

		if (!sameWorld(pos1, pos2))
			return null;

		Set<Location> set = new HashSet<>();

		int maxX, minX, maxY, minY, maxZ, minZ;
		World world = pos1.getWorld();

		if (pos1.getBlockX() > pos2.getBlockX()) {
			maxX = pos1.getBlockX();
			minX = pos2.getBlockX();
		} else {
			maxX = pos2.getBlockX();
			minX = pos1.getBlockX();
		}

		if (pos1.getBlockZ() > pos2.getBlockZ()) {
			maxZ = pos1.getBlockZ();
			minZ = pos2.getBlockZ();
		} else {
			maxZ = pos2.getBlockZ();
			minZ = pos1.getBlockZ();
		}

		if (pos1.getBlockY() > pos2.getBlockY()) {
			maxY = pos1.getBlockY();
			minY = pos2.getBlockY();
		} else {
			maxY = pos2.getBlockY();
			minY = pos1.getBlockY();
		}

		for (int x = minX; x < maxX; x++)
			for (int y = minY; y < maxY; y++)
				for (int z = minZ; z < maxZ; z++)
					set.add(new Location(world, x, y, z));

		return set;
	}

}