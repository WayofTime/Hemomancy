package hemomancy.common.spells.projectile;

import hemomancy.api.spells.projectile.IProjectileDamageModifier;
import hemomancy.common.util.DamageCounterExtendedProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class FireProjectileEffect implements IProjectileDamageModifier
{
    public float potency;

    public FireProjectileEffect(float potency)
    {
        this.potency = potency;
    }

    @Override
    public float getDamageAgainstEntity(Entity shooter, Entity hitEntity, double originalDamage)
    {
        if (hitEntity instanceof EntityLivingBase)
        {
            hitEntity.setFire(2 * (int) potency);
            DamageCounterExtendedProperties properties = DamageCounterExtendedProperties.get((EntityLivingBase) hitEntity);
            int iceCounter = properties.getIceCounters();
            if (iceCounter > 0)
            {
                properties.clearIceCounter();
                return (float) (originalDamage * 0.2f * iceCounter * potency);
            }
        }

        return 0;
    }

    @Override
    public float getPotency()
    {
        return this.potency;
    }
}
