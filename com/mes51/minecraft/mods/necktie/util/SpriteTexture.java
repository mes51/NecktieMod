package com.mes51.minecraft.mods.necktie.util;

import com.mes51.minecraft.mods.necktie.gui.GuiBase;
import org.lwjgl.opengl.GL11;

/**
 * Package: com.mes51.minecraft.mods.necktie.util
 * Date: 2014/05/02
 * Time: 2:51
 */
public class SpriteTexture
{
    private String location = null;
    private int textureX = 0;
    private int textureY = 0;
    private int width = 0;
    private int height = 0;

    public SpriteTexture(String location, int x, int y, int width, int height)
    {
        this.location = location;
        this.textureX = x;
        this.textureY = y;
        this.width = width;
        this.height = height;
    }

    public int getTextureX()
    {
        return this.textureX;
    }

    public int getTextureY()
    {
        return this.textureY;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public void render(GuiBase gui, int x, int y)
    {
        render(gui, x, y, this.width, this.height);
    }

    public void render(GuiBase gui, int x, int y, int w, int h)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        gui.bindTextureFromResource(this.location);
        gui.drawTexturedModalRect(
                x,
                y,
                this.textureX,
                this.textureY,
                Math.max(Math.min(w, this.width), 0),
                Math.max(Math.min(h, this.height), 0)
        );
    }
}
