package hemomancy;

import hemomancy.common.items.ItemAmpoule;
import hemomancy.common.items.ItemModTester;
import hemomancy.common.items.ItemSimpleSpell;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems 
{
	public static Item itemModTester;
	public static Item itemSimpleSpell;
	public static Item itemAmpoule;
	
    public static void init()
    {
    	itemModTester = new ItemModTester();
    	itemSimpleSpell = new ItemSimpleSpell();
    	itemAmpoule = new ItemAmpoule();
    }

    public static void registerItems()
    {
    	GameRegistry.registerItem(itemModTester, "itemModTester");
    	GameRegistry.registerItem(itemSimpleSpell, "itemSimpleSpell");
    	GameRegistry.registerItem(itemAmpoule, "itemAmpoule");
    }
}
