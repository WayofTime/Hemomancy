package hemomancy.common.spells.projectile;

import hemomancy.api.spells.projectile.IOnProjectileCollideEffect;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class UndeadTurnProjectileEffect implements IOnProjectileCollideEffect
{
	public float potency;
	
	public UndeadTurnProjectileEffect(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean onProjectileHitEntity(Entity projectile, EntityPlayer shooter, EntityLivingBase hitEntity) 
	{
		return turnUndead(shooter, hitEntity, potency);
	}

	@Override
	public boolean onProjectileCollideWithBlock(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit) 
	{
		return false;
	}

	@Override
	public boolean onProjectileBounce(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit) 
	{
		return false;
	}
	
	public boolean turnUndead(Entity shooter, EntityLivingBase hitEntity, float potency)
	{
		if(hitEntity instanceof EntityLiving)
		{
			EntityLiving livingEntity = (EntityLiving)hitEntity;
			if(livingEntity.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)
			{
				List<EntityAIBase> removedEntries = new ArrayList();
				
				List taskList = livingEntity.targetTasks.taskEntries;
				for(Object obj : taskList)
				{
					if(obj instanceof EntityAINearestAttackableTarget)
					{
						EntityAINearestAttackableTarget attackTargetTask = (EntityAINearestAttackableTarget)obj;
						
						removedEntries.add(attackTargetTask);
					}
				}
				
				for(EntityAIBase ai : removedEntries)
				{
					livingEntity.targetTasks.removeTask(ai);
				}
				
				if(livingEntity.getAttackTarget() == shooter)
				{
					livingEntity.setAttackTarget(null);
				}
				
				if(livingEntity instanceof EntityCreature)
				{
					livingEntity.tasks.addTask(4, new EntityAIAttackOnCollide((EntityCreature)livingEntity, EntityMob.class, 1.0D, false));
					livingEntity.targetTasks.addTask(1, new EntityAINearestAttackableTarget((EntityCreature)livingEntity, EntityMob.class, true));
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public float getPotency() 
	{
		return potency;
	}
}
