package hemomancy.client.render.beam;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
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
	    if (!FMLClientHandler.instance().getClient().gameSettings.fancyGraphics) 
	    {
	    	visibleDistance = 25;
	    }
	    if (renderentity.getDistance(player.posX, player.posY, player.posZ) > visibleDistance) 
	    {
	    	this.particleMaxAge = 0;
	    }
  	}
  
  	public void updateCircle(double x, double y, double z, EnumFacing sideHit)
	{
  		this.tX = x;
	    this.tY = y;
	    this.tZ = z;
	    this.sideHit = sideHit;
	    while (this.particleMaxAge - this.particleAge < 4) 
	    {
	    	this.particleMaxAge += 1;
	    }
	}
  
  	@Override
  	public void onUpdate()
  	{
	    this.prevPosX = this.player.posX;
	    this.prevPosY = (this.player.posY + this.offset);
	    this.prevPosZ = this.player.posZ;
	
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
	private int type = 0;
	private EnumFacing sideHit = EnumFacing.UP;
  
	public void setType(int type)
	{
		this.type = type;
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
  
	//Renders the floating circle onto the block and face.
	@Override
	public void func_180434_a(WorldRenderer wr, Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		Tessellator tessellator = Tessellator.getInstance();

		tessellator.draw();
//    	tessellator.func_78381_a();
//	  	wr.finishDrawing();
    
		GL11.glPushMatrix();
		float rot = (float)(this.worldObj.provider.getWorldTime() % (360 / this.rotationspeed) * this.rotationspeed) + this.rotationspeed * f;
    
		float size = 1.0F;
	    if (this.pulse)
	    {
	    	size = Math.min(this.particleAge / 4.0F, 1.0F);
	    	size = (float)(this.prevSize + (size - this.prevSize) * f);
	    }
	    float op = 0.4F;
	    if ((this.pulse) && (this.particleMaxAge - this.particleAge <= 4)) 
	    {
	    	op = 0.4F - (4 - (this.particleMaxAge - this.particleAge)) * 0.1F;
	    }
	    switch (this.type)
	    {
	    default: 
	    	Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("hemomancy", "textures/spells/beam/array.png")); 
	    	break;
	    }
	    GL11.glTexParameterf(3553, 10242, 10497.0F);
	    GL11.glTexParameterf(3553, 10243, 10497.0F);
	    
	    GL11.glDisable(2884);
	    
	    GL11.glEnable(3042);
	    GL11.glBlendFunc(770, 1);
	
//    	GL11.glDepthMask(false);
	    
	    double prex = this.player.prevPosX;
	    double prey = this.player.prevPosY;
	    double prez = this.player.prevPosZ;
	    double px = this.player.posX;
	    double py = this.player.posY;
	    double pz = this.player.posZ;
	    
	    float xx = -(float)(prex + (px - prex) * f - tX);
	    float yy = (float)(tY - (prey + (py - prey) * f));
	    float zz = -(float)((prez + (pz - prez) * f) - tZ);
	    GL11.glTranslated(xx, yy, zz);    
	    
	    System.out.println(xx + ", " + yy + ", " + zz);
	        
	    GL11.glTranslatef(sideHit.getFrontOffsetX() * 0.01f, sideHit.getFrontOffsetY() * 0.01f, sideHit.getFrontOffsetZ() * 0.01f);
	    
	    switch(sideHit)
	    {
		case DOWN:
			GL11.glTranslatef(0, 0, 1);
			GL11.glRotatef(-90.0f, 1, 0, 0);
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
			GL11.glTranslatef(0, 0, 1);
			GL11.glRotatef(90.0f, 0, 1, 0);
			break;
	    }
	
	    
	    
    	GL11.glPushMatrix();
    	GL11.glTranslatef(0.5f, 0.5f, 0);
    	GL11.glRotatef(rot, 0, 0, 1);
	    double var31 = 0.0D;
	    double var33 = 1.0D;
	    double var35 = 0;
	    double var37 = 1;
	      
	//    GL11.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
	    wr.startDrawingQuads();
	    wr.setBrightness(200);
	    wr.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, op);
	    wr.addVertexWithUV(0.5, 0.5, 0.0D, var33, var37);
	    wr.addVertexWithUV(0.5, -0.5, 0.0D, var33, var35);
	    wr.addVertexWithUV(-0.5, -0.5, 0.0D, var31, var35);
	    wr.addVertexWithUV(-0.5, 0.5, 0.0D, var31, var37);
	    tessellator.draw();
	      
	    GL11.glPopMatrix();
	    
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    GL11.glDepthMask(true);
	    GL11.glDisable(3042);
	    GL11.glEnable(2884);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    
	    
	    GL11.glPopMatrix();
	
	    wr.startDrawingQuads();
	    this.prevSize = size;
    }
}