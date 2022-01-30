package noppes.npcs.client.gui.custom.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;
import noppes.npcs.scripted.gui.ScriptGuiLine;
import noppes.npcs.scripted.interfaces.ICustomGuiComponent;
import org.lwjgl.opengl.GL11;

public class CustomGuiLine extends Gui implements IGuiComponent {
    int id;
    GuiCustom parent;
    int x1;
    int y1;
    int x2;
    int y2;
    int color;
    int thickness;

    public CustomGuiLine(int id, int x1, int y1, int x2, int y2, int color, int thickness){
        this.id = id;
        this.x1 = GuiCustom.guiLeft + x1;
        this.y1 = GuiCustom.guiTop + y1;
        this.x2 = GuiCustom.guiLeft + x2;
        this.y2 = GuiCustom.guiTop + y2;
        this.color = color;
        this.thickness = thickness;
    }

    public void setParent(GuiCustom parent) {
        this.parent = parent;
    }

    @Override
    public int getID() {
        return this.id;
    }

    public void onRender(Minecraft mc, int mouseX, int mouseY, int mouseWheel, float partialTicks) {
        double distance = Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));

        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8  & 255) / 255f;
        float blue = (color & 255) / 255f;

        mc.getTextureManager().bindTexture(new ResourceLocation("customnpcs:textures/gui/misc.png"));
        GL11.glPushMatrix();
            GL11.glColor4f(red,green,blue,1.0F);
            GL11.glTranslatef(this.x1,this.y1,0.0F);
            GL11.glRotated(-Math.toDegrees(Math.atan2(x2-x1,y2-y1)),0.0F,0.0F,1.0F);
            GL11.glScaled(thickness, distance,0.0D);
            this.drawTexturedModalRect(0,0,0,0, 1, 1);
        GL11.glPopMatrix();
    }

    public ICustomGuiComponent toComponent() {
        return new ScriptGuiLine(this.id, this.x1,this.y1,this.x2,this.y2,this.color,this.thickness);
    }

    public static CustomGuiLine fromComponent(ScriptGuiLine component) {
        return new CustomGuiLine(component.getID(), component.getX1(), component.getY1(), component.getX2(), component.getY2(), component.getColor(), component.getThickness());
    }
}
