package com.mes51.minecraft.mods.necktie.util;

import com.mes51.minecraft.mods.necktie.container.ContainerBase;
import com.mes51.minecraft.mods.necktie.container.ContainerMachineBlock;
import com.mes51.minecraft.mods.necktie.container.ContainerNecktieBeacon;
import com.mes51.minecraft.mods.necktie.gui.GuiBase;
import com.mes51.minecraft.mods.necktie.gui.GuiMachineBlock;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityBase;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityMachineBlock;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityNecktieBeacon;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

import java.lang.reflect.Constructor;

/**
 * Package: com.mes51.minecraft.mods.necktie.util
 * Date: 2014/05/02
 * Time: 0:26
 */
public enum GuiId
{
    MACHINE_BASE(0, TileEntityMachineBlock.class, ContainerMachineBlock.class),
    NECKTIE_BEACON(1, TileEntityNecktieBeacon.class, ContainerNecktieBeacon.class);

    private static final int GUI_ID_START = 1000;

    private int guiId = 0;
    private Class<? extends TileEntityBase> tileEntityClass = null;
    private Class<? extends ContainerBase<? extends TileEntityBase>> containerClass = null;

    public static GuiId byId(int guiId)
    {
        int index = guiId - GUI_ID_START;
        GuiId[] ids = values();
        if (index > -1 && index < ids.length)
        {
            return ids[index];
        }
        else
        {
            return null;
        }
    }

    private GuiId(int guiId, Class<? extends TileEntityBase> tileEntityClass, Class<? extends ContainerBase<? extends TileEntityBase>> containerClass)
    {
        this.guiId = guiId + GUI_ID_START;
        this.tileEntityClass = tileEntityClass;
        this.containerClass = containerClass;
    }

    public int getGuiId()
    {
        return this.guiId;
    }

    public Class<? extends TileEntityBase> getTileEntityClass()
    {
        return this.tileEntityClass;
    }

    public Object createContainer(InventoryPlayer playerInventory, TileEntity tileEntity)
    {
        try
        {
            if (tileEntity != null && tileEntity.getClass().equals(this.tileEntityClass))
            {
                Constructor constructor = this.containerClass.getConstructor(IInventory.class, TileEntity.class);
                return constructor.newInstance(playerInventory, tileEntity);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }
}
