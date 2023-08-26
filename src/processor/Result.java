package processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

public class Result implements Runnable {
	TreeMap<Integer,TreeMap<String, Integer>> map;
	int order;
	String basefile;
	public Result(TreeMap<Integer,TreeMap<String, Integer>> map,int order,String basefile) {
		this.map=map;
		this.order=order;
		this.basefile=basefile;
	}
	
	public void add(int num,TreeMap<String, Integer> map1) {
		map.put(num, map1);
	}
	public TreeMap<Integer,TreeMap<String, Integer>> getmap(){
		return map;
	}

	public void process(int order, String basefile) {
		TreeMap<String, Integer> count1 = new TreeMap<String, Integer>();
		String answer = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(basefile + order + ".txt"));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] arr2 = line.split(" ");
				if (arr2[0].equals("ClientId:")) {
					answer = arr2[1];
				}

				else if (arr2[0].equals("ClientId:") == false) {
					if (count1.containsKey(arr2[0])) {
						count1.put(arr2[0], count1.get(arr2[0]) + 1);
					} else if (count1.containsKey(arr2[0]) == false) {
						count1.put(arr2[0], 1);

					}
				}

			}
			bufferedReader.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		synchronized (map) {
			map.put(Integer.valueOf(answer), count1);
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		process(order,basefile);
	}
}
