package com.mes51.minecraft.mods.necktie.util.enumerable;

import com.mes51.minecraft.mods.necktie.util.Tuple;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.SingleArgFunc;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Package: com.mes51.minecraft.mods.necktie.util.enumerable
 * Date: 14/01/14
 * Time: 1:08
 */
class OrderByEnumerable<T, TKey extends Comparable<TKey>> extends Enumerable<T>
{
    private Iterable<T> iterable = null;
    private SingleArgFunc<T, TKey> func = null;
    private boolean descend = false;

    public OrderByEnumerable(Iterable<T> iterable, SingleArgFunc<T, TKey> func, boolean descend)
    {
        this.iterable = iterable;
        this.func = func;
        this.descend = descend;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            private Iterator<T> iterator = null;

            @Override
            public boolean hasNext()
            {
                if (iterator == null)
                {
                    this.sort();
                }
                return iterator.hasNext();
            }

            @Override
            public T next()
            {
                if (iterator == null)
                {
                    this.sort();
                }
                return iterator.next();
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }

            private void sort()
            {
                List<Tuple<T, TKey>> sortedList = Enumerable.from(iterable)
                        .select(
                                new SingleArgFunc<T, Tuple<T, TKey>>()
                                {
                                    @Override
                                    public Tuple<T, TKey> func(T value)
                                    {
                                        return new Tuple<T, TKey>(value, func.func(value));
                                    }
                                }
                        )
                        .toList();
                Collections.sort(sortedList, new Comparator<Tuple<T, TKey>>()
                {
                    @Override
                    public int compare(Tuple<T, TKey> o1, Tuple<T, TKey> o2)
                    {
                        return o1.getItem2().compareTo(o2.getItem2());
                    }
                });
                if (descend)
                {
                    Collections.reverse(sortedList);
                }
                this.iterator = Enumerable.from(sortedList)
                        .select(
                                new SingleArgFunc<Tuple<T, TKey>, T>()
                                {
                                    @Override
                                    public T func(Tuple<T, TKey> value)
                                    {
                                        return value.getItem1();
                                    }
                                }
                        )
                        .iterator();
            }
        };
    }
}
