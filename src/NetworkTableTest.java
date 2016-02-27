
import java.util.*;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class NetworkTableTest {
	
/* Before running this program:
	1. Run GRIP, including a "Publish LinesReport" that has Name "myLinesReport"
	2. Start menu -> run "CMD", then type "ipconfig" at the prompt.  Find "IPv4 Address" 
	     and put it in the setIPAddress command below.
	3. Run C:\Users\team\wpilib\tools\OutlineViewer.jar.  Input your IP address and 
	     click "Start Server".
*/	
	public static void main(String[] args) {

		// NetworkTable data
		NetworkTable table;
		Set<String> keys;
		double[] angle, x1, y1, x2, y2;
		double[] networkTableDefault = new double[] { -1.0 };

		// Goals.  lastGoal = last used index in array.
		GoalOnScreen[] goal = new GoalOnScreen[10];
		int lastGoal = -1;
		
		int i, j;
	
		NetworkTable.setClientMode();
//		NetworkTable.setIPAddress("192.168.1.64");
		table = NetworkTable.getTable("GRIP/myLinesReport");

		System.out.println(table.toString());

		for (i=0;i<1;i++) {
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
			
			lastGoal = findGoals(angle, x1, y1, x2, y2, goal);
			System.out.println("Goals found = " + lastGoal);
			for (j=0; j<lastGoal; j++) {
				System.out.println(goal[j]);
			}
		}
	}
	
	public static int findGoals(double[] angle, double[] x1, double[] y1, 
			double[] x2, double[] y2, GoalOnScreen[] goal) {

		int goalFound = -1;
		int i, j;
		double ty, tx;
		double tol = 25.1;  // points must be within 5 pixels to be "adjacent"
		double curLTol, curRTol;
		GoalOnScreen gHoriz = new GoalOnScreen();
		GoalOnScreen gVert = new GoalOnScreen();
		boolean foundLeft, foundRight;

		// Always make point 1 the bottom point
		for (i=0; i<angle.length; i++) {
			if (y2[i]>y1[i]) {
				tx = x2[i];
				ty = y2[i];
				x2[i] = x1[i];
				y2[i] = y1[i];
				x1[i] = tx;
				y1[i] = ty;
			}
		}
		
		// loop through line segments looking for horizontal pieces
		for (i=0; i<angle.length; i++) {
			
			// is this a horizontal piece?
			if ( (angle[i]<45 && angle[i]>-45) || angle[i]>135 || angle[i]<-135 ) {
				if (x1[i] < x2[i]) {
					gHoriz.botL.x = x1[i];
					gHoriz.botL.y = y1[i];
					gHoriz.botR.x = x2[i];
					gHoriz.botR.y = y2[i];
				} else {
					gHoriz.botL.x = x2[i];
					gHoriz.botL.y = y2[i];
					gHoriz.botR.x = x1[i];
					gHoriz.botR.y = y1[i];
				}

				// Currently no closest vertical line found (yet)
				foundLeft = false;
				foundRight = false;
				curLTol = tol;
				curRTol = tol;
				
				// Find closest vertical pieces in U shape (opening on top)
				for (j=0; j<angle.length; j++) {
					// only look for vertical pieces
					if ( (angle[j]>45 && angle[j]<135) || (angle[j]<-45 && angle[j]>-135) ) {
						// Look at bottom point -- match left or right to horizontal piece?
						if ( sqr(x1[j]-gHoriz.botL.x)+sqr(y1[j]-gHoriz.botL.y) < curLTol) {
							foundLeft = true;
							curLTol = sqr(x1[j]-gHoriz.botL.x)+sqr(y1[j]-gHoriz.botL.y);
							gVert.botL.x = x1[j];
							gVert.botL.y = y1[j];
							gVert.topL.x = x2[j];
							gVert.topL.y = y2[j];
						} else if ( sqr(x1[j]-gHoriz.botR.x)+sqr(y1[j]-gHoriz.botR.y) < curRTol) {
							foundRight = true;
							curRTol = sqr(x1[j]-gHoriz.botR.x)+sqr(y1[j]-gHoriz.botR.y);
							gVert.botR.x = x1[j];
							gVert.botR.y = y1[j];
							gVert.topR.x = x2[j];
							gVert.topR.y = y2[j];
						}
					}
				}
				
				if (foundLeft && foundRight) {
					goalFound++;
					gVert.copyGoal(goal[goalFound]);
				}
			}
		}
		
		return goalFound;
	}
	
	public static double sqr(double x) {
		return x*x;
	}
}