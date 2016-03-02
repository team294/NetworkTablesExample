import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Canvas extends JComponent{

	double[] x1, y1, x2, y2;
	GoalOnScreen[] goal;
	int numGoals;
	GoalOnScreen gFound;
	
	public void paintIt() {
	    repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    
	    int i;
	    
	    g.setColor(new Color(0,0,0));
	    for (i=0; i<x1.length; i++) {
	    	g.drawLine((int)x1[i], (int)y1[i], (int)x2[i], (int)y2[i]);
	    }

	    g.setColor(new Color(255,0,0));
	    for (i=0; i<numGoals; i++) {
	    	g.drawLine((int)goal[i].topL.x, (int)goal[i].topL.y, (int)goal[i].botL.x, (int)goal[i].botL.y);
	    	g.drawLine((int)goal[i].botL.x, (int)goal[i].botL.y, (int)goal[i].botR.x, (int)goal[i].botR.y);
	    	g.drawLine((int)goal[i].topR.x, (int)goal[i].topR.y, (int)goal[i].botR.x, (int)goal[i].botR.y);
	    }
	    
	    g.setColor(new Color(0, 255,0));
    	g.drawLine((int)gFound.topL.x, (int)gFound.topL.y, (int)gFound.botL.x, (int)gFound.botL.y);
    	g.drawLine((int)gFound.botL.x, (int)gFound.botL.y, (int)gFound.botR.x, (int)gFound.botR.y);
    	g.drawLine((int)gFound.topR.x, (int)gFound.topR.y, (int)gFound.botR.x, (int)gFound.botR.y);
	    
	}
	

	public void drawAll(double[] x1, double[] y1, 
			double[] x2, double[] y2, GoalOnScreen[] goal, int numGoals, GoalOnScreen gFound) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.goal = goal;
		this.numGoals = numGoals;
		this.gFound = gFound;
		
		JFrame testFrame = new JFrame();
	    testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    setPreferredSize(new Dimension(640, 480));
	    testFrame.getContentPane().add(this, BorderLayout.CENTER);
	    JPanel buttonsPanel = new JPanel();
	    JButton clearButton = new JButton("Paint");
	    buttonsPanel.add(clearButton);
	    testFrame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
	    clearButton.addActionListener(new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent e) {
	            paintIt();
	        }
	    });
	    testFrame.pack();
	    testFrame.setVisible(true);
	}

}
