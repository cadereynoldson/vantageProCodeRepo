
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
 * Class for calculating the heat index given the relative humidity and temperature. This uses the  
 * @author Cade Reynoldson
 * @version 1.0
 * @date 4/12/2020
 */
public class HeatIndex extends AbstractSensor<Double> implements Runnable {
    


    /** The master treeset which contains information of the measured temperatures. */
    private TreeSet<DataPacket<Double>> temperatureInput;
    
    /** The master treeset which contains information of the measured humidities. */
    private TreeSet<DataPacket<Double>> humidityInput;
    
    /** The string representing what this sensor "measures" */
    private static String measurementString = "heat index";
    
    /** The interval (in seconds) to calculate the dew point. */
    private static final long CALCULATION_INTERVAL = 15;
    
    /**
     * Creates a new instance of dewpoint calculation thread. 
     * @param outputSet The set to output data into. 
     * @param f the file to output data to. 
     * @param temperatureInput the temperature input set which will be used for calculation. 
     * @param humidityInput the humidity input set which will be used for calculation. 
     */
    public HeatIndex(TreeSet<DataPacket<Double>> outputSet, File f, 
            TreeSet<DataPacket<Double>> temperatureInput, TreeSet<DataPacket<Double>> humidityInput) {
        super(outputSet, f);
        this.temperatureInput = temperatureInput;
        this.humidityInput = humidityInput;
        this.sensorName = "Heat index";
    }
    
    /**
     * Iterates over the data for the past interval, indicated by the calculation interval and calculates the dew point of each "pair"
     * of values. Will skip over the end values if the one set of data is "longer" than the other.
     * @return The a tree set of datapackets which contain the calculated dew point values. 
     */
    public TreeSet<DataPacket<Double>> calculateHeatIndex() {
        TreeSet<DataPacket<Double>> calculatedHeatIndexes = new TreeSet<DataPacket<Double>>();
        ZonedDateTime time = ZonedDateTime.now().minusSeconds(CALCULATION_INTERVAL);
        TreeSet<DataPacket<Double>> tempTail = (TreeSet<DataPacket<Double>>) temperatureInput.tailSet(new DataPacket<Double>(time, "Temperature", measurementString, 0.0));
        TreeSet<DataPacket<Double>> humidityTail = (TreeSet<DataPacket<Double>>) humidityInput.tailSet(new DataPacket<Double>(time, "Humidity", measurementString, 0.0));
        if (tempTail.isEmpty() || humidityTail.isEmpty())
            throw new IllegalArgumentException("Input for humidity or temperature is empty!");
        Iterator<DataPacket<Double>> tempIterator = tempTail.iterator();
        Iterator<DataPacket<Double>> humidityIterator = humidityTail.iterator();
        while (tempIterator.hasNext() && humidityIterator.hasNext()) { //While both of the sets contain elements to iterate over, calculate their dew points.
            DataPacket<Double> tempData = tempIterator.next();
            DataPacket<Double> humidityData = humidityIterator.next();
            double dewPoint = calculateHeatIndex(tempData.getValue(), humidityData.getValue());
            calculatedHeatIndexes.add(new DataPacket<Double>(ZonedDateTime.now(), this.sensorName, measurementString, dewPoint));
        }
        if (tempIterator.hasNext()) { //If the temperature iterator conatins extra values, notify console.
            System.out.println("Extra values contained in the temperature set.");
        } else if (humidityIterator.hasNext()) { //If the humidity iterator contains extra values, notify console. 
            System.out.println("Extra values contained in the humidity set.");
        }
        return calculatedHeatIndexes;
    }
    
    /**
     * Calculates the heat index given the relative humidity and temperature (in farenheit). 
     * @param relativeHumidity the relative humidity. NOTE: for 80% humidity, plug in 80, not 0.80 
     * @param temperature the temperature.
     * @return the heat index. 
     */
    public static double calculateHeatIndex(double relativeHumidity, double temperature) {
        double heatIndex = simpleEquation(relativeHumidity, temperature);
        if (heatIndex >= 80.0) 
            return regressionEquation(relativeHumidity, temperature);
        else
            return heatIndex;
    }
    
    /**
     * Calculates the heat index in cases that the temperature is greater than 80 degrees.
     * @param relativeHumidity the relative humidity.
     * @param temperature the temperature.
     * @return the heat index.
     */
    private static double regressionEquation(double relativeHumidity, double temperature) {
        double temperatureSquared = temperature * temperature; //Save the temperature squared
        double relativeHumiditySquared = relativeHumidity * relativeHumidity; //Save the RH squared
        double heatIndex = 
                -42.379 
                + (2.04901523 * temperature) 
                + (10.14333127 * relativeHumidity)
                - (0.22475541 * temperature * relativeHumidity) 
                - (0.00683783 * temperatureSquared)
                - (0.05481717 * relativeHumiditySquared) 
                + (0.00122874 * temperatureSquared * relativeHumidity)
                + (0.00085282 * temperature * relativeHumiditySquared) 
                - (0.00000199 * temperatureSquared * relativeHumiditySquared);
        if ((relativeHumidity < 13) && (80 <= temperature) && (temperature <= 112)) { //if the relative humidity is less than 13% and temperature is between 80 and 112 degrees farenheit, make an adjustment. 
            double adjustment = ((13 - relativeHumidity) / 4) * Math.sqrt((17 - Math.abs(temperature - 95)) / 17);
            heatIndex -= adjustment;
        } else if ((relativeHumidity > 85) && (80 <= temperature) && (temperature <= 87)) { //if the relative humidity is greater than 85% and temp is between 80 and 87 degress, make an adjustment
            double adjustment = ((relativeHumidity - 85) / 10) * ((87 - temperature) / 5);
            heatIndex += adjustment;
        }
        return heatIndex;
    }
    
    /**
     * The simple equation for calculating the heat index. 
     * @param relativeHumidity the relative humidity. 
     * @param temperature the temperature.
     * @return the "simple" calculation of heat index.
     */
    private static double simpleEquation(double relativeHumidity, double temperature) {
        return 0.5 * (temperature + 61.0 + ((temperature - 68.0) * 1.2) + (relativeHumidity * 0.094));
    }
    
    @Override
    public void run() {
        //Initialization of thread outputs. 
        try {
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
        } catch (FileNotFoundException e){
            System.out.println("File not found --- Heat index sensor");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO Exeption --- Heat index sensor");
            e.printStackTrace();
        }
        //Code to run the thread. 
        TreeSet<DataPacket<Double>> calculatedHeatIndexes = calculateHeatIndex();
        //Add the calculated dew points to the output set. 
        for (DataPacket<Double> heatIndex : calculatedHeatIndexes) { 
            outputSet.add(heatIndex);
        }
        //Write calculated dew points to the output file. 
        try {
            oos.writeObject(calculatedHeatIndexes);
        } catch (IOException e) {
            System.out.println("Error writing the calculated heat indexes");
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