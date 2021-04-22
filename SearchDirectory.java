package InformationRetrieval;
import java.util.*;
import java.io.*;

public class SearchDirectory{

	public static void main(String[] args) throws Exception {
		
		BufferedReader sc=new BufferedReader(new InputStreamReader(System.in));
		ArrayList<String> pathnames=new ArrayList<>();
		String currDir=System.getProperty("user.dir");
	
		explore(currDir, pathnames);
		BooleanRetrievalModel brm=new BooleanRetrievalModel(pathnames);
		InvertedIndex ii=new InvertedIndex(pathnames);
		PositionalIndex pi=new PositionalIndex(pathnames);
		TermFrequency tf;
		String q;

			System.out.println("Choose one model:\n1. Boolean Retrieval Model\n2. Inverted Indexing\n3. Positional Indexing\n4. Term Frequency");
			int choice=Integer.parseInt(sc.readLine());
			switch(choice){

				case 1: 
					System.out.print("Enter a boolean query: ");
					q=sc.readLine();
					brm.query(q);
					brm.printResult();
					break;

				case 2: 
					System.out.print("Enter a boolean query: ");
					q=sc.readLine();
					ii.query(q);
					ii.printResult();
					break;

				case 3:
					System.out.print("Enter a phrase query: ");
					q=sc.readLine();
					pi.query(q);
					pi.printResult();
					break;

				case 4: 
					System.out.print("Enter a word: ");
					q=sc.readLine();
					new TermFrequency(pathnames, q);
					break;

				default: break;

			}
	
		
	}

	static void explore(String startDir, ArrayList<String> al){
		File dir=new File(startDir);
		File [] files=dir.listFiles();

		if(files==null) return;

		for(File file: files){
			if(file.isDirectory()){
				explore(file.getAbsolutePath(),al);
			}else{
				al.add(file.getAbsolutePath());
			}
		}
	}

}