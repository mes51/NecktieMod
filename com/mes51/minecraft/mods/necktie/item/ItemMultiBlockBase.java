package com.mes51.minecraft.mods.necktie.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTextureTile;

/**
 * Package: com.mes51.minecraft.mods.necktie.item
 * Date: 2014/05/17
 * Time: 16:58
 */
public abstract class ItemMultiBlockBase extends ItemMultiTextureTile
{
    private String realUnlocalizedName = "";

    public ItemMultiBlockBase(int par1, Block par2Block, String[] par3ArrayOfStr)
    {
        super(par1, par2Block, par3ArrayOfStr);
    }

    @Override
    public Item setUnlocalizedName(String par1Str)
    {
        this.realUnlocalizedName = par1Str;
        return super.setUnlocalizedName(par1Str);
    }

    @Override
    public String getUnlocalizedName()
    {
        return this.realUnlocalizedName;
    }
}
