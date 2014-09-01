package com.mes51.minecraft.mods.necktie.util.enumerable;

import com.mes51.minecraft.mods.necktie.util.enumerable.operator.Predicate;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Package: com.mes51.minecraft.mods.necktie.util.enumerable
 * Date: 14/01/06
 * Time: 11:44
 */
class WhereEnumerable<T> extends Enumerable<T>
{
    private Predicate<T> predicate = null;
    private Iterable<T> iterable = null;

    public WhereEnumerable(Iterable<T> iterable, Predicate<T> predicate)
    {
        this.iterable = iterable;
        this.predicate = predicate;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            private Iterator<T> iterator = iterable.iterator();
            private T nextValue = null;

            @Override
            public boolean hasNext()
            {
                if (this.nextValue != null)
                {
                    return true;
                }

                boolean hit = false;
                while (!hit && this.iterator.hasNext())
                {
                    T value = this.iterator.next();
                    if (WhereEnumerable.this.predicate.predicate(value))
                    {
                        this.nextValue = value;
                        hit = true;
                    }
                }
                return hit;
            }

            @Override
            public T next()
            {
                if (hasNext())
                {
                    T value = this.nextValue;
                    this.nextValue = null;
                    return value;
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
