package controller;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import calculations.RainfallRate;
import console.Console;

import java.util.concurrent.ScheduledExecutorService;

import sensors.RainSensor;
import sensors.TemperatureSensor;
import sensors.WindSensor;
import toDelete.RainDataPacket;

// https://dev4devs.com/2016/06/21/java-how-to-create-threads-that-return-values/
// https://www.baeldung.com/java-runnable-callable

public class Controller {

	public static final int rainSensorUpdateInterval = 20; // 20 to 24 seconds
	public static final int rainfallRateUpdateInterval = 20; // 20 to 24
	public static final int tempSensorUpdateInterval = 10; // 10 to 12
	public static final int windChillUpdateInterval = 10; // 10 to 12
	public static final int windDirectionUpdateInterval = 3; // 2.5 to 3
	public static final int windSpeedUpdateInterval = 3; // 2.5 to 3
	public static final int heatIndexUpdateInterval = 10; // 10 to 12
	public static final int humiditySensorUpdateInterval = 10; 
	
	public static final int rateInitialDelay = 5; // 10 seconds



	public static final TreeSet<DataPacket<Double>> rainfallSet = new TreeSet<DataPacket<Double>>();
	public static final TreeSet<DataPacket<Double>> rainRateSet = new TreeSet<DataPacket<Double>>();
	public static final TreeSet<DataPacket<Double>> temperatureSet = new TreeSet<DataPacket<Double>>();
	public static final TreeSet<DataPacket<Integer>> humiditySet = new TreeSet<DataPacket<Integer>>();
	public static final TreeSet<DataPacket<Integer>> windChillSet = new TreeSet<DataPacket<Integer>>();
	public static final TreeSet<DataPacket<Integer>> windDirectionSet = new TreeSet<DataPacket<Integer>>();
	public static final TreeSet<DataPacket<Integer>> windSpeedSet = new TreeSet<DataPacket<Integer>>();
	
	public static File rainfallFile = new File("rainfallSerializedOutput.txt");
	public static File rainRateFile = new File("rainfallRateSerializedOutput.txt");
	public static File temperatureFile = new File("temperatureSerializedOutput.txt");
	public static File humidityFile = new File("humiditySerializedOutput.txt");
	public static File windChillFile = new File("windChillSerializedOutput.txt");
	public static File windDirectionFile = new File("windDirectionSerializedOutput.txt");
	public static File windSpeedFile = new File("windSpeedSerializedOutput.txt");

	public static Console con = new Console();

	public static void main(String[] args) throws Exception {

		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//		TemperatureSensor temp = new TemperatureSensor();
		WindSensor windSpeed = new WindSensor(windSpeedSet, windSpeedFile, 30);
		RainSensor rain = new RainSensor(rainfallSet, rainfallFile);
		RainfallRate rainfallRate = new RainfallRate(rainRateSet, rainRateFile, rainfallSet);
		
		scheduledExecutorService.scheduleAtFixedRate(windSpeed, 0, 
				windSpeedUpdateInterval, TimeUnit.SECONDS);
		
		
		scheduledExecutorService.scheduleAtFixedRate(rain, 0, 
				rainSensorUpdateInterval, TimeUnit.SECONDS);
		
		
		
		// Rates:
	//ZonedDateTime eventTime = ZonedDateTime.now();

//		DataPacket<Double> rdp = new DataPacket<Double>(eventTime, "rain", "0", 30.1);
//		rainfallSet.add(rdp);
//		
//		eventTime = ZonedDateTime.now();
//		
//		DataPacket<Double> rdp2 = new DataPacket<Double>(eventTime, "rain", "0", 20.0);
//		rainfallSet.add(rdp2);
		
		scheduledExecutorService.scheduleAtFixedRate(rainfallRate, rateInitialDelay, 
				rainfallRateUpdateInterval, TimeUnit.SECONDS);
	}
}
