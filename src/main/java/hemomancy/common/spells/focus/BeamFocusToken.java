package hemomancy.common.spells.focus;

import hemomancy.Hemomancy;
import hemomancy.api.ApiUtils;
import hemomancy.api.events.SpellCastEvent;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.SpellSituation;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.ProficiencyHandler;
import hemomancy.common.spells.beam.IBeamToken;
import hemomancy.common.spells.beam.IBlockBeamEffect;
import hemomancy.common.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
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
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class BeamFocusToken extends SpellToken implements IFocusToken
{
	private List<SpellToken> tokenList = new ArrayList();
	
	private static HashMap<String, Object> beam = new HashMap();
	
	public List<IBlockBeamEffect> blockEffects = new ArrayList();
	
	private double beamLength = 10.0;
	public boolean ignoreEntities = false;
	
	public float manaCost = 0;
	public float bloodCost = 0;

	public BeamFocusToken() 
	{
		super("textures/tokens/BeamToken.png");
		this.setUnlocalizedName("token.tokenBeam.name");
	}

	@Override
	public void addSpellTokenToFocus(SpellToken token) 
	{
		if(token instanceof BeamFocusToken || token instanceof IBeamToken)
		{
			tokenList.add(token);
			this.manaCost += token.getManaCostOfToken(this, 1);
			this.bloodCost += token.getBloodCostOfToken(this, 1);
		}
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityPlayer player) 
	{		
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
        return 72000;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int timeLeft)
    {
    	String pp = "R" + player.getName();
        if (!player.worldObj.isRemote) 
        {
        	pp = "S" + player.getName();
        }
        
        beam.put(pp, null);
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
		return new BeamFocusToken();
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
		return token instanceof IBeamToken;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) 
	{
		float potency = 1;
		
		for(SpellToken token : tokenList)
		{
			token.setPotencyOfToken(potency);
		}
		
		SpellCastEvent castEvent = new SpellCastEvent(player, tokenList, potency);
		if(MinecraftForge.EVENT_BUS.post(castEvent))
		{
			return;
		}
		
		for(SpellToken token : this.tokenList)
		{
			if(token instanceof IBeamToken)
			{
				((IBeamToken)token).manipulateBeamFocus(this, potency);
			}
		}
				
		if(!ApiUtils.canDrainManaAndBlood(player, this.getManaCost(potency), this.getBloodCost(potency)))
		{
			player.stopUsingItem();
			return;
		}
		
		this.renderBeam(player);
		
		if(player.worldObj.getTotalWorldTime() % 5 == 0 && !ApiUtils.drainManaAndBlood(player, this.getManaCost(potency), this.getBloodCost(potency)))
		{
			System.out.println("what?");
//			player.clearItemInUse();
			return;
		}
		
		boolean flag = false;
		
		World world = player.worldObj;
		
		MovingObjectPosition mop = Utils.getMovingObjectPositionFromPlayer(world, player, flag, beamLength, true, !ignoreEntities);

        if (mop == null)
        {
            return;
        }
        else
        {
        	if((mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK || mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY))
        	{
				boolean success = false;

        		switch(mop.typeOfHit)
        		{
				case BLOCK:
					BlockPos pos = mop.getBlockPos();
					IBlockState state = world.getBlockState(pos);
					Block block = state.getBlock();
					EnumFacing sideHit = mop.sideHit;
					
					for(IBlockBeamEffect effect : blockEffects)
					{
						if(effect.collideWithBlock(world, player, pos, state, block, sideHit))
						{
			        		System.out.println("Called");

							success = true;
						}
					}
					
					if(success)
					{
						ProficiencyHandler.handleSuccessfulSpellCast(player, tokenList, potency, SpellSituation.BEAM_BLOCK);
					}
					
					break;
					
				case ENTITY:
//					mop.entityHit.attackEntityFrom(DamageSource.cactus, 3);
					break;
				default:
					return;
        		}
        	}
        }
	}
	
	public void renderBeam(EntityPlayer player)
	{
		String pp = "R" + player.getName();
        if (!player.worldObj.isRemote) 
        {
        	pp = "S" + player.getName();
        }
        
        MovingObjectPosition mop = Utils.getMovingObjectPositionFromPlayer(player.worldObj, player, false);
        Vec3 vec = player.getLookVec();
        double tx = player.posX + vec.xCoord * 10.0;
        double ty = player.posY + player.height + vec.yCoord * 10.0;
        double tz = player.posZ + vec.zCoord * 10.0;
        int impact = 0;
        
        if(mop != null)
        {
        	tx = mop.hitVec.xCoord;
        	ty = mop.hitVec.yCoord;
        	tz = mop.hitVec.zCoord;
        }
        
        if(player.worldObj.isRemote)
        {
        	beam.put(pp, Hemomancy.proxy.beamCont(player.worldObj, player, tx, ty, tz, 2, 0x0cff00, false, impact > 0 ? 2.0F : 0.0F, beam.get(pp), impact));
        }
	}
}
