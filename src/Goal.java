
public class Goal {
	// Goal dimensions
	static final double goalHeight = 12.0;  // Height of vertical bar, in inches
	static final double goalWidth = 24.0;  // Width of horizontal bar, in inches
	
	// Camera constants
	static final double cameraXRatio = 10/10;  	// Sx/Sz
	static final double cameraYRatio = 10/10;  	// SY/Sz
	static final double cameraXHalfRes = 640/2;		// Resolution
	static final double cameraYHalfRes = 480/2;		// Resolution
	
	// Camera
	double angCamera;	//Camera angle, in radians.  0 = horizontal, pi/2 = vertical
	
	public void calcGoal(GoalOnScreen sGoal, double angleCamera) {
		// Calculations
		double tanTheta, sinTheta, cosTheta;  
		double xppTopL, xppBotL, xppBotR, xppTopR;
		double yppTopL, yppBotL, yppBotR, yppTopR;
		double ybzTopL, ybzBotL, ybzBotR, ybzTopR;
		
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
		
	}
	
	
}
