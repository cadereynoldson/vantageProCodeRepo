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

import console.Console;

import java.util.concurrent.ScheduledExecutorService;

import sensors.RainSensor;
import sensors.RainfallRate;
import sensors.TemperatureSensor;
import sensors.WindSensor;
import toDelete.RainDataPacket;

// https://dev4devs.com/2016/06/21/java-how-to-create-threads-that-return-values/
// https://www.baeldung.com/java-runnable-callable

public class Controller {

	private static int rainSensorUpdateInterval = 1; // temporary value for now
	private static int rainfallRateUpdateInterval = 2; // temporary value for now
	public static TreeSet<DataPacket> rainSet = new TreeSet<DataPacket>();
	public static TreeSet<DataPacket> rainRateSet = new TreeSet<DataPacket>();

	public static File f;
	public static FileOutputStream fos;
	public static ObjectOutputStream oos;
	public static Console con = new Console();

	public static void main(String[] args) throws Exception {

		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
		TemperatureSensor temp = new TemperatureSensor();
		WindSensor wind = new WindSensor(30);
		RainSensor rain = new RainSensor();
		RainfallRate rainfallRate = new RainfallRate();

		//scheduledExecutorService.scheduleAtFixedRate(temp, 0, 3, TimeUnit.SECONDS);
		//scheduledExecutorService.scheduleAtFixedRate(wind, 0, 1, TimeUnit.SECONDS);
			
		scheduledExecutorService.scheduleAtFixedRate(rain, 0, 
				rainSensorUpdateInterval, TimeUnit.SECONDS);
		scheduledExecutorService.scheduleAtFixedRate(rainfallRate, 0, 
				rainfallRateUpdateInterval, TimeUnit.SECONDS);
	}
}
