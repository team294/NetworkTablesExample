
import java.util.*;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class NetworkTableTest {
	
/* Before running this program:
	1. Run GRIP, including a "Publish Contours Report" that has Name "myContoursReport"
	2. Start menu -> run "CMD", then type "ipconfig" at the prompt.  Find "IPv4 Address" 
	     and put it in the setIPAddress command below.
	3. Run C:\Users\team\wpilib\tools\OutlineViewer.jar.  Input your IP address and 
	     click "Start Server".
*/	
	public static void main(String[] args) {

		Set<String> keys;
		double[] angles, angleDefault;
		int i;
		NetworkTable table;
	
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("127.0.0.1");
		table = NetworkTable.getTable("GRIP/myContoursReport");

		myPrint(table.toString());

		angleDefault = new double[] { -1.0 };
		
		for (i=0;i<3;i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			keys = table.getKeys();
			
			myPrint("Number of keys = " + keys.size());
			myPrint("Keys = " + keys);
						
			angles = table.getNumberArray("angle", angleDefault );
			
			myPrint("angle array length = " + angles.length);
			myPrint("angle[0] value = " + angles[0]);
		}
	}
	
	public static void myPrint(String s) {
		System.out.println(s);
	}
}