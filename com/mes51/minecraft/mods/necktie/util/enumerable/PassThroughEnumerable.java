package com.mes51.minecraft.mods.necktie.util.enumerable;

import java.util.Iterator;

/**
 * Package: com.mes51.minecraft.mods.necktie.util.enumerable
 * Date: 14/01/14
 * Time: 1:24
 */
class PassThroughEnumerable<T> extends Enumerable<T>
{
    private Iterable<T> iterable = null;

    public PassThroughEnumerable(Iterable<T> iterable)
    {
        this.iterable = iterable;
    }

    @Override
    public Iterator<T> iterator()
    {
        return this.iterable.iterator();
    }
}
