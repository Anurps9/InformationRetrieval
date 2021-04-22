package InformationRetrieval;
import java.util.*;
import java.io.*;


public class TermFrequency{

	TermFrequency(ArrayList<String> pathnames, String query) throws Exception{
		HashMap<String, Integer> hs=new HashMap<>();
		int numberOfDocuments=pathnames.size();
		BufferedReader [] br=new BufferedReader[numberOfDocuments];
		for(int i=0; i<numberOfDocuments; ++i) br[i]=new BufferedReader(new FileReader(pathnames.get(i)));
		
		for(int i=0; i<numberOfDocuments; ++i) {
			String terms[]=new String[10000];
			int countTerms=0;
			String t="";
			while((t=br[i].readLine())!=null){
				String [] currTerms=t.split(" ");
				for(int j=0; j<currTerms.length; ++j){
					if(currTerms[j].equals(query)) countTerms++;
				}
			}
			System.out.println(pathnames.get(i)+": "+countTerms);
		}
		

	}


}