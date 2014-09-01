package com.mes51.minecraft.mods.necktie.util.enumerable.operator;

/**
 * Package: com.mes51.minecraft.mods.necktie.util.enumerable.operator
 * Date: 14/01/25
 * Time: 16:46
 */
public interface ThreeArgFunc<T1, T2, T3, TResult>
{
    public TResult func(T1 t1, T2 t2, T3 t3);
}
