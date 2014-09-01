package com.mes51.minecraft.mods.necktie.util;

/**
 * Package: com.mes51.minecraft.mods.necktie.util
 * Date: 2014/05/02
 * Time: 2:54
 */
public class Point
{
    public int x;
    public int y;

    public Point(Point point)
    {
        this.x = point.x;
        this.y = point.y;
    }

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
