/**
 * 
 */
package northgate.maths.Swing;

/**
 * @author Kyle
 *
 */
public class Vector3D{
	private double x, y, z;
	public Vector3D(float pos,float pos2,float pos3){
		this.x = pos;
		this.y = pos2;
		this.z = pos3;
	}
	

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}
	/**
	 * @return the z
	 */
	public double getZ() {
		return z;
	}
	/**
	 * @param z the z to set
	 */
	public void setZ(double z) {
		this.z = z;
	}
	
	public String toString(){
		return ("x = " + x + " : y = " + y + " : z = ");
		
	}
}
