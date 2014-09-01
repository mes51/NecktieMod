package com.mes51.minecraft.mods.necktie.util;

/**
 * Package: com.mes51.minecraft.mods.necktie.util
 * Date: 2014/05/02
 * Time: 2:54
 */
public class Size
{
    public int width;
    public int height;

    public Size(Size size)
    {
        this.width = size.width;
        this.height = size.height;
    }

    public Size(int width, int height)
    {
        this.width = width;
        this.height = height;
    }
}
