package sensors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.ZonedDateTime;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import console.Console;
import controller.Controller;
import controller.DataPacket;

public class RainSensor extends AbstractSensor implements Runnable {
	private String sensor = "Rain";
	private String measurementString = "rainfall";


	public static File f = new File("rainSensorSerializedOutput.txt");
	private static final int maxVal = 9999; // 99.99"
	private static final int minVal = 0; // 0"
	public double getRain() {
		// [0, 99.99], resolution 0.01"
		double randomNumber = (rand.nextInt(maxVal + 1 - minVal) + minVal) / 100.0;  
		if (randomNumber < 0.04) {
			randomNumber = 0;
		}
		return randomNumber;
	}
	
	public void run() {
		try {
			fos = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			oos = new ObjectOutputStream(fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		eventTime = ZonedDateTime.now();
		
		DataPacket rdp = new DataPacket(eventTime, sensor, measurementString, getRain());
		Controller.rainSet.add(rdp);
		 
		 TreeSet<DataPacket> rainSerialize = (TreeSet<DataPacket>) 
				 Controller.rainSet.tailSet(new DataPacket(ZonedDateTime
		          .now()
		          .minusSeconds(60), sensor, measurementString, 0.0));
	
			try {
				oos.writeObject(rainSerialize);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				Controller.con.readSerializedData(f);
				oos.flush();
			    oos.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
