package com.mes51.minecraft.mods.necktie.util;

import java.io.Serializable;

/**
 * Package: com.mes51.minecraft.mods.necktie.util
 * Date: 13/08/13
 * Time: 0:32
 */
public class Tuple<T, E> implements Serializable
{
    private T item1 = null;
    private E item2 = null;

    public Tuple() { }

    public Tuple(T item1, E item2)
    {
        this.item1 = item1;
        this.item2 = item2;
    }

    public T getItem1()
    {
        return this.item1;
    }

    public E getItem2()
    {
        return this.item2;
    }
}
