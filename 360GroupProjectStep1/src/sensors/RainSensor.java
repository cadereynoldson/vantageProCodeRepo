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

public class RainSensor extends AbstractSensor<Double> implements Runnable {


	private String sensor = "Rain";
	private String measurementString = "rainfall";

	//private TreeSet<DataPacket<Double>> rainfallSet = new TreeSet<DataPacket<Double>>();
	//public static File f = new File("rainfallSerializedOutput.txt");
	//private File f;
	
	// max rated accuraccy is rate for 10"/hr
	// (99.99"/3600) = 0.002777"/sec max accurate rate (per measurement)
	// (99.99"/3600) * 20 = 0.05555"/20sec max accurate rate (per measurement)
	// Max rated daily rainfall is 99.99"
	// (99.99"/3600) = 0.0227775"/sec max rate (per measurement)
	// (99.99"/3600) * 20 = 0.5555"/20sec max rate (per measurement)
	
	private static final int maxVal = 55; // 0.55"
	private static final int minVal = 0; // 0"
	
	public RainSensor(TreeSet<DataPacket<Double>> outputSet, File f) {
		super(outputSet, f);
	}
	
	public double getRain() {
		// [0, 99.99], resolution 0.01"
		double randomNumber = (rand.nextInt(maxVal + 1 - minVal) + minVal) / 100.0;  
		return randomNumber;
	}
	
//	public void run() {
//		System.out.println("runRainSensorExecuted");
//		try {
//			fos = new FileOutputStream(f);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			oos = new ObjectOutputStream(fos);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		eventTime = ZonedDateTime.now();
//		
//		DataPacket rdp = new DataPacket(eventTime, sensor, measurementString, getRain());
//		Controller.rainSet.add(rdp);
//		 
//		 TreeSet<DataPacket> rainSerialize = (TreeSet<DataPacket>) 
//				 Controller.rainSet.tailSet(new DataPacket(ZonedDateTime
//		          .now()
//		          .minusSeconds(60), sensor, measurementString, 0.0));
//	
//			try {
//				oos.writeObject(rainSerialize);
//
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			
//			try {
//				Controller.con.readSerializedData(f);
//				oos.flush();
//			    oos.close();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			//System.out.println("RainSensorThread");
//
//	}
	
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
		
		DataPacket<Double> rdp = new DataPacket<Double>(eventTime, sensor, measurementString, getRain());
		outputSet.add(rdp);
		 
		 TreeSet<DataPacket<Double>> rainSerialize = (TreeSet<DataPacket<Double>>) 
				 outputSet.tailSet(new DataPacket(ZonedDateTime
		          .now()
		          .minusSeconds(60), sensor, measurementString, 0.0));
	
			try {

				oos.writeObject(rainSerialize);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				Controller.con.<Double>readSerializedData(f);
				oos.flush();
			    oos.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("RainSensorThread");

	}

//	public void rainRateUpdate() {
//		System.out.println("rainRate");
//		try {
//			fos = new FileOutputStream(rainRateFile);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			oos = new ObjectOutputStream(fos);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		eventTime = ZonedDateTime.now();
//		
//		DataPacket rdp = new DataPacket(eventTime, sensor, measurementString, getRain());
//		Controller.rainSet.add(rdp);
//		 
//		 TreeSet<DataPacket> rainSerialize = (TreeSet<DataPacket>) 
//				 Controller.rainSet.tailSet(new DataPacket(ZonedDateTime
//		          .now()
//		          .minusSeconds(60), sensor, measurementString, 0.0));
//	
//			try {
//				oos.writeObject(rainSerialize);
//
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			
//			try {
//				Controller.con.readSerializedData(f);
//				oos.flush();
//			    oos.close();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			//System.out.println("RainSensorThread");
//
//	}
}
