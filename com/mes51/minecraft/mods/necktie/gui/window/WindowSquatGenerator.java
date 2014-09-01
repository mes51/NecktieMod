package com.mes51.minecraft.mods.necktie.gui.window;

import com.mes51.minecraft.mods.necktie.gui.GuiBase;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityMachineBlock;
import com.mes51.minecraft.mods.necktie.tileentity.machine.MachineTileEntityBase;
import com.mes51.minecraft.mods.necktie.tileentity.machine.MachineTileEntitySquatGenerator;
import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.util.Size;

/**
 * Package: com.mes51.minecraft.mods.necktie.gui.window
 * Date: 2014/05/02
 * Time: 3:01
 */
public class WindowSquatGenerator extends WindowBase<TileEntityMachineBlock>
{
    private static final Size WINDOW_SIZE = new Size(178, 60);
    private static final int TEXT_TITLE_Y = 10;
    private static final int TEXT_INFO_Y = 30;
    private static final int TEXT_INFO_PADDING_Y = 10;

    public WindowSquatGenerator(GuiBase guiBase)
    {
        super(guiBase, WINDOW_SIZE.width, WINDOW_SIZE.height);
    }

    @Override
    protected void renderForeground(int mouseX, int mouseY)
    {
        String title = "Squat Generator";
        drawStringCenter(title, TEXT_TITLE_Y, 0x000000);

        MachineTileEntityBase machineTileEntity = getTileEntity().getMachineTileEntity();
        if (machineTileEntity instanceof MachineTileEntitySquatGenerator)
        {
            String stored = "Stored Energy: " + Const.Gui.STORAGE_FORMAT.format(((MachineTileEntitySquatGenerator)machineTileEntity).getStored());
            drawStringCenter(stored, TEXT_INFO_Y, 0x000000);

            String squatCount = "Squat Count " + Const.Gui.STORAGE_FORMAT.format(((MachineTileEntitySquatGenerator)machineTileEntity).getSquatCount());
            drawStringCenter(squatCount, TEXT_INFO_Y + TEXT_INFO_PADDING_Y, 0x000000);
        }
    }

    @Override
    public boolean canCloaseWindowPressE()
    {
        return true;
    }
}
