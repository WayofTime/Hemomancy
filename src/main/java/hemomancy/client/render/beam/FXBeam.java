package hemomancy.client.render.beam;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;

/**
 * This code is an adaptation of the Beam Rendering found within Thaumcraft. Apart from some minor changes, it is solely the work of Azanor.
 * This is mainly used so that I can become familiar with how to code a proper beam.
 */
public class FXBeam extends EntityFX
{
  public int particle = 16;
  EntityPlayer player = null;
  private double offset = 0.0D;
  
  public FXBeam(World par1World, EntityPlayer player, double tx, double ty, double tz, float red, float green, float blue, int age)
  {
    super(par1World, player.posX, player.posY, player.posZ, 0.0D, 0.0D, 0.0D);
    if (player.getEntityId() != Minecraft.getMinecraft().getRenderViewEntity().getEntityId()) {
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
    float xd = (float)(player.posX - this.tX);
    float yd = (float)(player.posY + this.offset - this.tY);
    float zd = (float)(player.posZ - this.tZ);
    this.length = MathHelper.sqrt_float(xd * xd + yd * yd + zd * zd);
    double var7 = MathHelper.sqrt_double(xd * xd + zd * zd);
    this.rotYaw = ((float)(Math.atan2(xd, zd) * 180.0D / 3.141592653589793D));
    this.rotPitch = ((float)(Math.atan2(yd, var7) * 180.0D / 3.141592653589793D));
    this.prevYaw = this.rotYaw;
    this.prevPitch = this.rotPitch;
    
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
  
  public void updateBeam(double x, double y, double z)
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
    
    this.prevYaw = this.rotYaw;
    this.prevPitch = this.rotPitch;
    
    float xd = (float)(this.player.posX - this.tX);
    float yd = (float)(this.player.posY + this.offset - this.tY);
    float zd = (float)(this.player.posZ - this.tZ);
    
    this.length = MathHelper.sqrt_float(xd * xd + yd * yd + zd * zd);
    
    double var7 = MathHelper.sqrt_double(xd * xd + zd * zd);
    
    this.rotYaw = ((float)(Math.atan2(xd, zd) * 180.0D / 3.141592653589793D));
    for (this.rotPitch = ((float)(Math.atan2(yd, var7) * 180.0D / 3.141592653589793D)); this.rotPitch - this.prevPitch < -180.0F; this.prevPitch -= 360.0F) {}
    while (this.rotPitch - this.prevPitch >= 180.0F) {
      this.prevPitch += 360.0F;
    }
    while (this.rotYaw - this.prevYaw < -180.0F) {
      this.prevYaw -= 360.0F;
    }
    while (this.rotYaw - this.prevYaw >= 180.0F) {
      this.prevYaw += 360.0F;
    }
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
  
  private float length = 0.0F;
  private float rotYaw = 0.0F;
  private float rotPitch = 0.0F;
  private float prevYaw = 0.0F;
  private float prevPitch = 0.0F;
  private Entity targetEntity = null;
  private double tX = 0.0D;
  private double tY = 0.0D;
  private double tZ = 0.0D;
  private double ptX = 0.0D;
  private double ptY = 0.0D;
  private double ptZ = 0.0D;
  private int type = 0;
  
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
    double prey = this.player.prevPosY + this.offset;
    double prez = this.player.prevPosZ;
    double px = this.player.posX;
    double py = this.player.posY + this.offset;
    double pz = this.player.posZ;
    
    prex -= MathHelper.cos(this.player.prevRotationYaw / 180.0F * 3.141593F) * 0.066F;
    prey -= 0.06D;
    prez -= MathHelper.sin(this.player.prevRotationYaw / 180.0F * 3.141593F) * 0.04F;
    Vec3 vec3d = this.player.getLook(1.0F);
    prex += vec3d.xCoord * 0.3D;
    prey += vec3d.yCoord * 0.3D;
    prez += vec3d.zCoord * 0.3D;
    
    px -= MathHelper.cos(this.player.rotationYaw / 180.0F * 3.141593F) * 0.066F;
    py -= 0.06D;
    pz -= MathHelper.sin(this.player.rotationYaw / 180.0F * 3.141593F) * 0.04F;
    vec3d = this.player.getLook(1.0F);
    px += vec3d.xCoord * 0.3D;
    py += vec3d.yCoord * 0.3D;
    pz += vec3d.zCoord * 0.3D;
    
    float xx = (float)(prex + (px - prex) * f - interpPosX);
    float yy = (float)(prey + (py - prey) * f - interpPosY);
    float zz = (float)(prez + (pz - prez) * f - interpPosZ);
    GL11.glTranslated(xx, yy, zz);
    
    float ry = (float)(this.prevYaw + (this.rotYaw - this.prevYaw) * f);
    float rp = (float)(this.prevPitch + (this.rotPitch - this.prevPitch) * f);
    GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
    GL11.glRotatef(180.0F + ry, 0.0F, 0.0F, -1.0F);
    GL11.glRotatef(rp, 1.0F, 0.0F, 0.0F);
    
    double var44 = -0.15D * size;
    double var17 = 0.15D * size;
    double var44b = -0.15D * size * this.endMod;
    double var17b = 0.15D * size * this.endMod;
    
    GL11.glRotatef(rot, 0.0F, 1.0F, 0.0F);
    for (int t = 0; t < 3; t++)
    {
      double var29 = this.length * size * var9;
      double var31 = 0.0D;
      double var33 = 1.0D;
      double var35 = -1.0F + var12 + t / 3.0F;
      double var37 = this.length * size * var9 + var35;
      
      GL11.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
      System.out.println("t: " + t);
      wr.startDrawingQuads();
      wr.setBrightness(200);
      wr.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, op);
      wr.addVertexWithUV(var44b, var29, 0.0D, var33, var37);
      wr.addVertexWithUV(var44, 0.0D, 0.0D, var33, var35);
      wr.addVertexWithUV(var17, 0.0D, 0.0D, var31, var35);
      wr.addVertexWithUV(var17b, var29, 0.0D, var31, var37);
//      wr.finishDrawing();
      tessellator.draw();
//      tessellator.func_78381_a();
    }
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glDepthMask(true);
    GL11.glDisable(3042);
    GL11.glEnable(2884);
    
    GL11.glPopMatrix();
    if (this.impact > 0) {
//      renderImpact(tessellator, f, f1, f2, f3, f4, f5);
    }
//    Minecraft.getMinecraft().renderEngine.bindTexture(UtilsFX.getParticleTexture());
//    wr.finishDrawing();
//    tessellator.draw();
    wr.startDrawingQuads();
//    tessellator.startDrawingQuads();
    this.prevSize = size;
  }
  
