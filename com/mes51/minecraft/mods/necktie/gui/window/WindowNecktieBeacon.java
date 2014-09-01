package com.mes51.minecraft.mods.necktie.gui.window;

import com.mes51.minecraft.mods.necktie.container.slot.SlotRestrictInventory;
import com.mes51.minecraft.mods.necktie.gui.GuiBase;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityNecktieBeacon;
import com.mes51.minecraft.mods.necktie.util.Size;
import net.minecraft.inventory.Slot;

import java.util.List;

/**
 * Package: com.mes51.minecraft.mods.necktie.gui.window
 * Date: 2014/05/20
 * Time: 23:49
 */
public class WindowNecktieBeacon extends WindowBase<TileEntityNecktieBeacon>
{
    private static final Size WINDOW_SIZE = new Size(178, 178);
    private static final int TEXT_TITLE_Y = 10;

    public WindowNecktieBeacon(GuiBase guiBase)
    {
        super(guiBase, WINDOW_SIZE.width, WINDOW_SIZE.height);
    }

    @Override
    protected void renderForeground(int mouseX, int mouseY)
    {
        String title = "Necktie Beacon";
        drawStringCenter(title, TEXT_TITLE_Y, 0x000000);

        for (Slot slot : (List<Slot>)getContainer().inventorySlots)
        {
            int sx = slot.xDisplayPosition - 1;
            int sy = slot.yDisplayPosition - 1;
            if (slot instanceof SlotRestrictInventory)
            {
                drawInventorySlot(sx, sy, InventoryType.NECKTIE);
            }
            else
            {
                drawInventorySlot(sx, sy);
            }
        }
    }

    @Override
    public boolean canCloaseWindowPressE()
    {
        return true;
    }
}
