package com.mes51.minecraft.mods.necktie.util;

import net.minecraft.util.Vec3;
import net.minecraftforge.common.ForgeDirection;
import org.lwjgl.util.vector.Vector3f;

/**
 * Package: com.mes51.minecraft.mods.timemachine.util
 * Date: 2013/08/26
 * Time: 15:12
 */
public class Vec3i
{
    public int x;
    public int y;
    public int z;

    public Vec3i(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3 toVec3()
    {
        return Vec3.createVectorHelper(this.x, this.y, this.z);
    }

    public Vector3f toVector3f()
    {
        return new Vector3f(this.x, this.y, this.z);
    }

    public double getDistance(Vec3i v)
    {
        double dx = this.x - v.x;
        double dy = this.y - v.y;
        double dz = this.z - v.z;

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public Vec3i add(Vec3i v)
    {
        return new Vec3i(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    public Vec3i add(ForgeDirection dir)
    {
        return new Vec3i(this.x + dir.offsetX, this.y + dir.offsetY, this.z + dir.offsetZ);
    }
}
