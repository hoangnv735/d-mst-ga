package ga;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Util {
	public static String dataFolder = "./data/";

	public static File[] listFile(String folder) {
		File _folder = new File(folder);
		File[] files = _folder.listFiles();
		return files;
	}	
	
	public static Data readFile(String filename) {
		File file = new File(dataFolder + filename);
		Data data = new Data();
		BufferedReader br = null;
        String curLine = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        try {
			// skip description
			for (int i = 0; i < 6; i++) {
				br.readLine();
			}
			while (true) {
				curLine = br.readLine();
				String[] str = curLine.split(" ");
				if (str[0].startsWith("E")) {
					break;
				}
				int id = Integer.parseInt(str[0])-1;
				int x = Integer.parseInt(str[1])-1;
				int y = Integer.parseInt(str[2])-1;
				Node city = new Node(id, x, y);
				data.nodes.add(city);
			}
			data.n = data.nodes.size();
        } catch (IOException ex) {
        	ex.printStackTrace();
        }      
        try {
            br.close();
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
        return data;
	}
	
	public static void printData(Data data) {
		System.out.println("Data Info");
		System.out.println("Number of nodes: " + data.n);
	}
	
	public static Data[] readManyFiles(String[] listFilenames) {
		Data[] data = new Data[listFilenames.length];
		for (int i = 0; i < listFilenames.length; i++) {
			data[i] = readFile(listFilenames[i]);
//			printData(data[i]);
		}
		return data;
	}
}
