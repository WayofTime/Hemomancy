package hemomancy.api.spells;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Proper implementation of this interface will allow the player to cast spells like any form of item can.
 *
 */
public interface IFocusToken 
{
	/**
	 * Adds a spell token to the Focus's list. The modder is responsible for making sure that the token fits with the Focus.
	 * @param token SpellToken being added to the spell
	 */
	public void addSpellTokenToFocus(SpellToken token);

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ);
	
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player);
    
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityPlayer player);
    
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker);
    
    public EnumAction getItemUseAction(ItemStack stack);
    
    public int getMaxItemUseDuration(ItemStack stack);
    
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int timeLeft);
    
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ);
    
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity);  
    
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count);
    
    public float getBloodCost(float potency);
    
    public float getManaCost(float potency);
}
