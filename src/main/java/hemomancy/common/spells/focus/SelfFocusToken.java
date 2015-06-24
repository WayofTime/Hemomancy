package hemomancy.common.spells.focus;

import hemomancy.api.ApiUtils;
import hemomancy.api.events.SpellCastEvent;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.ISelfToken;
import hemomancy.api.spells.SpellToken;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class SelfFocusToken extends SpellToken implements IFocusToken
{
    private List<SpellToken> tokenList = new ArrayList<SpellToken>();

    public float manaCost = 0;
    public float bloodCost = 0;
    public int focusUseDuration = 20;

    public SelfFocusToken()
    {
        super("textures/tokens/SelfToken.png");
        setUnlocalizedName("token.tokenSelf.name");
    }

    @Override
    public void addSpellTokenToFocus(SpellToken token)
    {
        if (token instanceof SelfFocusToken || token instanceof ISelfToken)
        {
            tokenList.add(token);
            this.manaCost += token.getManaCostOfToken(this, 1);
            this.bloodCost += token.getBloodCostOfToken(this, 1);

            if (token instanceof ISelfToken)
            {
                this.setUseDurationToMinimum(((ISelfToken) token).getSelfTokenUseDurationMinimum());
            }
        }
    }
    
    public int setUseDurationToMinimum(int min)
    {
    	focusUseDuration = Math.max(focusUseDuration, min);
    	
    	return focusUseDuration;
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityPlayer player)
    {
    	float potency = 1;
        float potencyMultiplier = 1;
        
        System.out.println("Activate!");

        for (SpellToken token : tokenList)
        {
            token.setPotencyOfToken(potency * potencyMultiplier);
        }

        SpellCastEvent castEvent = new SpellCastEvent(player, tokenList, potency * potencyMultiplier);
        if (MinecraftForge.EVENT_BUS.post(castEvent))
        {
            return stack;
        }

        if (ApiUtils.drainManaAndBlood(player, this.getManaCost(potency * potencyMultiplier), this.getBloodCost(potency * potencyMultiplier)))
        {
            for (SpellToken token : tokenList)
            {
                if (token instanceof ISelfToken)
                {
                    ((ISelfToken) token).applyEffectToPlayer(world, player, this, potency * potencyMultiplier);
                }
            }
        }
        
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
        return focusUseDuration + 1;
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
    public float getBloodCostOfToken(IFocusToken token, float potency)
    {
        // TODO Auto-generated method stub
        return 5 * potency * potency;
    }

    @Override
    public float getManaCostOfToken(IFocusToken token, float potency)
    {
        // TODO Auto-generated method stub
        return 8 * potency * potency;
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
    public SpellToken copy()
    {
        return new SelfFocusToken();
    }

    @Override
    public boolean isSpellTokenCompatible(List<SpellToken> tokenList, SpellToken token)
    {
        return token instanceof ISelfToken;
    }

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) 
	{
		
	}
}