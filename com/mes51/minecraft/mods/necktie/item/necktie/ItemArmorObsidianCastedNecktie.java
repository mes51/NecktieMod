package com.mes51.minecraft.mods.necktie.item.necktie;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Package: com.mes51.minecraft.mods.necktie.item.necktie
 * Date: 2014/06/01
 * Time: 3:18
 */
public class ItemArmorObsidianCastedNecktie extends ItemArmorCastedNecktieBase
{
    private static Item instance = null;

    public static void register(int itemId)
    {
        int index = Necktie.proxy.getArmorRenderPrefix("armorNecktie");
        instance = new ItemArmorObsidianCastedNecktie(itemId, index);
        register((ItemArmorCastedNecktieBase)instance, Const.Item.ItemName.OBSIDIAN_CASTED_NECKTIE, "Obsidian Casted Necktie");
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemArmorObsidianCastedNecktie(int par1, int index)
    {
        super(par1, Const.Material.Armor.CASTED_OBSIDIAN, index, Const.Material.Tool.CASTED_OBSIDIAN, 3, "Obsidian");
        setUnlocalizedName(Const.Item.ItemName.OBSIDIAN_CASTED_NECKTIE);
        setTextureName(Const.Item.ItemName.OBSIDIAN_CASTED_NECKTIE);
    }

    @Override
    public PotionEffect[] getBeaconEffect(EntityPlayer player)
    {
        return new PotionEffect[] {
                new PotionEffect(Potion.resistance.getId(), 0, 2)
        };
    }
}
