package com.mes51.minecraft.mods.necktie.util.enumerable;

import com.mes51.minecraft.mods.necktie.util.enumerable.operator.Action;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.SingleArgFunc;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.Predicate;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.TwoArgFunc;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Package: com.mes51.minecraft.mods.necktie.util.enumerable
 * Date: 14/01/06
 * Time: 10:56
 */
public abstract class Enumerable<T> implements Iterable<T>
{
    public static <T> Enumerable<T> from(Iterable<T> iterable)
    {
        return new PassThroughEnumerable<T>(iterable);
    }

    public static <T> Enumerable<T> from(T[] iterable)
    {
        List<T> objectList = new ArrayList<T>(iterable.length);
        for (T v : iterable)
        {
            objectList.add(v);
        }
        return from(objectList);
    }

    public static Enumerable<Byte> from(byte[] iterable)
    {
        List<Byte> objectList = new ArrayList<Byte>(iterable.length);
        for (byte v : iterable)
        {
            objectList.add(v);
        }
        return from(objectList);
    }

    public static Enumerable<Short> from(short[] iterable)
    {
        List<Short> objectList = new ArrayList<Short>(iterable.length);
        for (short v : iterable)
        {
            objectList.add(v);
        }
        return from(objectList);
    }

    public static Enumerable<Character> from(char[] iterable)
    {
        List<Character> objectList = new ArrayList<Character>(iterable.length);
        for (char v : iterable)
        {
            objectList.add(v);
        }
        return from(objectList);
    }

    public static Enumerable<Integer> from(int[] iterable)
    {
        List<Integer> objectList = new ArrayList<Integer>(iterable.length);
        for (int v : iterable)
        {
            objectList.add(v);
        }
        return from(objectList);
    }

    public static Enumerable<Long> from(long[] iterable)
    {
        List<Long> objectList = new ArrayList<Long>(iterable.length);
        for (long v : iterable)
        {
            objectList.add(v);
        }
        return from(objectList);
    }

    public static Enumerable<Float> from(float[] iterable)
    {
        List<Float> objectList = new ArrayList<Float>(iterable.length);
        for (float v : iterable)
        {
            objectList.add(v);
        }
        return from(objectList);
    }

    public static Enumerable<Double> from(double[] iterable)
    {
        List<Double> objectList = new ArrayList<Double>(iterable.length);
        for (double v : iterable)
        {
            objectList.add(v);
        }
        return from(objectList);
    }

    public static Enumerable<Boolean> from(boolean[] iterable)
    {
        List<Boolean> objectList = new ArrayList<Boolean>(iterable.length);
        for (boolean v : iterable)
        {
            objectList.add(v);
        }
        return from(objectList);
    }

    public static Enumerable<Integer> range(int start, int length)
    {
        if (length > -1)
        {
            return new RangeEnumerable(start, length);
        }
        else
        {
            throw new IndexOutOfBoundsException("length");
        }
    }

    public static <T> Enumerable<T> repeat(final T target, int length)
    {
        return range(0, length).select(
                new SingleArgFunc<Integer, T>()
                {
                    @Override
                    public T func(Integer value)
                    {
                        return target;
                    }
                }
        );
    }

    public static <T> Enumerable<T> empty()
    {
        return Enumerable.from(Collections.<T>emptyList());
    }

