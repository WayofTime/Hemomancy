package hemomancy.common.spells;

import hemomancy.api.spells.SpellSituation;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.util.LevelHandler;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

public class ProficiencyHandler 
{
	public static void handleSuccessfulSpellCast(EntityPlayer player, List<SpellToken> tokenList, SpellSituation situation)
	{		System.out.println("" + situation);

		if(player == null || tokenList.isEmpty())
		{
			return;
		}
		
		
		float exp = 0;
		
		for(SpellToken token : tokenList)
		{
			exp += Math.abs(token.expForSituationSuccess(situation));
		}
		
		int intExp = (int)exp;
		LevelHandler.increaseExpOfPlayer(player, intExp + (player.worldObj.rand.nextFloat() < (exp - intExp) ? 1 : 0));
	}
}
