/**
 * 
 */
package northgate.maths.opengl;





import northgate.maths.Parent;
import northgate.maths.Swing.EquFrame;
import northgate.maths.Swing.Vector3D;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
/**
 * @author Kyle
 *
 */
//TODO: Update Controls, Viewing, Color, Cart/Vector Lines (GL_LINES)
public class GLWindow {
	
	// Historical, Maps used in Projection to Frustum Conversion
    private final float piover180 = 0.0174532925f;
	
    // to Display
	public Vector3D[][] drawvecs = new Vector3D[EquFrame.numPlane][4];
	public boolean[] needWriting = new boolean[EquFrame.numPlane];
	public float[][] color = new float[EquFrame.numPlane][3];
	public float[] alpha = new float[EquFrame.numPlane];
	
	
	public final float[] axis = {
			0, 0, -100,
			0, 0, 100,
			0, -100, 0,
			0, 100, 0,
			-100, 0, 0,
			100, 0, 0
			};
	public final float[] opposites  = {0, 6, 1, 7, 2, 4, 3, 5};
	private int excludedCorner;
	private int[] linkage = new int[] {7, 6, 4, 5, 3, 2, 0, 1};
	public final float[] renderFrame = {
			-100, 100, 100, //Back top left
			100, 100, 100, //Back top right
			100, -100, 100, //Back bottom right
			-100, -100, 100, //Back bottom left
			
			-100, 100, -100, //Front top left
			100, 100, -100, //Front top right
			100, -100, -100, //Front bottom right
			-100, -100, -100, //Front bottom left
			
	};
	

	
	public float camX, camY, camZ;
	public float xRot, yRot;
	
	public int MouseX, MouseY;
	public int oldMouseX, oldMouseY;
	
	//public boolean[] Pressed = new boolean[4];
	
	public GLWindow() throws InterruptedException {
	
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle("Renderer");
			Display.setResizable(false);
			Display.create();
		} catch (LWJGLException e) { // Fix for old Computers | Nvidia Processors
			if(e.getMessage() == "Pixel Format Not Accelerated"){
				System.setProperty("Dorg.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
				Display.setTitle(Display.getTitle() + " -missing latest Gcard drivers (Running in safe mode)");
				try {
					Display.create();
				} catch (LWJGLException e1) {
					System.out.print("Error Occured, OpenGL SoftwareMode failed, Could not Create Display");
					e1.printStackTrace();
				}
			}
		}
		
		initVar();
		initGL();
		
		while(!Display.isCloseRequested()){
			
			glPushMatrix();
			Update();
			GLRender();
			glPopMatrix();
			Display.update();
			upDatevecs();
			Thread.sleep(1000/60);
		}
		Display.destroy();
		System.exit(0);
	}
	
	/**
	 * 
	 */
	private void GLRender() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();

		
		
	
		
		glRotatef(xRot,1 , 0, 0);
		glRotatef(-yRot, 0, 1, 0);
		glTranslatef(-camX, -camY, -camZ);
		
		
		
		
	
		RenderFrame();
		
		// Axis
		glBegin(GL_LINES);
		
		 glColor3d(1, 0, 0);
		 glVertex3f(axis[0], axis[1], axis[2]);
		 glVertex3f(axis[3], axis[4], axis[5]);
			
		 glColor3d(0, 1, 0);
		 glVertex3f(axis[6], axis[7], axis[8]);
		 glVertex3f(axis[9], axis[10], axis[11]);
			
		 glColor3d(0, 0, 1);
		 glVertex3f(axis[12], axis[13], axis[14]);
		 glVertex3f(axis[15], axis[16], axis[17]);
			
			
		glEnd();
		
		// Draw Planes
		for(int i = 0 ; i < drawvecs.length; i++){
			if(needWriting[i]){
				glColor4d(color[i][0], color[i][1], color[i][2], alpha[i]);
				if (drawvecs[i][3] == null) {
					glBegin(GL_LINES);
					for(int j = 0 ; j < 2; j++){
						glVertex3d(drawvecs[i][j].getX(), drawvecs[i][j].getY(), drawvecs[i][j].getZ());
					}
					glEnd();
				}
			}
		}

