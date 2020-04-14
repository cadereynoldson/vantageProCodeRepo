package calculations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import console.Console;
import controller.Controller;
import controller.DataPacket;
import sensors.AbstractSensor;

public class RainfallRate extends AbstractSensor<Double> implements Runnable {
	private String sensor = "Rain";
	private String measurementString = "rain rate";
	
	// Take the last 60 seconds to analyze.
	private static final long PREV_DATA_LENGTH = 60;
	
	private static final int maxVal = 3000; // 30"/hr
	private static final int minVal = 0; // 0"/hr
	private TreeSet<DataPacket<Double>> inputSet;
	public RainfallRate(TreeSet<DataPacket<Double>> outputSet, File f, TreeSet<DataPacket<Double>> inputSet) {
		super(outputSet, f);
		this.inputSet = inputSet;
	}

	public double calcRainRate() {
		// 0 or [0.4, 30]
		 TreeSet<DataPacket<Double>> inputSetTail = (TreeSet<DataPacket<Double>>) 
				 inputSet.tailSet(new DataPacket<Double>(ZonedDateTime
		          .now()
		          .minusSeconds(PREV_DATA_LENGTH), sensor, measurementString, 0.0));

		 if (inputSetTail.size() == 0) {
			 throw new IllegalArgumentException("rain rate input set is empty!");
		 }
		 BigDecimal sumVal = new BigDecimal(0);
		 for (DataPacket<Double> dp: inputSetTail) {
			 BigDecimal dpVal = new BigDecimal(dp.getValue());
			 sumVal = sumVal.add(dpVal);
		 }
		 BigDecimal inputSizeBig = new BigDecimal(inputSetTail.size());
	       BigDecimal rateBig = sumVal.divide(inputSizeBig, RoundingMode.HALF_UP);
	       //BigDecimal rateBig = sumVal.divide(sumVal, inputSetTail.size());

		 double rate = rateBig.doubleValue();
		 if (rate < 0.04) {
			 rate = 0;
		 }
		 if (rate > 30) {
			 rate = 30;
		 }
		 return rate;

//		double randomNumber = (rand.nextInt(maxVal + 1 - minVal) + minVal) /100.0 ;  
//		if (randomNumber < 0.04) {
//			randomNumber = 0;
//		}
//		return randomNumber;
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

		DataPacket<Double> rdp = new DataPacket<Double>(eventTime, sensor, measurementString, calcRainRate());

		outputSet.add(rdp);
		 TreeSet<DataPacket<Double>> rainSerialize = (TreeSet<DataPacket<Double>>) 
				 outputSet.tailSet(new DataPacket<Double>(ZonedDateTime
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
			//System.out.println("RainFallRateThread");
	}
}
