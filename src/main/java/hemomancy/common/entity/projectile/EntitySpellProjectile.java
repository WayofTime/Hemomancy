package hemomancy.common.entity.projectile;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.api.spells.SpellTokenRegistry;
import hemomancy.api.spells.projectile.IOnProjectileCollideEffect;
import hemomancy.api.spells.projectile.IOnProjectileUpdateEffect;
import hemomancy.common.spells.ProjectileFocusToken;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySpellProjectile extends Entity implements IProjectile
{
	private float potency = 1;
	
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block inTile;
    private int inData;
    private boolean inGround;
    /** The owner of this arrow. */
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    public double damage = 2.0D;
    
    private float gravity = 0.01f;
    /** The amount of knockback an arrow applies when it hits a mob. */
    private int knockbackStrength;
    
    private boolean collideWithFluids = false;
    
    public List<SpellToken> tokenList = new ArrayList();
    
    public ProjectileFocusToken focus = null;
    
    public List<IOnProjectileUpdateEffect> onUpdateEffectList = new ArrayList();
	public List<IOnProjectileCollideEffect> onCollideEffectList = new ArrayList();

    public EntitySpellProjectile(World worldIn)
    {
        super(worldIn);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
    }

    public EntitySpellProjectile(World worldIn, double x, double y, double z)
    {
        super(worldIn);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
        this.setPosition(x, y, z);
    }

    public EntitySpellProjectile(World world, EntityLivingBase shooter, EntityLivingBase shotEntity, float p_i1755_4_, float velocity)
    {
        super(world);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = shooter;

        this.posY = shooter.posY + (double)shooter.getEyeHeight() - 0.10000000149011612D;
        double d0 = shotEntity.posX - shooter.posX;
        double d1 = shotEntity.getEntityBoundingBox().minY + (double)(shotEntity.height / 3.0F) - this.posY;
        double d2 = shotEntity.posZ - shooter.posZ;
        double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);

        if (d3 >= 1.0E-7D)
        {
            float f2 = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
            float f3 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
            double d4 = d0 / d3;
            double d5 = d2 / d3;
            this.setLocationAndAngles(shooter.posX + d4, this.posY, shooter.posZ + d5, f2, f3);
            float f4 = (float)(d3 * 0.20000000298023224D);
            this.setThrowableHeading(d0, d1 + (double)f4, d2, p_i1755_4_, velocity);
        }
    }

    public EntitySpellProjectile(World worldIn, EntityLivingBase shooter, float velocity)
    {
        super(worldIn);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = shooter;

        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(shooter.posX, shooter.posY + (double)shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, velocity * 1.5F, 1.0F);
    }
    
    public EntitySpellProjectile(World worldIn, EntityLivingBase shooter, ProjectileFocusToken focus, List<SpellToken> tokenList)
    {
        super(worldIn);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = shooter;
        
        this.tokenList = tokenList;
        this.focus = focus;
        
        float velocity = 1;

        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(shooter.posX, shooter.posY + (double)shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, velocity * 1.5F, 1.0F);
    }

    @Override
    protected void entityInit()
    {
    	
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     *  
     * @param inaccuracy Higher means more error.
     */
    @Override
    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy)
    {
        float f2 = MathHelper.sqrt_double(x * x + y * y + z * z);
        x /= (double)f2;
        y /= (double)f2;
        z /= (double)f2;
        x += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)inaccuracy;
        y += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)inaccuracy;
        z += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)inaccuracy;
        x *= (double)velocity;
        y *= (double)velocity;
        z *= (double)velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f3 = MathHelper.sqrt_double(x * x + z * z);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(x, z) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(y, (double)f3) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    /**
     * setPositionAndRotation2
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void func_180426_a(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean bool)
    {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void setVelocity(double x, double y, double z)
    {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(x * x + z * z);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(x, z) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(y, (double)f) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f) * 180.0D / Math.PI);
        }

        BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
        IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (block.getMaterial() != Material.air)
        {
            block.setBlockBoundsBasedOnState(this.worldObj, blockpos);
            AxisAlignedBB axisalignedbb = block.getCollisionBoundingBox(this.worldObj, blockpos, iblockstate);

            if (axisalignedbb != null && axisalignedbb.isVecInside(new Vec3(this.posX, this.posY, this.posZ)))
            {
                this.inGround = true;
            }
        }

        if (this.inGround)
        {
            int j = block.getMetaFromState(iblockstate);

            if (block == this.inTile && j == this.inData)
            {
                ++this.ticksInGround;

                if (this.ticksInGround >= 1)
                {
                    this.setDead();
                }
            }
            else
            {
                this.inGround = false;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksInAir = 0;
            }
        }
        else
        {
            ++this.ticksInAir;
            Vec3 vec31 = new Vec3(this.posX, this.posY, this.posZ);
            Vec3 vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec3, collideWithFluids, true, false);
            vec31 = new Vec3(this.posX, this.posY, this.posZ);
            vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (movingobjectposition != null) //Either a block or an entity has been hit!
            {
                vec3 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            int i;
            float f1;

            for (i = 0; i < list.size(); ++i)
            {
                Entity entity1 = (Entity)list.get(i);

                if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5))
                {
                    f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().expand((double)f1, (double)f1, (double)f1);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);

                    if (movingobjectposition1 != null)
                    {
                        double d1 = vec31.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D)
                        {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) //I have hit an entity.
            {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)movingobjectposition.entityHit;

                if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer))
                {
                    movingobjectposition = null;
                }
            }

            float f2;
            float f3;
            float f4;

            if (movingobjectposition != null)
            {
                if (movingobjectposition.entityHit != null) //I have hit an entity.
                {
                    this.onCollideWithEntity(movingobjectposition);
                }
                else //I have hit a block.
                {
                    BlockPos blockpos1 = movingobjectposition.getBlockPos();
                    this.xTile = blockpos1.getX();
                    this.yTile = blockpos1.getY();
                    this.zTile = blockpos1.getZ();
                    iblockstate = this.worldObj.getBlockState(blockpos1);
                    this.inTile = iblockstate.getBlock();
                    this.inData = this.inTile.getMetaFromState(iblockstate);
                    this.motionX = (double)((float)(movingobjectposition.hitVec.xCoord - this.posX));
                    this.motionY = (double)((float)(movingobjectposition.hitVec.yCoord - this.posY));
                    this.motionZ = (double)((float)(movingobjectposition.hitVec.zCoord - this.posZ));
                    f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double)f3 * 0.05000000074505806D;
                    this.posY -= this.motionY / (double)f3 * 0.05000000074505806D;
                    this.posZ -= this.motionZ / (double)f3 * 0.05000000074505806D;
                    this.inGround = true;

                    if (this.inTile.getMaterial() != Material.air)
                    {
                        this.inTile.onEntityCollidedWithBlock(this.worldObj, blockpos1, iblockstate, this);
                    }
                    
                    if(onCollideWithBlock(blockpos1, this.inTile, iblockstate))
                    {
                    	this.setDead();
                    }
                }
            }
//
//            if (this.getIsCritical())
//            {
//                for (i = 0; i < 4; ++i)
//                {
//                    this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * (double)i / 4.0D, this.posY + this.motionY * (double)i / 4.0D, this.posZ + this.motionZ * (double)i / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ, new int[0]);
//                }
//            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

            for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f2) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
            {
                ;
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
            {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F)
            {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
            {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            f3 = 0.99F;
            f1 = this.getGravity();

            if (this.isInWater())
            {
                for (int l = 0; l < 4; ++l)
                {
                    f4 = 0.25F;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double)f4, this.posY - this.motionY * (double)f4, this.posZ - this.motionZ * (double)f4, this.motionX, this.motionY, this.motionZ, new int[0]);
                }

                f3 = 0.6F;
            }

            if (this.isWet())
            {
                this.extinguish();
            }

            this.motionX *= (double)f3;
            this.motionY *= (double)f3;
            this.motionZ *= (double)f3;
            this.motionY -= (double)f1;
            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }

    public float getGravity()
    {
    	return this.gravity;
    }
    
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setShort("xTile", (short)this.xTile);
        tagCompound.setShort("yTile", (short)this.yTile);
        tagCompound.setShort("zTile", (short)this.zTile);
        tagCompound.setShort("life", (short)this.ticksInGround);
        ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
        tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        tagCompound.setByte("inData", (byte)this.inData);
        tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        tagCompound.setDouble("damage", this.damage);
        tagCompound.setFloat("gravity", gravity);
        
        tagCompound.setTag("tokenList", SpellTokenRegistry.writeSpellTokensToTag(tokenList, new NBTTagCompound()));
        tagCompound.setBoolean("fluidCollide", collideWithFluids);
        tagCompound.setFloat("potency", potency);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound)
    {
        this.xTile = tagCompound.getShort("xTile");
        this.yTile = tagCompound.getShort("yTile");
        this.zTile = tagCompound.getShort("zTile");
        this.ticksInGround = tagCompound.getShort("life");

        if (tagCompound.hasKey("inTile", 8))
        {
            this.inTile = Block.getBlockFromName(tagCompound.getString("inTile"));
        }
        else
        {
            this.inTile = Block.getBlockById(tagCompound.getByte("inTile") & 255);
        }

        this.inData = tagCompound.getByte("inData") & 255;
        this.inGround = tagCompound.getByte("inGround") == 1;

        if (tagCompound.hasKey("damage", 99))
        {
            this.damage = tagCompound.getDouble("damage");
        }
        
        this.gravity = tagCompound.getFloat("gravity");
        this.potency = tagCompound.getFloat("potency");
        
        this.tokenList = SpellTokenRegistry.readSpellTokensFromTag(tagCompound.getCompoundTag("tokenList"));
        IFocusToken focusToken = SpellTokenRegistry.getPreparedFocusFromList(tokenList);
        if(focusToken instanceof ProjectileFocusToken)
        {
        	this.focus = (ProjectileFocusToken)focusToken;
        	this.focus.prepareProjectileForEntity(worldObj, this, potency);
        }
                
        collideWithFluids = tagCompound.getBoolean("fluidCollide");
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn)
    {
        if (!this.worldObj.isRemote && this.inGround)
        {            
            onCollideWithEntity(new MovingObjectPosition(entityIn));
        }
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    public void setDamage(double damage)
    {
        this.damage = damage;
    }

    public double getDamage()
    {
        return this.damage;
    }

    /**
     * Sets the amount of knockback the arrow applies when it hits a mob.
     */
    public void setKnockbackStrength(int kb)
    {
        this.knockbackStrength = kb;
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    @Override
    public boolean canAttackWithItem()
    {
        return false;
    }
    
    public boolean onCollideWithBlock(BlockPos pos, Block block, IBlockState blockState)
    {
    	return true;
    }
    
    public void onCollideWithEntity(MovingObjectPosition movingobjectposition)
    {
        int k = MathHelper.ceiling_double_int(this.damage);

        DamageSource damagesource;

        if (this.shootingEntity == null)
        {
            damagesource = DamageSource.causeThrownDamage(this, this);
        }
        else
        {
            damagesource = DamageSource.causeThrownDamage(this, this.shootingEntity);
        }

        if (movingobjectposition.entityHit.attackEntityFrom(damagesource, (float)k))
        {
            if (movingobjectposition.entityHit instanceof EntityLivingBase)
            {
                if (this.knockbackStrength > 0)
                {
                    float velocity = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

                    if (velocity > 0.0F)
                    {
                        movingobjectposition.entityHit.addVelocity(this.motionX * (double)this.knockbackStrength * 0.6000000238418579D / (double)velocity, 0.1D, this.motionZ * (double)this.knockbackStrength * 0.6000000238418579D / (double)velocity);
                    }
                }

                if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
                {
                    ((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
                }
            }

            this.setDead();
        }
//        else //Enable to allow bouncing...
//        {
//            this.motionX *= -0.10000000149011612D;
//            this.motionY *= -0.10000000149011612D;
//            this.motionZ *= -0.10000000149011612D;
//            this.rotationYaw += 180.0F;
//            this.prevRotationYaw += 180.0F;
//            this.ticksInAir = 0;
//        }
    }
}