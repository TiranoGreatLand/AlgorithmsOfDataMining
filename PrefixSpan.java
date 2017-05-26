import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class PrefixSpan {
	public ArrayList<ArrayList<ArrayList<Character>>> rawdata = new ArrayList<>();
	public static int minSup;
	public Set<ArrayList<Character>> csets = new HashSet<>();
	
	public void dataInput() {
		System.out.println("please input the data every line as : string1 string2...");
		System.out.println("x means the sid of this line, and string1, string2 means subsequence"
				+"\nEveryline's subsequences will be merged and transfer into an array of char");
		Scanner scanner = new Scanner(System.in);
		String string = scanner.nextLine();
		while(!string.equals("over")) {
			String[] subs = string.split(" ");
			ArrayList<ArrayList<Character>> aac = new ArrayList<>();
			for(String string2 : subs) {
//				System.out.print(string2);
				char[] stcs = string2.toCharArray();
				ArrayList<Character>  ac = new ArrayList<>();
				for(char c : stcs) ac.add((Character)c);
				aac.add(ac);
				csets.add(ac);
			}
//			System.out.println();
//			System.out.println(aac);
			rawdata.add(aac);
			string = scanner.nextLine();
		}
		System.out.println("input over");
		System.out.println("PLZ input the minimal support number");
		minSup = scanner.nextInt();
		scanner.close();
	}
	
	public ArrayList<ArrayList<Character>> cset = new ArrayList<>();
	public class SortBySize implements Comparator<ArrayList<Character>> {
		@Override
		public int compare(ArrayList<Character> o1, ArrayList<Character> o2) {
			// TODO Auto-generated method stub
			return o1.size()-o2.size();
		}
	}
	
	public void subsetsAdd(ArrayList<Character> ac) {
		if(ac.size()<=1) return;
		for(Character c : ac) {
			ArrayList<Character> tac = new ArrayList<>();
			tac.addAll(ac);
			tac.remove(c);
			csets.add(tac);
			subsetsAdd(tac);
		}
	}
	
	public void findclists() {
		cset.addAll(csets);
		for(ArrayList<Character> ac : cset) {
			subsetsAdd(ac);
		}
		cset.clear();
		cset.addAll(csets);
		Collections.sort(cset, new SortBySize());
/*
		for(ArrayList<Character> ac : cset) {
			System.out.println(ac);
		}
*/
		csets.clear();
		sacFind();
		findAAC();
	}
	
	public class sinAcIdx{
		int rawdataIndex;       
		int secondIndex;          //对于单层ac，只要是出现的index就行;对于双层aac，则是最后一个ac出现的index		                                    
	}                                       //对于单层ac，如果出现了多个一样的char，则记录出现的首个char就跳到下一个ac
	
	public Map<ArrayList<Character>, ArrayList<sinAcIdx>> sinMap = new HashMap<>();
	
	public void sacFind() {
		int len = rawdata.size();
		for(ArrayList<Character> ac : cset) {
			int[] flag = new int[len];
			acJudge(flag, ac);
		}
	}
	
	public void acJudge(int[] flag, ArrayList<Character> ac) {
		ArrayList<sinAcIdx> asa = new ArrayList<>();
		int len = rawdata.size();
		for(int i=0;i<len;i++) {
			ArrayList<ArrayList<Character>> aac = rawdata.get(i);
			int al = aac.size();
			for(int j=0;j<al;j++) {
				ArrayList<Character> tac = aac.get(j);
				if(acBelongs(ac, tac)) {
					sinAcIdx tmps = new sinAcIdx();
					tmps.rawdataIndex = i; tmps.secondIndex = j;
					asa.add(tmps);
					flag[i] = 1;
				}
			}
		}
		int sum = 0;
		for(int i=0;i<len;i++) sum += flag[i];
		if(sum>=minSup) {
			sinMap.put(ac, asa);
			sinaclst tsa = new sinaclst();
			tsa.ac = ac;
			tsa.as = asa;
			tsa.flag = flag;
			ArrayList<ArrayList<Integer>> taas = new ArrayList<>();
			for(int i=0;i<len;i++) {
				ArrayList<Integer> as = new ArrayList<>();
				taas.add(as);
			}
			for(sinAcIdx sa : asa) {
				taas.get(sa.rawdataIndex).add(sa.secondIndex);
			}
			tsa.aas = taas;
			acList.add(tsa);
		}
	}
	
	public boolean acBelongs(ArrayList<Character> a, ArrayList<Character> b) {
		int al = a.size(), bl = b.size(), i=0, j=0, sum = 0;
		if(al>bl) return false;
		while(i<al && j<bl) {
			while(i<al && j<bl && a.get(i)<b.get(j)) i++;
			while(i<al && j<bl && b.get(j)<a.get(i)) j++;
			if(i<al && j<bl && a.get(i)==b.get(j)) {
				sum++; i++;j++;
			}
		}
		if(sum==al) return true;
		else return false;
	}
	
	int counter = 0;
    
	public class sinaclst {
		ArrayList<Character> ac ;
		int[] flag;
		ArrayList<sinAcIdx> as ;
		ArrayList<ArrayList<Integer>> aas;
	}
	public ArrayList<sinaclst> acList = new ArrayList<>();
	
	public class aacnode {
		ArrayList<ArrayList<Character>> aac =new ArrayList<>();
		int[] flag;
		ArrayList<sinAcIdx> as = new ArrayList<>();
		ArrayList<ArrayList<Integer>> aas = new ArrayList<>();
	}
	ArrayList<aacnode>  anList = new ArrayList<>();
	
	//public Map<ArrayList<ArrayList<Character>>, ArrayList<sinAcIdx>> aacMap = new HashMap<>();
	public Set<ArrayList<ArrayList<Character>>> aacJudge = new HashSet<>();
	
	public void findAAC() {
		int acl = acList.size();
		for(int i=0;i<acl;i++) {
			aacnode naac = new aacnode();
			naac.aac.add(acList.get(i).ac);
			naac.flag = acList.get(i).flag;
			naac.as = acList.get(i).as;
			naac.aas.addAll(acList.get(i).aas);
			anList.add(naac);
			aacloop(naac);
		}
/*		
		for(sinaclst tsa : acList) {
			System.out.print(tsa.ac+" ");
			for(int i : tsa.flag) System.out.print(i+" ");
			System.out.println(tsa.aas);
			System.out.println();
		}
*/     
	}
	
	public void aacloop(aacnode naac) {
		int rdl = rawdata.size();
		for(sinaclst tac : acList) {
			int sum = 0;
			aacnode taac = new aacnode();
			ArrayList<ArrayList<Character>> tach = new ArrayList<>();
			tach.addAll(naac.aac);
			int[] flag = new int[rdl];
			for(int i=0;i<rdl;i++) {
				ArrayList<Integer> tai = new ArrayList<>();
				if((naac.flag[i] & tac.flag[i])==1) {
					ArrayList<Integer> ai1 = naac.aas.get(i), ai2 = tac.aas.get(i);
					int l1 = ai1.size(), l2 = ai2.size(), s1 = 0, s2 = 0;
					while(s1<l1 && s2<l2) {
						while(s2<l2 && ai2.get(s2)<=ai1.get(s1)) s2++;
						if(s2<l2 && ai2.get(s2)>ai1.get(s1)) {
							tai.add(ai2.get(s2));
							break;
						}
					}
					if(tai.size()>0) {
						sum++;
						flag[i] = 1;
					}
				}
				taac.aas.add(tai);
			}
			if(sum>=minSup) {
				tach.add(tac.ac);
				if(!aacJudge.contains(tach)) {
					taac.aac = tach;
					taac.flag = flag;
					anList.add(taac);
					aacloop(taac);
				}
			}
		}
	}
	
	public void findIndex(ArrayList<Integer> tai, ArrayList<Integer> ai1,
			ArrayList<Integer> ai2) {
		
	}
	
	public static void main(String[] args) {
		PrefixSpan pSpan = new PrefixSpan();
		pSpan.dataInput();
		pSpan.findclists();
		for(aacnode a : pSpan.anList) {
			System.out.println(a.aac+" "+a.aas);
		}
	}
	
 }
