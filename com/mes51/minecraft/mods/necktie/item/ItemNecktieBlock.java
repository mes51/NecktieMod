package com.mes51.minecraft.mods.necktie.item;

import com.mes51.minecraft.mods.necktie.block.BlockNecktie;
import com.mes51.minecraft.mods.necktie.item.necktie.ItemArmorDiamondNecktie;
import com.mes51.minecraft.mods.necktie.item.necktie.ItemArmorIronNecktie;
import com.mes51.minecraft.mods.necktie.item.necktie.ItemArmorNecktie;
import com.mes51.minecraft.mods.necktie.item.necktie.ItemArmorNukeNecktie;
import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.util.Util;
import com.mes51.minecraft.mods.necktie.util.enumerable.Enumerable;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.SingleArgFunc;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Package: com.mes51.minecraft.mods.necktie.item
 * Date: 2014/05/17
 * Time: 15:32
 */
public class ItemNecktieBlock extends ItemMultiBlockBase
{
    private static final String[] BLOCK_NAMES = Enumerable.from(BlockNecktie.SubType.values()).select(
            new SingleArgFunc<BlockNecktie.SubType, String>()
            {
                @Override
                public String func(BlockNecktie.SubType value)
                {
                    return value.getUnlocalizedName();
                }
            }
    ).toArray(String.class);

    private static Item instance = null;

    public static void register()
    {
        instance = new ItemNecktieBlock(BlockNecktie.getInstance().blockID - Const.Block.BLOCK_ID_OFFSET);
        GameRegistry.registerItem(instance, Const.Item.ItemName.NECKTIE_BLOCK);
        for (BlockNecktie.SubType type : BlockNecktie.SubType.values())
        {
            LanguageRegistry.addName(type.getItemStack(), type.getDisplayName());
        }
    }

    public static void registerRecipe()
    {
        GameRegistry.addRecipe(
                BlockNecktie.SubType.NECKTIE.getItemStack(),
                new Object[] {
                        "NNN",
                        "NNN",
                        "NNN",
                        'N', new ItemStack(ItemArmorNecktie.getInstance(), 1, Short.MAX_VALUE)
                }
        );
        GameRegistry.addRecipe(
                BlockNecktie.SubType.IRON_NECKTIE.getItemStack(),
                new Object[] {
                        "NNN",
                        "NNN",
                        "NNN",
                        'N', new ItemStack(ItemArmorIronNecktie.getInstance(), 1, Short.MAX_VALUE)
                }
        );
        GameRegistry.addRecipe(
                BlockNecktie.SubType.DIAMOND_NECKTIE.getItemStack(),
                new Object[] {
                        "NNN",
                        "NNN",
                        "NNN",
                        'N', new ItemStack(ItemArmorDiamondNecktie.getInstance(), 1, Short.MAX_VALUE)
                }
        );

        if (Util.ic2Loaded())
        {
            GameRegistry.addRecipe(
                    BlockNecktie.SubType.NUKE_NECKTIE.getItemStack(),
                    new Object[] {
                            "NNN",
                            "NNN",
                            "NNN",
                            'N', new ItemStack(ItemArmorNukeNecktie.getInstance(), 1, Short.MAX_VALUE)
                    }
            );
        }
    }

    public ItemNecktieBlock(int par1)
    {
        super(par1, BlockNecktie.getInstance(), BLOCK_NAMES);
        setUnlocalizedName(Const.Item.ItemName.NECKTIE_BLOCK);
    }
}
