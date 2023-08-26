package processor;


import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class OrdersProcessor {
	String filename;
	String multi;
	int num;
	String basefile;
	String outfile;
	public OrdersProcessor(String filename, String multi, int num, String basefile, String outfile) {
		this.filename = filename;
		this.multi = multi;
		this.num = num;
		this.basefile = basefile;
		this.outfile = outfile;
	}

	
	public static void main(String[] args) throws InterruptedException {

		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter item's data file name:");
		String filename = scanner.next();

		System.out.println("Enter 'y' for multiple threads, any other character otherwise:");
		String multi = scanner.next();

		System.out.println("Enter number of orders to process:");
		int num = scanner.nextInt();
		System.out.println("Enter order's base filename:");
		String basefile = scanner.next();

		System.out.println("Enter result's filename:");
		String outfile = scanner.next();
		scanner.close();
		long startTime = System.currentTimeMillis();
		TreeMap<Integer, TreeMap<String, Integer>> map = new TreeMap<Integer, TreeMap<String, Integer>>();
		Map<String, Double> items = new HashMap<String, Double>();
		Item product = new Item(items, filename);
		if (multi.equals("y")) {
			Thread[] threads = new Thread[num];

			for (int i = 1; i <= num; i++) {
				threads[i - 1] = new Thread(new Result(map, i, basefile));
				threads[i - 1].start();
			}
			for (int i = 1; i <= num; i++) {
				threads[i - 1].join();
			}

		} else {
			for (int i = 1; i <= num; i++) {
				Thread msg1Tread = new Thread(new Result(map, i, basefile));
				msg1Tread.run();
			}
		}
		product.items();
		String result = "";
		TreeMap<String, Integer> summary = new TreeMap<String, Integer>();
		ArrayList<Integer> sortedmap = new ArrayList<Integer>(map.keySet());
		Collections.sort(sortedmap);
		Map<String, Double> items1 = product.getmap();

		double summarytotal = 0;
		for (int i : sortedmap) {
			System.out.println("Reading order for client with id: "+i);
			result += "----- Order details for client with Id: " + i + " -----";
			result += "\n";
			TreeMap<String, Integer> item = map.get(i);
			ArrayList<String> sorteditems = new ArrayList<String>(item.keySet());
			Collections.sort(sorteditems);
			double sum1 = 0;
			for (String s : sorteditems) {
				Double d = Double.valueOf(item.get(s));
				result += "Item's name: " + s + ", " + "Cost per item: "
						+ NumberFormat.getCurrencyInstance().format(items1.get(s)) + ", " + "Quantity: " + item.get(s)
						+ ", " + "Cost: " + NumberFormat.getCurrencyInstance().format(d * items1.get(s));
				result += "\n";
				sum1 += d * items1.get(s);
				if (summary.containsKey(s)) {
					summary.put(s, summary.get(s) + item.get(s));
				} else if (summary.containsKey(s) == false) {
					summary.put(s, item.get(s));
				}
			}
			summarytotal += sum1;
			result += "Order Total: " + NumberFormat.getCurrencyInstance().format(sum1);
			result += "\n";
		}
		result += "***** Summary of all orders *****";
		result += "\n";
		ArrayList<String> sortedsummary = new ArrayList<String>(summary.keySet());
		Collections.sort(sortedsummary);
		for (String item : sortedsummary) {
			double quantity = Double.valueOf(summary.get(item));
			result += "Summary - Item's name: " + item + ", " + "Cost per item: "
					+ NumberFormat.getCurrencyInstance().format(items1.get(item)) + ", " + "Number sold: "
					+ summary.get(item) + ", " + "Item's Total: "
					+ NumberFormat.getCurrencyInstance().format(quantity * items1.get(item));
			result += "\n";
		}
		result += "Summary Grand Total: " + NumberFormat.getCurrencyInstance().format(summarytotal);
		result += "\n";
		try {
			FileWriter fw = new FileWriter(outfile);

			fw.write(result);

			fw.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Processing time (msec): " + (endTime - startTime));
		System.out.println("Results can be found in the file: " + outfile);
	}

}