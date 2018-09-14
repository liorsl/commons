package net.apartium.commons.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.apartium.commons.Validate;

public class FileUtils {

	public static void write(File file, String content) {
		Validate.notNull(content, "content +-");

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<String> read(File file) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			List<String> list = new ArrayList<>();
			list.add(reader.readLine());
			reader.close();
			return list;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void createIfDoesNotExist(File...files) {
		Arrays.asList(files).forEach((f) -> {if (!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}});
		
	}

}
