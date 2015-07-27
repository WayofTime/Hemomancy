package hemomancy.common.spells.focus;

import hemomancy.Hemomancy;
import hemomancy.api.ApiUtils;
import hemomancy.api.events.SpellCastEvent;
import hemomancy.api.spells.ICustomDamageSource;
import hemomancy.api.spells.IDamageModifier;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.SpellSituation;
import hemomancy.api.spells.SpellToken;
import hemomancy.api.spells.beam.IBeamToken;
import hemomancy.api.spells.beam.IBlockBeamEffect;
import hemomancy.api.spells.beam.IEntityBeamEffect;
import hemomancy.api.spells.effect.IAfterHitEffect;
import hemomancy.common.spells.ProficiencyHandler;
import hemomancy.common.spells.beam.IBeamManipulatorBlock;
import hemomancy.common.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class BeamFocusToken extends SpellToken implements IFocusToken
{
	private List<SpellToken> tokenList = new ArrayList();
	
	private static HashMap<String, Object> beam = new HashMap();
	private static HashMap<String, Object> circle = new HashMap();
	
	public Map<String, Float> damageMap = new HashMap();
	public List<IBlockBeamEffect> blockEffects = new ArrayList();
	public List<IEntityBeamEffect> entityEffects = new ArrayList();
	public List<IDamageModifier> damageModifierList = new ArrayList();
	public List<IAfterHitEffect> afterHitEffects = new ArrayList();
	public List<ICustomDamageSource> damageSources = new ArrayList();
	
	private double beamLength = 10.0;
	public boolean ignoreEntities = false;
	public boolean dealDamage = true;
	
	public boolean collideWithLiquids = true;
	
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
    	String pp = getStringForPlayer(player);
        
        beam.put(pp, null);
        circle.put(pp, null);
        
        BlockPos pos = lastPosMap.get(pp);
        int timer = hitCount.containsKey(pp) ? hitCount.get(pp) : 0;
        if(timer > 0 && pos != null)
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
        }
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

	static HashMap<String, BlockPos> lastPosMap = new HashMap();
	static HashMap<String, Integer> hitCount = new HashMap();
	static HashMap<String, EnumFacing> lastSideMap = new HashMap();
	
	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) 
	{
		int timeUsed = count;
		
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
		
		if(player.worldObj.getTotalWorldTime() % 5 != 0 || !ApiUtils.drainManaAndBlood(player, this.getManaCost(potency), this.getBloodCost(potency)))
		{
//			player.clearItemInUse();
			return;
		}
				
		World world = player.worldObj;
		
		MovingObjectPosition mop = Utils.getMovingObjectPositionFromPlayer(world, player, collideWithLiquids, beamLength, true, !ignoreEntities);

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
					
					if(block instanceof IBeamManipulatorBlock)
					{
						if(((IBeamManipulatorBlock)block).manipulateBeam(this, world, pos, player))
						{
							return;
						}
					}
					
					EnumFacing sideHit = mop.sideHit;
					
					for(IBlockBeamEffect effect : blockEffects)
					{
						if(effect.collideWithBlock(world, player, pos, state, block, sideHit))
						{
							success = true;
						}
					}
					
					if(success)
					{
						ProficiencyHandler.handleSuccessfulSpellCast(player, tokenList, potency, SpellSituation.BEAM_BLOCK);
					}
					
					break;
					
				case ENTITY:
					if(mop.entityHit instanceof EntityLivingBase)
					{
						EntityLivingBase livingEntity = (EntityLivingBase)mop.entityHit;
						
						float currentHealth = livingEntity.getHealth();
						
						if(this.dealDamage)
			    		{
							float damage = 0;
							
							for(Entry<String, Float> entry : this.damageMap.entrySet())
							{
								damage += entry.getValue();
							}
							
							float newDamage = damage;
					    	
					    	for(IDamageModifier modifier : this.damageModifierList)
					    	{
					    		newDamage += modifier.getDamageAgainstEntity(player, mop.entityHit, damage);
					    	}
					    	
					    	DamageSource source = null;
					    	
					    	for(ICustomDamageSource src : this.damageSources)
					    	{
					    		source = src.getDamageSourceAgainstEntity(player, livingEntity, newDamage);
					    		if(source != null)
					    		{
					    			break;
					    		}
					    	}
					    	
					    	if(source != null)
					    	{
					    		livingEntity.attackEntityFrom(source, newDamage);
					    	}
			    		}
						
						for(IEntityBeamEffect effect : entityEffects)
						{
							if(effect.onBeamHitEntity(player, livingEntity, timeUsed))
							{
								success = true;
							}
						}
						
						float damageDealt = currentHealth - livingEntity.getHealth();
						
						if(damageDealt > 0)
			            {
							for(IAfterHitEffect effect : afterHitEffects)
							{
								if(effect.applyAfterDamageEffect(player, livingEntity, damageDealt))
								{
									success = true;
								}
							}
			            }
						
						if(success)
						{
							ProficiencyHandler.handleSuccessfulSpellCast(player, tokenList, potency, SpellSituation.BEAM_ENTITY);
						}
					}
					
					break;
				default:
					return;
        		}
        	}
        }
	}
	
	public void renderBeam(EntityPlayer player)
	{
		String pp = getStringForPlayer(player);
        
        MovingObjectPosition mop = Utils.getMovingObjectPositionFromPlayer(player.worldObj, player, collideWithLiquids, beamLength, true, !ignoreEntities);
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
        	if(mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        	{
        		circle.put(pp, Hemomancy.proxy.beamContact(player.worldObj, player, mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ(), mop.sideHit, 2, 0x0cff00, false, impact > 0 ? 2.0F : 0.0F, circle.get(pp), impact));
        	}
        }
	}
	
	public static String getStringForPlayer(EntityPlayer player)
	{
		String pp = "R" + player.getName();
        if (!player.worldObj.isRemote) 
        {
        	pp = "S" + player.getName();
        }
        
        return pp;
	}
}
