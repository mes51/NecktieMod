package com.mes51.minecraft.mods.necktie.gui;

import com.mes51.minecraft.mods.necktie.container.ContainerMachineBlock;
import com.mes51.minecraft.mods.necktie.gui.window.WindowBase;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityMachineBlock;
import com.mes51.minecraft.mods.necktie.tileentity.machine.MachineTileEntityBase;
import com.mes51.minecraft.mods.necktie.util.Vec3i;
import net.minecraft.inventory.IInventory;

/**
 * Package: com.mes51.minecraft.mods.necktie.gui
 * Date: 2014/05/02
 * Time: 1:07
 */
public class GuiMachineBlock extends GuiSingleWindowBase<TileEntityMachineBlock>
{
    public GuiMachineBlock(IInventory playerInventory, Vec3i tileEntityPos)
    {
        super(new ContainerMachineBlock(playerInventory, tileEntityPos));
    }

    @Override
    protected WindowBase<TileEntityMachineBlock> openWindow()
    {
        MachineTileEntityBase machineTileEntity = this.tileEntity.getMachineTileEntity();
        if (machineTileEntity != null)
        {
            try
            {
                return machineTileEntity.getWindowClass().getConstructor(GuiBase.class).newInstance(this);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        else
        {
            return null;
        }
    }
}
