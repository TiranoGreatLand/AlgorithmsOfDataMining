import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

//It is the two-phase

public class FHUIM {

	public static int ti;
	public int numofitem;
	public int[] values;
	public int[][] table;
	public int coefficient;
	public static int[] tus;
	public void dataInput() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("PLZ input the number of items");
		numofitem = scanner.nextInt();
		System.out.println("PLZ input the number of TIDs");
		ti = scanner.nextInt();
		values = new int[numofitem];
		table = new int[ti][numofitem];
		tus = new int[ti];
		System.out.println("PLZ input the data of table");
		for(int i=0;i<ti; i++) {
			for(int j=0;j<numofitem;j++) {
				table[i][j] = scanner.nextInt();
			}
		}
		System.out.println("PLZ input the values of each item");
		for(int i=0;i<numofitem;i++) {
			values[i] = scanner.nextInt();
		}
		System.out.println("PLZ input the coefficient");
		coefficient = scanner.nextInt();
		scanner.close();
		ArrayList<itemSupport> ais = new ArrayList<>();
		for(int i=0;i<numofitem;i++) {
			itemSupport ist = new itemSupport();
			ist.items.add(i);
			ais.add(ist);
		}
		for(int i=0;i<ti;i++) {
			for(int j=0;j<numofitem; j++) {
				if(table[i][j]!=0) {
					tus[i] = table[i][j]*values[j];
					ais.get(j).u += (table[i][j]*values[j]);
					ais.get(j).tids.add(i);
				}
			}
		}
		HashMap<ArrayList<Integer>,itemSupport> tHashMap = new HashMap<>();
		for(itemSupport i : ais) {
			tHashMap.put(i.items, i);
		}
		dataTable.add(tHashMap);
		
