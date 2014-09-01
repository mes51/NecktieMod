package com.mes51.minecraft.mods.necktie.util.enumerable;

import java.util.Iterator;

/**
 * Package: com.mes51.minecraft.mods.necktie.util.enumerable
 * Date: 14/01/25
 * Time: 15:25
 */
public class ConcatEnumerable<T> extends Enumerable<T>
{
    private Iterable<T> first = null;
    private Iterable<T> second = null;

    public ConcatEnumerable(Iterable<T> first, Iterable<T> second)
    {
        this.first = first;
        this.second = second;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            private Iterator<T> firstItr = first.iterator();
            private Iterator<T> secondItr = second.iterator();

            @Override
            public boolean hasNext()
            {
                return this.firstItr.hasNext() || this.secondItr.hasNext();
            }

            @Override
            public T next()
            {
                if (this.firstItr.hasNext())
                {
                    return this.firstItr.next();
                }
                else
                {
                    return this.secondItr.next();
                }
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }
}
