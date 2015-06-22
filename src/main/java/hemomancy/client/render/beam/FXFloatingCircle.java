package hemomancy.client.render.beam;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;

public class FXFloatingCircle extends EntityFX
{
	public int particle = 16;
	EntityPlayer player = null;
	private double offset = 0.0D;
  
  	public FXFloatingCircle(World par1World, EntityPlayer player, double tx, double ty, double tz, EnumFacing sideHit, float red, float green, float blue, int age)
  	{
  		super(par1World, player.posX, player.posY, player.posZ, 0.0D, 0.0D, 0.0D);
//    	if (player.getEntityId() != Minecraft.getMinecraft().getRenderViewEntity().getEntityId()) 
  		{
		  	this.offset = (player.height / 2.0F + 0.25D);
	  	}
	  	this.particleRed = red;
	    this.particleGreen = green;
	    this.particleBlue = blue;
	    this.player = player;
	    setSize(0.02F, 0.02F);
	    this.noClip = true;
	    this.motionX = 0.0D;
	    this.motionY = 0.0D;
	    this.motionZ = 0.0D;
	    this.tX = tx;
	    this.tY = ty;
	    this.tZ = tz;
	    this.sideHit = sideHit;
	
	    this.particleMaxAge = age;
	    
	    Entity renderentity = FMLClientHandler.instance().getClient().getRenderViewEntity();
	    int visibleDistance = 50;
	    if (!FMLClientHandler.instance().getClient().gameSettings.fancyGraphics) {
	    	visibleDistance = 25;
	    }
	    if (renderentity.getDistance(player.posX, player.posY, player.posZ) > visibleDistance) {
	    	this.particleMaxAge = 0;
	    }
  	}
  
  public void updateCircle(double x, double y, double z, EnumFacing sideHit)
  {
    this.tX = x;
    this.tY = y;
    this.tZ = z;
    while (this.particleMaxAge - this.particleAge < 4) {
      this.particleMaxAge += 1;
    }
  }
  
  @Override
  public void onUpdate()
  {
    this.prevPosX = this.player.posX;
    this.prevPosY = (this.player.posY + this.offset);
    this.prevPosZ = this.player.posZ;
    this.ptX = this.tX;
    this.ptY = this.tY;
    this.ptZ = this.tZ;  

    if (this.impact > 0) {
      this.impact -= 1;
    }
    if (this.particleAge++ >= this.particleMaxAge) {
      setDead();
    }
  }
  
  public void setRGB(float r, float g, float b)
  {
    this.particleRed = r;
    this.particleGreen = g;
    this.particleBlue = b;
  }
  
  private double tX = 0.0D;
  private double tY = 0.0D;
  private double tZ = 0.0D;
  private double ptX = 0.0D;
  private double ptY = 0.0D;
  private double ptZ = 0.0D;
  private int type = 0;
  private EnumFacing sideHit = EnumFacing.UP;
  
  public void setType(int type)
  {
    this.type = type;
  }
  
  private float endMod = 1.0F;
  
  public void setEndMod(float endMod)
  {
    this.endMod = endMod;
  }
  
  private boolean reverse = false;
  
  public void setReverse(boolean reverse)
  {
    this.reverse = reverse;
  }
  
  private boolean pulse = true;
  
  public void setPulse(boolean pulse)
  {
    this.pulse = pulse;
  }
  
  private int rotationspeed = 5;
  
  public void setRotationspeed(int rotationspeed)
  {
    this.rotationspeed = rotationspeed;
  }
  
  private float prevSize = 0.0F;
  public int impact;
  
  //Renders the particle
  @Override
  public void func_180434_a(WorldRenderer wr, Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
	  Tessellator tessellator = Tessellator.getInstance();

	  tessellator.draw();
//    tessellator.func_78381_a();
//	  wr.finishDrawing();
    
    GL11.glPushMatrix();
    float var9 = 1.0F;
    float slide = Minecraft.getMinecraft().thePlayer.ticksExisted;
    float rot = (float)(this.worldObj.provider.getWorldTime() % (360 / this.rotationspeed) * this.rotationspeed) + this.rotationspeed * f;
    
    float size = 1.0F;
    if (this.pulse)
    {
      size = Math.min(this.particleAge / 4.0F, 1.0F);
      size = (float)(this.prevSize + (size - this.prevSize) * f);
    }
    float op = 0.4F;
    if ((this.pulse) && (this.particleMaxAge - this.particleAge <= 4)) {
      op = 0.4F - (4 - (this.particleMaxAge - this.particleAge)) * 0.1F;
    }
    switch (this.type)
    {
    default: 
      Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("hemomancy", "textures/misc/beam.png")); 
      break;
    case 1: 
    	Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("hemomancy", "textures/misc/beam1.png")); 
      break;
    case 2: 
    	Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("hemomancy", "textures/misc/beam2.png")); 
      break;
    case 3: 
    	Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("hemomancy", "textures/misc/beam3.png"));
      break;
    }
    GL11.glTexParameterf(3553, 10242, 10497.0F);
    GL11.glTexParameterf(3553, 10243, 10497.0F);
    
    GL11.glDisable(2884);
    
    float var11 = slide + f;
    if (this.reverse) {
      var11 *= -1.0F;
    }
    float var12 = -var11 * 0.2F - MathHelper.floor_float(-var11 * 0.1F);
    
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 1);

