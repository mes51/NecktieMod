package com.mes51.minecraft.mods.necktie.util.enumerable;

import java.util.Iterator;

/**
 * Package: com.mes51.minecraft.mods.necktie.util.enumerable
 * Date: 14/01/25
 * Time: 15:17
 */
public class RangeEnumerable extends Enumerable<Integer>
{
    private int start = 0;
    private int length = 0;

    public RangeEnumerable(int start, int length)
    {
        this.start = start;
        this.length = length;
    }

    @Override
    public Iterator<Integer> iterator()
    {
        return new Iterator<Integer>()
        {
            private int count;

            @Override
            public boolean hasNext()
            {
                return this.count < length;
            }

            @Override
            public Integer next()
            {

                return start + this.count++;
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }
}
