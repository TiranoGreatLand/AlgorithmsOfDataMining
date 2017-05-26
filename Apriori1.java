import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Apriori1 {

	public class iscode {
		int times;
		Long[] code;
		public iscode(int tt, Long[] tc) {
			times = tt;
			code = tc;
		}
	}
	
	public Map<String, Integer> singleItemNum = new HashMap<>();
	public ArrayList<ArrayList<iscode>> itemsets = new ArrayList<>();
	public ArrayList<Long[]> setscode = new ArrayList<>();
	public Map<String, Long[]> singlecode = new HashMap<>();
	public int threshold;
	public int codelen=0, longlen=0;
	public float minconf;
	
	public void dataScan() {
		Scanner scanner = new Scanner(System.in);
		String tmps = "";
		String[] TID = {};
		ArrayList<String[]> tmpsets = new ArrayList<>();
		System.out.println("PLZ input the datas");
		while((tmps=scanner.nextLine())!=null) {
			TID = tmps.split(",");
			tmpsets.add(TID);
			for(String s: TID) {
				if(singleItemNum.containsKey(s)) {
					singleItemNum.put(s, singleItemNum.get(s)+1);
				}
				else {
					singleItemNum.put(s, 1);
				}
			}
		}
		System.out.println("PLZ input the threshold");
		threshold = scanner.nextInt();
		System.out.println("PLZ input the minconf");
		minconf = scanner.nextFloat();
		System.out.println("Scan data that over the threshold.");
		Set<String> keys = singleItemNum.keySet();
		for( String s : keys) {
			if(singleItemNum.get(s)<threshold) {
				singleItemNum.remove(s);
			}
		}
		codelen = singleItemNum.size();
		scanner.close();
		firstFloor();
		for(String[] tStrings : tmpsets) {
			int tl = tStrings.length;
			Long[] tmpl = new Long[longlen];
			for(int i=0;i<tl;i++) {
				if(singlecode.containsKey(tStrings[i])) {
					icodeXor(tmpl, singlecode.get(tStrings[i]), longlen);
				}
			}
			setscode.add(tmpl);
		}
	}
	
	public static void icodeXor(Long[] a, Long[] b, int len) {
		for(int i=0;i<len;i++) {
			a[i] ^= b[i];
		}
	}
	
	public void firstFloor() {
		ArrayList<iscode> firstfloor = new ArrayList<>();
		longlen = codelen/63;
		if(codelen%63!=0) longlen++;
		int il = 0, shifting = 0;
		Set<String> keys = singleItemNum.keySet();
		for(String s: keys) {
			Long[] tmpc = new Long[longlen];
			if(shifting==63) {
				shifting = 0;
				il++;
			}
			singlecode.put(s, tmpc);
			tmpc[il] = (long)1<<shifting;
			shifting++;
			iscode tmpi = new iscode(singleItemNum.get(s), tmpc);
			firstfloor.add(tmpi);
		}
		itemsets.add(firstfloor);
	}
	
	public static boolean bigger(Long[] a, Long[] b, int len) {
		for(int i=len-1;i>=0;i--) {
			if(a[i]>b[i]) return true;
		}
		return false;
	}
	
	public static boolean belongs(Long[] a, Long[] b, int len) {
		Long[] cLongs = new Long[len];
		for(int i=0;i<len;i++) {
			cLongs[i] = a[i]&b[i];
		}
		for(int i=0;i<len;i++) {
			if(cLongs[i]!=a[i]) return false;
		}
		return true;
	}
	
	public void nextSteps() {
		ArrayList<iscode> nextLst = new ArrayList<>();
		ArrayList<iscode> lastLst = itemsets.get(itemsets.size()-1);
		ArrayList<iscode> firstflr = itemsets.get(0);
		int lll = lastLst.size(), ffl = firstflr.size();
		iscode tmp1, tmp2;
		int sum;
		for(int i=lll;i>=0;i--) {
			tmp1 = lastLst.get(i);
			for(int j=ffl;j>=0;j--) {
				tmp2 = firstflr.get(j);
				if(!bigger(tmp2.code, tmp1.code, longlen)) {
					break;
				}
				else {
					Long[] tmpl = tmp1.code;
					icodeXor(tmpl, tmp2.code, longlen);
					sum = 0;
					for(Long[] tl : setscode) {
						if(belongs(tmpl, tl, longlen)) sum++;
					}
					if(sum>=threshold) {
						iscode tmpi = new iscode(sum, tmpl);
						nextLst.add(tmpi);
					}
				}
			}
		}
		if(nextLst.size()>0) {
			itemsets.add(nextLst);
		}
		if(itemsets.size()>1) {
			nextSteps();
		}
	}
	
	public class isasso {
		Long[] preif;
		Long[] postthen;
		float confidence;
	}
	
	public ArrayList<isasso> assosication = new ArrayList<>();
	
	public static boolean nonOverlap(Long[] a, Long[] b, int len) {
		for(int i=0;i<len;i++) {
			if((a[i]&b[i])!=0) return false;
		}
		return true;
	}
	
	public static boolean longequal(Long[] a, Long[] b, int len) {
		for(int i=0;i<len;i++) {
			if(a[i]!=b[i]) return false;
		}
		return true;
	}
	
	//选择x层一个itemset点，扫描其从x到最后一层可能的ifthen情况
	public void confScan(iscode post, int poststep,  int step, int len) {
		if(poststep+step+1>=len) return;
		int sum = 0;
		ArrayList<iscode> tmpal = itemsets.get(step);
		ArrayList<iscode> ifthenal = itemsets.get(poststep+step+1);
		int tmpl = tmpal.size(), itl = ifthenal.size();
		for(int i=0;i<tmpl;i++) {
			if(nonOverlap(post.code, tmpal.get(i).code, longlen)) {
				Long[] ifthen = tmpal.get(i).code;
				icodeXor(ifthen, post.code, longlen);
				for(int j=0;j<itl;j++) {
					if(longequal(ifthen, ifthenal.get(j).code, longlen)) {
						float tf = (float)ifthenal.get(j).times/post.times;
						if(tf>=minconf) {
							isasso tmpis = new isasso();
							tmpis.confidence = tf;
							tmpis.preif = ifthenal.get(j).code;
							tmpis.postthen = post.code;
							assosication.add(tmpis);
							sum++;
						}
					}
				}
			}
		}
		if(sum>0) {
			confScan(post, poststep, step+1, len);
		}
	}
	
	public void AssoFind() {
		int len = itemsets.size();
		for(int i=0;i<len;i++) {
			ArrayList<iscode> ical = itemsets.get(i);
			int tmpl = ical.size();
			for(int j=0;j<tmpl;j++) {
				confScan(ical.get(j), i, i, len);
			}
		}
	}
	
}
