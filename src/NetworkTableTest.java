
import java.util.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class NetworkTableTest {
	
/* Before running this program:
	1. Run GRIP, including a "Publish LinesReport" that has Name "myLinesReport"
	2. Start menu -> run "CMD", then type "ipconfig" at the prompt.  Find "IPv4 Address" 
	     and put it in the setIPAddress command below.
	3. Run C:\Users\team\wpilib\tools\OutlineViewer.jar.  Input your IP address and 
	     click "Start Server".
*/	
	
	public final static int maxGoals = 10;

	public static void main(String[] args) {

		// NetworkTable data
		NetworkTable table;
		Set<String> keys;
		double[] angle, x1, y1, x2, y2;
		double[] networkTableDefault = new double[] { -1.0 };

		// Goals on the screen
		GoalOnScreen[] sGoal = new GoalOnScreen[maxGoals];
		GoalOnScreen sGFound = new GoalOnScreen();
		int numGoals = -1;
		
		// Create output window
	    final Canvas comp = new Canvas();
		
	    // Goal in real space
	    Goal goal = new Goal();
	    
		int i, j;
	
		// Initialize goal array
		for (i=0;i<10;i++) {
			sGoal[i] = new GoalOnScreen();
		}

		NetworkTable.setClientMode();
		NetworkTable.setTeam(294);
//		NetworkTable.setIPAddress("roborio-294-frc.local");
		table = NetworkTable.getTable("GRIP/myLinesReport");

		System.out.println(table.toString());

		for (i=0;i<10;i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			keys = table.getKeys();
			
			System.out.println("Number of keys = " + keys.size());
			System.out.println("Keys = " + keys);
						
			angle = table.getNumberArray("angle", networkTableDefault );
			x1 = table.getNumberArray("x1", networkTableDefault );
			y1 = table.getNumberArray("y1", networkTableDefault );
			x2 = table.getNumberArray("x2", networkTableDefault );
			y2 = table.getNumberArray("y2", networkTableDefault );
			
			System.out.println("angle array length = " + angle.length);
			System.out.println("angle[0] value = " + angle[0]);
			
			numGoals = GoalOnScreen.findGoals(angle, x1, y1, x2, y2, sGoal);
			GoalOnScreen.selectGoal(sGoal, numGoals, sGFound);
			
			comp.drawAll(x1, y1, x2, y2, sGoal, numGoals, sGFound);
			comp.paintIt();
			
			goal.calcGoal(sGFound, 42.0);
			
			System.out.println("Goals found = " + numGoals + "\n");
			System.out.println("Main goal: " + sGFound + "\n");
			System.out.println("Main goal:  Dist = " + goal.dAvg + ", angle = " + goal.betaL + ", width = " + goal.widthL + "\n");
//			for (j=0; j<numGoals; j++) {
//				System.out.println(sGoal[j] + "\n");
//			}
		}
	}
	
	
}