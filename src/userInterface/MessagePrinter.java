package userInterface;

import java.util.HashMap;
import motor.relation.Row;
import motor.relation.Relation;

/**
 * Prints user messages
 *
 * @author eddycastro
 */
public class MessagePrinter {

	private static int idNextPrinter = 0;
	private static HashMap<Integer, Printer> printers = new HashMap<>();

	/**
	 * Prints the message to the user
	 *
	 * @param message Message to print
	 */
	public static void printUserMessage(String message) {
		if (printers.isEmpty()) {
			System.out.println(message);
		} else {
			for (Printer printer : printers.values()) {
				printer.printMessage(message);
			}
		}
	}

	/**
	 * Prints an error message to the user
	 *
	 * @param message
	 */
	public static void printErrorMessage(String message) {
		if (printers.isEmpty()) {
			System.err.println(message);
		} else {
			for (Printer printer : printers.values()) {
				printer.printError(message);
			}
		}
	}

	/**
	 * Prints a relation
	 *
	 * @param relation Relation to print
	 */
	public static void printRelation(Relation relation) {
		if (printers.isEmpty()) {
			for (Row row : relation) {
				System.out.println(row);
			}
		} else {
			for (Printer printer : printers.values()) {
				printer.printRelation(relation);
			}
		}
	}

	/**
	 * Gets a user confirmation
	 *
	 * @param message
	 * @return
	 */
	public static boolean getConfirmation(String message) {
		if (printers.isEmpty()) {
			return true;
		} else {
			for (Printer printer : printers.values()) {
				if (printer.getConfirmation(message) != true) {
					return false;
				}
			}

			return true;
		}
	}

	/**
	 * Registers a new printer.
	 *
	 * @param printer
	 */
	public static int registerPrinter(Printer printer) {
		printers.put(idNextPrinter, printer);
		return idNextPrinter++;
	}

	/**
	 * Eliminates a printer with the given index
	 *
	 * @param printerId ID of the printer to remove
	 */
	public static boolean deletePrinter(int printerId) {
		if (printers.containsKey(printerId)) {
			printers.remove(printerId);
			return true;
		}
		return false;
	}
}
