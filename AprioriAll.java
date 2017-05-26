import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class AprioriAll {
	
	public int minSup;
	public Map<Integer, ArrayList<ArrayList<Integer>>> rawdata = new HashMap<>();
	public Set<ArrayList<Integer>> alLists = new HashSet<>();
	
	public void addToSet(ArrayList<Integer> ai) {
		alLists.add(ai);
		int al = ai.size();
		if(al>1) {
			for(int i=al-1;i>=0;i--) {
				ArrayList<Integer> tai = new ArrayList<>();
				tai.addAll(ai);
				tai.remove(ai.get(i));
				addToSet(tai);
			}
		}
	}
	
	public void datainput() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("PLZ input cid and itemset that splited by whitespace ,"
				+ " and every item in itemset is splited by , ");
		String string ;
		int tmpi;
		string = scanner.nextLine();
		while(string.length()>0) {
			System.out.println(string);
			String[] ss = string.split(" ");
			int cid = Integer.parseInt(ss[0]);
			String[] tcs = ss[1].split(",");
			int cl = tcs.length;
			ArrayList<Integer> tai = new ArrayList<>();
			for(int i=0;i<cl;i++) {
				tmpi = Integer.parseInt(tcs[i]);
				tai.add(tmpi);
			}
			addToSet(tai);
				ArrayList<ArrayList<Integer>> aai;
				if(rawdata.containsKey(cid)) {
					aai = rawdata.get(cid);
				} else {
					aai = new ArrayList<>();
				}
				aai.add(tai);
				rawdata.put(cid, aai);	
			string = scanner.nextLine();
		}
		System.out.println("PLZ input the MinSupport.");
		minSup = scanner.nextInt();
		for(int i : rawdata.keySet()) {
			ArrayList<ArrayList<Integer>> aai = rawdata.get(i);
			Collections.sort(aai, new SortAi());
			rawdata.put(i, aai);
		}
		scanner.close();
	}

	public class SortAi implements Comparator<ArrayList<Integer>> {
		@Override
		public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
			// TODO Auto-generated method stub
			int l1 = o1.size(), l2 = o2.size(), i1=0, i2=0;
			while(i1<l1 && i2<l2) {
				if(o1.get(i1)!=o2.get(i2)) return o1.get(i1)-o2.get(i2);
				i1++;i2++;
			}
			return l1-l2;
		}
	}
	
	public class SortI implements Comparator<Integer> {
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1-o2;
		}
	}
	
	public ArrayList<ArrayList<ArrayList<Integer>>> passiss = new ArrayList<>(); 
	public ArrayList<ArrayList<Integer>> sortists = new ArrayList<>();
	public Set<ArrayList<Integer>> selectedist = new HashSet<>();
	int lenOfist;
	
	public void fisFind() {
		sortists.addAll(alLists);
		alLists.clear();
		System.out.println(sortists);
		Collections.sort(sortists, new SortAai());
		System.out.println(sortists);
		lenOfist = sortists.size();
		for(int i=0;i<lenOfist;i++) {
			ArrayList<ArrayList<Integer>> aai = new ArrayList<>();
			aai.add(sortists.get(i));
			finds(i+1, aai);
		}
		sortists.clear();
		for(ArrayList<ArrayList<Integer>> aai : passiss) {
			selectedist.addAll(aai);
		}
//		sortists.addAll(selectedist);
//		Collections.sort(sortists, new SortAai());
//		System.out.println("Now: "+sortists);
		HashSet<ArrayList<Integer>> tmphs = new HashSet<>();
		tmphs.addAll(selectedist);
//		System.out.println("Now: "+tmphs);
		selectedist.clear();
		sortists.clear();
		sortists.addAll(tmphs);
		Collections.sort(sortists, new SortAai());
		System.out.println("Now: "+sortists);
		int[] flags = new int[sortists.size()];
		int sl = sortists.size();
		for(int i=0;i<sl;i++) {
			ArrayList<Integer> a1 = sortists.get(i);
			for(int j=i+1;j<sl;j++) {
				ArrayList<Integer> a2 = sortists.get(j);
				if(saiBelongs(a1, a2)) {
					flags[i] = 1;
				} else if(saiBelongs(a2, a1)) {
					flags[j] = 1;
				}
			}
		}
		ArrayList<ArrayList<Integer>>newtai = new ArrayList<>();
		for(int i=0;i<sl;i++) {
			if(flags[i]!=1) newtai.add(sortists.get(i));
		}
		Collections.sort(newtai, new SortAai());
		sortists.clear();
		sortists.addAll(newtai);
		System.out.println("Now: "+sortists);
		sl= sortists.size();
		for(int i=0;i<sl;i++) {
			ArrayList<Integer> ai = sortists.get(i);
			ArrayList<Integer> index = new ArrayList<>();
			transtoai(ai, index);
			aiToAI.put(ai, index);
		}
		for(ArrayList<Integer> ai : aiToAI.keySet()) {
			System.out.println(ai+" "+aiToAI.get(ai));
		}
	}
	
	public void transtoai(ArrayList<Integer> ai, ArrayList<Integer> index) {
		ArrayList<ArrayList<Integer>> allsub = new ArrayList<>();
		if(ai.size()<=0) return;
		allsub.add(ai);
		int al = ai.size();
		if(al>1) {
			for(int i=0;i<al;i++) {
				ArrayList<Integer> tai = new ArrayList<>();
				tai.addAll(ai);
				tai.remove(ai.get(i));
				allSubAdd(tai, allsub);
			}
		}
		Collections.sort(allsub, new SortAi2());
		System.out.println(allsub);
		for(ArrayList<Integer> newai :  allsub) {
			if(!aiterms.containsKey(newai)) {
				aiterms.put(newai, count++);
			}
			index.add(aiterms.get(newai));
		}
	}
	
	public class SortAi2 implements Comparator<ArrayList<Integer>> {
		@Override
		public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
			// TODO Auto-generated method stub
			int l1 = o1.size(), l2 = o2.size(), i1=0, i2=0;
			if(l1==l2) {
				while(i1<l1 && i2<l2) {
					if(o1.get(i1)!=o2.get(i2)) return o1.get(i1)-o2.get(i2);
					i1++;i2++;
				}
				return l1-l2;
			} else {
				return l1-l2;
			}
		}
	}
	
	public void allSubAdd(ArrayList<Integer> ai, ArrayList<ArrayList<Integer>> allsub) {
		int al = ai.size();
		allsub.add(ai);
		if(al>1) {
			for(int i=0;i<al;i++) {
				ArrayList<Integer> tai = new ArrayList<>();
				tai.addAll(ai);
				tai.remove(ai.get(i));
				allSubAdd(tai, allsub);
			}
		}
	}
	
	public int count = 1;
	public Map<ArrayList<Integer>, ArrayList<Integer>> aiToAI = new HashMap<>();
	public Map<ArrayList<Integer>, Integer> aiterms = new HashMap<>();
	
	public void finds(Integer start, ArrayList<ArrayList<Integer>> aai) {
		int ret = exists(aai);
		if(ret>=minSup) {
			passiss.add(aai);
			for(int i=start;i<lenOfist;i++) {
				ArrayList<ArrayList<Integer>> newaai = new ArrayList<>();
				newaai.addAll(aai);
				newaai.add(sortists.get(i));
				finds(i+1, newaai);
			}
		}
	}
	
	public int exists(ArrayList<ArrayList<Integer>> aai) {
		int ret = 0;
		int aal = aai.size();
		for(int i : rawdata.keySet()) {
			ArrayList<ArrayList<Integer>>taai = rawdata.get(i);
			int tl = taai.size();
			if(tl>=aal) {
				if(aaiBelongs(aai, taai)) {
					ret++;
				}
			}
		}
		return ret;
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
	
	public class SortAai implements Comparator<ArrayList<Integer>> {
		@Override
		public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
			int l1 = o1.size(), l2 = o2.size(), i1=0, i2=0;
			while(i1<l1 && i2<l2) {
				if(o1.get(i1)!=o2.get(i2))return o1.get(i1)-o2.get(i2);
				i1++;i2++;
			}
			return l1-l2;
		}
	}
	
	public Map<Integer, ArrayList<ArrayList<Integer>>> transData = new HashMap<>();
	
	public void itemtrams() {
		for(int i : rawdata.keySet()) {
			ArrayList<ArrayList<Integer>> aai = rawdata.get(i);
			ArrayList<ArrayList<Integer>> newadd = new ArrayList<>();
			for(ArrayList<Integer> ai : aai) {
				ArrayList<Integer> aIntegers = new ArrayList<>();
				for(ArrayList<Integer> key : aiterms.keySet()) {
					if(saiBelongs(key, ai)) {
						aIntegers.add(aiterms.get(key));
					}
				}
				if(aIntegers.size()>0) {
					Collections.sort(aIntegers, new SortI());
					newadd.add(aIntegers);
				}
			}
			transData.put(i, newadd);
		}
		for(int i: transData.keySet()) {
			System.out.println(transData.get(i));
		}
	}
	
	public Map<ArrayList<Integer>, Integer> transtimes = new HashMap<>();
	
	public void aifinds() {
		for(int i=0;i<count;i++) {
			ArrayList<Integer> ai = new ArrayList<>();
			ai.add(i);
			subfinds(i+1, ai);
		}
	}
	
	public void search(ArrayList<Integer> ai) {
		int sum = 0;
		if(ai.size()>transData.size()) return;
		int al = ai.size();
		for(int i : transData.keySet()) {
			ArrayList<ArrayList<Integer>> aai = transData.get(i);
			int aal = aai.size(), x1 = 0, x2 = 0;
			while(x1<al && x2<aal) {
				
			}
		}
	}
	
	public void subfinds(int start, ArrayList<Integer> ai) {
		for(int i=start;i<count;i++) {
			ArrayList<Integer> newai = new ArrayList<>();
			newai.add(i);
		}
	}
	
	public static void main(String[] args) {
		AprioriAll al = new AprioriAll();
		al.datainput();
		for(int i : al.rawdata.keySet()) {
			System.out.println(i+" "+al.rawdata.get(i));
		}
		al.fisFind();
		al.itemtrams();
		al.aifinds();
//		System.out.println(al.passiss);
	}
	
}
