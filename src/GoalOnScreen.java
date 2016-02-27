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
		return "TopL: " + topL + "/nBotL: " + botL + "/nTopR: " + topR + "/nBotR: " + botR;
	}
}
