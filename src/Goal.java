
public class Goal {
	// Goal dimensions
	static final double goalHeight = 12.5;  // Height of vertical bar, in inches
	static final double goalWidth = 16.25;  // Width of horizontal bar, in inches
	
	// Camera constants
	static final double cameraXRatio = 84.25/90.0/2.0;  	// Sx/Sz
	static final double cameraYRatio = cameraXRatio*240.0/320.0;  	// SY/Sz
//	static final double cameraXRatio = 18.8/19.25;  	// Sx/Sz (1/2 Sx)
//	static final double cameraYRatio = 13.25/19.25;  	// SY/Sz (1/2 Sy)
	static final double cameraXHalfRes = 640/2;		// Resolution
	static final double cameraYHalfRes = 480/2;		// Resolution
	
	// Camera
	double angCamera;	//Camera angle, in radians.  0 = horizontal, pi/2 = vertical
	
	public double dL, dR, dAvg;						 // Distance from camera to goal left/right edges, in inches
	public double betaL, betaR, betaAvg;				 // Horizontal angle from camera centerline to goal left/right edges
	public double widthL;								 // Apparent width of goal (due to angle to goal), in inches
	
	public void calcGoal(GoalOnScreen sGoal, double angleCamera) {
		// Calculations
		double tanTheta, sinTheta, cosTheta;  
		double xppTopL, xppBotL, xppBotR, xppTopR;	 // Normalized x location of each point screen
		double yppTopL, yppBotL, yppBotR, yppTopR;	 // Normalized y location of each point screen
		double xbzTopL, xbzBotL, xbzBotR, xbzTopR;   // Ratio of x/z for each point on goal
		double ybzTopL, ybzBotL, ybzBotR, ybzTopR;   // Ratio of y/z for each point on goal
		
		angCamera = angleCamera*Math.PI/180.0;
		tanTheta = Math.tan(angCamera);
		sinTheta = Math.sin(angCamera);
		cosTheta = Math.cos(angCamera);
		
		xppTopL = cameraXRatio * (sGoal.topL.x - cameraXHalfRes) / cameraXHalfRes;
		xppTopR = cameraXRatio * (sGoal.topR.x - cameraXHalfRes) / cameraXHalfRes;
		xppBotL = cameraXRatio * (sGoal.botL.x - cameraXHalfRes) / cameraXHalfRes;
		xppBotR = cameraXRatio * (sGoal.botR.x - cameraXHalfRes) / cameraXHalfRes;
		
		yppTopL = cameraYRatio * (cameraYHalfRes - sGoal.topL.y) / cameraYHalfRes;
		yppTopR = cameraYRatio * (cameraYHalfRes - sGoal.topR.y) / cameraYHalfRes;
		yppBotL = cameraYRatio * (cameraYHalfRes - sGoal.botL.y) / cameraYHalfRes;
		yppBotR = cameraYRatio * (cameraYHalfRes - sGoal.botR.y) / cameraYHalfRes;
		
		ybzTopL = (yppTopL + tanTheta) / (1 - yppTopL*tanTheta);
		ybzTopR = (yppTopR + tanTheta) / (1 - yppTopR*tanTheta);
		ybzBotL = (yppBotL + tanTheta) / (1 - yppBotL*tanTheta);
		ybzBotR = (yppBotR + tanTheta) / (1 - yppBotR*tanTheta);
		
		xbzTopL = xppTopL / (cosTheta - yppTopL*sinTheta);
		xbzTopR = xppTopR / (cosTheta - yppTopR*sinTheta);
		xbzBotL = xppBotL / (cosTheta - yppBotL*sinTheta);
		xbzBotR = xppBotR / (cosTheta - yppBotR*sinTheta);
		
		dL = goalHeight/(ybzTopL - ybzBotL) * Math.sqrt(1 + xbzBotL*xbzTopL);
		dR = goalHeight/(ybzTopR - ybzBotR) * Math.sqrt(1 + xbzBotR*xbzTopR);
		dAvg = (dL+dR)/2;
		
		betaL = Math.atan(xbzBotL) * 180.0/Math.PI;
		betaR = Math.atan(xbzBotR) * 180.0/Math.PI;
		betaAvg = (betaL + betaR)/2.0;
		
		widthL = goalHeight/(ybzTopL - ybzBotL) * Math.cos(betaL*Math.PI/180.0) * (xbzBotR - xbzBotL);
	}
}
