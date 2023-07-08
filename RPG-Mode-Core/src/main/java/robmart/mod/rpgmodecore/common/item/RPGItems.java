package robmart.mod.rpgmodecore.common.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robmart.mod.rpgmodecore.common.reference.Reference;

public class RPGItems {
    protected static Item of(String id, Item item) {
        Identifier identifier = new Identifier(Reference.MOD_ID, id);
        Registry.register(Registries.ITEM, identifier, item);
        return item;
    }

    protected static Item of(String id, Item item, ItemGroup group) {
        Item rItem = of(id, item);
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> content.add(rItem));
        return rItem;
    }
}
