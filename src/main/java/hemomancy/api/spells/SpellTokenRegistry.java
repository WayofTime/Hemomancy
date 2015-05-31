package hemomancy.api.spells;

import hemomancy.api.ApiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class SpellTokenRegistry 
{
	public static Map<String, SpellToken> tokenMap = new HashMap();
	
	public static void registerSpellToken(String key, SpellToken token)
	{
		token.key = key;
		tokenMap.put(key, token);
	}
	
	public static SpellToken getCopyOfToken(SpellToken token)
	{
		SpellToken copyToken = token.copy();
		copyToken.key = token.key;
		return copyToken;
	}
	
	public static SpellToken getSpellTokenForKey(String key)
	{
		SpellToken token = tokenMap.get(key);
		if(token != null)
		{
			SpellToken copyToken = token.copy();
			copyToken.key = key;
			return copyToken;
		}
		
		return null;
	}
	
	public static String getKeyForSpellToken(SpellToken token)
	{
		return token.key;
	}
	
	public static List<SpellToken> getTokenListForKeys(String[] keys)
	{
		ArrayList<SpellToken> tokenList = new ArrayList();
		
		for(String key : keys)
		{
			SpellToken token = getSpellTokenForKey(key);
			if(token != null)
			{
				tokenList.add(token);
			}
		}
		
		return tokenList;
	}
	
	public static NBTTagCompound writeSpellTokensToTag(List<SpellToken> tokenList, NBTTagCompound tag)
	{
		NBTTagList inventoryList = new NBTTagList();
        for (int i = 0; i < tokenList.size(); i++)
        {
            if (tokenList.get(i) != null)
            {
                NBTTagCompound tokenTag = new NBTTagCompound();
                tokenTag.setString("key", getKeyForSpellToken(tokenList.get(i)));
                inventoryList.appendTag(tokenTag);
            }
        }

        tag.setTag("SpellTokens", inventoryList);
		return tag;
	}
	
	public static List<SpellToken> readSpellTokensFromTag(NBTTagCompound tag)
	{
		HashMap<String, SpellToken> tempTokenMap = new HashMap();
		NBTTagList inventoryList = tag.getTagList("SpellTokens", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < inventoryList.tagCount(); i++)
        {
            NBTTagCompound tokenTag = inventoryList.getCompoundTagAt(i);
            String key = tokenTag.getString("key");
            SpellToken token = getSpellTokenForKey(key);
            
            if(token != null)
            {
            	tempTokenMap.put(key, token);
            }
        }
        
		List<SpellToken> tokenList = new ArrayList();
		
		for(SpellToken token : tempTokenMap.values())
		{
			tokenList.add(token);
		}
        
        return tokenList;
	}
	
	public static ItemStack writeSpellTokensToItemStack(ItemStack stack, List<SpellToken> tokenList)
	{
		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null)
		{
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}
		
		writeSpellTokensToTag(tokenList, tag);
		
		return stack;
	}
	
	public static List<SpellToken> readSpellTokensFromItemStack(ItemStack stack)
	{
		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null)
		{
			return new ArrayList();
		}
		
		return readSpellTokensFromTag(tag);
	}
	
	public static IFocusToken getPreparedFocusFromItemStack(ItemStack stack)
	{
		List<SpellToken> tokenList = readSpellTokensFromItemStack(stack);
		
		return getPreparedFocusFromList(tokenList);
	}
	
	/**
	 * Prepares an IFocusToken from a list of spell tokens.
	 * @param tokenList
	 * @return
	 */
	public static IFocusToken getPreparedFocusFromList(List<SpellToken> tokenList)
	{
		IFocusToken focusToken = null;
		
		for(SpellToken token : tokenList)
		{
			if(token instanceof IFocusToken)
			{
				focusToken = (IFocusToken)token;
				break;
			}
		}
		
		if(focusToken != null)
		{
			tokenList.remove(focusToken);
			
			tokenList.add(getCopyOfToken((SpellToken)focusToken)); //Add a copy of the focus token so that the list is still complete.
			
			for(SpellToken token : tokenList)
			{
				focusToken.addSpellTokenToFocus(token);
			}
		}
		
		return focusToken;
	}
	
	public static boolean hasFocusToken(List<SpellToken> tokenList)
	{
		for(SpellToken token : tokenList)
		{
			if(token instanceof IFocusToken)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static NBTTagCompound getTokenTagOfPlayer(EntityPlayer player)
	{
		NBTTagCompound persistentTag = ApiUtils.getPersistentDataTag(player);
		
		return persistentTag.getCompoundTag(ApiUtils.IDTag + "SpellTokens");
	}
	
	public static void setTokenTagOfPlayer(EntityPlayer player, NBTTagCompound tag)
	{
		NBTTagCompound persistentTag = ApiUtils.getPersistentDataTag(player);

		persistentTag.setTag(ApiUtils.IDTag + "SpellTokens", tag);
	}
	
	public static List<SpellToken> getTokenListOfPlayer(EntityPlayer player)
	{
		List<SpellToken> tokenList = new ArrayList();
		
		NBTTagCompound tag = getTokenTagOfPlayer(player);
		
		if(tag != null)
		{
			return readSpellTokensFromTag(tag);
		}
		
		return tokenList;
	}
	
	private static void setTokenListOfPlayer(EntityPlayer player, List<SpellToken> tokenList)
	{
		NBTTagCompound tag = new NBTTagCompound();
		
		writeSpellTokensToTag(tokenList, tag);
		setTokenTagOfPlayer(player, tag);
	}
	
	public static void addSpellTokenToPlayer(EntityPlayer player, String key)
	{
		SpellToken token = getSpellTokenForKey(key);
		if(token != null)
		{
			addSpellTokenToPlayer(player, token);
		}
	}
	
	public static void addSpellTokenToPlayer(EntityPlayer player, SpellToken token)
	{
		List<SpellToken> tokenList = getTokenListOfPlayer(player);
		
		for(SpellToken testToken : tokenList)
		{
			if(token.equals(testToken))
			{
				return;
			}
		}
		
		tokenList.add(token);
		
		setTokenListOfPlayer(player, tokenList);
	}
}
