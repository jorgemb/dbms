package motor;

import exceptions.TableException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import static motor.DataType.CHAR;
import static motor.DataType.DATE;
import static motor.DataType.FLOAT;
import static motor.DataType.INT;
import static motor.DataType.NULL;

/**
 * Represents any value
 *
 * @author Jorge
 */
public class Data implements java.io.Serializable, Comparable<Data> {

	private Object value;
	private DataType type;

	/**
	 * Defines date format
	 */
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	static {
		dateFormat.setLenient(false);
	}

	/**
	 * Constructor
	 *
	 * @param value Saved value
	 */
	public Data(Object value) {
		this.value = value;

		// Verifies special case double vs float
		if (value instanceof Double) {
			this.value = ((Number) value).floatValue();
		}

		this.type = getTypes();
	}

	/**
	 * Constructor with cast
	 *
	 * @param value
	 * @param expectedType
	 */
	Data(Object value, DataType expectedType) {
		this.value = value;
		this.type = expectedType;
	}

	public Object getValue() {
		return value;
	}

	public final DataType getTypes() throws TableException {
		// Checks if type has already been determined
		if (type != null) {
			return type;
		}

		// Determines data type
		if (value == null) {
			return DataType.NULL;
		}

		if (value instanceof Integer) {
			return DataType.INT;
		} else if (value instanceof Float || value instanceof Double) {
			return DataType.FLOAT;
		} else if (value instanceof String) {
			try {
				String stringValue = (String) value;
				int dashes = stringValue.length() - stringValue.replace("-", "").length();
				if (dashes == 2) {
					dateFormat.parse((String) value);
					return DataType.DATE;
				} else {
					return DataType.CHAR;
				}
			} catch (ParseException ex) {
				throw new TableException(TableException.ErrorType.InvalidData,
					String.format("Invalid format for date %s (%s)..",
						value, ex.getMessage()));
			}
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return "Datum{" + "value=" + value + " type=" + getTypes().name() + '}';
	}

	/**
	 * String representation
	 */
	public String representation() {
		switch (getTypes()) {
			case INT:
				return Integer.toString((Integer) value);
			case FLOAT:
				return Float.toString((Float) value);
			case CHAR:
				return String.format("\'%s\'", value);
			case DATE:
				return String.format("f\'%s\'", value);
			case NULL:
				return "NULL";
			default:
				throw new AssertionError();
		}
	}

	/**
	 * Returns default value for type.
	 *
	 * @param type Data type
	 * @return
	 */
	public static Data getDefaultValue(DataType type) {
		switch (type) {
			case INT:
				return new Data(new Integer(0), INT);
			case FLOAT:
				return new Data(new Float(0.0f), FLOAT);
			case CHAR:
				return new Data("", CHAR);
			case DATE:
				return new Data("1-1-1970", DATE);  // Default date
			case NULL:
				return new Data(null, NULL);
			default:
				throw new AssertionError();
		}
	}

	/**
	 * Tries to convert data to date
	 *
	 * @param datum
	 * @return
	 */
	public static Date getDate(Data datum) {
		if (datum.getTypes() != DataType.DATE) {
			throw new TableException(TableException.ErrorType.InvalidData, "Cannot convert value to date.");
		}
		try {
			Date d = dateFormat.parse((String) datum.value);
			return d;
		} catch (ParseException ex) {
			throw new TableException(TableException.ErrorType.InvalidData, "Cannot convert value to date.");
		}
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 61 * hash + Objects.hashCode(this.value);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Data other = (Data) obj;

		// Check null
		if (this.value == null && other.value == null) {
			return true;
		}

		if (!Objects.equals(this.value, other.value)) {
			return false;
		}
		return true;
	}

	/**
	 * Compares two values
	 *
	 * @param t
	 * @return
	 */
	@Override
	public int compareTo(Data t) {
		DataType leftType = this.getTypes();
		DataType rightType = t.getTypes();

		// NULL case
		if (leftType == NULL || rightType == NULL) {
			if (leftType == rightType) {
				return 0;
			} else if (leftType == NULL) {
				return -1;
			} else {
				return 1;
			}
		}

		if (leftType != rightType) {
			throw new TableException(TableException.ErrorType.InvalidData,
				String.format("Fields of type %s and %s cannot be compared.", leftType.name(), rightType.name()));
		}

		switch (leftType) {
			case INT:
				return Integer.compare((Integer) this.value, (Integer) t.value);
			case FLOAT:
				return Float.compare((Float) this.value, (Float) t.value);
			case CHAR:
				return ((String) this.value).compareTo((String) t.value);
			case DATE: {
				try {
					Date leftDate = dateFormat.parse((String) this.value);
					Date rightDate = dateFormat.parse((String) t.value);
					return leftDate.compareTo(rightDate);
				} catch (ParseException ex) {
					throw new TableException(TableException.ErrorType.InvalidData, "Date cannot be parsed: " + ex.getMessage());
				}
			}
			default:
				throw new AssertionError();
		}
	}

	/**
	 * Converts a data to another type.
	 *
	 * @param targetType Target type
	 * @return New data
	 * @throws TableException Si la conversión no es posible.
	 */
	public Data convertTo(DataType targetType) throws TableException {
		// Checks if same type
		if (this.getTypes() == targetType) {
			return new Data(this.value);
		}

		// Checks NULL
		if (targetType == NULL) {
			return new Data(null);
		}

		// Checks current is NULL
		if (this.getTypes() == NULL) {
			throw new TableException(TableException.ErrorType.InvalidData,
				"No se puede convertir el dato de NULL a " + targetType.name());
		}

		// Check conversion to CHAR
		if (targetType == CHAR) {
			return new Data(this.value.toString(), CHAR);
		}

		// Conversion from CHAR
		try {
			if (this.getTypes() == CHAR) {
				switch (targetType) {
					case INT:
						return new Data(Integer.parseInt((String) this.value));
					case FLOAT:
						return new Data(Float.parseFloat((String) this.value));
					default:
						throw new TableException(TableException.ErrorType.InvalidData,
							"Cannot convert CHAR value to " + targetType.name());
				}
			}
		} catch (TableException | NumberFormatException ex) {
			throw new TableException(TableException.ErrorType.InvalidData,
				String.format("Cannot conver CHAR value to %s (%s).", targetType.name(), ex.getMessage()));
		}

		// INT to other
		if (this.getTypes() == INT && targetType == FLOAT) {
			return new Data(((Number) this.value).floatValue(), FLOAT);
		}

		// Convert from FLOAT
		if (this.getTypes() == FLOAT && targetType == INT) {
			return new Data(((Number) this.value).intValue(), INT);
		}

		// Unsupported conversion
		throw new TableException(TableException.ErrorType.InvalidData,
			String.format("Cannot convert value from %s to %s.",
				this.getTypes().name(), targetType.name()));
	}
}
