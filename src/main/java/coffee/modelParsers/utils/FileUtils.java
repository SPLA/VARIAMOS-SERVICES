package coffee.modelParsers.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	/**
	 * Reads the files from a directory to create a list of File objects
	 * 
	 * @param directoryPath: path from the directory where the files are
	 * @return List of File objects that represents the files of the directory
	 */
	public static List<File> readFileFromDirectory(String directoryPath) {
		File Dir = new File(directoryPath);
		List<File> fileList = new ArrayList<File>();
		File[] lista_Archivos = Dir.listFiles();
		if (lista_Archivos != null) {
			for (int i = 0; i < lista_Archivos.length; i++) {
				if (lista_Archivos[i].isFile()) {
					fileList.add(lista_Archivos[i]);
				}

			}
		}
		return fileList;

	}

	/**
	 * Creates a Hlvl code file at the location given which content is the string given
	 * 
	 * @param program: String that contains the Hlvl code
	 * @param path: location where the Hlvl code file should be created
	 */
	public static void writeHLVLProgram(String path, String program) {
		try {
			System.out.println("escribiendo en archivo: "+path);
			FileWriter fw = new FileWriter(path);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter salida = new PrintWriter(bw);
			salida.println(program);
			salida.close();
			bw.close();
			fw.close();
		} catch (java.io.IOException ioex) {
			System.out.println("Error ocurred: " + ioex.toString());
		}
	}
}
