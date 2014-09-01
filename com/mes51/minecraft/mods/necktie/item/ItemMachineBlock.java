package com.mes51.minecraft.mods.necktie.item;

import com.mes51.minecraft.mods.necktie.block.BlockMachine;
import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.util.Util;
import com.mes51.minecraft.mods.necktie.util.enumerable.Enumerable;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.SingleArgFunc;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import ic2.api.item.Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTextureTile;
import net.minecraft.item.ItemStack;

/**
 * Package: com.mes51.minecraft.mods.necktie.item
 * Date: 2014/05/01
 * Time: 13:20
 */
public class ItemMachineBlock extends ItemMultiBlockBase
{
    private static final String[] BLOCK_NAMES = Enumerable.from(BlockMachine.SubType.values()).select(
            new SingleArgFunc<BlockMachine.SubType, String>()
            {
                @Override
                public String func(BlockMachine.SubType value)
                {
                    return value.getUnlocalizedName();
                }
            }
    ).toArray(String.class);

    private static Item instance = null;

    public static void register()
    {
        if (Util.ic2Loaded())
        {
            instance = new ItemMachineBlock(BlockMachine.getInstance().blockID - Const.Block.BLOCK_ID_OFFSET);
            GameRegistry.registerItem(instance, Const.Item.ItemName.MACHINE_BLOCK);
            for (BlockMachine.SubType type : BlockMachine.SubType.values())
            {
                LanguageRegistry.addName(type.getItemStack(), type.getDisplayName());
            }
        }
    }

    public static void registerRecipe()
    {
        if (Util.ic2Loaded())
        {
            GameRegistry.addRecipe(
                    BlockMachine.SubType.SQUAT_GENERATOR.getItemStack(),
                    new Object[] {
                            "RRR",
                            "CGC",
                            "MBM",
                            'R', Items.getItem("rubberTrampoline"),
                            'C', Items.getItem("coil"),
                            'G', Items.getItem("generator"),
                            'M', Items.getItem("elemotor"),
                            'B', new ItemStack(Items.getItem("batBox").getItem(), 1, Short.MAX_VALUE)
                    }
            );
        }
    }

    public ItemMachineBlock(int par1)
    {
        super(par1, BlockMachine.getInstance(), BLOCK_NAMES);
        setUnlocalizedName(Const.Item.ItemName.MACHINE_BLOCK);
    }
}