		for(int i=0;i<ti;i++) {
			tus[i] = 0;
			for(int j=0;j<numofitem;j++) {
				tus[i] += (table[i][j]*values[j]);
			}
		}
	}
	
	public class itemSupport {
		ArrayList<Integer> items = new ArrayList<>();
		int u=0;
		int sup=0;
		int tu=0;
		ArrayList<Integer> tids = new ArrayList<>();
		ArrayList<itemSupport> sups = new ArrayList<>();
		ArrayList<itemSupport> subs = new ArrayList<>();
	}
	public ArrayList<HashMap<ArrayList<Integer>,itemSupport>> dataTable = new ArrayList<>();
	
	public void TableMaker(int step) {
		HashMap<ArrayList<Integer>,itemSupport> tmpais = dataTable.get(step);
		HashMap<ArrayList<Integer>,itemSupport> newadd = new HashMap<>();
		ArrayList<ArrayList<Integer>> keys = new ArrayList<>();
		keys.addAll(tmpais.keySet());
		int l = keys.size();
		if(l<(step+2) || l==1) return ;
		for(int i=0;i<l;i++) {
			for(int j=i+1;j<l;j++) {
				ArrayList<Integer> a1 = keys.get(i), a2 = keys.get(j);
				ArrayList<Integer> ret = oneNear(a1, a2);
				if(ret.size()>0) {
					if(!newadd.containsKey(ret)) {
						itemSupport tis = new itemSupport();
						ArrayList<Integer> indexs = interaction(tmpais.get(a1).tids, tmpais.get(a2).tids);
						if(indexs.size()>0) {
							tis.items.addAll(ret);
							tis.tids = indexs;
							nodeCom(tis);
							nodeAs(tis, ret, tmpais);
							newadd.put(ret, tis);
						}
					}
				}
			}
		}
		if(newadd.size()>0) {
			dataTable.add(newadd);
			TableMaker(step+1);
		}
	}
	
	public ArrayList<Integer> interaction(ArrayList<Integer> a1, ArrayList<Integer> a2) {
		ArrayList<Integer> integers = new ArrayList<>();
		int l1 = a1.size(), l2 = a2.size(), i=0, j=0;
		while(i<l1 && j<l2) {
			while(i<l1 && j<l2 && a1.get(i)<a2.get(j)) i++;
			while(j<l2 && i<l1 && a2.get(j)<a1.get(i)) j++;
			if(i<l1 && j<l2 && a1.get(i)==a2.get(j)) {
				integers.add(a1.get(i));
				i++;j++;
			}
		}
		return integers;
	}
	
	public ArrayList<Integer> oneNear(ArrayList<Integer> a1, ArrayList<Integer> a2) {
		ArrayList<Integer> ret = new ArrayList<>();
		Set<Integer> ns = new TreeSet<>();
		ns.addAll(a1); ns.addAll(a2);
		if((ns.size()-a1.size())==1) {
			ret.addAll(ns);
		}
		return ret;
	}
	
	public void nodeCom(itemSupport tis) {
		int l = tis.tids.size();
		int l2 = tis.items.size();
		for(int i=0;i<l;i++) {
			int x = tis.tids.get(i);
			for(int j=0;j<l2;j++) {
				int y = tis.items.get(j);
				tis.u += (table[x][y]*values[y]);
			}
		}
	}
	
	public void nodeAs(itemSupport tis, ArrayList<Integer> ai,
			HashMap<ArrayList<Integer>,itemSupport> tmpais) {
		int l = ai.size();
		System.out.println("sub search");
		System.out.println(ai);
		for(int i=0;i<l;i++) {
			ArrayList<Integer> tai = new ArrayList<>();
			tai.addAll(ai);
			tai.remove(ai.get(i));
			if(tmpais.containsKey(tai)) {
				tis.subs.add(tmpais.get(tai));
				System.out.print(tai);
			}
		}
		System.out.println();
	}
	
	public void supCalc(int step) {
		if(step==dataTable.size()) return;
		HashMap<ArrayList<Integer>,itemSupport> hai = dataTable.get(step);
		if(step==0) {
			for(ArrayList<Integer> ai : hai.keySet()) {
				itemSupport tis = hai.get(ai);
				tis.sup = tis.u;
				hai.put(ai, tis);
			}
		}
		else {
			itemSupport tsub;
			for(ArrayList<Integer> ai : hai.keySet()) {
				System.out.print(ai);
				itemSupport tis = hai.get(ai);
				int m = 0, supr = ti, sum = 0;
				int l = tis.subs.size();
				for(int i=0;i<l;i++) {
					tsub = tis.subs.get(i);
					System.out.print(tsub.u+" ");
					if(tsub.u>=coefficient) {
						m++;
						sum += (ti*tsub.u/tsub.tids.size());
						if(tsub.tids.size()<supr) supr = tsub.tids.size();
					}
				}
				sum = sum*supr/step/ti;
				sum+=((step+1-m)*coefficient/step);
				tis.sup = sum;
				hai.put(ai, tis);
				System.out.println();
			}
		}
		supCalc(step+1);
	}
	
	public void tuCalc(int step) {
		if(step==dataTable.size()) return;
		HashMap<ArrayList<Integer>,itemSupport> hai = dataTable.get(step);
		for(ArrayList<Integer> ai : hai.keySet()) {
			itemSupport tis = hai.get(ai);
			for(int j : tis.tids) {
				tis.tu += tus[j];
			}
			hai.put(ai, tis);
		}
		tuCalc(step+1);
	}
	
	public static void main(String[] args){
		FHUIM fm = new FHUIM();
		fm.dataInput();
		fm.TableMaker(0);
		ArrayList<HashMap<ArrayList<Integer>,itemSupport>> ahi = fm.dataTable;
		int l = ahi.size();
		System.out.println("first, calculate the utility");
		for(int i=0;i<l;i++) {
			HashMap<ArrayList<Integer>,itemSupport> hai = ahi.get(i);
			for(ArrayList<Integer>ai : hai.keySet()) {
				itemSupport tis = hai.get(ai);
				System.out.print(tis.u+"/"+tis.tids.size()+" ");
			}
			System.out.println();
		}
		fm.supCalc(0);
		System.out.println("second, calculate the next");
		for(int i=0;i<l;i++) {
			System.out.println("to search the " +(i+1)+" layer ");
			HashMap<ArrayList<Integer>,itemSupport> hai = ahi.get(i);
			for(ArrayList<Integer>ai : hai.keySet()) {
				itemSupport tis = hai.get(ai);
				System.out.print(ai+""+tis.items+""+tis.u+" "+tis.sup+":");
				for(itemSupport istt : tis.subs) {
					System.out.print(istt.items+""+istt.u);
				}
				System.out.println();
			}
			System.out.println();
		}
		
		System.out.println("thrid, calculate the two-phase");
		for(int i=0;i<ti;i++) {
			System.out.println("T"+i+":"+tus[i]);
		}
		fm.tuCalc(0);
		for(int i=0;i<l;i++) {
			HashMap<ArrayList<Integer>,itemSupport> hai = ahi.get(i);
			for(ArrayList<Integer>ai : hai.keySet()) {
				itemSupport tis = hai.get(ai);
				System.out.print(tis.tu+"/"+tis.tids.size()+" ");
			}
			System.out.println();
		}
	}
}
