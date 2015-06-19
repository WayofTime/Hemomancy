package hemomancy.common.items;

import hemomancy.Hemomancy;
import hemomancy.api.mana.ISpellCostClient;
import hemomancy.api.mana.ManaHandler;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.SpellTokenRegistry;
import hemomancy.common.spells.focus.SelfFocusToken;
import hemomancy.common.util.Utils;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemSimpleSpell extends Item implements ISpellCostClient
{
	private static HashMap<String, Object> beam = new HashMap();
	
    public ItemSimpleSpell()
    {
        super();
        setMaxStackSize(1);
        setCreativeTab(Hemomancy.tabHemomancy);
        this.setUnlocalizedName("itemSimpleSpell");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (player.isSneaking())
        {
            if (!world.isRemote)
            {
                ManaHandler.setManaOfPlayer(player, 100);
            }
        } else
        {
            IFocusToken focus = getPreparedFocus(itemStack);

            String pp = "R" + player.getName();
            if (!player.worldObj.isRemote) 
            {
            	pp = "S" + player.getName();
            }
            
            MovingObjectPosition mop = Utils.getMovingObjectPositionFromPlayer(world, player, false);
            Vec3 vec = player.getLookVec();
            double tx = player.posX + vec.xCoord * 10.0;
            double ty = player.posY + vec.yCoord * 10.0;
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
            	beam.put(pp, Hemomancy.proxy.beamCont(player.worldObj, player, tx, ty + player.height, tz, 2, 65382, false, impact > 0 ? 2.0F : 0.0F, beam.get(pp), impact));
            }
            
            if (focus != null)
            {
                if (focus.getMaxItemUseDuration(itemStack) > 0 && focus instanceof SelfFocusToken)
                {
                    player.setItemInUse(itemStack, focus.getMaxItemUseDuration(itemStack));
                } else
                {
                    focus.onItemRightClick(itemStack, world, player);
                }
            }
        }

        return itemStack;
    }
    
    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
    {
    	String pp = "R" + player.getName();
        if (!player.worldObj.isRemote) 
        {
        	pp = "S" + player.getName();
        }
        
        MovingObjectPosition mop = Utils.getMovingObjectPositionFromPlayer(player.worldObj, player, false);
        Vec3 vec = player.getLookVec();
        double tx = player.posX + vec.xCoord * 10.0;
        double ty = player.posY + vec.yCoord * 10.0;
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
        	beam.put(pp, Hemomancy.proxy.beamCont(player.worldObj, player, tx, ty + player.height, tz, 2, 65382, false, impact > 0 ? 2.0F : 0.0F, beam.get(pp), impact));
        }
    }

    @Override
    public float getManaCostForClientRender(ItemStack stack)
    {
        IFocusToken focus = getPreparedFocus(stack);

        if (focus != null)
        {
            return focus.getManaCost(1);
        }

        return 0;
    }

    @Override
    public float getBloodCostForClientRender(ItemStack stack)
    {
        IFocusToken focus = getPreparedFocus(stack);

        if (focus != null)
        {
            return focus.getBloodCost(1);
        }

        return 0;
    }

    public IFocusToken getPreparedFocus(ItemStack stack)
    {
        IFocusToken focus = SpellTokenRegistry.getPreparedFocusFromItemStack(stack);

        return focus;
    }

    public int getMaxItemUseDuration(ItemStack stack)
    {
        IFocusToken focus = getPreparedFocus(stack);

        if (focus != null)
        {
            return focus.getMaxItemUseDuration(stack);
        }

        return 0;
    }

    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityPlayer player)
    {
        IFocusToken focus = getPreparedFocus(stack);

        if (focus != null)
        {
            return focus.onItemUseFinish(stack, world, player);
        }

        return stack;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        IFocusToken focus = getPreparedFocus(stack);

        if (focus != null && focus.getItemUseAction(stack) != null)
        {
            return focus.getItemUseAction(stack);
        }

        return super.getItemUseAction(stack);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        IFocusToken focus = getPreparedFocus(stack);

        
        
        if (focus != null)
        {
            return focus.onItemUse(stack, player, worldIn, pos, side, hitX, hitY, hitZ);
        }

        return super.onItemUse(stack, player, worldIn, pos, side, hitX, hitY, hitZ);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer player, int timeLeft)
    {
        IFocusToken focus = getPreparedFocus(stack);

        if (focus != null)
        {
            focus.onPlayerStoppedUsing(stack, worldIn, player, timeLeft);
        }
        
        String pp = "R" + player.getName();
        if (!player.worldObj.isRemote) 
        {
        	pp = "S" + player.getName();
        }
        
        beam.put(pp, null);

        super.onPlayerStoppedUsing(stack, worldIn, player, timeLeft);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        IFocusToken focus = getPreparedFocus(stack);

        if (focus != null)
        {
            return focus.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ);
        }

        return super.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        IFocusToken focus = getPreparedFocus(stack);

        if (focus != null)
        {
            return focus.onLeftClickEntity(stack, player, entity);
        }

        return super.onLeftClickEntity(stack, player, entity);
    }
}