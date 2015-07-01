package hemomancy.common.spells.focus;

import hemomancy.api.ApiUtils;
import hemomancy.api.events.SpellCastEvent;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.ISelfToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.api.spells.summon.ISummonToken;
import hemomancy.common.entity.mob.EntitySummon;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class SummonFocusToken extends SpellToken implements IFocusToken
{
    private List<SpellToken> tokenList = new ArrayList<SpellToken>();

    public float manaCost = 0;
    public float bloodCost = 0;
    public int focusUseDuration = 20;

    public SummonFocusToken()
    {
        super("textures/tokens/SelfToken.png");
        setUnlocalizedName("token.tokenSummon.name");
    }

    @Override
    public void addSpellTokenToFocus(SpellToken token)
    {
        if (token instanceof SummonFocusToken || token instanceof ISelfToken)
        {
            tokenList.add(token);
            this.manaCost += token.getManaCostOfToken(this, 1);
            this.bloodCost += token.getBloodCostOfToken(this, 1);

//            if (token instanceof ISelfToken)
//            {
//                this.setUseDurationToMinimum(((ISelfToken) token).getSelfTokenUseDurationMinimum());
//            }
        }
    }
    
    public int setUseDurationToMinimum(int min)
    {
    	focusUseDuration = Math.max(focusUseDuration, min);
    	
    	return focusUseDuration;
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityPlayer player)
    {
    	float potency = 1;
        
        System.out.println("Activate!");

        for (SpellToken token : tokenList)
        {
            token.setPotencyOfToken(potency);
        }

        SpellCastEvent castEvent = new SpellCastEvent(player, tokenList, potency);
        if (MinecraftForge.EVENT_BUS.post(castEvent))
        {
            return stack;
        }
        
        for(SpellToken token : this.tokenList)
		{
			if(token instanceof ISummonToken)
			{
				((ISummonToken)token).manipulateSummonFocus(this, potency);
			}
		}

        if (ApiUtils.drainManaAndBlood(player, this.getManaCost(potency), this.getBloodCost(potency)) && !world.isRemote)
        {
        	EntitySummon summon = new EntitySummon(world, player.posX, player.posY, player.posZ);
        	prepareSummon(world, summon, potency);
        	world.spawnEntityInWorld(summon);
        	
//            for (SpellToken token : tokenList)
//            {
//                if (token instanceof ISelfToken)
//                {
//                    ((ISelfToken) token).applyEffectToPlayer(world, player, this, potency * potencyMultiplier);
//                }
//            }
        }
        
        return stack;
    }
    
    public void prepareSummon(World world, EntitySummon summon, float potency)
    {
    	summon.tokenList = this.tokenList;
    	summon.potency = potency;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
    	player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
    	
        return stack;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        return false;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return focusUseDuration + 1;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int timeLeft)
    {
        
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	return false;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        return false;
    }

    @Override
    public float getBloodCostOfToken(IFocusToken token, float potency)
    {
        // TODO Auto-generated method stub
        return 5 * potency * potency;
    }

    @Override
    public float getManaCostOfToken(IFocusToken token, float potency)
    {
        // TODO Auto-generated method stub
        return 8 * potency * potency;
    }

    @Override
    public float getBloodCost(float potency)
    {
        return bloodCost;
    }

    @Override
    public float getManaCost(float potency)
    {
        return manaCost;
    }

    @Override
    public SpellToken copy()
    {
        return new SummonFocusToken();
    }

    @Override
    public boolean isSpellTokenCompatible(List<SpellToken> tokenList, SpellToken token)
    {
        return token instanceof ISelfToken;
    }

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) 
	{
		
	}
}