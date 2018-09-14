package net.apartium.commons.minecraft;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.apartium.commons.ExceptionHandler;
import net.apartium.commons.bukkit.PlayerUtils;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class PlayerSelectorWrapper {

	private PlayerSelectorWrapper() {}
	
	public static Player[] getPlayers(CommandSender sender, String arg) {
		List<Player> list = new ArrayList<>();
		try {
			Object[] entityPlayers = (Object[]) MethodUtils.invokeStaticMethod(NMSUtils.NMS_PLAYERSELECTOR, 
					"getPlayers", NMSUtils.getHandle(sender), arg);
			
			for (Object obj : entityPlayers)
				list.add(Bukkit.getPlayer(PlayerUtils.getName(obj)));
			
			return list.toArray(new Player[list.size()]);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			ExceptionHandler.getInstance().handle(e);
			return new Player[0];
		}
	}
}
