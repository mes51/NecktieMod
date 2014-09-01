package com.mes51.minecraft.mods.necktie.util.enumerable;

import com.mes51.minecraft.mods.necktie.util.enumerable.operator.SingleArgFunc;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.TwoArgFunc;

import java.util.Iterator;

/**
 * Package: com.mes51.minecraft.mods.necktie.util.enumerable
 * Date: 14/01/12
 * Time: 18:33
 */
class SelectEnumerable<T, TResult> extends Enumerable<TResult>
{
    private TwoArgFunc<T, Integer, TResult> func = null;
    private Iterable<T> iterable = null;

    public SelectEnumerable(Iterable<T> iterable, final SingleArgFunc<T, TResult> func)
    {
        this(iterable, new TwoArgFunc<T, Integer, TResult>()
        {
            @Override
            public TResult func(T t, Integer integer)
            {
                return func.func(t);
            }
        });
    }

    public SelectEnumerable(Iterable<T> iterable, TwoArgFunc<T, Integer, TResult> func)
    {
        this.func = func;
        this.iterable = iterable;
    }

    @Override
    public Iterator<TResult> iterator()
    {
        return new Iterator<TResult>()
        {
            private Iterator<T> iterator = iterable.iterator();
            private int count = 0;

            @Override
            public boolean hasNext()
            {
                return iterator.hasNext();
            }

            @Override
            public TResult next()
            {
                return func.func(this.iterator.next(), this.count++);
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }
}