//    GL11.glDepthMask(false);
    
    double prex = this.player.prevPosX;
    double prey = this.player.prevPosY;
    double prez = this.player.prevPosZ;
    double px = this.player.posX;
    double py = this.player.posY;
    double pz = this.player.posZ;
    
//    prex -= MathHelper.cos(this.player.prevRotationYaw / 180.0F * 3.141593F) * 0.066F;
//    prey -= 0.06D;
//    prez -= MathHelper.sin(this.player.prevRotationYaw / 180.0F * 3.141593F) * 0.04F;
//    Vec3 vec3d = this.player.getLook(1.0F);
//    prex += vec3d.xCoord * 0.3D;
//    prey += vec3d.yCoord * 0.3D;
//    prez += vec3d.zCoord * 0.3D;
//    
//    px -= MathHelper.cos(this.player.rotationYaw / 180.0F * 3.141593F) * 0.066F;
//    py -= 0.06D;
//    pz -= MathHelper.sin(this.player.rotationYaw / 180.0F * 3.141593F) * 0.04F;
//    vec3d = this.player.getLook(1.0F);
//    px += vec3d.xCoord * 0.3D;
//    py += vec3d.yCoord * 0.3D;
//    pz += vec3d.zCoord * 0.3D;
    
    float xx = -(float)(prex + (px - prex) * f - tX);
    float yy = (float)(tY - (prey + (py - prey) * f));
    float zz = -(float)((prez + (pz - prez) * f) - tZ);
    GL11.glTranslated(xx, yy, zz);
    
    System.out.println(xx + ", " + yy + ", " + zz);
        
    GL11.glTranslatef(sideHit.getFrontOffsetX() * 0.01f, sideHit.getFrontOffsetY() * 0.01f, sideHit.getFrontOffsetZ() * 0.01f);
    
    switch(sideHit)
    {
	case DOWN:
		GL11.glRotatef(90.0f, 1, 0, 0);
		break;
	case EAST:
		GL11.glRotatef(-90.0f, 0, 1, 0);
		GL11.glTranslatef(0, 0, -1);
		break;
	case NORTH:
		break;
	case SOUTH:
		GL11.glRotatef(180.0f, 0, 1, 0);
		GL11.glTranslatef(-1, 0, -1);
		break;
	case UP:
		GL11.glTranslatef(0, 1, 0);
		GL11.glRotatef(90.0f, 1, 0, 0);
		break;
	case WEST:
		GL11.glRotatef(-90.0f, 0, 1, 0);
		break;
    }
    
//    GL11.glRotatef(90.0F, sideHit.getFrontOffsetY(), 0.0F, 0.0F);
    //TODO
    
//    GL11.glRotatef(90.0F, 0.0F, 0.0F, 0.0F);
//    GL11.glRotatef(rp, 1.0F, 0.0F, 0.0F);
    
    double var44 = -0.15D * size;
    double var17 = 0.15D * size;
    double var44b = -0.15D * size * this.endMod;
    double var17b = 0.15D * size * this.endMod;
    
//    GL11.glRotatef(rot, 0.0F, 1.0F, 0.0F);
    for (int t = 0; t < 1; t++)
    {
      double var29 = size * var9;
      double var31 = 0.0D;
      double var33 = 1.0D;
      double var35 = -1.0F + var12 + t / 3.0F;
      double var37 = size * var9 + var35;
      
//      GL11.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
      wr.startDrawingQuads();
      wr.setBrightness(200);
      wr.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, op);
      wr.addVertexWithUV(1, 1, 0.0D, var33, var37);
      wr.addVertexWithUV(1, 0.0D, 0.0D, var33, var35);
      wr.addVertexWithUV(0, 0.0D, 0.0D, var31, var35);
      wr.addVertexWithUV(0, 1, 0.0D, var31, var37);
      tessellator.draw();
    }
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glDepthMask(true);
    GL11.glDisable(3042);
    GL11.glEnable(2884);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

    
    GL11.glPopMatrix();
    if (this.impact > 0) 
    {
//      renderImpact(tessellator, f, f1, f2, f3, f4, f5);
    }

    wr.startDrawingQuads();
    this.prevSize = size;
  }
}
