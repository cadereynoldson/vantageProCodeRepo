package controller;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class DataPacket implements Comparable<DataPacket>, Serializable {
			
	// protected means that this class and any subclasses may access the property. 
	//protected String sensorName = "Abstract Sensor";
	protected ZonedDateTime eventTime;
	protected String sensor;
	protected String measurement;
	protected double value;

	@Override
	public int compareTo(DataPacket o) {
		return eventTime.compareTo(o.eventTime);
	}
	
	 public DataPacket(ZonedDateTime eventTime, String sensor, String measurement, double value) {
	        this.eventTime = eventTime;
	        this.measurement = measurement;
	        this.value = value;
	        sensor = "null";
	    }

    public DataPacket(ZonedDateTime eventTime, String measurement, double value) {
        this.eventTime = eventTime;
        this.measurement = measurement;
        this.value = value;
        sensor = "null";
    }
    

    public DataPacket(ZonedDateTime eventTime, double value) {
        this(eventTime, "null", value);
    }
    
    public DataPacket(ZonedDateTime eventTime) {
        this(eventTime, "null", 0.0);
    }
    
    // returns sensor type for the given data packet
    public String getSensor() {
    	return sensor;
    }
 
    // returns sensor type for the given data packet
    public String getMeasurement() {
    	return measurement;
    }
    
 // returns sensor type for the given data packet
    public double getValue() {
    	return value;
    }

    // return time when data packet was recorded
    public ZonedDateTime getEventTime() {
        return eventTime;
    }
    
    public String toString() {
        return "[" + value + "]";    	
    }

}
