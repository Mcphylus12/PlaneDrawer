/**
 * 
 */
package northgate.maths.Swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

/**
 * @author Kyle
 *
 */
@SuppressWarnings("serial")
public class Equation extends JPanel implements ActionListener{
	
	//Constants
	public final static int posCartLine = 0;
	public final static int posVecLine = 1;
	public final static int posCartPlane = 2;
	public final static int posVecPlane = 3;
	public final static int posNone = 4;
	
	//Logic Vars
	//TODO: Change this back to single per Equation boolean
	// Changed due to errors occurring, backtraced to different source
	public static boolean[] needUpdate = new boolean[EquFrame.numPlane];
	public float alpha;
	public Integer Id;
	
	//Vector Plane Vars
	Vector3D posVec;
	Vector3D fdirVec;
	Vector3D sdirVec;
	public JTextField[] vecPInputs = new JTextField[9];
	public JLabel vecPLabel = new JLabel("r = (  _  ) + t(  _  ) + s(  _  )");
	
	//Vector Line Vars
	public JTextField[] vecLInputs = new JTextField[6];
	public JLabel vecLLabel = new JLabel("r = (  _  ) + t(  _  )");
	
	//Cartesian Plane Vars
	public double x;
	public double y;
	public double z;
	public double K;
	public JTextField[] cartPInputs = new JTextField[4];
	public JLabel cartPLabel = new JLabel("_x+      _y+      _z=      _");
	
	//Cartesian Line Vars
	public JTextField[] cartLInputs = new JTextField[9];
	public JLabel cartLLabel = new JLabel("_x -      _=      _y -      _=      _z -      _");
	public JLabel cartLUabel = new JLabel("________     ________     ________");
	
	public String[] Type = {"Cartesian-Line", "Vector-Line", "Cartesian-Plane", "Vector-Plane", "None"};
	public JComboBox<String> TypeSel = new JComboBox<String>(Type);
	
