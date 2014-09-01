package com.mes51.minecraft.mods.necktie.gui.window;

import com.mes51.minecraft.mods.necktie.container.ContainerBase;
import com.mes51.minecraft.mods.necktie.gui.GuiBase;
import com.mes51.minecraft.mods.necktie.gui.control.ControlBase;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityBase;
import com.mes51.minecraft.mods.necktie.util.Size;
import com.mes51.minecraft.mods.necktie.util.SpriteTexture;
import com.mes51.minecraft.mods.necktie.util.enumerable.Enumerable;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.Predicate;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Package: com.mes51.minecraft.mods.necktie.gui.window
 * Date: 2014/05/02
 * Time: 2:42
 */
public abstract class WindowBase<T extends TileEntityBase>
{
    public static enum InventoryType
    {
        NORMAL(0, 0),
        NECKTIE(0, 76);

        private SpriteTexture texture = null;

        private InventoryType(int x, int y)
        {
            this.texture = new SpriteTexture("textures/gui/itemSlot.png", x, y, 18, 18);
        }

        public void render(GuiBase guiBase, int x, int y)
        {
            this.texture.render(guiBase, x, y);
        }
    }

    private static final String INVENTORY_TEXTURE_NAME = "textures/gui/itemSlot.png";
    private static final Size PLAYER_INVENTORY_SIZE = new Size(162, 76);

    protected int x = 0;
    protected int y = 0;
    protected int width = 0;
    protected int height = 0;
    protected boolean noBackgroundPanel = false;
    protected List<ControlBase> controls = new ArrayList<ControlBase>();
    protected ControlBase focusedControl = null;
    protected GuiBase guiBase = null;

    private BufferedImage windowTexture = null;
    private int textureId = -1;

    public static BufferedImage createWindowTexture(int width, int height)
    {
        int size = (int)Math.pow(2, Math.ceil(Math.log(Math.max(width, height)) / Math.log(2)));
        BufferedImage texture = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        float aspect = width / (float)height;

        Graphics2D g = texture.createGraphics();
        Shape inner = new RoundRectangle2D.Double(0.0, 0.0, width, height, 12.0, 12.0);
        Shape outer = new RoundRectangle2D.Double(1.5, 1.5, width - 3.0, height - 3.0, 3.0, 12.0);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(3.0F));
        g.setColor(new Color(0xC6C6C6));
        g.fill(inner);
        g.setPaint(
                new GradientPaint(
                        width * 0.5F - 2.0F,
                        height * 0.5F - aspect * 2.0F,
                        new Color(0xEEEEEE),
                        width * 0.5F + 2.0F,
                        height * 0.5F + aspect * 2.0F,
                        new Color(0x333333)
                )
        );
        g.draw(outer);
        g.dispose();

