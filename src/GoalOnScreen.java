import java.awt.geom.Point2D;

public class GoalOnScreen {
	// These are indexes into the GRIP NetworkTable output Line arrays 
	public Point2D.Double topL = new Point2D.Double();
	public Point2D.Double topR = new Point2D.Double();
	public Point2D.Double botL = new Point2D.Double();
	public Point2D.Double botR = new Point2D.Double();
	
	public void copyGoal( GoalOnScreen dest ) {
		dest.topL.x = this.topL.x;
		dest.topL.y = this.topL.y;
		dest.topR.x = this.topR.x;
		dest.topR.y = this.topR.y;
		dest.botL.x = this.botL.x;
		dest.botL.y = this.botL.y;
		dest.botR.x = this.botR.x;
		dest.botR.y = this.botR.y;
	}
	
	public String toString() {
		return "TopL: " + topL + "\nBotL: " + botL + "\nTopR: " + topR + "\nBotR: " + botR;
	}
	
	private static double sqr(double x) {
		return x*x;
	}

	/**
	 * Finds all U-shaped goals in the scene.
	 * @param angle
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param goal Array of goals found (all objects in array must be created in advance)
	 * @return Number of goals found
	 */
	public static int findGoals(double[] angle, double[] x1, double[] y1, 
			double[] x2, double[] y2, GoalOnScreen[] goal) {

		int curGoal = -1;
		int i, j;
		double ty, tx;
		double tol = 25.1;  // points must be within 3 pixels to be "adjacent"
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
		for (i=0; (i<angle.length)&&(curGoal<goal.length); i++) {
			
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
					(goal[i].botL.y < goal[largestGoal].botL.y) ) {
				interiorGoal = i;
			}
		}

		if (interiorGoal==-1) {
			goal[largestGoal].copyGoal(foundGoal);
		} else {
			goal[interiorGoal].copyGoal(foundGoal);
		}
	}
}
