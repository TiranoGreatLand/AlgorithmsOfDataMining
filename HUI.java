import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HUI {
	public static int numOfItem;
	public static int tid;
	public int[] tus;
	public int[] twus;
	public static int minSup;
	public int[][] table;
	public int[] values;
	public int[] rtus;
	public Map<Integer, Integer> iti = new HashMap<>();
	
	public void datainput() {
		System.out.println("item 0 represents A, 1 represents B, etc... ");
		Scanner scanner = new Scanner(System.in);
		System.out.println("plz input the number of items");
		numOfItem = scanner.nextInt();
		System.out.println("PLZ input the num of itemsets");
		tid = scanner.nextInt();
		tus = new int[tid];
		rtus = new int[tid];
		values = new int[numOfItem];
		twus = new int[numOfItem];
		table = new int[tid][numOfItem];
		ArrayList<ArrayList<Integer>> itemIndex = new ArrayList<>();
		for(int i=0;i<numOfItem;i++) {
			ArrayList<Integer> tmpa = new ArrayList<>();
			itemIndex.add(tmpa);
		}
		System.out.println("plz input the values of items"); 
		for(int i=0;i<numOfItem;i++) values[i] = scanner.nextInt();
		System.out.println("plz input the data table");
		for(int i=0;i<tid;i++) {
			for(int j=0;j<numOfItem;j++) {
				table[i][j] = scanner.nextInt();
				tus[i] += (table[i][j]*values[j]);
				if(table[i][j]>0) {
					itemIndex.get(j).add(i);
				}
			}
		}
		System.out.println("plz input the minsupport");
		minSup = scanner.nextInt();
		scanner.close();
		for(int i=0;i<numOfItem;i++) {
			ArrayList<Integer> aIntegers = itemIndex.get(i);
			for(int x : aIntegers){
				twus[i] += tus[x];
			}
		}
		for(int i=0;i<numOfItem;i++) {
			if(twus[i]<minSup) {
				twus[i] = 0;
				values[i] = 0;
			}
		}
		for(int i=0;i<tid;i++) {
			for(int j=0;j<numOfItem;j++) {
				rtus[i] += (values[j]*table[i][j]);
			}
		}
/*		
		System.out.println("tu");
		for(int i : tus) System.out.print(i+" ");
		System.out.println();
		System.out.println("twu");
		for(int i : twus) System.out.print(i+" ");
		System.out.println();
		System.out.println("rtu");
		for(int i : rtus) System.out.print(i+" ");
		System.out.println();
*/
	}
	
	public class header {
		int item;
		int twu;
		inode firstNext=null;
		inode lastNext=null;
	}
	ArrayList<header> hds = new ArrayList<>();
	
	public class itemset {
		int tid;
		ArrayList<Integer> items = new ArrayList<>();
		int rtu;
	}
	ArrayList<itemset> its = new ArrayList<>();
	
	public void dataSelect() {
		for(int i=0;i<numOfItem;i++) {
			if(values[i]>0) {
				header th = new header();
				th.item = i;
				th.twu = twus[i];
				hds.add(th);
			}
		}
		Collections.sort(hds, new SortByTws());
		int hl = hds.size();
		for(int i=0; i<hl;i++) {
			iti.put(hds.get(i).item, i);
		}
		int len = hds.size();
		for(int i=0;i<tid;i++) {
			itemset newi = new itemset();
			newi.tid = i;
			newi.rtu = rtus[i];
			for(int j=0;j<len;j++) {
				int x = hds.get(j).item;
				if(table[i][x]!=0) {
					newi.items.add(x);
				}
			}
			its.add(newi);
		}
/*
		for(itemset is : its) {
			System.out.println(is.items+""+is.rtu);
		}
/*
		for(header h : hds) {
			System.out.println(h.item+" "+h.twu);
		}
*/
	}
	
	public class SortByTws implements Comparator<header> {
		@Override
		public int compare(header arg0, header arg1) {
			// TODO Auto-generated method stub
			return arg1.twu-arg0.twu;
		}	
	}
	
	public class inode {
		int item=-1;          //-1表示root
 		int time = 0;
		int va = 0;
		inode sib=null;
		inode ancient=null;
		ArrayList<inode> descs = new ArrayList<>();
	}
	
	public inode root = new inode();
	
	public inode ibelongslst(int a, ArrayList<inode> b) {
		if(b.size()==0) return null;
		for(inode i : b) {
			if(i.item==a) return i;
		}
		return null;
	}
	
	public void TreeMaker() {
		inode tmpi;
		for(int i=0;i<tid;i++) {
			tmpi = root;
			itemset is = its.get(i);
			for(int x :  is.items) {
				inode ti = ibelongslst(x, tmpi.descs);
				if(ti==null) {
					inode newi = new inode();
					newi.ancient = tmpi;
					newi.item = x;
					newi.time = 1;
					newi.va = is.rtu;
					tmpi.descs.add(newi);
					int index = iti.get(x);
					header hdr = hds.get(index);
					if(hdr.firstNext==null) {
						hdr.firstNext = newi;
						hdr.lastNext = newi;
					} else {
						hdr.lastNext.sib = newi;
						hdr.lastNext = newi;
					}
					tmpi = newi;
				}	else {
					tmpi = ti;
					tmpi.time++;
					tmpi.va += is.rtu;
				}
				System.out.print(tmpi.item+" "+tmpi.time+" "+tmpi.va+" | ");
			}
			System.out.println();
		}
	}
	
	public class Path{
		ArrayList<Integer> items = new ArrayList<>();
		int va = 0;
	}
	ArrayList<ArrayList<Path>> allPaths = new ArrayList<>();
	
	public void CndTreeFinder() {
		int hl = hds.size();
		inode tmpi ;
		for(int i=hl-1;i>0;i--) {
			ArrayList<Path> aPaths = new ArrayList<>();
			header hdr = hds.get(i);
			inode ti = hdr.firstNext;
			while(ti!=null) {
				//System.out.println(ti.item+" "+ti.time+" "+ti.va);
				Path tPath = new Path();
				tPath.va = ti.va;
				tmpi = ti.ancient;
				while(tmpi!=root) {
					tPath.items.add(tmpi.item);
					tmpi = tmpi.ancient;
				}
				aPaths.add(tPath);
				ti = ti.sib;
			}
			allPaths.add(aPaths);
		}
	}
	
	public class condN{
		int item;
		int pu=0;
	}
	
	public ArrayList<ArrayList<Path>> PHUIs = new ArrayList<>();
	
	public void PathToPHU() {
		int len = hds.size()-1;
		for(ArrayList<Path> ap : allPaths) {
			ArrayList<Path> apadd = new ArrayList<>();
			Map<Integer, condN> imap = new HashMap<>();
			ArrayList<condN> acn = new ArrayList<>();
			for(Path p : ap) {
				for(int i : p.items) {
					if(imap.containsKey(i)) {
						condN tmpc = imap.get(i);
						tmpc.pu += p.va;
						imap.put(i, tmpc);
					} else {
						condN tmpc = new condN();
						tmpc.item = i;
						tmpc.pu = p.va;
						imap.put(i, tmpc);
					}
				}
			}
			for(int i : imap.keySet()) {
				if(imap.get(i).pu>=minSup) {
					acn.add(imap.get(i));
				}
			}
			if(acn.size()>0) {
				Collections.sort(acn, new SortByRank());
				CTreeAdd(len, acn, apadd);
				if(apadd.size()>0) {
					PHUIs.add(apadd);
				}
			}
			
/*
			for(condN c : acn) {
				System.out.print(c.item+" "+c.pu+" | ");
			}
			System.out.println();
*/
			len--;
		}
	}
	
	public void CTreeAdd(int index, ArrayList<condN> acn, ArrayList<Path> apadd) {
		header lasth = hds.get(index);
		Path firstp = new Path();
		firstp.items.add(lasth.item);
		firstp.va = lasth.twu;
		apadd.add(firstp);
		int len = acn.size();
		for(int i=0;i<len;i++) {
			Path newp = new Path();
			newp.items.add(acn.get(i).item);
			newp.va = acn.get(i).pu;
			apadd.add(newp);
			subCTA(newp, i+1, len, acn, apadd);
		}
	}
	
	public void subCTA(Path lastp, int start, int len, ArrayList<condN> acn, 
			ArrayList<Path> apadd ) {
		for(int i=start; i<len;i++) {
			Path newp = new Path();
			newp.va = lastp.va;
			newp.items.addAll(lastp.items);
			newp.items.add(acn.get(i).item);
			apadd.add(newp);
			subCTA(newp, i+1, len, acn, apadd);
		}
	}
	
	public class SortByRank implements Comparator<condN> {
		@Override
		public int compare(condN o1, condN o2) {
			// TODO Auto-generated method stub
			return iti.get(o2.item)-iti.get(o1.item) ;
		}	
	}
	
	public static void main(String[] args) {
		HUI hui = new HUI();
		hui.datainput();
		hui.dataSelect();
		hui.TreeMaker();
		hui.CndTreeFinder();
		System.out.println("the paths");
		for(ArrayList<Path> ap : hui.allPaths) {
			for(Path p : ap) {
				System.out.print(p.items+""+p.va+" ");
			}
			System.out.println();
		}
		System.out.println("find the frequent ultility sets");
		hui.PathToPHU();
		for(ArrayList<Path> ap : hui.PHUIs) {
			int len = ap.size();
			Path rootp = ap.get(0);
			int x = rootp.items.get(0);
			String rs = (char)((int)'A'+x)+"";
			System.out.print(rs+"/"+rootp.va+" ");
			for(int i=1;i<len;i++) {
				Path tPath = ap.get(i);
				String ns = (char)((int)'A'+x)+"";
				for(int j : tPath.items) {
					ns += ((char)((int)'A'+j)+"");
				}
				System.out.print(ns+"/"+tPath.va+" ");
			}
			System.out.println();
		}
		
	}
	
}
