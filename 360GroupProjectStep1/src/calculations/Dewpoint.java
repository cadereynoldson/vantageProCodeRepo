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
 * Calculates the dew point given the data provided for the temperature input and humidity input. 
 * @author Cade Reynoldson
 * @version 1.0
 */
public class Dewpoint extends AbstractSensor<Double> implements Runnable {

    /** The master treeset which contains information of the measured temperatures. */
    private TreeSet<DataPacket<Double>> temperatureInput;
    
    /** The master treeset which contains information of the measured humidities. */
    private TreeSet<DataPacket<Double>> humidityInput;
    
    /** The string representing what this sensor "measures" */
    private static String measurementString = "dew point";
    
    /** The interval (in seconds) to calculate the dew point. */
    private static final long CALCULATION_INTERVAL = 15;
    
    /**
     * Creates a new instance of dewpoint calculation thread. 
     * @param outputSet The set to output data into. 
     * @param f the file to output data to. 
     * @param temperatureInput the temperature input set which will be used for calculation. 
     * @param humidityInput the humidity input set which will be used for calculation. 
     */
    public Dewpoint(TreeSet<DataPacket<Double>> outputSet, File f, 
            TreeSet<DataPacket<Double>> temperatureInput, TreeSet<DataPacket<Double>> humidityInput) {
        super(outputSet, f);
        this.temperatureInput = temperatureInput;
        this.humidityInput = humidityInput;
        this.sensorName = "Dew Point";
    }
    
    /**
     * Iterates over the data for the past interval, indicated by the calculation interval and calculates the dew point of each "pair"
     * of values. Will skip over the end values if the one set of data is "longer" than the other.
     * @return The a tree set of datapackets which contain the calculated dew point values. 
     */
    public TreeSet<DataPacket<Double>> calculateDewPoint() {
        TreeSet<DataPacket<Double>> calculatedDewPoints = new TreeSet<DataPacket<Double>>();
        ZonedDateTime time = ZonedDateTime.now().minusSeconds(CALCULATION_INTERVAL);
        TreeSet<DataPacket<Double>> tempTail = (TreeSet<DataPacket<Double>>) temperatureInput.tailSet(new DataPacket<Double>(time, "Temperature", measurementString, 0.0));
        TreeSet<DataPacket<Double>> humidityTail = (TreeSet<DataPacket<Double>>) humidityInput.tailSet(new DataPacket<Double>(time, "Temperature", measurementString, 0.0));
        if (tempTail.isEmpty() || humidityTail.isEmpty())
            throw new IllegalArgumentException("Input for humidity or temperature is empty!");
        Iterator<DataPacket<Double>> tempIterator = tempTail.iterator();
        Iterator<DataPacket<Double>> humidityIterator = humidityTail.iterator();
        while (tempIterator.hasNext() && humidityIterator.hasNext()) { //While both of the sets contain elements to iterate over, calculate their dew points.
            DataPacket<Double> tempData = tempIterator.next();
            DataPacket<Double> humidityData = humidityIterator.next();
            double dewPoint = calculateDewPoint(tempData.getValue(), humidityData.getValue());
            calculatedDewPoints.add(new DataPacket<Double>(ZonedDateTime.now(), this.sensorName, measurementString, dewPoint));
        }
        if (tempIterator.hasNext()) { //If the temperature iterator conatins extra values, notify console.
            System.out.println("Extra values contained in the temperature set.");
        } else if (humidityIterator.hasNext()) { //If the humidity iterator contains extra values, notify console. 
            System.out.println("Extra values contained in the humidity set.");
        }
        return calculatedDewPoints;
    }
    
    /**
     * Calculates the dew point given an air temperature
     * @param airTemperature
     * @param relativeHumidity
     * @return
     */
    public static double calculateDewPoint(double airTemperature, double relativeHumidity) {
        double lnHumidity = Math.log(relativeHumidity / 100);
        double tMult = 17.27 * airTemperature;
        double tPlus = 237.3 + airTemperature;
        return (237.3 * (lnHumidity + (tMult / tPlus))) / (17.27 - (lnHumidity + (tMult / tPlus))); 
    }
    
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
        TreeSet<DataPacket<Double>> calculatedDewPoints = calculateDewPoint();
        //Add the calculated dew points to the output set. 
        for (DataPacket<Double> dewPoint : calculatedDewPoints) { 
            outputSet.add(dewPoint);
        }
        //Write calculated dew points to the output file. 
        try {
            oos.writeObject(calculatedDewPoints);
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
