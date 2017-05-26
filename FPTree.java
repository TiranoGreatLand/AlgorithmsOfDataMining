import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class FPTree {

	public ArrayList<String[]> inputdatas = new ArrayList<>();
	public Map<String, Integer> itemnums = new HashMap<>();
	public int MinTime;
	public class rawItem {
		int times;
		String name;
		FPTNode firstNext;
		FPTNode lastNext;
	}
	public ArrayList<rawItem> rItems = new ArrayList<>();
	
	public void dataScan()  {
		Scanner scanner = new Scanner(System.in) ;
		String tmpline;
		System.out.println("PLZ input data");
		tmpline=scanner.nextLine();
		while(tmpline.length()>0) {
			String[] datas = tmpline.split(",");
			inputdatas.add(datas);
			for(String string: datas) {
				if(itemnums.containsKey(string))  {
					itemnums.put(string, itemnums.get(string)+1);
				}
				else {
					itemnums.put(string, 1);
				}
			}
			tmpline=scanner.nextLine();
		}
		System.out.println("PLZ input the Minsup ");
		MinTime = scanner.nextInt();
		Set<String> keys = itemnums.keySet();
		for(String s: keys) {
			if(itemnums.get(s)>=MinTime) {
				rawItem tmpr = new rawItem();
				tmpr.times = itemnums.get(s);
				tmpr.name = s;
				rItems.add(tmpr);
			}
		}
		Collections.sort(rItems, new Comparator<rawItem>() {
			@Override
			public int compare(rawItem o1, rawItem o2) {
				// TODO Auto-generated method stub
				return o2.times - o1.times;
			}
		});
		int count = 0;
		for(rawItem ri: rItems) {
			System.out.println(ri.name+" "+ri.times);
			sortmap.put(ri.name, count++);
		}
		System.out.println("---------------");
		for(String s: sortmap.keySet()) {
			System.out.println(s+" "+sortmap.get(s));
		}
		
		scanner.close();
	}
	
	public Map<String, Integer> sortmap = new HashMap<>();
	public class tmpsai {
		String name;
		int rank;
	}
	ArrayList<ArrayList<tmpsai>> selectedItems = new ArrayList<>();
	
	public void itemSelect() {
		int len = inputdatas.size();
		for(int i=0;i<len;i++) {
			String[] tmps = inputdatas.get(i);
			ArrayList<tmpsai> everyfloor = new ArrayList<>();
			for(String s: tmps) {
				if(sortmap.containsKey(s)) {
					tmpsai ts = new tmpsai();
					ts.name = s;
					ts.rank = sortmap.get(s);
					everyfloor.add(ts);
				}
			}
			Collections.sort(everyfloor, new Comparator<tmpsai>() {
				@Override
				public int compare(tmpsai o1, tmpsai o2) {
					// TODO Auto-generated method stub
					return o1.rank - o2.rank;
				}
			});
		    selectedItems.add(everyfloor);
		}
		for(ArrayList<tmpsai> at: selectedItems) {
			for(tmpsai ta: at) {
				System.out.print(ta.name+" "+ta.rank+" | ");
			}
			System.out.println();
		}
	}
	
	public class FPTNode {
		String name;
		int times;
		FPTNode sibling;
		FPTNode parent;
		ArrayList<FPTNode> dest = new ArrayList<>();
	}
	
	public FPTNode root = new FPTNode();
	
	public void FPTreeMaker() {
		FPTNode last ;
		int len = selectedItems.size();
		for(int i=0;i<len;i++) {
			ArrayList<tmpsai> tmpal = selectedItems.get(i);
			int alen = tmpal.size();
			last = root;
			for(int j=0;j<alen;j++) {
					int flag = 0;
					int ldl = last.dest.size();
					for(int k=0;k<ldl;k++) {
						if(last.dest.get(k).name.equals(tmpal.get(j).name)) {
							last.dest.get(k).times++;
							flag = 1;
							System.out.print(last.dest.get(k).name+" "+last.dest.get(k).times+" | ");
							last = last.dest.get(k);
							break;
						}
					}
					if(flag == 0) {
						FPTNode tmpf = new FPTNode();
						tmpf.times = 1;
						tmpf.name = tmpal.get(j).name;
						tmpf.parent = last;
						System.out.print(" new "+tmpf.name+" "+tmpf.times+" | ");
						last.dest.add(tmpf);
						last = tmpf;
						for(rawItem rwi : rItems) {
							if(rwi.name.equals(last.name)) {
								if(rwi.firstNext==null) {
									rwi.firstNext = last;
									rwi.lastNext = last;
								}
								else {
									rwi.lastNext.sibling = last;
									rwi.lastNext = last;
								}
								System.out.print(" root "+rwi.name+" "+rwi.firstNext.times+" "+rwi.lastNext.times);
								break;
							}
						}
					}
			}
			System.out.println();
			System.out.println("nextline");
		}
	}
	
	public class setsT{
		String name;
		int times;
	}
	
	public ArrayList<HashMap<String, Integer>> itemsetsnum = new ArrayList<>();
	public ArrayList<ArrayList<setsT>> setsTs = new ArrayList<>();
	
	public void setsSelect() {
		for(ArrayList<setsT> als : setsTs) {
			HashMap<String, Integer> map = new HashMap<>();
			setsMerge(als, map);
			itemsetsnum.add(map);
		}
	}
	
	public void addToMap(setsT s, HashMap<String, Integer> map){
		if(map.containsKey(s.name)) {
			if(s.times>map.get(s.name)) {
				map.put(s.name, s.times);
			}
		}
		else {
			map.put(s.name, s.times);
		}
	}
	
	public void setsMerge(ArrayList<setsT> als, HashMap<String, Integer> map) {
		int l = als.size();
		for(int i=0;i<l;i++) {
			if(als.get(i).times>=MinTime) {
				addToMap(als.get(i), map);
			}
			selectISt(i+1, l, als.get(i), als, map);
		}
	}
	
	public void selectISt(int sp, Integer l, setsT s,
			ArrayList<setsT> als, HashMap<String, Integer> map){
		for(int i=sp;i<l;i++) {
			char[] cs1 = s.name.toCharArray();
			char[] cs2 = als.get(i).name.toCharArray();
			int csl1 = cs1.length, csl2 = cs2.length;
			StringBuilder sb = new StringBuilder();
			for(int j=0;j<csl1 && j<csl2;j++) {
				if(cs1[j]==cs2[j]) {
					sb.append(cs1[j]);
				}
				else {
					break;
				}
			}
			setsT news = new setsT();
			news.name = sb.toString();
			news.times = s.times+als.get(i).times;
			if(news.times>=MinTime) {
				addToMap(news, map);
			}
			selectISt(i+1, l, news, als, map);
		}
	}
	
	public void processing() {
		int len = rItems.size();
		for(int i = len-1;i>=0;i--) {
			System.out.println(rItems.get(i).name+" "+rItems.get(i).times);
			ArrayList<setsT> als = new ArrayList<>();
			conditionalTreeMaker(rItems.get(i), als);
			setsTs.add(als);
		}
		 setsSelect();
	}
	
	public FPTNode retFPT(FPTNode f1) {
		FPTNode ret = new FPTNode();
		ret.dest.addAll(f1.dest);
		ret.name = f1.name;
		ret.times = f1.times;
		ret.parent = f1.parent;
		ret.sibling = f1.sibling;
		return ret;
	}
	//处理从ri的兄弟节点延展出来的那一条链
	public void conditionalTreeMaker(rawItem ri, ArrayList<setsT> als) {
		FPTNode fptf = retFPT(ri.firstNext);
		while(fptf!=null) {
			StringATMaker(fptf,als);
			fptf = fptf.sibling;
		}
	}
	
	private void StringATMaker(FPTNode fptf, ArrayList<setsT> als) {
		String start = "";
		FPTNode tmp = retFPT(fptf.parent);
		while(tmp!=null && tmp!=root) {
			if(tmp.name!=null)start = tmp.name+start;
			tmp = tmp.parent;
		}
		start = fptf.name+start;
		setsT nT = new setsT();
		nT.name = start;
		nT.times = fptf.times;
		als.add(nT);
	}

	/*
	public void NodeMerge(String name, FPTNode f1, HashMap<String, Integer> map) {
		FPTNode tf1 = retFPT(f1);
		while(tf1.sibling!=null) {
			FPTNode tf2 = retFPT(tf1.sibling);
			while(tf2!=null) {
				FindSameStem(name, tf1.times+tf2.times, tf1.parent, tf2.parent, tf2.sibling, map);
				tf2 = tf2.sibling;
			}
			tf1 = tf1.sibling;
			if(tf1.times>=MinTime) {
				setMakeFromTree(tf1.name, tf1.times, tf1.parent, map);
			}
		}
	}
	
	public void FindSameStem(String name, Integer times, FPTNode f1, FPTNode f2, 
			FPTNode sib, HashMap<String, Integer> map) {
		if(f1==root || f2==root) return;
		while(sortmap.get(f1.name)!=sortmap.get(f2.name))  {
			if(f1!=root && sortmap.get(f1.name)>sortmap.get(f2.name)) f1 = f1.parent;
			if(f2!=root && sortmap.get(f2.name)>sortmap.get(f1.name)) f2 = f2.parent;
			if(f1==root || f2==root) break;
		}
		if(f1==root || f2==root) {
			return;
		}
		else {
			if(times>=MinTime) {
				setMakeFromTree(name, times, f1, map);
			}
			if(sib!=null) {
				FPTNode tsib = retFPT(sib);
				while(tsib!=null) {
					FindSameStem(name, times+tsib.times, f1, tsib, tsib.sibling, map);
					tsib = tsib.sibling;
				}
			}
		}
	}
	
	public void setMakeFromTree(String name, Integer times, FPTNode fpn, HashMap<String, Integer> map) {
		if(map.containsKey(name)) {
			if(times>map.get(name)){
				map.put(name, times);	
			}
		} else {
			map.put(name, times);
		}
		if(fpn==root) return;
		FPTNode fp1 = retFPT(fpn);
		while(fp1!=root) {
			setMakeFromTree(name+fp1.name, times, fp1.parent, map);
			fp1 = fp1.parent;
		}
	}
	*/
	
	public static void main(String[] args) {
		FPTree fp = new FPTree();
		fp.dataScan();
		fp.itemSelect();
		System.out.println("-----------1------------");
		fp.FPTreeMaker();
		System.out.println("-----------2------------");
		fp.processing();
		//ArrayList<HashMap<String, Integer>> itemsetsnum
		System.out.println("-----------last output------------");
		for(HashMap<String, Integer> hm : fp.itemsetsnum) {
			for(String s : hm.keySet()) {
				System.out.print(s+" "+hm.get(s)+" | ");
			}
			System.out.println();
		}
	}
	
}
