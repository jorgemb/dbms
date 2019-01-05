package JSON;

/**
 *
 * @author Eddy
 */
public class Table {

	private boolean Error;
	private String Type;
	private Schema Schema;
	private Object[][] Data;

	public boolean isError() {
		return Error;
	}

	public String getType() {
		return Type;
	}

	public Object[][] obtenerDatos() {
		return Data;
	}

	public Schema getSchema() {
		return Schema;
	}

	public class Schema {

		private String[] Names;
		private String[] Types;

		public String[] getNames() {
			return Names;
		}

		public String[] getTypes() {
			return Types;
		}

	}

}
