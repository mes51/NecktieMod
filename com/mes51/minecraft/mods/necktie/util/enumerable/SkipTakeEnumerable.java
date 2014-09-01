package com.mes51.minecraft.mods.necktie.util.enumerable;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Package: com.mes51.minecraft.mods.necktie.util.enumerable
 * Date: 14/01/06
 * Time: 11:51
 */
class SkipTakeEnumerable<T> extends Enumerable<T>
{
    private int count = 0;
    private int skip = 0;
    private Iterable<T> iterable = null;

    public SkipTakeEnumerable(Iterable<T> iterable, int skip, int count)
    {
        this.iterable = iterable;
        this.skip = skip;
        this.count = count;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            private Iterator<T> iterator = iterable.iterator();
            private boolean skipp = false;
            private int taken = 0;

            @Override
            public boolean hasNext()
            {
                if (!skipp)
                {
                    for (int i = 0; i < skip && this.iterator.hasNext(); i++)
                    {
                        this.iterator.next();
                    }
                    this.skipp = true;
                }

                return (count < 0 || this.taken < count) && this.iterator.hasNext();
            }

            @Override
            public T next()
            {
                if (hasNext())
                {
                    this.taken++;
                    return this.iterator.next();
                }
                else
                {
                    throw new NoSuchElementException();
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
