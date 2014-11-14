/**
 * 
 */
package northgate.maths.Swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * @author Kyle
 *
 */
@SuppressWarnings("serial")
public class EquFrame extends JFrame implements ActionListener{
	
	//Range Constants
	private static final int maxX = 80;
	private static final int maxY = 80;
	private static final int maxZ = 80;
	
	//Cartesian Constants
	private static final int X = 0;
	private static final int Y = 1;
	private static final int Z = 2;
	private static final int D = 3;
	
	//Vector Constants
	private static final int POSX = 0;
	private static final int POSY = 1;
	private static final int POSZ = 2;
	
	private static final int DIR1X = 3;
	private static final int DIR1Y = 4;
	private static final int DIR1Z = 5;
	
	private static final int DIR2X = 6;
	private static final int DIR2Y = 7;
	private static final int DIR2Z = 8;
	
	
	public static final int numPlane = 5;
	private Vector3D[][] vecs = new Vector3D[numPlane][4];
	boolean[] needDraw = new boolean[numPlane];
	float[] alpha = new float[numPlane];
	
	private Equation[] equations = new Equation[numPlane];
	
	public JButton btn = new JButton("Clear"); // Now Auto Draws
	
	public EquFrame(){
		super("Editing Panel");
		setBounds(0, 0, 250, 700);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		addEquations();
		
		//btn.setBounds(getWidth()/2 - 35, getHeight() - 53, 70, 20);
		btn.setBounds(5, getHeight() - 53, 230, 20);
		btn.addActionListener(this);
		add(btn);
		
		setVisible(true);
		
		Arrays.fill(needDraw, true);
		
		// Declare All Initial Vectors as (0,0,0)
		for(int i1 = 0 ; i1 < numPlane; i1++){
			for(int i2 = 0 ; i2 < 4; i2++){
				vecs[i1][i2] = new Vector3D(0, 0, 0);
			}
		}
		
	}
	
	public void addEquations(){
		for(int i = 0; i < numPlane; i++){
			equations[i] = new Equation(5, 5 + 130*i, i);
			add(equations[i]);
		}
	}
	
	public Vector3D[][] exportVectors() {
		for(int i = 0; i < numPlane; i++) {
			Equation e = equations[i];
			String s = "";
			if (Equation.needUpdate[i]) {
				// s = s + ":" + i;
				needDraw[i] = updateVecs(e, i);
				e.update(false);
				alpha[i] = e.alpha;
			}
			if (i == 4 && s != "") {
				// System.out.println(s);
			}
		}
		
		return vecs;
	}
	
	public boolean updateVecs(Equation e, Integer i) {
		if (e.TypeSel.getSelectedIndex() == Equation.posCartLine) {
			float[] out = getInput(e.cartLInputs);
			vecs[i] = createCartLine(out);
			return true;
		}
		
		if (e.TypeSel.getSelectedIndex() == Equation.posVecLine) {
			float[] out = getInput(e.vecLInputs);
			vecs[i] = createVectorLine(out);
			return true;
		}
		
		if (e.TypeSel.getSelectedIndex() == Equation.posCartPlane) {
			float[] out = getInput(e.cartPInputs);
			vecs[i] = createCartPlane(out);
			return true;
		}
		
		if (e.TypeSel.getSelectedIndex() == Equation.posVecPlane) {
			float[] out = getInput(e.vecPInputs);
			vecs[i] = createVectorPlane(out);
			return true;
		}
		
		if (e.TypeSel.getSelectedIndex() == Equation.posNone) {
			Vector3D[] vector = new Vector3D[4];
			for (int n = 0; n < 4; n++) { vector[n] = new Vector3D(0,0,0); }
			vecs[i] = vector;
			return false;
		}
		
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if (a.getSource() == btn) {
			for (Equation e : equations) { e.Clear(); e.toPos(Equation.posNone);}
		}
	}

	public boolean[] getNeedDraw() {
		return needDraw;
	}
	
	public float[] getAlpha() {
		return alpha;
	}
	
	//Utility Functions
	
	private float[] getInput(JTextField[] f) {
		float[] out = new float[f.length];
		Arrays.fill(out, 0);
		
		int i = 0;
		for (JTextField t : f) {
			String v = t.getText();
			if (v != null) {
				try {
				    out[i] = Float.valueOf(v);
				} catch (Exception e){}
			}
			i++;
		}
		
		return out;
	}
	
	//TODO : Complete
	private Vector3D[] createCartLine(float[] o) {
		Vector3D[] vector = new Vector3D[4];
		float[] veci = new float[6];
		
		for (int i = 0; i < 4; i++) { vector[i] = new Vector3D(0,0,0); } 
		
		veci[0] = o[1]/o[0];
		veci[1] = o[4]/o[3];
		veci[2] = o[7]/o[6];
		
		veci[3] = o[2]/o[0];
		veci[4] = o[5]/o[3];
		veci[5] = o[8]/o[6];
		
		vector = createVectorLine(veci);

		return vector;
	}
	
