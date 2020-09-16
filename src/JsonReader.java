import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public interface JsonReader {
	
	final String jsonFolderPath = "Data\\";

	default JsonObject getJson(String fileName) {
		return new Gson()
				.fromJson(getFileContents(fileName), JsonObject.class)
				.getAsJsonObject("content");
	}
	
	default String getFileContents(String fileName) {
		String filePath = jsonFolderPath + fileName + ".json";
		BufferedReader fileReader;
		String fileContents = "";
		try {
			fileReader = new BufferedReader(new FileReader(filePath));
			while (true) {
				try {
					String line = fileReader.readLine();
					if (line == null) break; // Break on EOF
					fileContents += line;
				} catch (IOException e) {
					System.err.println("No file was found at path " + filePath);
					System.exit(1);
				}
			}
			try {
				fileReader.close();
			} catch (IOException e) {
				System.err.println("File reader could not be closed for path " + filePath);
				System.exit(1);
			}
		} catch (FileNotFoundException e1) {
			System.err.println("No file was found at path " + filePath);
			System.exit(1);
		}
		return fileContents;
	}
	
	default String getName(int id) {
		JsonObject idsJson = getJson("ObjectInformation");
		return idsJson
				.get("" + id)
				.toString()
				.split("/")
				[0];
	}

}
