package hemomancy.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderEntitySpellProjectile extends Render
{
	float sizeModifier = 3;
    public RenderEntitySpellProjectile(RenderManager renderManager) 
    {
		super(renderManager);
	}

	public void doRenderProjectile(Entity entityShot, double x, double y, double z, float par8, float par9)
    {
//		System.out.println("(" + x + ", " + y + ", " + z + ")");
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(0.1F, 0.1F, 0.1F);
        this.bindTexture(this.getEntityTexture(entityShot));
        Tessellator tes = Tessellator.getInstance();
        GL11.glRotatef(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        WorldRenderer wr = tes.getWorldRenderer();
        wr.startDrawingQuads();
        wr.setNormal(0.0F, 1.0F, 0.0F);
        wr.addVertexWithUV(-0.5F * sizeModifier, -0.5F * sizeModifier, 0.0D, 0, 1);
        wr.addVertexWithUV(0.5F * sizeModifier, -0.5F * sizeModifier, 0.0D, 1, 1);
        wr.addVertexWithUV(0.5F * sizeModifier, 0.5F * sizeModifier, 0.0D, 1, 0);
        wr.addVertexWithUV(-0.5F * sizeModifier, 0.5F * sizeModifier, 0.0D, 0, 0);
        tes.draw();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float par8, float par9)
    {
        this.doRenderProjectile(entity, x, y, z, par8, par9);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return new ResourceLocation("hemomancy", "textures/entities/defaultSpellProjectile.png");
    }
}
