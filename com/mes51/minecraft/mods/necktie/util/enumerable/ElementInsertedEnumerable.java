package com.mes51.minecraft.mods.necktie.util.enumerable;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Package: com.mes51.minecraft.mods.necktie.util.enumerable
 * Date: 14/03/30
 * Time: 0:01
 */
public class ElementInsertedEnumerable<T> extends Enumerable<T>
{
    public Iterable<T> source = null;
    public int index = -1;
    public T value = null;

    public ElementInsertedEnumerable(Iterable<T> source, int index, T value)
    {
        this.source = source;
        this.index = index;
        this.value = value;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            private int currentIndex = 0;
            private Iterator<T> iterator = source.iterator();

            @Override
            public boolean hasNext()
            {
                return this.iterator.hasNext() || this.currentIndex <= index;
            }

            @Override
            public T next()
            {
                T result = null;
                if (this.currentIndex == index)
                {
                    result = value;
                }
                else if (this.iterator.hasNext())
                {
                    result = this.iterator.next();
                }
                else if (this.currentIndex > index)
                {
                    throw new NoSuchElementException();
                }
                this.currentIndex++;
                return result;
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }
}
