package com.mes51.minecraft.mods.necktie.container.slot;

import com.mes51.minecraft.mods.necktie.util.Point;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Package: com.mes51.minecraft.mods.necktie.container.slot
 * Date: 2014/05/20
 * Time: 23:51
 */
public class SlotSwitchable extends Slot
{
    private boolean enable = false;

    public SlotSwitchable(IInventory par1IInventory, int par2, int par3, int par4, boolean enable)
    {
        super(par1IInventory, par2, par3, par4);
        this.enable = enable;
    }

    public boolean isEnable()
    {
        return this.enable;
    }

    public void setEnable(boolean enable)
    {
        this.enable = enable;
    }

    public void setPosition(Point position)
    {
        this.xDisplayPosition = position.x;
        this.yDisplayPosition = position.y;
    }

    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return this.enable;
    }
}
