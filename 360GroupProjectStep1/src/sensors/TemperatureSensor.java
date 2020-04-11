package sensors;

import controller.Controller;

// https://stackoverflow.com/questions/2444019/how-do-i-generate-a-random-integer-between-min-and-max-in-java

// updateInterval: 60 seconds

public class TemperatureSensor extends AbstractSensor implements Runnable {
		private String sensorName = "Temperature Sensor";
		private static final int maxTemp = 150; // 150F
		private static final int minTemp = -40; // -40F
		private int updateInterval = 10; // 10 seconds

		public int getTemp() {
			int sleepTime = 60 * 1000;
			
			long unixTime = System.currentTimeMillis() / 1000L;
			
			System.out.println("unixtime is " + unixTime);
			//return 10;
			//Random rand = new Random();
			int randomNumber = rand.nextInt(maxTemp + 1 - minTemp) + minTemp; // [minTemp, maxTemp]
			return randomNumber;
		}
		
		public void setUpdateInterval(int updateInterval) {
			if (updateInterval < 10) {
				throw new IllegalArgumentException("Temp Sensor Update Interval < 10 sec");
			} else if (updateInterval > 12) {
				throw new IllegalArgumentException("Temp Sensor Uptdae Interval > 12 sec");
			} else {
				this.updateInterval = updateInterval;
			}
		}
		
		public void run() {
			long unixTime = System.currentTimeMillis() / 1000L;
			Controller.tempMap.put(unixTime, "tempVal");
			System.out.println(unixTime + " This is the temp sensor updating every 3 seconds");
		}
}
