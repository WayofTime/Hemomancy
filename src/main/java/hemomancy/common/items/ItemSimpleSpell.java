package hemomancy.common.items;

import hemomancy.Hemomancy;
import hemomancy.api.mana.ISpellCostClient;
import hemomancy.api.mana.ManaHandler;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.SpellTokenRegistry;
import hemomancy.common.spells.focus.SelfFocusToken;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemSimpleSpell extends Item implements ISpellCostClient
{
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
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        IFocusToken focus = getPreparedFocus(stack);

        if (focus != null)
        {
            return focus.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
        }

        return super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft)
    {
        IFocusToken focus = getPreparedFocus(stack);

        if (focus != null)
        {
            focus.onPlayerStoppedUsing(stack, worldIn, playerIn, timeLeft);
        }

        super.onPlayerStoppedUsing(stack, worldIn, playerIn, timeLeft);
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