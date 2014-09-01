package com.mes51.minecraft.mods.necktie.render;

import com.mes51.minecraft.mods.necktie.entity.EntityAttackEffect;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Package: com.mes51.minecraft.mods.necktie.render
 * Date: 2014/04/28
 * Time: 17:23
 */
public class RenderEntityAttackEffect extends RenderEntity
{
    private static final String TEXTURE_DOMAIN = "com.mes51.minecraft.mods.necktie";
    private static final String TEXTURE_PATH = "textures/entity/attackEffect_";
    private static final double SIZE = 0.5;

    public static void register()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityAttackEffect.class, new RenderEntityAttackEffect());
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        renderEffect((EntityAttackEffect) par1Entity, par2, par4, par6);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return new ResourceLocation(TEXTURE_DOMAIN, TEXTURE_PATH + ((EntityAttackEffect)par1Entity).getTextureId() + ".png");
    }

    private void renderEffect(EntityAttackEffect effect, double x, double y, double z)
    {
        bindEntityTexture(effect);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glRotatef(effect.rotationYaw - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glScaled(SIZE, SIZE, SIZE);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_CULL_FACE);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-1.0, -1.0, -1.0, 1.0, 1.0);
        tessellator.addVertexWithUV(-1.0, -1.0, 1.0, 0.0, 1.0);
        tessellator.addVertexWithUV(-1.0, 1.0, 1.0, 0.0, 0.0);
        tessellator.addVertexWithUV(-1.0, 1.0, -1.0, 1.0, 0.0);
        tessellator.draw();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}
