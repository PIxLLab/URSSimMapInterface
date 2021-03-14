package wearable;

import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.DeviceListener;



//Wearable_Seperate
public class MyoData{
	String gesture = "";
	public void testtheMyo() {
	try {
		long t= System.currentTimeMillis();
		long end = t+5000;
		
		Hub hub = new Hub("com.example.hello-myo");

		System.out.println("Attempting to find a Myo...");
		Myo myo = hub.waitForMyo(10000);

		if (myo == null) {
			throw new RuntimeException("Unable to find a Myo!");
		}

		System.out.println("Connected to a Myo armband!");
		DeviceListener dataCollector = new DataCollector();
		hub.addListener(dataCollector);

		while (System.currentTimeMillis() < end) {
			hub.run(1000 / 20);
			System.out.print(dataCollector);
			String[] test = dataCollector.toString().split("\\[");
			String[] test1 = test[test.length-1].split(" ");
			gesture = test1[0];
			System.out.println(gesture);
		}
	} catch (Exception e) {
		System.err.println("Error: ");
		e.printStackTrace();
		System.exit(1);
		}
	}
	
}
