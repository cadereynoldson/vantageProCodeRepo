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
import toDelete.RainDataPacket;
import toDelete.Save;

// Display Information from Controller, through serialized connection.
// could include a GUI if we get to that.
public class Console {

	public void readSerializedData(File f) throws Exception {
		FileInputStream fis = new FileInputStream(f);
		ObjectInputStream ois = new ObjectInputStream(fis);
		//Save obj1 = (Save) ois.readObject();
		 TreeSet<DataPacket> obj1 = (TreeSet<DataPacket>) ois.readObject();

		// get a DataPacket from the set, used for the println statement.
		 DataPacket dp = obj1.iterator().next(); 
		System.out.println("serialized " + dp.getMeasurement() + " data is: " + obj1.toString());
	    ois.close();
	}
}
