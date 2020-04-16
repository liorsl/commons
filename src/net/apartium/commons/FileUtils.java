package net.apartium.commons;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.apartium.commons.ExceptionHandler;
import net.apartium.commons.StringUtils;
import net.apartium.commons.Validate;

public class FileUtils {

	/**
	 * Unzip a zip to a specified directory
	 *
	 * @param pathToZip
	 *            the zip file
	 * @param destinationPath
	 *            the directory to extract the zip file to
	 */
	public static void unzip(String pathToZip, String destinationPath) {
		byte[] byteBuffer = new byte[1024];

		try {
			new File(destinationPath).mkdirs();
			ZipInputStream inZip = new ZipInputStream(new FileInputStream(pathToZip));
			ZipEntry inZipEntry = inZip.getNextEntry();
			while (inZipEntry != null) {
				String fileName = inZipEntry.getName();
				File unZippedFile = new File(StringUtils.complete(destinationPath, File.separator) + fileName);
				if (inZipEntry.isDirectory()) {
					unZippedFile.mkdirs();
				} else {
					unZippedFile.getParentFile().mkdirs();
					unZippedFile.createNewFile();
					FileOutputStream unZippedFileOutputStream = new FileOutputStream(unZippedFile);
					int length;
					while ((length = inZip.read(byteBuffer)) > 0) {
						unZippedFileOutputStream.write(byteBuffer, 0, length);
					}
					unZippedFileOutputStream.close();
				}
				inZipEntry = inZip.getNextEntry();
			}
			// inZipEntry.close();
			inZip.close();
			System.out.println("Finished Unzipping");
		} catch (IOException e) {
			ExceptionHandler.getInstance().handle(e);
		}
	}

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
