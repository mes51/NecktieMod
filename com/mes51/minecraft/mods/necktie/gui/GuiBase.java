package com.mes51.minecraft.mods.necktie.gui;

import com.mes51.minecraft.mods.necktie.container.ContainerBase;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureObject;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Package: com.mes51.minecraft.mods.necktie.gui
 * Date: 2014/05/02
 * Time: 0:28
 */
public abstract class GuiBase<T extends TileEntityBase> extends GuiContainer
{
    private static final int INVENTORY_RECT_SIZE = 16;
    private static final String TEXTURE_DOMAIN = "com.mes51.minecraft.mods.necktie";
    private static final Map<Integer, TextureObject> TEXTURES = new HashMap<Integer, TextureObject>();

    protected T tileEntity = null;

    public GuiBase(ContainerBase<T> par1Container)
    {
        super(par1Container);
        this.tileEntity = par1Container.getEntity();
    }

    public void addButton(GuiButton button)
    {
        this.buttonList.add(button);
    }

    public FontRenderer getFontRenderer()
    {
        if (this.fontRenderer != null)
        {
            return this.fontRenderer;
        }
        else
        {
            return Minecraft.getMinecraft().fontRenderer;
        }
    }

    public void setZLevel(float level)
    {
        this.zLevel = level;
    }

    public void keyTyped(char par1, int par2)
    {
        // esc or E
        if (par2 == 1 || par2 == this.mc.gameSettings.keyBindInventory.keyCode)
        {
            closeGui();
        }
    }

    public void closeGui()
    {
        this.mc.thePlayer.closeScreen();
    }

    public int createTexture(BufferedImage image)
    {
        TextureObject textureObject = new DynamicTexture(image);
        TEXTURES.put(textureObject.getGlTextureId(), textureObject);
        return textureObject.getGlTextureId();
    }

    public void bindTexture(int textureId)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, TEXTURES.get(textureId).getGlTextureId());
    }

    public void deleteTexture(int textureId)
    {
        GL11.glDeleteTextures(TEXTURES.get(textureId).getGlTextureId());
        TEXTURES.remove(textureId);
    }

    public void bindTextureFromResource(String texturePath)
    {
        ResourceLocation location = new ResourceLocation(TEXTURE_DOMAIN, texturePath);
        this.mc.getTextureManager().bindTexture(location);
    }

    public void renderIcon(Icon icon, int x, int y)
    {
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        itemRenderer.zLevel = 200.0F;
        itemRenderer.renderIcon(x, y, icon, 16, 16);
        this.zLevel = 0.0F;
        itemRenderer.zLevel = 0.0F;
    }

    public void renderItemIcon(ItemStack itemStack, int x, int y)
    {
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        itemRenderer.zLevel = 200.0F;
        FontRenderer font = itemStack.getItem().getFontRenderer(itemStack);
        if (font == null)
        {
            font = this.getFontRenderer();
        }
        itemRenderer.renderItemAndEffectIntoGUI(font, this.mc.renderEngine, itemStack, x, y);
        this.zLevel = 0.0F;
        itemRenderer.zLevel = 0.0F;
    }

    public void renderRect(int x, int y, int width, int height, int color)
    {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        this.drawGradientRect(x, y, x + width, y + height, color, color);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public void drawToolTip(List<String> texts, int x, int y)
    {
        drawHoveringText(texts, x, y, this.fontRenderer);
    }

    public void playSound(String file, float volume, float pitch)
    {
        this.mc.sndManager.playSoundFX("random.click", volume, pitch);
    }

    @Override
    public void handleMouseInput()
    {
        super.handleMouseInput();

        // from GuiScreen#handleMouseInput
        int i = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int j = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        for (Slot slot : (List<Slot>)this.inventorySlots.inventorySlots)
        {
            if (isPointInRegion(slot.xDisplayPosition, slot.yDisplayPosition, INVENTORY_RECT_SIZE, INVENTORY_RECT_SIZE, i, j))
            {
                return;
            }
        }

        int wheel = Mouse.getDWheel();
        if (wheel != 0)
        {
            mouseWheelScrolled(i, j, wheel);
        }
        else if (Mouse.getEventButton() == -1)
        {
            mouseMovedOrUp(i, j, -1);
        }
    }

    protected void mouseWheelScrolled(int mouseX, int mouseY, int delta) { }
}
