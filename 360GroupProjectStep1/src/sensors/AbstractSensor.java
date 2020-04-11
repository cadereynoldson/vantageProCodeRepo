package sensors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.ZonedDateTime;
import java.util.Random;

public class AbstractSensor {

	// protected means that this class and any subclasses may access the property. 
	protected String sensorName = "Abstract Sensor";
    protected ZonedDateTime eventTime;
	protected int MAX_UPDATE_INTERVAL = 24; // seconds
	protected int MIN_UPDATE_INTERVAL = 20; // seconds
	protected int updateInterval = 10;
	protected long seed = 24; 
	// for each of time we create a new instance of one of the sensors, 
	// 	create a new random object, and initialize it with a seed to we can reproduce 
	// 	the values it generates later in our JUnit Tests 
	// 	(make sensor output pseudo-random and reproducible)
	protected Random rand = new Random(seed);
	File f;
	protected FileOutputStream fos;
	protected ObjectOutputStream oos;
	
	public void setUpdateInterval(int updateInterval) {
		if (updateInterval < 10) {
			throw new IllegalArgumentException(sensorName + " Update Interval < " + 
		MIN_UPDATE_INTERVAL + " sec");
		} else if (updateInterval > 12) {
			throw new IllegalArgumentException(sensorName + " Update Interval > " + 
		MAX_UPDATE_INTERVAL + " sec");
		} else {
			this.updateInterval = updateInterval;
		}
	}
}
