package sensors;

import java.io.File;
import java.util.Random;
import java.util.TreeSet;

import controller.Controller;
import controller.DataPacket;

/*
 * 
    Cable Length, Anemometer: 40 feet (12 m) (included) 240 feet (73 m) (maximum recommended)
    Note: Maximum displayable wind decreases as the length of cable increases. At 140’ (42 m) of cable, 
    	the maximum wind speed displayed is 135 mph (60 m/s); at 240’ (73 m), the maximum wind speed 
    	displayed is 100 mph (34 m/s).
         - When calculating the max wind speed for our random input generator, we need to factor in the 
         cable length as a field.
         
         * 0 <= length < 140: 
 */
public class WindSensor extends AbstractSensor<Integer> implements Runnable{
	
	private final int maxWindSpeed; // MPH
	//private double windUpdateInterval;
	public WindSensor(TreeSet<DataPacket<Integer>> outputSet, File f, int cableLength) {
		super(outputSet, f);
		//this.windUpdateInterval = windUpdateInterval;
		maxWindSpeed = calcMaxWindSpeed(cableLength);
	}
	
	private int calcMaxWindSpeed(int cableLength) {
		if (cableLength <= 0) {
			throw new IllegalArgumentException("Cable Length cannot be <= 0 ft for wind sensor");
		} else if (cableLength <= 140) {
			return 135;
		} else if (cableLength <= 240) {
			return 100;
		} else {
			throw new IllegalArgumentException("Cable Length cannot be > 240ft for wind sensor");
		}
	}
	public void run() {
		//System.out.println("This is the wind sensor");
		long unixTime = System.currentTimeMillis() / 1000L;
		//Controller.windMap.put(unixTime, "test");
		System.out.println(unixTime + " This is the wind sensor updating every 3 second" + getWindSpeed());
	}
	
	public int getWindSpeed() {
		//Random rand = new Random();
		return rand.nextInt(maxWindSpeed + 1); // [0, maxWindSpeed]
	}

}
