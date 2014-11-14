package northgate.maths.Swing;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EquationListener implements DocumentListener, ChangeListener {

	private Equation e;
	
	public EquationListener(Equation e) {
		this.e = e;
	}
	
	@Override
	public void changedUpdate(DocumentEvent arg0) { update(arg0); }
	@Override
	public void insertUpdate(DocumentEvent arg0) { update(arg0); }
	@Override
	public void removeUpdate(DocumentEvent arg0) { update(arg0); }
	
	public void update(DocumentEvent arg0) { e.update(true); }
	

	@Override
	public void stateChanged(ChangeEvent arg0) {
		JSlider source = (JSlider)arg0.getSource();
	    if (!source.getValueIsAdjusting()) {
	        e.alpha = (float)(source.getValue()) / 255;
	    }
	    e.update(true);
	}

}
