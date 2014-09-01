package com.mes51.minecraft.mods.necktie.util.enumerable.operator;

/**
 * Package: com.mes51.minecraft.mods.necktie.util.enumerable.operator
 * Date: 14/01/12
 * Time: 22:20
 */
public interface TwoArgFunc<T1, T2, TResult>
{
    public TResult func(T1 t1, T2 t2);
}
