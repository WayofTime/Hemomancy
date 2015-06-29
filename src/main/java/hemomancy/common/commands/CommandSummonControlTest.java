package hemomancy.common.commands;

import hemomancy.Hemomancy;
import hemomancy.client.GuiHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandSummonControlTest extends CommandBase
{
	@Override
	public String getName() 
	{
		return "hemomancySummon";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) 
	{
		return "commands.hemomancy.summon";
	}

	@Override
	public void execute(ICommandSender icommandsender, String[] astring) throws PlayerNotFoundException 
	{
		EntityPlayerMP targetPlayer = getCommandSenderAsPlayer(icommandsender);
				
		targetPlayer.openGui(Hemomancy.instance, GuiHandler.SUMMON_GUI, targetPlayer.worldObj, (int)targetPlayer.posX, (int)targetPlayer.posY, (int)targetPlayer.posZ);
	}
}
