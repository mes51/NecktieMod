package com.mes51.minecraft.mods.necktie.util;

/**
 * Package: com.mes51.minecraft.mods.necktie.util
 * Date: 2014/05/02
 * Time: 2:55
 */
public class Rect
{
    public int x = 0;
    public int y = 0;
    public int width = 0;
    public int height = 0;

    public Rect(Size size, Point point)
    {
        this.x = point.x;
        this.y = point.y;
        this.width = size.width;
        this.height = size.height;
    }

    public Rect(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Size getSize()
    {
        return new Size(this.width, this.height);
    }

    public Point getPoint()
    {
        return new Point(this.x, this.y);
    }
}
