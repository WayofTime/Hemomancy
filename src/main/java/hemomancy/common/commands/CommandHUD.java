package hemomancy.common.commands;

import hemomancy.Hemomancy;
import hemomancy.client.GuiHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandHUD extends CommandBase
{

	@Override
	public String getName() 
	{
		return "hemomancyHud";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) 
	{
		return "commands.hemomancy.hud";
	}

	@Override
	public void execute(ICommandSender icommandsender, String[] astring) throws PlayerNotFoundException 
	{
		EntityPlayerMP targetPlayer = getCommandSenderAsPlayer(icommandsender);
				
		targetPlayer.openGui(Hemomancy.instance, GuiHandler.HUD_GUI, targetPlayer.worldObj, (int)targetPlayer.posX, (int)targetPlayer.posY, (int)targetPlayer.posZ);
	}
}
