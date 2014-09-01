package com.mes51.minecraft.mods.necktie.container;

import com.mes51.minecraft.mods.necktie.container.slot.SlotRestrictInventory;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityNecktieBeacon;
import com.mes51.minecraft.mods.necktie.util.Vec3i;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

/**
 * Package: com.mes51.minecraft.mods.necktie.container
 * Date: 2014/05/20
 * Time: 1:18
 */
public class ContainerNecktieBeacon extends ContainerBase<TileEntityNecktieBeacon>
{
    public ContainerNecktieBeacon(IInventory playerInventory, TileEntity tileEntity)
    {
        super(playerInventory, tileEntity);
    }

    public ContainerNecktieBeacon(IInventory playerInventory, Vec3i tileEntityPos)
    {
        super(playerInventory, tileEntityPos);
    }

    @Override
    protected TileEntityNecktieBeacon getDefaultTileEntity()
    {
        return new TileEntityNecktieBeacon();
    }

    @Override
    protected void layoutContainer()
    {
        addSlot(
                new SlotRestrictInventory(this.entity, 0, 81, 44),
                InventoryType.INSERTABLE
        );

        addPlayerInventory(9, 96);
    }
}