  public void renderImpact(WorldRenderer wr, float f, float f1, float f2, float f3, float f4, float f5)
  {
    GL11.glPushMatrix();
    GL11.glDepthMask(false);
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 1);
    
//    UtilsFX.bindTexture(ParticleEngine.particleTexture);
    
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.66F);
    int part = this.particleAge % 16;
    
    float var8 = part / 16.0F;
    float var9 = var8 + 0.0624375F;
    float var10 = 0.3125F;
    float var11 = var10 + 0.0624375F;
    float var12 = this.endMod / 2.0F / (6 - this.impact);
    
    float var13 = (float)(this.ptX + (this.tX - this.ptX) * f - interpPosX);
    float var14 = (float)(this.ptY + (this.tY - this.ptY) * f - interpPosY);
    float var15 = (float)(this.ptZ + (this.tZ - this.ptZ) * f - interpPosZ);
    float var16 = 1.0F;
    
    wr.startDrawingQuads();
    wr.setBrightness(200);
    wr.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, 0.66F);
    wr.addVertexWithUV(var13 - f1 * var12 - f4 * var12, var14 - f2 * var12, var15 - f3 * var12 - f5 * var12, var9, var11);
    wr.addVertexWithUV(var13 - f1 * var12 + f4 * var12, var14 + f2 * var12, var15 - f3 * var12 + f5 * var12, var9, var10);
    wr.addVertexWithUV(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12, var8, var10);
    wr.addVertexWithUV(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12, var8, var11);
    
    wr.finishDrawing();
    
    GL11.glDisable(3042);
    GL11.glDepthMask(true);
    GL11.glPopMatrix();
  }
}
