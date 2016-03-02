
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

		// Goals
		GoalOnScreen[] goal = new GoalOnScreen[10];
		int numGoals = -1;
		GoalOnScreen gFound = new GoalOnScreen();
		
		int i, j;
	
		// Initialize goal array
		for (i=0;i<10;i++) {
			goal[i] = new GoalOnScreen();
		}

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
			
			numGoals = findGoals(angle, x1, y1, x2, y2, goal);
			selectGoal(goal, numGoals, gFound);
			
			System.out.println("Goals found = " + numGoals);
			for (j=0; j<numGoals; j++) {
				System.out.println(goal[j]);
			}
		}
	}
	
	/**
	 * Finds all U-shaped goals in the scene.
	 * @param angle
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param goal Array of goals found
	 * @return Number of goals found
	 */
	public static int findGoals(double[] angle, double[] x1, double[] y1, 
			double[] x2, double[] y2, GoalOnScreen[] goal) {

		int curGoal = -1;
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
					curGoal++;
					gVert.copyGoal(goal[curGoal]);
				}
			}
		}
		
		return curGoal+1;
	}
	
	/**
	 * Find the interior of the closest goal on the screen.
	 * @param goal Array of goals
	 * @param numGoals Number of goals in array
	 * @param foundGoal Closest goal interior (initialize this object before calling)
	 */
	public static void selectGoal(GoalOnScreen[] goal, int numGoals, GoalOnScreen foundGoal) {
		int i, largestGoal, interiorGoal;
		double size;
		
		// Find largest goal
		size = 0;
		largestGoal = 0;
		for (i=0; i<numGoals; i++) {
			if ( (goal[i].botR.x - goal[i].botL.x) > size) {
				size = goal[i].botR.x - goal[i].botL.x;
				largestGoal = i;
			}
		}
		
		interiorGoal = -1;
		// Find interior, if it was detected
		for (i=0; i<numGoals; i++) {
			if ( (goal[i].botL.x > goal[largestGoal].botL.x) &&
					(goal[i].botR.x < goal[largestGoal].botR.x) &&
					(goal[i].botL.y > goal[largestGoal].botL.y) ) {
				interiorGoal = i;
			}
		}

		if (interiorGoal==-1) {
			goal[largestGoal].copyGoal(foundGoal);
		} else {
			goal[interiorGoal].copyGoal(foundGoal);
		}
	}
	
	public static double sqr(double x) {
		return x*x;
	}
}