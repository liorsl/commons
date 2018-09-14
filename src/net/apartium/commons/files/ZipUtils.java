package net.apartium.commons.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.apartium.commons.ExceptionHandler;
import net.apartium.commons.StringUtils;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class ZipUtils {

	private ZipUtils() {}
	
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
}
