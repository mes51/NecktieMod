package com.mes51.minecraft.mods.necktie.util.enumerable;

import com.mes51.minecraft.mods.necktie.util.Tuple;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.Action;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.SingleArgFunc;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.ThreeArgFunc;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.TwoArgFunc;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Package: com.mes51.minecraft.mods.necktie.util.enumerable
 * Date: 14/01/25
 * Time: 16:44
 */
public class SelectManyEnumerable<T, TCollection, TResult> extends Enumerable<TResult>
{
    private Iterable<T> source = null;
    private SingleArgFunc<T, Iterable<TCollection>> collectionSelector = null;
    private ThreeArgFunc<T, TCollection, Integer, Iterable<TResult>> func = null;

    public SelectManyEnumerable(Iterable<T> source, final SingleArgFunc<T, Iterable<TResult>> func)
    {
        this(source, new TwoArgFunc<T, Integer, Iterable<TResult>>()
        {
            @Override
            public Iterable<TResult> func(T t, Integer integer)
            {
                return func.func(t);
            }
        });
    }

    public SelectManyEnumerable(Iterable<T> source, final TwoArgFunc<T, Integer, Iterable<TResult>> func)
    {
        this.source = source;
        this.func = new ThreeArgFunc<T, TCollection, Integer, Iterable<TResult>>()
        {
            @Override
            public Iterable<TResult> func(T t, TCollection tCollection, Integer integer)
            {
                return func.func(t, integer);
            }
        };
        this.collectionSelector = new SingleArgFunc<T, Iterable<TCollection>>()
        {
            @Override
            public Iterable<TCollection> func(T value)
            {
                return Enumerable.<TCollection>repeat(null, 1);
            }
        };
    }

    public SelectManyEnumerable(Iterable<T> source, SingleArgFunc<T, Iterable<TCollection>> collectionSelector, final TwoArgFunc<T, TCollection, Iterable<TResult>> func)
    {
        this(source, collectionSelector, new ThreeArgFunc<T, TCollection, Integer, Iterable<TResult>>()
        {
            @Override
            public Iterable<TResult> func(T t, TCollection tCollection, Integer integer)
            {
                return func.func(t, tCollection);
            }
        });
    }

    public SelectManyEnumerable(Iterable<T> source, SingleArgFunc<T, Iterable<TCollection>> collectionSelector, ThreeArgFunc<T, TCollection, Integer, Iterable<TResult>> func)
    {
        this.source = source;
        this.collectionSelector = collectionSelector;
        this.func = func;
    }

    @Override
    public Iterator<TResult> iterator()
    {
        Enumerable<Tuple<T, TCollection>> flattenCollection = Enumerable.<Tuple<T, TCollection>>empty();
        Enumerable<Enumerable<Tuple<T, TCollection>>> converted = Enumerable.from(this.source)
                .select(
                        new SingleArgFunc<T, Enumerable<Tuple<T, TCollection>>>()
                        {
                            @Override
                            public Enumerable<Tuple<T, TCollection>> func(final T sourceElement)
                            {
                                return Enumerable.from(SelectManyEnumerable.this.collectionSelector.func(sourceElement))
                                        .select(
                                                new SingleArgFunc<TCollection, Tuple<T, TCollection>>()
                                                {
                                                    @Override
                                                    public Tuple<T, TCollection> func(TCollection value)
                                                    {
                                                        return new Tuple<T, TCollection>(sourceElement, value);
                                                    }
                                                }
                                        );
                            }
                        }
                );
        for (Enumerable<Tuple<T, TCollection>> e : converted)
        {
            flattenCollection = flattenCollection.concat(e);
        }

        final Enumerable<Tuple<T, TCollection>> fixedFlattenCollection = flattenCollection;
        return new Iterator<TResult>()
        {
            private Iterator<Tuple<T, TCollection>> iterator = fixedFlattenCollection.iterator();
            private Iterator<TResult> resultIterator = null;
            private TResult nextValue = null;
            private int count = 0;

            @Override
            public boolean hasNext()
            {
                if (this.nextValue != null)
                {
                    return true;
                }

                while ((this.resultIterator == null || !this.resultIterator.hasNext()) && this.iterator.hasNext())
                {
                    Tuple<T, TCollection> element = this.iterator.next();
                    this.resultIterator = func.func(element.getItem1(), element.getItem2(), this.count++).iterator();
                }
                if ( this.resultIterator != null && this.resultIterator.hasNext())
                {
                    this.nextValue = this.resultIterator.next();
                }
                return this.nextValue != null;
            }

            @Override
            public TResult next()
            {
                if (this.nextValue != null)
                {
                    TResult result = this.nextValue;
                    this.nextValue = null;
                    return result;
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
