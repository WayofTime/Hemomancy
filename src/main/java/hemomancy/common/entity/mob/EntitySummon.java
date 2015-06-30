package hemomancy.common.entity.mob;

import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntitySummon extends EntityZombie
{
	public BlockPos startingBlockArea = new BlockPos(0,0,0);
	public BlockPos endingBlockArea = new BlockPos(0,0,0);
	
	public boolean workArea = false;
	
	public EntitySummon(World worldIn) 
	{
		super(worldIn);
	}
	
	public EntitySummon(World world, double xPos, double yPos, double zPos)
	{
		this(world);
		
		this.posX = xPos;
		this.posY = yPos;
		this.posZ = zPos;
		
		startingBlockArea = new BlockPos(xPos - 5, yPos - 2, zPos - 5);
		endingBlockArea = new BlockPos(xPos + 5, yPos + 2, zPos + 5);
		
		workArea = true;
	}
	
	@Override
	protected void applyEntityAI()
    {
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityMob.class, 1.0D, true));
//        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityPigZombie.class}));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityMob.class, true));
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        
        
    }
	
	@Override
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
    }
	
	@Override
	public void onLivingUpdate()
    {
		this.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 100, 0));
		
		super.onLivingUpdate();
    }
	
	public BlockPos getCentralPositionInBlockArea()
	{
		if(workArea)
		{
			BlockPos pos = new BlockPos((startingBlockArea.getX() + endingBlockArea.getX()) / 2, (startingBlockArea.getY() + endingBlockArea.getY()) / 2, (startingBlockArea.getZ() + endingBlockArea.getZ()) / 2);
			
			return pos;
		}
		
		return null;
	}
	
	
}
