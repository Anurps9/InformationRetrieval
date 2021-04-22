package InformationRetrieval;
import java.util.*;
import java.io.*;
import java.util.Map.Entry;

public class BooleanRetrievalModel{

	BufferedReader br[];
	ArrayList<String> pathnames;
	int numberOfDocuments;
	int numberOfUniqueWords;
	ArrayList<String> [] collection;
	Map<String, Integer> wordToIndex;
	Map<Integer, String> indexToWord;
	boolean [][] termIncidenceMatrix;
	static final Set<String> qOpr = new HashSet<>(Arrays.asList(new String [] {"AND","NOT","OR"}));
	ArrayDeque<String> stack;
	boolean res[];

	//constructor


	BooleanRetrievalModel(ArrayList<String> documents) throws IOException{ 
		pathnames=documents;
		numberOfDocuments=pathnames.size();
		br=new BufferedReader[numberOfDocuments];
		for(int i=0; i<numberOfDocuments; ++i) br[i]=new BufferedReader(new FileReader(pathnames.get(i)));
		
		//stack to hold queries
		stack = new ArrayDeque<>();
		
		//initialize the collection
		collection=new ArrayList[numberOfDocuments];
		for(int i=0; i<numberOfDocuments; ++i) {
			collection[i]=new ArrayList<>();
		}
		
		//initialize the directories
		wordToIndex=new HashMap<>();
		indexToWord=new HashMap<>();
		
		
		storeCollection();
		createTermIncidenceMatrix();
	}


	//to store the collection
	void storeCollection() throws IOException{
		
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
				collection[i].add(terms[j]);
				if(!wordToIndex.containsKey(terms[j])) {
					++numberOfUniqueWords;
					wordToIndex.put(terms[j], numberOfUniqueWords-1);
					indexToWord.put(numberOfUniqueWords-1, terms[j]);
				}
				
			}
			
		}
		
	}
	
	//to create Term Incidence Matrix
	void createTermIncidenceMatrix() {
		
		termIncidenceMatrix=new boolean[numberOfUniqueWords][numberOfDocuments];
		
		for(int i=0; i<numberOfDocuments; ++i) {
			
			for(int j=0; j<collection[i].size(); ++j) {
				
				int row=wordToIndex.get(collection[i].get(j));
				int col=i;
				termIncidenceMatrix[row][col]=true;
				
			}
			
		}
		
	}
	
	//to process query
	boolean [] query(String s) {
		//divide query into tokens
		String query[]=s.split(" ");
		res=null;
		for(int i=0; i<query.length; ++i) query[i]=query[i].toUpperCase();     
		
		for(String q: query) {												
			
			if(qOpr.contains(q)) {
				stack.push(q);
			} else {
				int termIndex=wordToIndex.get(q);
				if(stack.isEmpty()) {
					res=termIncidenceMatrix[termIndex];
					continue;
				}
				
				String pq=stack.pop();

				if(pq.equals("NOT")) {
					boolean [] tmp=not(termIncidenceMatrix[termIndex]);         
					if(stack.isEmpty()) res=tmp;
					else {
						String tpq=stack.pop();
						if(tpq.equals("AND")) res=and(res,tmp);
						else res=or(res,tmp);
					}
				} else {
					if(pq.equals("OR")) res=or(res,termIncidenceMatrix[termIndex]);
					else if(pq.equals("AND")) res=and(res,termIncidenceMatrix[termIndex]);
				}
				
			}
			
		}
		
		return res;
		
	}
	
	static boolean [] and(boolean b1[], boolean b2[]) {
		boolean res[]=new boolean[b1.length];
		for(int i=0; i<b1.length; ++i) res[i]=b1[i]&b2[i];
		return res;
	}
	static boolean [] or(boolean b1[], boolean b2[]) {
		boolean res[]=new boolean[b1.length];
		for(int i=0; i<b1.length; ++i) res[i]=b1[i]|b2[i];
		return res;
	}
	static boolean [] not(boolean b1[]) {
		boolean res[]=new boolean[b1.length];
		for(int i=0; i<b1.length; ++i) res[i]=!b1[i];
		return res;
	}
	
	static void printIndent(String s, int n){
		int t=s.length();
		for(int i=0; i<n-t; ++i){
			s+=" ";
		}
		System.out.print(s);
	}
	
	//to show term Incidence matrix
	void showIndexing(){
		System.out.println("Term Incidence Matrix: ");
		for(Entry<String, Integer> e: wordToIndex.entrySet()) {
			
			printIndent(e.getKey(), 15);
			System.out.println(Arrays.toString(termIncidenceMatrix[e.getValue()]));
			
		}
	}

	void printResult(){
		if(res==null) System.out.println("NO SUCH DOCUMENT");
		int satisfy=0;
		for(boolean b: res) if(b) satisfy++;
		System.out.println(satisfy+" documents satisfy the query.");
		for(int i=0; i<res.length; ++i){
			if(res[i]){
				System.out.println(pathnames.get(i));
			}
		}
	}
			
}