		for(int i = 0 ; i < drawvecs.length; i++){
			if(needWriting[i]){
				glColor4d(color[i][0], color[i][1], color[i][2], alpha[i]);
				if (drawvecs[i][3] != null) {
					glBegin(GL_QUADS);
					for(int j = 0 ; j < drawvecs[i].length; j++){
						glVertex3d(drawvecs[i][j].getX(), drawvecs[i][j].getY(), drawvecs[i][j].getZ());
					}
					glEnd();
				}
			}
		}
	}

	private void RenderFrame() {
		int i = 0;
		glBegin(GL_LINES);
		glColor3d(1, 1, 1);
		boolean draw = true;

		for(i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				for (int l=0; l<8; l+=2){
					if((i == opposites[l] && j == opposites[l+1]) || i == getExcludedVertex() || j == getExcludedVertex()){
						draw = false;
						break;
						}else {
							draw = true;
							
						}
					}
					if(i<j && draw){
						
						drawPointFromArray(i);
						drawPointFromArray(j);
		
					}
				

			}
		}
		 
		 
		  i = 0;
		 glEnd();
	}
	
	private void drawPointFromArray(int i){ 
		i*=3;
		glVertex3f(renderFrame[i++], renderFrame[i++], renderFrame[i++]);
	}
	
		
		
	
	
	private float toRads(float degrees){
		return degrees * piover180;
	}
	
	private void Update() {
		// Mouse Handling
		MouseX = Mouse.getX();
		MouseY = Mouse.getY();
		
		float xChange = MouseX - oldMouseX;
		float yChange = MouseY - oldMouseY;
		
		oldMouseX = MouseX;
		oldMouseY = MouseY;
		
		// Left Click
		if(Mouse.isButtonDown(1)){

			

			
			
		}
		
		// Right Click
		if(Mouse.isButtonDown(0)){
			xRot += yChange/10;
			yRot += xChange/10;
			
			
			

			
			calculateExcludedVertex();
			
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			camZ -= 2 * Math.cos(toRads(yRot))* Math.cos(toRads(xRot));
			camY -= 2 * Math.sin(toRads(xRot));
			camX -= 2 * Math.sin(toRads(yRot));
			
			calculateExcludedVertex();
		}
			
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			camX -= 2 * Math.cos(toRads(yRot));
			camZ += 2 * Math.sin(toRads(yRot));
			
			calculateExcludedVertex();
		}
				
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			camZ += 2 * Math.cos(toRads(yRot))* Math.cos(toRads(xRot));
			camY += 2 * Math.sin(toRads(xRot));
			camX += 2 * Math.sin(toRads(yRot));
			
			calculateExcludedVertex();
		}
					
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			camX += 2 * Math.cos(toRads(yRot));
			camZ -= 2 * Math.sin(toRads(yRot));
			
			calculateExcludedVertex();
		}
		
		//TODO
		int DWheel = Mouse.getDWheel();
		camZ += -DWheel*0.5 * Math.sin(toRads(yRot))* Math.sin(toRads(xRot));
		camY += -DWheel*0.5 * Math.cos(toRads(xRot));
		camX += -Mouse.getDWheel()*0.5 * Math.sin(toRads(yRot));
		
	
		if (xRot >  360) xRot -= 360;
		if (yRot >  360) yRot -= 360;
		if (xRot < -360) xRot += 360;
		if (yRot < -360) yRot += 360;
		
	}
	
	@SuppressWarnings("unused")
	private float normalize(float x) {
		if (x > 315)  return    0;
		if (x > 225)  return  270;
		if (x > 135)  return  180;
		if (x > 45)	  return   90;
		if (x > -45)  return    0;
		if (x > -135) return  -90;
		if (x > -225) return -180;
		if (x >  315) return -270;
		return -360;
	}
	
	private void initGL() {

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glFrustum(-0.0552, 0.0552, -0.0414, 0.0414, 0.1f, 1000f);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glViewport(0, 0, 800, 600);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glLineWidth(10);
		glClearColor(0, 0.2f, 0, 1);
	}
	
	private void initVar() {
		
		
		oldMouseX = Mouse.getX();
		oldMouseY = Mouse.getY();
		camX = 220;
		camY = 120;
		camZ = 420;
		yRot = 30;
		xRot = 20;
		
		//Arrays.fill(Pressed, false);
		
		float[][] validColors = {new float[]{0, 0.7f, 1},
				                 new float[]{0.5f, 1, 0},
				                 new float[]{0.4f, 0, 1},
				                 new float[]{0.5f, 1, 0.5f},
				                 new float[]{1, 0.5f, 0.5f}
								};
		
		
		color = validColors;
		calculateExcludedVertex();
	}

	/**
	 * 
	 */


	public void upDatevecs(){
		this.drawvecs = Parent.equframe.exportVectors();
		this.needWriting = Parent.equframe.getNeedDraw();
		this.alpha = Parent.equframe.getAlpha();
	}
	
	private void calculateExcludedVertex(){
		excludedCorner  = Sign(camX) > 0 ? 1 : 0;
		excludedCorner += Sign(camY) > 0 ? 2 : 0;
		excludedCorner += Sign(camZ) > 0 ? 4 : 0;
		excludedCorner = linkage[excludedCorner];
	}
	
	private int getExcludedVertex(){
		return excludedCorner;
	}
	
	private int Sign(Float f){
		try{
			return (int) (f/Math.abs(f));
		}catch(Exception e){
			return 0;
		}
	}
	
	
	

}