        return texture;
    }

    public WindowBase(GuiBase guiBase, int width, int height)
    {
        this.guiBase = guiBase;
        this.width = width;
        this.height = height;
        calcWindowPos();
    }

    public void render(int mouseX, int mouseY)
    {
        mouseX -= this.x;
        mouseY -= this.y;
        calcWindowPos();

        if (!this.noBackgroundPanel)
        {
            if (this.windowTexture == null && this.width > 0 && this.height > 0)
            {
                this.windowTexture = createWindowTexture(this.width, this.height);
                this.textureId = createTextureFromImage(this.windowTexture);
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.guiBase.bindTextureFromResource("textures/gui/blank.png");
            bindTexture(this.textureId);
            drawTexture(0, 0, this.width, this.height);
        }

        renderBackground(mouseX, mouseY);

        for (ControlBase control : this.controls)
        {
            if (control.isVisible())
            {
                control.render(mouseX - control.getX(), mouseY - control.getY());
            }
        }

        renderForeground(mouseX, mouseY);
    }

    public void renderAfterInventory(int mouseX, int mouseY)
    {
        mouseX -= this.x;
        mouseY -= this.y;

        for (ControlBase control : this.controls)
        {
            if (control.isVisible())
            {
                control.renderAfterInventory(mouseX - control.getX(), mouseY - control.getY());
            }
        }
    }

    protected void renderBackground(int mouseX, int mouseY) { }

    protected abstract void renderForeground(int mouseX, int mouseY);

    public void addButton(GuiButton button)
    {
        this.guiBase.addButton(button);
    }

    public void actionPerformed(GuiButton button) { }

    public boolean canCloaseWindowPressE()
    {
        return false;
    }

    public void activate()
    {
        calcWindowPos();
        /*for (ControlBase control : this.controls)
        {
            if (control instanceof ControlSystemButton)
            {
                this.addButton(((ControlSystemButton)control).createButton(this.x, this.y));
            }
        }*/
    }

    public void close()
    {
        this.deleteTexture();
    }

    public void mouseClicked(int mouseX, int mouseY)
    {
        ControlBase prevFocusedControl = this.focusedControl;
        this.focusedControl = null;
        int mx = mouseX - this.x;
        int my = mouseY - this.y;
        for (ControlBase control : findControlByPoint(mx, my))
        {
            if (control.isFocusable() && control.isEnabled())
            {
                this.focusedControl = control;
            }
            control.mouseClicked(mx - control.getX(), my - control.getY());
        }
        if (prevFocusedControl != this.focusedControl)
        {
            if (prevFocusedControl != null)
            {
                prevFocusedControl.deFocus();
            }
            if (this.focusedControl != null)
            {
                this.focusedControl.focus();
            }
        }
    }

    public void mouseUp(int mouseX, int mouseY)
    {
        int mx = mouseX - this.x;
        int my = mouseY - this.y;
        for (ControlBase control : findControlByPoint(mx, my))
        {
            control.mouseUp(mx - control.getX(), my - control.getY());
        }
    }

    public void mouseMoved(int mouseX, int mouseY)
    {
        int mx = mouseX - this.x;
        int my = mouseY - this.y;
        for (ControlBase control : findControlByPoint(mx, my))
        {
            control.mouseMoved(mx - control.getX(), my - control.getY());
        }
    }

    public void mouseWheelScrolled(int mouseX, int mouseY, int delta)
    {
        int mx = mouseX - this.x;
        int my = mouseY - this.y;
        for (ControlBase control : findControlByPoint(mx, my))
        {
            control.mouseWheelScrolled(mx - control.getX(), my - control.getY(), delta);
        }
    }

    public void keyPressed(int keyChar, int keyCode)
    {
        if (this.focusedControl != null)
        {
            this.focusedControl.keyPressed(keyChar, keyCode);
        }
    }

    public void addControl(ControlBase control)
    {
        this.controls.add(control);
    }

    public void clearControls()
    {
        this.controls.clear();
    }

    public void setZLevel(float level)
    {
        this.guiBase.setZLevel(level);
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int createTextureFromImage(BufferedImage image)
    {
        return this.guiBase.createTexture(image);
    }

    public void deleteTexture(int textureId)
    {
        this.guiBase.deleteTexture(textureId);
    }

    public void bindTexture(int textureId)
    {
        this.guiBase.bindTexture(textureId);
    }

    public void bindTextureFromResource(String texturePath)
    {
        this.guiBase.bindTextureFromResource(texturePath);
    }

    public int getStringWidth(String text)
    {
        return this.guiBase.getFontRenderer().getStringWidth(text);
    }

    public int getStringHeight()
    {
        return this.guiBase.getFontRenderer().FONT_HEIGHT;
    }

    public String trimStringToWidth(String text, int width)
    {
        return this.guiBase.getFontRenderer().trimStringToWidth(text, width);
    }

    public void drawString(String text, int x, int y, int color)
    {
        drawString(text, x, y, 1, 1, color);
    }

    public void drawString(String text, int x, int y, int scaleX, int scaleY, int color)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        this.guiBase.getFontRenderer().drawString(text, (x + this.x) * scaleX, (y + this.y) * scaleY, color);
    }

    public void drawStringCenter(String text, int y, int color)
    {
        drawString(text, (this.width - getStringWidth(text)) / 2, y, 1, 1, color);
    }

    public void renderItemIcon(ItemStack itemStack, int x, int y)
    {
        this.guiBase.renderItemIcon(itemStack, x + this.x, y + this.y);
    }

    public void renderItemIcon(ItemStack itemStack, int x, int y, int scaleX, int scaleY)
    {
        this.guiBase.renderItemIcon(itemStack, (x + this.x) * scaleX, (y + this.y) * scaleY);
    }

    public void renderRect(int x, int y, int width, int height, int color)
    {
        this.guiBase.renderRect(x + this.x, y + this.y, width, height, color);
    }

    public void drawTexture(int x, int y, int width, int height)
    {
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.guiBase.drawTexturedModalRect(x + this.x, y + this.y, 0, 0, width, height);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void drawPlayerInventorySlot(int x, int y)
    {
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        bindTextureFromResource(INVENTORY_TEXTURE_NAME);
        drawTexture(x, y, PLAYER_INVENTORY_SIZE.width, PLAYER_INVENTORY_SIZE.height);
    }

    public void drawInventorySlot(int x, int y)
    {
        drawInventorySlot(x, y, InventoryType.NORMAL);
    }

    public void drawInventorySlot(int x, int y, InventoryType type)
    {
        type.render(this.guiBase, x + this.x, y + this.y);
    }

    public void drawToolTip(String text, int x, int y)
    {
        List<String> list = new ArrayList<String>();
        list.add(text);
        drawToolTip(list, x, y);
    }

    public void drawToolTip(List<String> texts, int x, int y)
    {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        this.guiBase.drawToolTip(texts, x + this.x, y + this.y);
        GL11.glPopAttrib();
    }

    public boolean inRect(int x, int y, int width, int height, int mouseX, int mouseY)
    {
        int left = x;
        int top = y;
        int right = x + width;
        int bottom = y + height;
        return left < mouseX &&
                top < mouseY &&
                right > mouseX &&
                bottom > mouseY;
    }

    public void playSound(String file, float volume, float pitch)
    {
        this.guiBase.playSound(file, volume, pitch);
    }

    protected T getTileEntity()
    {
        return getContainer().getEntity();
    }

    protected ContainerBase<T> getContainer()
    {
        return (ContainerBase<T>)this.guiBase.inventorySlots;
    }

    protected void calcWindowPos()
    {
        this.x = (this.guiBase.width - this.width) / 2;
        this.y = (this.guiBase.height - this.height) / 2;
    }

    private void deleteTexture()
    {
        if (this.textureId > -1)
        {
            deleteTexture(this.textureId);
            this.textureId = -1;
        }
    }

    private Enumerable<ControlBase> findControlByPoint(final int x, final int y)
    {
        return Enumerable.from(this.controls).where(
                new Predicate<ControlBase>()
                {
                    @Override
                    public boolean predicate(ControlBase value)
                    {
                        return value.isVisible() && value.inRect(x, y);
                    }
                }
        );
    }

    @Override
    protected void finalize() throws Throwable
    {
        try
        {
            super.finalize();
        }
        finally
        {
            deleteTexture();
        }
    }
}
