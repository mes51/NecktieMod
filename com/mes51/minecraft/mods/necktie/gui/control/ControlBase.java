package com.mes51.minecraft.mods.necktie.gui.control;

import com.mes51.minecraft.mods.necktie.gui.window.WindowBase;

/**
 * Package: com.mes51.minecraft.mods.necktie.gui.control
 * Date: 2014/05/02
 * Time: 2:56
 */
public abstract class ControlBase
{
    protected WindowBase window;
    protected int x = 0;
    protected int y = 0;
    protected boolean focusable = false;
    protected boolean enabled = true;
    protected boolean visible = true;
    private Object tag = null;

    public ControlBase(WindowBase window, int x, int y)
    {
        this.window = window;
        this.x = x;
        this.y = y;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setTag(Object tag)
    {
        this.tag = tag;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public boolean isFocusable()
    {
        return this.focusable;
    }

    public boolean isEnabled()
    {
        return this.enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isVisible()
    {
        return this.visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public void focus() { }

    public void deFocus() { }

    public Object getTag()
    {
        return this.tag;
    }

    public boolean inRect(int mouseX, int mouseY)
    {
        return this.window.inRect(
                this.x,
                this.y,
                this.getWidth(),
                this.getHeight(),
                mouseX,
                mouseY
        );
    }

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract void render(int mouseX, int mouseY);

    public void renderAfterInventory(int mouseX, int mouseY) { }

    public void mouseClicked(int mouseX, int mouseY) { }

    public void mouseUp(int mouseX, int mouseY) { }

    public void mouseMoved(int mouseX, int mouseY) { }

    public void mouseWheelScrolled(int mouseX, int mouseY, int delta) { }

    public void keyPressed(int keyChar, int keyCode) { }
}
