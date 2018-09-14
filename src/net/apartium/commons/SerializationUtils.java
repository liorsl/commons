package net.apartium.commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public class SerializationUtils {

	public static Object fromString(String string) {
		try {
			byte[] data = Base64.getDecoder().decode(string);
			ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(data));
			Object object = inputStream.readObject();
			inputStream.close();
			return object;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	public static String toString(Serializable serializable) {
		Validate.notNull(serializable, "serializable +-");
		
		try {
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        ObjectOutputStream objectOutput = new ObjectOutputStream(outputStream);
	        objectOutput.writeObject(serializable);
	        objectOutput.close();
	        return Base64.getEncoder().encodeToString(outputStream.toByteArray()); 
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
