package InformationRetrieval;
import java.util.*;
import java.io.*;
import java.util.Map.Entry;

public class PositionalIndex {

	BufferedReader br[];
	HashMap<String, HashMap<Integer, ArrayList<Integer>>> pi;
	int numberOfDocuments, numberOfUniqueWords;
	int queryLength;
	HashMap<Integer, ArrayList<Integer>> al;
	ArrayList<String> pathnames;
	
	PositionalIndex(ArrayList<String> pathnames) throws IOException {
		this.pathnames=pathnames;
		numberOfDocuments=pathnames.size();
		br=new BufferedReader[numberOfDocuments];
		for(int i=0; i<numberOfDocuments; ++i) br[i]=new BufferedReader(new FileReader(pathnames.get(i)));
		//store the number of documents
		//initialize index
		pi=new HashMap<String, HashMap<Integer, ArrayList<Integer>>>();
		storeCollection();
	}
	
	void storeCollection()throws IOException {
		
		numberOfUniqueWords=0;
		
		for(int i=0; i<numberOfDocuments; ++i) {
			
			String terms[]=new String[10000];
			int countTerms=0;
			String t="";
			while((t=br[i].readLine())!=null){
				String [] currTerms=t.split(" ");
				for(int j=0; j<currTerms.length; ++j){
					terms[countTerms]=currTerms[j];
					countTerms++;
				}
			}
			for(int j=0; j<countTerms; ++j) terms[j]=terms[j].toUpperCase();
			for(int j=0; j<countTerms; ++j) {
				if(!pi.containsKey(terms[j])) pi.put(terms[j], new HashMap<Integer, ArrayList<Integer>>());
				HashMap<Integer, ArrayList<Integer>> tmp=pi.get(terms[j]);
				if(!tmp.containsKey(i)) tmp.put(i, new ArrayList<Integer>());
				ArrayList<Integer> al=tmp.get(i);
				al.add(j);
			}
			
		}
		
	}
	
	void query(String s){
		
		String query[]=s.split(" ");
		queryLength=query.length;
		for(int i=0; i<query.length; ++i) {
			query[i]=query[i].toUpperCase();
			if(!pi.containsKey(query[i])) return;
		}
		al=pi.get(query[0]);
		for(int i=1; i<query.length; ++i){
			if(!pi.containsKey(query[i])) System.out.println(query[i]+" not found");
			al=intersect(al, pi.get(query[i]));
		}
		
	}
	
	public static HashMap<Integer, ArrayList<Integer>> intersect(HashMap<Integer, ArrayList<Integer>> mp1, HashMap<Integer, ArrayList<Integer>> mp2){
		HashMap<Integer, ArrayList<Integer>> res=new HashMap<Integer, ArrayList<Integer>>();
		for(Entry<Integer, ArrayList<Integer>> e1: mp1.entrySet()){
			for(Entry<Integer, ArrayList<Integer>>  e2: mp2.entrySet()){
				Integer k1=e1.getKey(), k2=e2.getKey();
				
				if(k1==k2){
					res.put(k1, new ArrayList<Integer>());
					ArrayList<Integer> tmp=res.get(k1);
					ArrayList<Integer> al1=e1.getValue();
					ArrayList<Integer> al2=e2.getValue();
					for(int i: al1){
						for(int j: al2){
							if(i-j==1 || i-j==-1) tmp.add(Math.max(i,j));
						}
					}
				}
				
			}
		}
		
		return res;
		
	}
	
	void printResult(){
		if(al.size()==0){
			System.out.println("NO SUCH DOCUMENT");
			return;
		}
		HashMap<Integer, ArrayList<Integer>> tal=new HashMap<Integer, ArrayList<Integer>>();
		for(Entry<Integer, ArrayList<Integer>>  e: al.entrySet()){
			ArrayList<Integer> res=e.getValue();
			if(res.size()>0){
				tal.put(e.getKey(), res);
			}
		}
		al=tal;
		System.out.println(al.size()+" documents satisfy the query.");
		for(Entry<Integer, ArrayList<Integer>>  e: al.entrySet()){
			System.out.print(pathnames.get(e.getKey())+": ");
			System.out.print("<");
			ArrayList<Integer> res=e.getValue();
			for(int i=0; i<res.size(); ++i){
				System.out.print((res.get(i)+2-queryLength));
				if(i<res.size()-1) System.out.print(", ");
			}
			System.out.println(">");
		}
	}
	
	static void printIndent(String s, int n){
		int t=s.length();
		for(int i=0; i<n-t; ++i){
			s+=" ";
		}
		System.out.print(s);
	}
	
	void showIndexing(){
		System.out.println("#########################################################");
		
		for(Entry<String, HashMap<Integer, ArrayList<Integer>>>e: pi.entrySet()){
			String s=e.getKey();
			printIndent(s+": ",15);
			HashMap<Integer, ArrayList<Integer>> tmp=e.getValue();
			
			for(Entry<Integer, ArrayList<Integer>> te: tmp.entrySet()){
				System.out.print(te.getKey()+1+": <");
				for(int i: te.getValue()){
					System.out.print((i+1)+" ");
				}
				System.out.print(">, ");
			}
			System.out.println();
		}
	}

}
