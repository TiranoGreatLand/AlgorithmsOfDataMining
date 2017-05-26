
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class GSP {

	public Map<Integer, ArrayList<ArrayList<Integer>>> sequences = new HashMap<>();
	public Map<Integer, Integer> items = new HashMap<>();
	public static int minSup;
	
	public ArrayList<Integer> deleteNum = new ArrayList<>();
	public Set<Integer> singleNum = new TreeSet<>();
	
	public void dataInput() {
		System.out.println("PLZ input data everyline as TransactionID, itemNum, item1, item2, item3......");
		Scanner scanner = new Scanner(System.in);
		int transID, itemNum, itemi;
		ArrayList<ArrayList<Integer>> aai ;
		do {
			transID = scanner.nextInt();
			if(transID<0) break;
			if(sequences.containsKey(transID)) {
				aai = sequences.get(transID);
			}
			else {
				aai = new ArrayList<>();
			}
			itemNum = scanner.nextInt();
			ArrayList<Integer> tmpai = new ArrayList<>();
			for(int i=0;i<itemNum; i++) {
				itemi = scanner.nextInt();
				tmpai.add(itemi);
				if(items.containsKey(itemi)) {
					items.put(itemi, 1+items.get(itemi));
				}
				else {
					items.put(itemi, 1);
				}
			}
			aai.add(tmpai);
			sequences.put(transID, aai);
		}while(scanner.hasNextInt());
		System.out.println("PLZ input min Support");
		minSup = scanner.nextInt();
		scanner.close();
		
		for(int i : sequences.keySet()) {
			System.out.print(i+" : ");
			ArrayList<ArrayList<Integer>> tmpaai = sequences.get(i);
			for(ArrayList<Integer> ai : tmpaai) System.out.print(ai);
			System.out.println();
		}
		for(int i : items.keySet()) {
			System.out.println(i+" "+items.get(i));
			if(items.get(i)<minSup) deleteNum.add(i);
			else singleNum.add(i);
		}
		for(int i : sequences.keySet()) {
			ArrayList<ArrayList<Integer>> tmps = sequences.get(i);
			ArrayList<ArrayList<Integer>> tmpadd = new ArrayList<>();
			for(ArrayList<Integer> ai : tmps) {
				ArrayList<Integer> tmpd = new ArrayList<>();
				for(int x : ai) {
					int flag = 0;
					for(int y : deleteNum) {
						if(x==y) flag++;
					}
					if(flag==0)tmpd.add(x); 
				}
				if(tmpd.size()>0) tmpadd.add(tmpd);
			}
			if(tmpadd.size()>0) {
				sequences.put(i, tmpadd);
			}
			else {
				sequences.remove(i);
			}
		}
		ArrayList<ArrayList<Integer>> tmpai = new ArrayList<>();
		for(int i : singleNum) {
			ArrayList<Integer> tmpsai = new ArrayList<>();
			tmpsai.add(i);
			tmpai.add(tmpsai);
		}
		setsSelect.add(tmpai);
		
		for(int i : sequences.keySet()) {
			System.out.print(i+" : ");
			ArrayList<ArrayList<Integer>> tmpaai = sequences.get(i);
			for(ArrayList<Integer> ai : tmpaai) System.out.print(ai);
			System.out.println();
		}
		
	}
	
	public ArrayList<ArrayList<ArrayList<Integer>>> setsSelect = new ArrayList<>();
	//先找合并的，再找序列的
	public void dataSelect(int step) {
		int sum = 0;
		ArrayList<ArrayList<Integer>> tmpaai = setsSelect.get(step);
		ArrayList<ArrayList<Integer>> tmpaai2 = setsSelect.get(0);
		ArrayList<ArrayList<Integer>> tmpnew = new ArrayList<>(); 
		int len = tmpaai.size(), len2 = tmpaai2.size();
		for(int i=0;i<len;i++) {
			for(int j=0;j<len2;j++) {
				saiJudge(tmpaai.get(i), tmpaai2.get(j), tmpnew);
			}
		}
		if(tmpnew.size()>0) {
			setsSelect.add(tmpnew);
			dataSelect(step+1);
		}
	}
	
	public void saiJudge(ArrayList<Integer> a, ArrayList<Integer> b,
			ArrayList<ArrayList<Integer>> newadd) {
		ArrayList<Integer> tmpai = new ArrayList<>();
		int sum = 0;
		tmpai.addAll(a); tmpai.addAll(b);
		ArrayList<ArrayList<Integer>> tmpaai;
		for(int i : sequences.keySet()) {
			tmpaai = sequences.get(i);
			for(ArrayList<Integer> ai : tmpaai) {
				if(saiBelongs(tmpai, ai)) {
					sum++;
				}
			}
		}
		if(sum>=minSup) {
			newadd.add(tmpai);
		}
	}
	
	public boolean saiBelongs(ArrayList<Integer> a, ArrayList<Integer> b) {
		int sum = 0;
		int al = a.size(), bl = b.size(), i=0, j=0;
		if(al>bl) return false;
		while(i<al && j<bl) {
			while(j<bl && a.get(i)!=b.get(j)) j++;
			if(i<al && j<bl && a.get(i)==b.get(j)) {
				sum++;
				i++;
				j++;
			}
		}
		if(sum==al) return true;
		else return false;
	}
	
	ArrayList<ArrayList<Integer>> selectAr = new ArrayList<>();
	ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>> sar = new ArrayList<>();
	public void seqSelect(int step) {
		if(step<0) {
			ArrayList<ArrayList<ArrayList<Integer>>> tmpaaai = new ArrayList<>();
			for(ArrayList<Integer> ai : selectAr) {
				ArrayList<ArrayList<Integer>> tmpaai = new ArrayList<>();
				tmpaai.add(ai);
				tmpaaai.add(tmpaai);
			}
			sar.add(tmpaaai);
			seqSelect(step+1);
		} else {
			ArrayList<ArrayList<ArrayList<Integer>>> tmpaaai = sar.get(step);
			ArrayList<ArrayList<ArrayList<Integer>>> newadd = new ArrayList<>();
			for(ArrayList<ArrayList<Integer>> aai : tmpaaai) {
				for(ArrayList<Integer> ai : selectAr) {
					ArrayList<ArrayList<Integer>> newa = new ArrayList<>();
					newa.addAll(aai);
					newa.add(ai);
					aaiJudge(newa, newadd);
				}
			}
			if(newadd.size()>0) {
				sar.add(newadd);
				seqSelect(step+1);
			}
		}
	}
	
	public void aaiJudge(ArrayList<ArrayList<Integer>> a, 
			ArrayList<ArrayList<ArrayList<Integer>>> newadd) {
		int sum = 0;
		for(int i : sequences.keySet()) {
			ArrayList<ArrayList<Integer>> tmpaai = sequences.get(i);
			if(aaiBelongs(a, tmpaai)) {
				sum++;
			}
		}
		if(sum>=minSup) {
			newadd.add(a);
		}
	}
	
	public boolean aaiBelongs(ArrayList<ArrayList<Integer>> a, 
			ArrayList<ArrayList<Integer>> b) {
		int al = a.size(), bl = b.size(), i=0, j=0, sum = 0;
		if(al>bl) return false;
		ArrayList<Integer> ta, tb;
		while(i<al && j<bl) {
			while(j<bl && !saiBelongs(a.get(i), b.get(j)) ) {
				j++;
			}
			if(i<al && j<bl && saiBelongs(a.get(i), b.get(j)) ) {
				sum++;
				i++;
				j++;
			}
		}
		if(sum==al) return true;
		else return false;
	}
	
	public static void main(String[] args) {
		GSP gsp = new GSP();
		gsp.dataInput();
		gsp.dataSelect(0);		
		for(ArrayList<ArrayList<Integer>> aai : gsp.setsSelect) {
			gsp.selectAr.addAll(aai);
			System.out.println(aai);
		}
		gsp.seqSelect(-1);
		System.out.println("-------------------------------------");
		for(ArrayList<ArrayList<ArrayList<Integer>>> aaai : gsp.sar) {
			System.out.println(aaai);
		}
	}
	
}
