package motor;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import userInterface.MessagePrinter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;

/**
 * Saves the initial configuration of every project
 *
 * @author Jorge
 */
public class Configuration {

	// Keys for global data
	public static final String DIRECTORY_DATABASE = "DATABASE_DIRECTORY";

	/**
	 * Singleton instance
	 */
	private static Configuration singleton;
	private static final String filename = "config.ini";

	/**
	 * Static constructor
	 */
	static {
		singleton = new Configuration();

		// Reads data in configuration file
		try {
			Gson gson = new Gson();
			String jsonData = FileUtils.readFileToString(new File(filename), Charset.forName("UTF-8"));

			java.lang.reflect.Type dataType = new TypeToken<HashMap<String, String>>() {
			}.getType();
			singleton.data = (HashMap<String, String>) gson.fromJson(jsonData, dataType);

		} catch (IOException | JsonIOException | JsonSyntaxException ex) {
			// Creates the ini file with default values
			singleton.data = new HashMap<>();

			// Adds all keys
			File directorioTrabajo = new File(System.getProperty("user.dir"));

			/* Database directory */
			File directorioBaseDatos = new File(directorioTrabajo, "DBMSData");
			singleton.data.put(DIRECTORY_DATABASE, directorioBaseDatos.getAbsolutePath());
			singleton.saveChanges();
		}
	}

	/**
	 * Adds a value to the configuration
	 *
	 * @param key Key to find the data
	 * @param value Value to store
	 */
	public static void addValue(String key, String value) {
		singleton.data.put(key, value);
		singleton.saveChanges();
	}

	/**
	 * Returns value associated to a key
	 *
	 * @param key Key to retrieve
	 * @return String string with the value, or null
	 */
	public static String getValue(String key) {
		if (singleton.data.containsKey(key)) {
			return singleton.data.get(key);
		} else {
			return null;
		}
	}

	/**
	 * Saves changes
	 */
	private boolean saveChanges() {
		Gson gson = new Gson();

		try {
			String jsonData = gson.toJson(data);
			FileUtils.writeStringToFile(new File(filename), jsonData);

		} catch (IOException iOException) {
			MessagePrinter.printErrorMessage("Error while saving configuration: " + iOException.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Saved data
	 */
	private HashMap<String, String> data;

	private Configuration() {
		this.data = new HashMap<>();
	}
}
