package hemomancy.common.spells;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.api.spells.projectile.IOnProjectileCollideEffect;
import hemomancy.api.spells.projectile.IOnProjectileUpdateEffect;
import hemomancy.common.entity.projectile.EntitySpellProjectile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ProjectileFocusToken extends SpellToken implements IFocusToken
{
	private List<SpellToken> tokenList = new ArrayList();
	public List<IOnProjectileUpdateEffect> onUpdateEffectList = new ArrayList();
	public List<IOnProjectileCollideEffect> onCollideEffectList = new ArrayList();
	
	public int bouncesLeft = 0;
	
	public Map<String, Double> damageMap = new HashMap();
	
	public ProjectileFocusToken() 
	{
		super("textures/tokens/ProjectileToken.png");
		damageMap.put("default", 5.0);
	}

	@Override
	public void addSpellTokenToFocus(SpellToken token) 
	{
		if(token instanceof ProjectileFocusToken || token instanceof IProjectileToken)
		{
			tokenList.add(token);
		}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) 
	{
		float potency = 1;
		EntitySpellProjectile projectile = new EntitySpellProjectile(world, player, this, tokenList);

		prepareProjectileForEntity(world, projectile, potency);
		world.spawnEntityInWorld(projectile);
		
		return stack;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityPlayer player) 
	{
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
		return EnumAction.NONE;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) 
	{
		return 0;
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
		return new ProjectileFocusToken();
	}
	
	public void prepareProjectileForEntity(World world, EntitySpellProjectile projectile, float potency)
	{
		for(SpellToken token : this.tokenList)
		{
			if(token instanceof IProjectileToken)
			{
				((IProjectileToken)token).manipulateProjectileFocus(this, potency);
			}
		}
		
		double damage = 0;
		
		projectile.onUpdateEffectList = this.onUpdateEffectList;
		projectile.onCollideEffectList = this.onCollideEffectList;
		
		for(Entry<String, Double> entry : this.damageMap.entrySet())
		{
			damage += entry.getValue();
		}
		
		projectile.damage = damage;
		
		projectile.bouncesLeft = bouncesLeft;
	}
}
