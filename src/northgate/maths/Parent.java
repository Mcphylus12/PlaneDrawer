/**
 * 
 */
package northgate.maths;

import java.io.File;

import javax.swing.JOptionPane;

import northgate.maths.Swing.EquFrame;
import northgate.maths.opengl.GLWindow;

/**
 * @author Kyle
 *
 */
public class Parent {
	
	public static EquFrame equframe;
	public static GLWindow glwindow;
	
	
	public static void main(String[] args) throws Exception {
		if (LoadNatives()) {
			createSwingWindow();
			createGLWindow();
		} else {
			JOptionPane.showMessageDialog(null,"Unsupport OS : " + System.getProperty("os.name"), "Error", JOptionPane.ERROR_MESSAGE); // Should also AutoClose Javaw Process	
		}
	}
	
	public static void createGLWindow() throws InterruptedException{
		glwindow = new GLWindow();
	}
	
	public static void createSwingWindow(){
		equframe = new EquFrame();
	}
	
	public static boolean LoadNatives() {
		String OS = System.getProperty("os.name").toLowerCase();
		String os_res = "";
		
		if (isWindows(OS)) {
			os_res = "windows";
		} else if (isMac(OS)) {
			os_res = "macosx";
		} else if (isUnix(OS)) {
			os_res = "linux";
		} else if (isSolaris(OS)) {
			os_res = "solaris";
		}
		
		if (os_res != "") System.setProperty("org.lwjgl.librarypath", new File("res" + System.getProperty("file.separator") + os_res).getAbsolutePath());
		return os_res != "";
	}
 
	public static boolean isWindows(String OS) { return (OS.indexOf("win") >= 0); }
 
	public static boolean isMac(String OS) { return (OS.indexOf("mac") >= 0); }
 
	public static boolean isUnix(String OS) { return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 ); }
 
	public static boolean isSolaris(String OS) { return (OS.indexOf("sunos") >= 0);	}
	

}
