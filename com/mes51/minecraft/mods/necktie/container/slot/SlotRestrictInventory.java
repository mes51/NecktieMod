package com.mes51.minecraft.mods.necktie.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Package: com.mes51.minecraft.mods.necktie.container.slot
 * Date: 2014/05/20
 * Time: 23:53
 */
public class SlotRestrictInventory extends SlotSwitchable
{
    private IInventory inventory = null;

    public SlotRestrictInventory(IInventory inventory, int par2, int par3, int par4)
    {
        this(inventory, par2, par3, par4, true);
    }

    public SlotRestrictInventory(IInventory inventory, int par2, int par3, int par4, boolean enable)
    {
        super(inventory, par2, par3, par4, enable);
        this.inventory = inventory;
    }

    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return super.isItemValid(par1ItemStack) &&
                this.inventory.isItemValidForSlot(this.getSlotIndex(), par1ItemStack);
    }
}
