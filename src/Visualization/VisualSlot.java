package Visualization;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import Datatypes.Course;

public class VisualSlot extends JPanel {

	private String name;
	private int slot;
	private int length;
	
	public VisualSlot(Course c) {
		setLayout(new BorderLayout());
		setBorder(new LineBorder(Color.BLACK));
		JLabel lbl = null;
		if(c == null) lbl = new JLabel("empty");
		else lbl = new JLabel(c.getName());
		
		lbl.setHorizontalAlignment(JLabel.CENTER);
		lbl.setVerticalAlignment(JLabel.CENTER);		
		add(lbl, BorderLayout.CENTER);
	}

}
