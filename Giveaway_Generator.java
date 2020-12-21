// Made by: Charlie Ringler

/*
 * This generator reads through a csv (comma-separated values) file that contains names and entries to the giveaway.
 * The names will be added to a list, the list will be shuffled, and 10 names will be selected at random.
 * The first name of the 10 selected is the winner, in the event that the first person is not eligible or does not want the Xbox,
 * we move on to the second name. This process repeats until an eligible and wanting person gets the Xbox.
 */

import java.io.File;
import java.util.Scanner;
import java.util.Map;
import java.util.Collections;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Giveaway_Generator {

	static boolean DEBUG = false; // Toggle for debug statements to be printed

	/*
	 * Prints the given List to the console in a human-readable format
	 */
	private static void printList(List<String> list) {
		System.out.print("[");
		for (int i = 0; i < list.size() - 1; i++) {
			System.out.print(list.get(i) + ", ");
		}
		System.out.println(list.get(list.size() - 1) + "]");
	}

	/*
	 * Prints the given array to the console in a human-readable format
	 */
	private static void printArray(String[] arr) {
//		System.out.print("[");
		for (int i = 0; i < arr.length - 1; i++) {
			System.out.println(arr[i] + ", ");
		}
		System.out.println(arr[arr.length - 1] + "");
	}

	/*
	 * Picks an entry from the list, removes all of its occurrences from the list,
	 * and returns it
	 */
	private static String grabEntry(List<String> entries) {
		Random r = new Random(); // Random number generator
		int index = r.nextInt(entries.size());	// Winner is chosen at random
		String winner = entries.get(index);

		/* 
		 * Remove instances of that winner from the list.
		 * The List.removeAll() method requires the use of the Collection interface
		 */
		Collection<String> c = new ArrayList<>();
		c.add(winner);
		entries.removeAll(c);

		return winner;
	}

	public static void main(String[] args) {
		try {
			// Variable and object declaration
			File entry_file = new File("Inputs/Entries.csv"); 	// csv input file
			Scanner file_reader = new Scanner(entry_file); 		// Scanner to read through the file
			Map<String, Integer> entry_count = new HashMap<>(); // Map containing <name, # of entries> pairs
			String[] winners = new String[10]; 					// 10 names are chosen and put into this array
			ArrayList<String> pool = new ArrayList<>(); 		// The pool of names, will be shuffled

			/* 
			 * Read through the csv file and parse the input
			 */
			file_reader.nextLine(); // Remove headers from csv file
			while (file_reader.hasNextLine()) {
				String line = file_reader.nextLine();
				String[] separated_values = line.split(",");	// Separate comma-separated values in string
				int num_entries = Integer.parseInt(separated_values[1]); // The second column in the csv file is the number of entries
				entry_count.put(separated_values[0], num_entries);
			}

			/*
			 * The names are added to the list. Each name is added the number of times that
			 * their entries determine.
			 * 1 entry = the name shows up once
			 * 2 entries = the name shows up twice
			 * 50 entries = the name shows up 50 times
			 * etc.
			 */
			for (Map.Entry<String, Integer> entry : entry_count.entrySet()) {
				for (int i = 0; i < entry.getValue(); i++) {
					pool.add(entry.getKey());
				}
			}
			if (DEBUG) {
				System.out.print("*DEBUG* initial list creation: ");
				printList(pool);
			}

			// Shuffle the list around
			Collections.shuffle(pool);
			if (DEBUG) {
				System.out.print("*DEBUG* post list shuffle: ");
				printList(pool);
			}

			/*
			 * 10 names are picked in order to maximize the chances that a person will
			 * receive the Xbox. If the first person picked isn't eligible or does not want
			 * the Xbox, we move down the list.
			 */
			for (int i = 0; i < winners.length; i++) {
				winners[i] = grabEntry(pool);
			}

			// Display the winner
			System.out.println("Winner list: ");
			printArray(winners);

			if (DEBUG) {
				System.out.print("*DEBUG* post name-grab (list will be shorter): ");
				printList(pool);
			}

			file_reader.close();
		} catch (Exception e) {
			System.err.println(e);
		}

	}

}
