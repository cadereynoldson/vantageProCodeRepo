package console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeSet;

import controller.Controller;
import controller.DataPacket;
import sensors.RainSensor;

// Display Information from Controller, through serialized connection.
// could include a GUI if we get to that.
public class Console {

	public <T> void readSerializedData(File f) throws Exception {
		//clearScreen();
		FileInputStream fis = new FileInputStream(f);
		ObjectInputStream ois = new ObjectInputStream(fis);
		//Save obj1 = (Save) ois.readObject();

		 TreeSet<DataPacket<T>> obj1 = (TreeSet<DataPacket<T>>) ois.readObject();

		// get a DataPacket from the set, used for the println statement.
		 DataPacket<T> dp = obj1.iterator().next(); 
		System.out.println("serialized " + dp.getMeasurement() + " data is: " + obj1.toString());
	    ois.close();
	}
	
	
	public static void clearScreen() {  
		//System.out.println(new String(new char[50]).replace("\0", "\r\n"));
	}
}