    public List<T> toList()
    {
        List<T> list = new ArrayList<T>();
        for (T t : this)
        {
            list.add(t);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public T[] toArray(Class<T> klass)
    {
        List<T> list = toList();
        T[] array = (T[])Array.newInstance(klass, list.size());
        return list.toArray((T[])array);
    }

    public byte[] toByteArray()
    {
        List<T> list = toList();
        byte[] result = new byte[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = (Byte)list.get(i);
        }
        return result;
    }

    public short[] toShortArray()
    {
        List<T> list = toList();
        short[] result = new short[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = (Short)list.get(i);
        }
        return result;
    }

    public char[] toCharArray()
    {
        List<T> list = toList();
        char[] result = new char[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = (Character)list.get(i);
        }
        return result;
    }

    public int[] toIntArray()
    {
        List<T> list = toList();
        int[] result = new int[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = (Integer)list.get(i);
        }
        return result;
    }

    public long[] toLongArray()
    {
        List<T> list = toList();
        long[] result = new long[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = (Long)list.get(i);
        }
        return result;
    }

    public float[] toFloatArray()
    {
        List<T> list = toList();
        float[] result = new float[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = (Float)list.get(i);
        }
        return result;
    }

    public double[] toDoubleArray()
    {
        List<T> list = toList();
        double[] result = new double[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = (Double)list.get(i);
        }
        return result;
    }

    public boolean[] toBooleanArray()
    {
        List<T> list = toList();
        boolean[] result = new boolean[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = (Boolean)list.get(i);
        }
        return result;
    }

    public Enumerable<T> each(Action<T> action)
    {
        return new EachEnumerable<T>(this, action);
    }

    public Enumerable<T> where(Predicate<T> predicate)
    {
        return new WhereEnumerable<T>(this, predicate);
    }

    public T first()
    {
        for (T t : this)
        {
            return t;
        }
        return null;
    }

    public T first(Predicate<T> predicate)
    {
        return this.where(predicate).first();
    }

    public boolean contains(Predicate<T> predicate)
    {
        return this.first(predicate) != null;
    }

    public boolean contains(final T v)
    {
        return this.contains(new Predicate<T>()
        {
            @Override
            public boolean predicate(T value)
            {
                return value == v;
            }
        });
    }

    public int indexOf(Predicate<T> predicate)
    {
        int index = 0;
        for (T t : this)
        {
            if (predicate.predicate(t))
            {
                return index;
            }
            else
            {
                index++;
            }
        }
        return -1;
    }

    public int count()
    {
        return this.toList().size();
    }

    public int count(Predicate<T> predicate)
    {
        return this.where(predicate).toList().size();
    }

    public Enumerable<T> take(int count)
    {
        if (count > -1)
        {
            return new SkipTakeEnumerable<T>(this, 0, count);
        }
        else
        {
            throw new IndexOutOfBoundsException("count");
        }
    }

    public Enumerable<T> skip(int count)
    {
        if (count > -1)
        {
            return new SkipTakeEnumerable<T>(this, count, -1);
        }
        else
        {
            throw new IndexOutOfBoundsException("count");
        }
    }

    public <TResult> Enumerable<TResult> select(SingleArgFunc<T, TResult> func)
    {
        return new SelectEnumerable<T, TResult>(this, func);
    }

    public <TResult> Enumerable<TResult> select(TwoArgFunc<T, Integer, TResult> func)
    {
        return new SelectEnumerable<T, TResult>(this, func);
    }

    public <TResult> Enumerable<TResult> selectMany(SingleArgFunc<T, Iterable<TResult>> func)
    {
        return new SelectManyEnumerable<T, T, TResult>(this, func);
    }

    public <TCollection, TResult> Enumerable<TResult> selectMany(SingleArgFunc<T, Iterable<TCollection>> collectionSelector, TwoArgFunc<T, TCollection, Iterable<TResult>> func)
    {
        return new SelectManyEnumerable<T, TCollection, TResult>(this, collectionSelector, func);
    }

    public <T2, TResult> Enumerable<TResult> zip(Iterable<T2> second, TwoArgFunc<T, T2, TResult> func)
    {
        return new ZipEnumerable<T, T2, TResult>(this, second, func);
    }

    public <TKey extends Comparable<TKey>> Enumerable<T> orderBy(SingleArgFunc<T, TKey> keySelector)
    {
        return new OrderByEnumerable<T, TKey>(this, keySelector, false);
    }

    public <TKey extends Comparable<TKey>> Enumerable<T> orderByDescending(SingleArgFunc<T, TKey> keySelector)
    {
        return new OrderByEnumerable<T, TKey>(this, keySelector, true);
    }

    public <TResult> Enumerable<TResult> cast()
    {
        return this.select(
                new SingleArgFunc<T, TResult>()
                {
                    @Override
                    public TResult func(T value)
                    {
                        return (TResult) value;
                    }
                }
        );
    }

    public Enumerable<T> concat(Iterable<T> second)
    {
        return new ConcatEnumerable<T>(this, second);
    }

    public Enumerable<T> concat(T value)
    {
        return new ConcatEnumerable<T>(this, Enumerable.repeat(value, 1));
    }

    public Enumerable<T> insert(int index, T value)
    {
        return new ElementInsertedEnumerable<T>(this, index, value);
    }

    public <TAccumulate> TAccumulate agglegate(final TAccumulate seed, TwoArgFunc<TAccumulate, T, TAccumulate> func)
    {
        TAccumulate result = seed;
        for (T t : this)
        {
            result = func.func(result, t);
        }
        return result;
    }

    public Enumerable<T> cache()
    {
        List<T> list = toList();
        return from(list);
    }
}
