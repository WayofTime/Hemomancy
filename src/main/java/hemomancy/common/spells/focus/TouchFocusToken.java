package hemomancy.common.spells.focus;

import hemomancy.api.ApiUtils;
import hemomancy.api.events.SpellCastEvent;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.ITouchToken;
import hemomancy.api.spells.SpellSituation;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.ProficiencyHandler;
import hemomancy.common.spells.touch.IClickBlockTouchEffect;
import hemomancy.common.util.Utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TouchFocusToken extends SpellToken implements IFocusToken
{
	private List<SpellToken> tokenList = new ArrayList();
	
	public List<IClickBlockTouchEffect> blockEffects = new ArrayList(); 
	
	public float manaCost = 0;
	public float bloodCost = 0;

	public TouchFocusToken() 
	{
		super("textures/tokens/TouchToken.png");
		this.setUnlocalizedName("token.tokenTouch.name");
	}

	@Override
	public void addSpellTokenToFocus(SpellToken token) 
	{
		if(token instanceof TouchFocusToken || token instanceof ITouchToken)
		{
			tokenList.add(token);
			this.manaCost += token.getManaCostOfToken(this, 1);
			this.bloodCost += token.getBloodCostOfToken(this, 1);
		}
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityPlayer player) 
	{
		float potency = 1;
				
		for(SpellToken token : tokenList)
		{
			token.setPotencyOfToken(potency);
		}
		
		SpellCastEvent castEvent = new SpellCastEvent(player, tokenList, potency);
		if(MinecraftForge.EVENT_BUS.post(castEvent))
		{
			return stack;
		}
		
		for(SpellToken token : this.tokenList)
		{
			if(token instanceof ITouchToken)
			{
				((ITouchToken)token).manipulateTouchFocus(this, potency);
			}
		}
		
		boolean flag = true;
		
		MovingObjectPosition mop = Utils.getMovingObjectPositionFromPlayer(world, player, flag);

        if (mop == null)
        {
            return stack;
        }
        else
        {
        	if((mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK || mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) && ApiUtils.drainManaAndBlood(player, this.getManaCost(potency), this.getBloodCost(potency)))
        	{

				boolean success = false;

        		switch(mop.typeOfHit)
        		{
				case BLOCK:
					BlockPos pos = mop.getBlockPos();
					IBlockState state = world.getBlockState(pos);
					Block block = state.getBlock();
					EnumFacing sideHit = mop.sideHit;
					
					for(IClickBlockTouchEffect effect : blockEffects)
					{
						if(effect.clickBlock(world, player, pos, state, block, sideHit))
						{
			        		System.out.println("Called");

							success = true;
						}
					}
					
					if(success)
					{
						ProficiencyHandler.handleSuccessfulSpellCast(player, tokenList, potency, SpellSituation.TOUCH_BLOCK);
					}
					
					break;
					
				case ENTITY:
					
					break;
				default:
					return stack;
        		}
        	}
        }
		
		return stack;
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
        return 51;
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
	public SpellToken copy() 
	{
		return new TouchFocusToken();
	}
	
	@Override
	public float getBloodCostOfToken(IFocusToken token, float potency) 
	{
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public float getManaCostOfToken(IFocusToken token, float potency) 
	{
		// TODO Auto-generated method stub
		return 1;
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
	public boolean isSpellTokenCompatible(List<SpellToken> tokenList, SpellToken token)
	{
		return token instanceof ITouchToken;
	}
}
