package com.mes51.minecraft.mods.necktie.container;

import com.mes51.minecraft.mods.necktie.tileentity.TileEntityMachineBlock;
import com.mes51.minecraft.mods.necktie.util.Vec3i;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

/**
 * Package: com.mes51.minecraft.mods.necktie.container
 * Date: 2014/05/02
 * Time: 1:06
 */
public class ContainerMachineBlock extends ContainerBase<TileEntityMachineBlock>
{
    public ContainerMachineBlock(IInventory playerInventory, TileEntity tileEntity)
    {
        super(playerInventory, tileEntity);
    }

    public ContainerMachineBlock(IInventory playerInventory, Vec3i tileEntityPos)
    {
        super(playerInventory, tileEntityPos);
    }

    @Override
    protected TileEntityMachineBlock getDefaultTileEntity()
    {
        return new TileEntityMachineBlock();
    }

    @Override
    protected void layoutContainer()
    {
        addPlayerInventory(FAR, FAR);
    }
}
