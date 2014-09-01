package com.mes51.minecraft.mods.necktie.gui;

import com.mes51.minecraft.mods.necktie.container.ContainerNecktieBeacon;
import com.mes51.minecraft.mods.necktie.gui.window.WindowBase;
import com.mes51.minecraft.mods.necktie.gui.window.WindowNecktieBeacon;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityNecktieBeacon;
import com.mes51.minecraft.mods.necktie.util.Vec3i;
import net.minecraft.inventory.IInventory;

/**
 * Package: com.mes51.minecraft.mods.necktie.gui
 * Date: 2014/05/20
 * Time: 1:17
 */
public class GuiNecktieBeacon extends GuiSingleWindowBase<TileEntityNecktieBeacon>
{
    public GuiNecktieBeacon(IInventory playerInventory, Vec3i tileEntityPos)
    {
        super(new ContainerNecktieBeacon(playerInventory, tileEntityPos));
    }

    @Override
    protected WindowBase<TileEntityNecktieBeacon> openWindow()
    {
        return new WindowNecktieBeacon(this);
    }
}
