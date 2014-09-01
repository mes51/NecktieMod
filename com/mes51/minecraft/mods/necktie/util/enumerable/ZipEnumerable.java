package com.mes51.minecraft.mods.necktie.util.enumerable;

import com.mes51.minecraft.mods.necktie.util.enumerable.operator.TwoArgFunc;

import java.util.Iterator;

/**
 * Package: com.mes51.minecraft.mods.necktie.util.enumerable
 * Date: 14/01/12
 * Time: 22:21
 */
public class ZipEnumerable<T1, T2, TResult> extends Enumerable<TResult>
{
    private Iterable<T1> first = null;
    private Iterable<T2> second = null;
    private TwoArgFunc<T1, T2, TResult> func = null;

    public ZipEnumerable(Iterable<T1> first, Iterable<T2> second, TwoArgFunc<T1, T2, TResult> func)
    {
        this.first = first;
        this.second = second;
        this.func = func;
    }

    @Override
    public Iterator<TResult> iterator()
    {
        return new Iterator<TResult>()
        {
            private Iterator<T1> iteratorFirst = first.iterator();
            private Iterator<T2> iteratorSecond = second.iterator();

            @Override
            public boolean hasNext()
            {
                return iteratorFirst.hasNext();
            }

            @Override
            public TResult next()
            {
                return func.func(this.iteratorFirst.next(), this.iteratorSecond.next());
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }
}
