package processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;


public class Item {
	Map<String, Double> items;
	String filename;
	public Item(Map<String, Double> items,String filename) {
		this.items=items;
		this.filename=filename;
	}
	
	public void add(String item,Double price) {
		items.put(item, price);
	}
	public Map<String, Double> getmap(){
		return items;
	}
	public void items() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
			String line;

			while ((line = bufferedReader.readLine()) != null) { /* null marks the end of file */
				/* We need println as readLine removes the newline */
				String[] arr1 = line.split(" ");
				double price = Double.parseDouble(arr1[1]);
				items.put(arr1[0], price);
			}
			bufferedReader.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