	private Vector3D[] createVectorLine(float[] o) {
		Vector3D[] vector = new Vector3D[4];
		
		vector[0] = new Vector3D(0,0,0);
		vector[1] = new Vector3D(0,0,0);
		vector[2] = null;
		vector[3] = null;
		
		if (o[03] != 0) {
			vector[0].setX(maxX * o[3]);
			vector[0].setY((vector[0].getX() - o[0])*(o[4]/o[3]) + o[1]);
			vector[0].setZ((vector[0].getX() - o[0])*(o[5]/o[3]) + o[2]);
			
			vector[1].setX(-maxX * o[3]);
			vector[1].setY((vector[1].getX() - o[0])*(o[4]/o[3]) + o[1]);
			vector[1].setZ((vector[1].getX() - o[0])*(o[5]/o[3]) + o[2]);
		} else {
			if (o[4] != 0) {
				vector[0].setX(o[0]);
				vector[0].setY(maxY * o[4]);
				vector[0].setZ((vector[0].getY() - o[1])*(o[5]/o[4]) + o[2]);
				
				vector[1].setX(o[0]);
				vector[1].setY(-maxY * o[4]);
				vector[1].setZ((vector[1].getY() - o[1])*(o[5]/o[4]) + o[2]);
			} else {
				if (o[05] != 0) {
					vector[0].setX(o[0]);
					vector[0].setY(o[1]);
					vector[0].setZ(maxZ);

					vector[1].setX(o[0]);
					vector[1].setY(o[1]);
					vector[1].setZ(-maxZ);
					
				}
			}
		}
		
		return vector;
	}
	
	//TODO : 0 Handling ?
	private Vector3D[] createCartPlane(float[] o) {
		Vector3D[] vector = new Vector3D[4];
		
		for (int i = 0; i < 4; i++) { vector[i] = new Vector3D(0,0,0); } 
		if(o[Y] != 0){
		vector[0].setX(-maxX);
		vector[0].setY((o[D] - (o[X] * -maxX) - (o[Z] * maxZ)) * (1/o[Y]));
		vector[0].setZ(maxZ);
		
		vector[1].setX(-maxX);
		vector[1].setY((o[D] - (o[X] * -maxX) - (o[Z] * -maxZ)) * (1/o[Y]));
		vector[1].setZ(-maxZ);
		
		vector[2].setX(maxX);
		vector[2].setY((o[D] - (o[X] * maxX) - (o[Z] * -maxZ)) * (1/o[Y]));
		vector[2].setZ(-maxZ);
		
		vector[3].setX(maxX);
		vector[3].setY((o[D] - (o[X] * maxX) - (o[Z] * maxZ)) * (1/o[Y]));
		vector[3].setZ(maxZ);
		} else if(o[X] != 0){
			vector[0].setX((o[D] - (o[Y] * -maxY) - (o[Z] * maxZ)) * (1/o[X]));
			vector[0].setY(-maxY);
			vector[0].setZ(maxZ);
			
			vector[1].setX((o[D] - (o[Y] * -maxY) - (o[Z] *-maxZ)) * (1/o[X]));
			vector[1].setY(-maxY);
			vector[1].setZ(-maxZ);
			
			vector[2].setX((o[D] - (o[Y] * maxY) - (o[Z] * -maxZ)) * (1/o[X]));
			vector[2].setY(maxY);
			vector[2].setZ(-maxZ);
			
			vector[3].setX((o[D] - (o[Y] * maxY) - (o[Z] * maxZ)) * (1/o[X]));
			vector[3].setY(maxY);
			vector[3].setZ(maxZ);
		} else if(o[Z] != 0){
			vector[0].setX(-maxX);
			vector[0].setY(maxY);
			vector[0].setZ((o[D] - (o[X] * -maxX) - (o[Y] * maxY)) * (1/o[Z]));
			
			vector[1].setX(-maxX);
			vector[1].setY(-maxY);
			vector[1].setZ((o[D] - (o[X] * -maxX) - (o[Y] * maxY)) * (1/o[Z]));
			
			vector[2].setX(maxX);
			vector[2].setY(-maxY);
			vector[2].setZ((o[D] - (o[X] * -maxX) - (o[Y] * maxY)) * (1/o[Z]));
			
			vector[3].setX(maxX);
			vector[3].setY(maxY);
			vector[3].setZ((o[D] - (o[X] * -maxX) - (o[Y] * maxY)) * (1/o[Z]));
		}
		
		
		return vector;
	}

	//TODO : Complete
	private Vector3D[] createVectorPlane(float[] o) {
		Vector3D[] vector = new Vector3D[4];
		float[] carti = new float[4];
		for (int i = 0; i < 4; i++) { vector[i] = new Vector3D(0,0,0); }
		
		carti[2] = 1;
		carti[0] = (o[4]*o[8] - o[5]*o[7])/(o[3]*o[7] - o[6]*o[4]);
		carti[1] = (o[3]*o[8] - o[6]*o[5])/(o[6]*o[4] - o[3]*o[7]);
		
		carti[3] = carti[0]*o[0] + carti[1]*o[1] + carti[2]*o[2];
		
		vector = createCartPlane(carti);
		return vector;
	}
	
}
