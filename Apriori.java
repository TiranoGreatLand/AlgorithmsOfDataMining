import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Apriori {

	public class icode{
		int len;
		int[] ics;
		public icode(int l) {
			len = l;
			ics = new int[l];
		}
	};	
	
	public class iset {
		int[] sets;
		int num;
		public iset(int[] ts, int tn) {
			sets = ts;
			num = tn;
		}
	}
//	public ArrayList<ArrayList<String>> ItemSets = new ArrayList<>();
	public Map<String, Integer> itemMap = new HashMap<>();
	public Map<String, Integer> itemnums = new HashMap<>();
	public ArrayList<icode> ItemCode = new ArrayList<>();
	public ArrayList<iset> singleItems = new ArrayList<>();
	public ArrayList<ArrayList<iset>> allsets = new ArrayList<>();
	public int threshold;
	public int maxCode;
	// first step
	public void dataInput() {
		Scanner scanner = new Scanner(System.in);
		String tmpTID = "";
		String[] tmpiss = {};
		int tmpl, i, cnt;
		String tmps = "";
		cnt = 0;
		System.out.println("PLZ input the datas, every itemset per line");
		while((tmpTID=scanner.nextLine())!=null) {
			tmpiss = tmpTID.split(",");
			tmpl = tmpiss.length;
			icode tmpic = new icode(tmpl);
			for(i=0;i<tmpl;i++) {
				tmps = tmpiss[i];
				if(!itemMap.containsKey(tmps)) {
					itemMap.put(tmps, cnt++);
					itemnums.put(tmps, 1);
				}
				else {
					itemnums.put(tmps, itemnums.get(tmps)+1);
				}
				tmpic.ics[i] = itemMap.get(tmps);
			}
			Arrays.sort(tmpic.ics);
			ItemCode.add(tmpic);
		}
		System.out.println("PLZ input the threshold");
		threshold = scanner.nextInt();
		scanner.close();		
	}
	
	//second step
	@SuppressWarnings("unchecked")
	public void itemsetchoice() {
		for(String s: itemMap.keySet()) {
			if(itemMap.get(s)>=threshold) {
				int[] ts = {itemnums.get(s)};
				iset tmpi = new iset(ts, 1);
				singleItems.add(tmpi);
			}
		}
		Collections.sort(singleItems, new SortByFN());
		maxCode = singleItems.get(singleItems.size()-1).sets[0];
		allsets.add(singleItems);	
	}
	
	@SuppressWarnings("rawtypes")
	class SortByFN implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			// TODO Auto-generated method stub
			iset i1 = (iset)o1;
			iset i2 = (iset)o2;
			if(i1.sets[0]<i2.sets[0]) return -1;
			return 1;
		}
		
	}
	
	// third step to n step till select a 
	public void itemsetchoice(int n) {
		ArrayList<iset> tmpis =new ArrayList<>();
		ArrayList<iset> lastis = allsets.get(allsets.size()-1);
		int len = lastis.size();
		for(int i=0;i<len;i++) {
			
		}
	}
	
}
