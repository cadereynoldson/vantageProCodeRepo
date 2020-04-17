package calculations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.TreeSet;

import controller.Controller;
import controller.DataPacket;
import sensors.AbstractSensor;

/**
 * Calculates the wind chill given the data provided for the temperature input and wind input. 
 * @author Cade Reynoldson
 * @version 1.0
 */
public class WindChill extends AbstractSensor<Double> implements Runnable {

    /** The master treeset which contains information of the measured temperatures. */
    private TreeSet<DataPacket<Double>> temperatureInput;
    
    /** The master treeset which contains information of the measured wind speeds. */
    private TreeSet<DataPacket<Double>> windInput;
    
    /** The string representing what this sensor "measures" */
    private static String measurementString = "wind chill";
    
    /** The interval (in seconds) to calculate the windchill. */
    private static final long CALCULATION_INTERVAL = 12;
    
    /**
     * Creates a new instance of windchill calculation thread. 
     * @param outputSet The set to output data into. 
     * @param f the file to output data to. 
     * @param temperatureInput the temperature input set which will be used for calculation. 
     * @param windInput the humidity input set which will be used for calculation. 
     */
    public WindChill(TreeSet<DataPacket<Double>> outputSet, File f, 
            TreeSet<DataPacket<Double>> temperatureInput, TreeSet<DataPacket<Double>> windInput) {
        super(outputSet, f);
        this.temperatureInput = temperatureInput;
        this.windInput = windInput;
        this.sensorName = "Wind chill";
    }
    
    /**
     * Iterates over the data for the past interval, indicated by the calculation interval. 
     * Since there will always be more wind data than temperature data, this method assumes for every ONE temperature
     * value, there will be THREE sets of data for windspeed.   
     * @return The a tree set of datapackets which contain the calculated dew point values. 
     */
    public TreeSet<DataPacket<Double>> calculateWindChill() {
        TreeSet<DataPacket<Double>> calculatedWindChills = new TreeSet<DataPacket<Double>>();
        ZonedDateTime time = ZonedDateTime.now().minusSeconds(CALCULATION_INTERVAL);
        TreeSet<DataPacket<Double>> tempTail = (TreeSet<DataPacket<Double>>) temperatureInput.tailSet(new DataPacket<Double>(time, "Temperature", measurementString, 0.0));
        TreeSet<DataPacket<Double>> windTail = (TreeSet<DataPacket<Double>>) windInput.tailSet(new DataPacket<Double>(time, "Wind", measurementString, 0.0));
        if (tempTail.isEmpty() || windTail.isEmpty())
            throw new IllegalArgumentException("Input for humidity or temperature is empty!");
        Iterator<DataPacket<Double>> tempIterator = tempTail.iterator();
        Iterator<DataPacket<Double>> windIterator = windTail.iterator();
        while (tempIterator.hasNext()) { //While the temperature iterator has points to iterate over, calculate their windchill with the average of their 3 respective values from the value set. 
            double windAverage = 0;
            for (int i = 0; i < 3; i++)
                windAverage += windIterator.next().getValue();
            windAverage /= 3;
            DataPacket<Double> tempData = tempIterator.next();
            double dewPoint = calculateWindChill(tempData.getValue(), windAverage);
            calculatedWindChills.add(new DataPacket<Double>(ZonedDateTime.now(), this.sensorName, measurementString, dewPoint));
        }
        if (windIterator.hasNext()) { //If the humidity iterator contains extra values, notify console. 
            System.out.println("Extra values contained in the wind set.");
        }
        return calculatedWindChills;
    }
    
    /**
     * Calculates the wind chill given a temperature (degrees farenheit) and a windspeed (miles per hour).
     * @param temperature the temperature in farenheit. 
     * @param windspeed the windspeed in miles per hour.
     * @return the calculated wind chill.
     */
    public static double calculateWindChill(double temperature, double windspeed) {
        return 35.74 + (0.6215 * temperature) - (35.75 * Math.pow(windspeed, 0.16)) + (0.4275 * temperature * Math.pow(windspeed, 0.16));
    }
    
    /**
     * Runs the windchill thread. 
     */
    @Override
    public void run() {
        //Initialization of thread outputs. 
        try {
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
        } catch (FileNotFoundException e){
            System.out.println("File not found --- Dew point sensor");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO Exeption --- Dew point sensor");
            e.printStackTrace();
        }
        //Code to run the thread. 
        TreeSet<DataPacket<Double>> calculatedWindChills = calculateWindChill();
        //Add the calculated dew points to the output set. 
        for (DataPacket<Double> windChill : calculatedWindChills) { 
            outputSet.add(windChill);
        }
        //Write calculated dew points to the output file. 
        try {
            oos.writeObject(calculatedWindChills);
        } catch (IOException e) {
            System.out.println("Error writing the calculated dew points");
            e.printStackTrace();
        }
        
        try {
            Controller.con.<Double>readSerializedData(f);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            System.out.println("Error closing file out.");
            e.printStackTrace();
        }
    }
    
}
