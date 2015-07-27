package hemomancy.common.spells.projectile;

import hemomancy.api.spells.IDamageModifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;

public class SpiritForceProjectileEffect implements IDamageModifier
{
    public float potency;

    public SpiritForceProjectileEffect(float potency)
    {
        this.potency = potency;
    }

    @Override
    public float getDamageAgainstEntity(Entity shooter, Entity hitEntity, double originalDamage)
    {
        if (hitEntity instanceof EntityLivingBase && ((EntityLivingBase) hitEntity).getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)
        {
            return (float) (originalDamage + 10 * (1 + Math.log(potency)));
        }
        return (float) originalDamage;
    }

    @Override
    public float getPotency()
    {
        return potency;
    }
}