	//Slider
	public JSlider alphaSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 230);
	
	public Equation(int x,int y, int Id){
		
		this.Id = Id;
		EquationListener l = new EquationListener(this);
		
		alpha = 1;
		
		TypeSel.setSelectedIndex(posNone);
		setBounds(x, y, 230, 120);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setLayout(null);
		TypeSel.addActionListener(this);
		TypeSel.setBounds(getWidth()/2 - 100, 5, 200, 20);
		add(TypeSel);
		
		iSlider(l);
		
		iCarL(l);
		iVecL(l);
		iCarP(l);
		iVecP(l);
		
		add(cartLLabel);
		add(cartLUabel);
		add(vecLLabel);
		add(cartPLabel);
		add(vecPLabel);

		this.update(true);
	}
	
	
	public void iSlider(EquationListener l) {
		alphaSlider.setBounds(5, getHeight() - 25, getWidth() - 10, 20);
		alphaSlider.addChangeListener(l);
		alphaSlider.setMajorTickSpacing(10);
		alphaSlider.setMinorTickSpacing(5);
		alphaSlider.setPaintTicks(true);
		alphaSlider.setVisible(true);
		add(alphaSlider);
	}
	
	
	//TODO
	public void iCarL(EquationListener l) {
		
		cartLLabel.setBounds(29, 45, 225, 15);
		cartLLabel.setVisible(false);
		
		cartLUabel.setBounds(17, 47, 225, 15);
		cartLUabel.setVisible(false);
		
		cartLInputs[0] = new JTextField();
		cartLInputs[0].setBounds(16, 45, 20, 15);
		cartLInputs[1] = new JTextField();
		cartLInputs[1].setBounds(55, 45, 20, 15);
		cartLInputs[2] = new JTextField();
		cartLInputs[2].setBounds(35, 63, 20, 15);
		cartLInputs[3] = new JTextField();
		cartLInputs[3].setBounds(87, 45, 20, 15);
		cartLInputs[4] = new JTextField();
		cartLInputs[4].setBounds(125, 45, 20, 15);
		cartLInputs[5] = new JTextField();
		cartLInputs[5].setBounds(106, 63, 20, 15);
		cartLInputs[6] = new JTextField();
		cartLInputs[6].setBounds(157, 45, 20, 15);
		cartLInputs[7] = new JTextField();
		cartLInputs[7].setBounds(195, 45, 20, 15);
		cartLInputs[8] = new JTextField();
		cartLInputs[8].setBounds(176, 63, 20, 15);
		for (JTextField j : cartLInputs){
			j.getDocument().addDocumentListener(l);
			add(j);
			j.setVisible(false);
		}
	}
	
	//TODO
	public void iVecL(EquationListener l) {
		vecLLabel.setBounds(65, 50, 225, 15);
		vecLLabel.setVisible(false);
		
		vecLInputs[0] = new JTextField();
		vecLInputs[0].setBounds(87, 30, 20, 15);
		vecLInputs[1] = new JTextField();
		vecLInputs[1].setBounds(87, 50, 20, 15);
		vecLInputs[2] = new JTextField();
		vecLInputs[2].setBounds(87, 70, 20, 15);
		vecLInputs[3] = new JTextField();
		vecLInputs[3].setBounds(131, 30, 20, 15);
		vecLInputs[4] = new JTextField();
		vecLInputs[4].setBounds(131, 50, 20, 15);
		vecLInputs[5] = new JTextField();
		vecLInputs[5].setBounds(131, 70, 20, 15);
		for (JTextField j : vecLInputs){
			j.getDocument().addDocumentListener(l);
			add(j);
			j.setVisible(false);
		}
	}

	public void iCarP(EquationListener l) {
		cartPLabel.setBounds(58, 45, 225, 15);
		cartPLabel.setVisible(false);
		
		cartPInputs[0] = new JTextField();
		cartPInputs[0].setBounds(45, 45, 20, 15);
		cartPInputs[1] = new JTextField();
		cartPInputs[1].setBounds(84, 45, 20, 15);
		cartPInputs[2] = new JTextField();
		cartPInputs[2].setBounds(123, 45, 20, 15);
		cartPInputs[3] = new JTextField();
		cartPInputs[3].setBounds(163, 45, 20, 15);
		for (JTextField j : cartPInputs){
			j.getDocument().addDocumentListener(l);
			add(j);
			j.setVisible(false);
		}
	}
	
	public void iVecP(EquationListener l) {
		vecPLabel.setBounds(43, 50, 225, 15);
		vecPLabel.setVisible(false);
		
		vecPInputs[0] = new JTextField();
		vecPInputs[0].setBounds(65, 30, 20, 15);
		vecPInputs[1] = new JTextField();
		vecPInputs[1].setBounds(65, 50, 20, 15);
		vecPInputs[2] = new JTextField();
		vecPInputs[2].setBounds(65, 70, 20, 15);
		vecPInputs[3] = new JTextField();
		vecPInputs[3].setBounds(109, 30, 20, 15);
		vecPInputs[4] = new JTextField();
		vecPInputs[4].setBounds(109, 50, 20, 15);
		vecPInputs[5] = new JTextField();
		vecPInputs[5].setBounds(109, 70, 20, 15);
		vecPInputs[6] = new JTextField();
		vecPInputs[6].setBounds(156, 30, 20, 15);
		vecPInputs[7] = new JTextField();
		vecPInputs[7].setBounds(156, 50, 20, 15);
		vecPInputs[8] = new JTextField();
		vecPInputs[8].setBounds(156, 70, 20, 15);
		for (JTextField j : vecPInputs){
			j.getDocument().addDocumentListener(l);
			add(j);
			j.setVisible(false);
		}
	}

	/* 
	 * @param arg0
	 */
	@Override
	public void actionPerformed(ActionEvent a) {
		if(a.getSource() == TypeSel){
			Clear();
			switch(TypeSel.getSelectedIndex()){
			case posCartLine:
				initCartesianLine();
				break;
			case posVecLine:
				initVectorLine();
				break;
			case posCartPlane:
				initCartesian();
				break;
			case posVecPlane:
				initVector();
				break;
			default:
				break;
			}
		}

		this.update(true);
	}

	/**
	 * 
	 */
	public void Clear() {
		posVec = null;
		fdirVec = null;
		sdirVec = null;
		x = 0;
		y = 0;
		z = 0;
		cartLLabel.setVisible(false);
		cartLUabel.setVisible(false);
		vecLLabel.setVisible(false);
		cartPLabel.setVisible(false);
		vecPLabel.setVisible(false);
		
		for (JTextField j : vecLInputs){
			j.setVisible(false);
		}
		
		for (JTextField j : cartLInputs){
			j.setVisible(false);
		}
		
		for (JTextField j : vecPInputs){
			j.setVisible(false);
		}
		
		for (JTextField j : cartPInputs){
			j.setVisible(false);
		}
		
		this.update(true);
	}

	/**
	 * 
	 */
	private void initVector() {
		
		for (JTextField j : vecPInputs){
			j.setVisible(true);
		}
		
		vecPLabel.setVisible(true);
	}

	/**
	 * 
	 */
	private void initCartesian() {
		
		for (JTextField j : cartPInputs){
			j.setVisible(true);
		}
		
		cartPLabel.setVisible(true);
	}
	
	private void initVectorLine() {
		
		for (JTextField j : vecLInputs){
			j.setVisible(true);
		}
		
		vecLLabel.setVisible(true);
	}

	/**
	 * 
	 */
	private void initCartesianLine() {
		
		for (JTextField j : cartLInputs){
			j.setVisible(true);
		}
		
		cartLLabel.setVisible(true);
		cartLUabel.setVisible(true);
	}
	
	public boolean toPos(Integer i) {
		if (i < 0 || i >= Type.length) return false;
		TypeSel.setSelectedIndex(i);
		return true;
	}
	
	public void update(Boolean b) {
		Equation.needUpdate[Id] = b;
	}
	
}

