package controller;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class DataPacket<T> implements Comparable<DataPacket<T>>, Serializable {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// protected means that this class and any subclasses may access the property. 
	//protected String sensorName = "Abstract Sensor";
	protected ZonedDateTime eventTime;
	//Map<String, >
	protected String sensor;
	protected String measurement;
	protected T value;

	@Override
	public int compareTo(DataPacket<T> o) {
		return eventTime.compareTo(o.eventTime);
	}
	
	 public DataPacket(ZonedDateTime eventTime, String sensor, String measurement, T value) {
	        this.eventTime = eventTime;
	        this.measurement = measurement;
	        this.value = value;
	        sensor = "null";
	    }

    public DataPacket(ZonedDateTime eventTime, String measurement, T value) {
        this.eventTime = eventTime;
        this.measurement = measurement;
        this.value = value;
        sensor = "null";
    }
    

    public DataPacket(ZonedDateTime eventTime, T value) {
        this(eventTime, "null", value);
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
    public T getValue() {
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
