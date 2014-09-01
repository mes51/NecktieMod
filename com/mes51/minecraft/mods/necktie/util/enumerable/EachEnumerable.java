package com.mes51.minecraft.mods.necktie.util.enumerable;

import com.mes51.minecraft.mods.necktie.util.enumerable.operator.Action;

import java.util.Iterator;

/**
 * Package: com.mes51.minecraft.mods.necktie.util.enumerable
 * Date: 14/01/06
 * Time: 11:04
 */
class EachEnumerable<T> extends Enumerable<T>
{
    private Action<T> action = null;
    private Iterable<T> iterable = null;

    public EachEnumerable(Iterable<T> iterable, Action<T> action)
    {
        this.iterable = iterable;
        this.action = action;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            private Iterator<T> iterator = iterable.iterator();

            @Override
            public boolean hasNext()
            {
                return this.iterator.hasNext();
            }

            @Override
            public T next()
            {
                T value = this.iterator.next();
                EachEnumerable.this.action.action(value);
                return value;
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }
}
