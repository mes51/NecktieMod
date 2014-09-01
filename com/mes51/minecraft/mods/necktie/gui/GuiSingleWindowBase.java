package com.mes51.minecraft.mods.necktie.gui;

import com.mes51.minecraft.mods.necktie.container.ContainerBase;
import com.mes51.minecraft.mods.necktie.gui.window.WindowBase;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityBase;
import net.minecraft.client.gui.GuiButton;

/**
 * Package: com.mes51.minecraft.mods.necktie.gui
 * Date: 2014/05/20
 * Time: 1:41
 */
public abstract class GuiSingleWindowBase<T extends TileEntityBase> extends GuiBase<T>
{
    private boolean buttonEventProcessed = false;
    private WindowBase<T> window = null;

    public GuiSingleWindowBase(ContainerBase<T> par1Container)
    {
        super(par1Container);
    }

    @Override
    public void initGui()
    {
        if (this.window != null)
        {
            this.window.activate();
            this.xSize = this.window.getWidth();
            this.ySize = this.window.getHeight();
        }

        super.initGui();
    }

    @Override
    public void keyTyped(char par1, int par2)
    {
        // esc or ((empty window or window cloaseable E) and E)
        if (par2 == 1 || ((this.window == null || this.window.canCloaseWindowPressE()) && par2 == this.mc.gameSettings.keyBindInventory.keyCode))
        {
            this.mc.thePlayer.closeScreen();
        }
        else if (this.window != null)
        {
            this.window.keyPressed(par1, par2);
        }
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        super.actionPerformed(par1GuiButton);

        // 同じ位置にボタンが複数あると全部が反応してしまう問題の対策
        if (this.buttonEventProcessed)
        {
            return;
        }

        if (this.window != null)
        {
            this.window.actionPerformed(par1GuiButton);
        }

        this.buttonEventProcessed = true;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        this.buttonEventProcessed = false;

        if (this.window == null)
        {
            this.window = openWindow();
            if (this.window != null)
            {
                initGui();
            }
        }

        if (this.window != null)
        {
            this.window.render(i, j);
        }
        else
        {
            String message = "loading...";
            this.fontRenderer.drawString(
                    message,
                    (this.width - this.fontRenderer.getStringWidth(message)) / 2,
                    (this.height - this.fontRenderer.FONT_HEIGHT) / 2,
                    0xffffffff
            );
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);

        if (this.window != null)
        {
            this.window.renderAfterInventory(par1, par2);
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        if (this.window != null)
        {
            this.window.mouseClicked(par1, par2);
        }
    }

    @Override
    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        super.mouseMovedOrUp(par1, par2, par3);
        if (this.window != null)
        {
            if (par3 > -1)
            {
                this.window.mouseUp(par1, par2);
            }
            else
            {
                this.window.mouseMoved(par1, par2);
            }
        }
    }

    @Override
    protected void mouseWheelScrolled(int mouseX, int mouseY, int delta)
    {
        super.mouseWheelScrolled(mouseX, mouseY, delta);
        if (this.window != null)
        {
            this.window.mouseWheelScrolled(mouseX, mouseY, delta);
        }
    }

    protected abstract WindowBase<T> openWindow();
}
