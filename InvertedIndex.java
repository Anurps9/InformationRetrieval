package InformationRetrieval;
import java.util.*;
import java.io.*;
import java.util.Map.Entry;

public class InvertedIndex {

	BufferedReader br[];
	int numberOfDocuments;
	int numberOfUniqueWords;
	ArrayList<String> [] collection;
	Map<String, Integer> wordToIndex;
	Map<Integer, String> indexToWord;
	LL[] invertedIndex;
	ArrayList<String> pathnames; 
	LL res;
	static final Set<String> qOpr = new HashSet<>(Arrays.asList(new String [] {"AND","NOT","OR"}));
	ArrayDeque<String> stack;
	
	InvertedIndex(ArrayList<String> pathnames) throws IOException{ 
		this.pathnames=pathnames;
		numberOfDocuments=pathnames.size();
		br=new BufferedReader[numberOfDocuments];
		for(int i=0; i<numberOfDocuments; ++i) br[i]=new BufferedReader(new FileReader(pathnames.get(i)));
		
		//initialize the collection
		collection=new ArrayList[numberOfDocuments];
		for(int i=0; i<numberOfDocuments; ++i) {
			collection[i]=new ArrayList<>();
		}
		
		//initialize the directories
		wordToIndex=new HashMap<>();
		indexToWord=new HashMap<>();
		
		stack=new ArrayDeque<>();
		storeCollection();
		createInvertedIndex();
		
	}
	
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
	
	void createInvertedIndex() {
		
		invertedIndex=new LL[numberOfUniqueWords];
		for(int i=0; i<numberOfUniqueWords; ++i)
			invertedIndex[i]=new LL();
		
		for(int i=0; i<numberOfDocuments; ++i) {
			
			for(int j=0; j<collection[i].size(); ++j) {
				
				int term=wordToIndex.get(collection[i].get(j));
				int doc=i;
				invertedIndex[term].insert(doc);
				
			}
			
		}
		
	}
	
	public void query(String s){
		
		String query[]=s.split(" ");
		for(int i=0; i<query.length; ++i) query[i]=query[i].toUpperCase();     
		
		res=null;
		
		for(String q: query) {												
			
			if(qOpr.contains(q)) {
				stack.push(q);
			} else {
				
				int termIndex=wordToIndex.get(q);

				if(stack.isEmpty()) {
					res=new LL(invertedIndex[termIndex]);
					continue;
				}
				
				String pq=stack.pop();

				if(pq.equals("NOT")) {
					LL tmp=invertedIndex[termIndex].negate();         
					if(stack.isEmpty()) res=tmp;
					else {
						String tpq=stack.pop();
						if(tpq.equals("AND")) res=res.merge(tmp);
						else res=res.union(tmp);
					}
				} else {
					if(pq.equals("OR")) res=res.union(invertedIndex[termIndex]);
					else if(pq.equals("AND")) res=res.merge(invertedIndex[termIndex]);
				}
				
			}
			
		}
		
	}
	
	public class Node{
		int data;
		Node next;
		Node(int d){
			data=d; next=null;
		}
		Node(){}
	}
	
	public class LL{
		int size;
		Node head;
		
		LL(){
			size=0; head=null;
		}
		
		LL(LL l){
			Node h=l.head;
			while(h!=null) {
				this.insert(h.data);
				h=h.next;
			}
		}
		
		public void insert(int n) {
			Node toInsert=new Node(n);
			if(head==null) {
				head=toInsert;
				return;
			}
			Node tmp=head;
			while(tmp.next!=null) tmp=tmp.next;
			tmp.next=toInsert;
			size++;
		}
		
		public LL merge(LL p1){
			
			Node n1=p1.head, n2=this.head;
			
			LL res=new LL();
			
			while(n1!=null && n2!=null) {
				if(n1.data==n2.data) {
					res.insert(n1.data);
					n1=n1.next; n2=n2.next;
				}
				else if(n1.data<n2.data) n1=n1.next;
				else n2=n2.next;
			}
			
			return res;
		}
		
		public LL union(LL p1) {
			
			LL res=new LL();
			
			Node n1=p1.head, n2=this.head;
			TreeSet<Integer> st=new TreeSet<>();
			
			while(n1!=null) {
				st.add(n1.data);
				n1=n1.next;
			}
			while(n2!=null) {
				st.add(n2.data);
				n2=n2.next;
			}
			for(int i: st) res.insert(i);
			return res;
		}
		
		public LL negate() {
			
			Set<Integer> ds=new HashSet<>();

			LL res=new LL();
			
			Node n1=this.head;
			
			while(n1!=null) {
				ds.add(n1.data);
				n1=n1.next;
			}
			
			for(int i=0; i<numberOfDocuments; ++i) {
				if(!ds.contains(i)) res.insert(i);
			}
			
			return res;
			
		}
				
	}

	public void printResult() {
			Node tmp=res.head;
			if(tmp==null) {
				System.out.println("NO SUCH DOCUMENT");
				return;
			}
			System.out.println(res.size+1+" documents satisfy the query.");
			while(tmp!=null) {
				System.out.println(pathnames.get(tmp.data));
				tmp=tmp.next;
			}
	}
	
	static void printIndent(String s, int n){
		int t=s.length();
		for(int i=0; i<n-t; ++i){
			s+=" ";
		}
		System.out.print(s);
	}

}
