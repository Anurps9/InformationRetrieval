import java.util.*;
import java.io.*;

public class NaiveBayesClassifier{

	ArrayList<String> classes;
	File [] trainingDocs;
	File classList;

	NaiveBayesClassifier(){
		classList="D:/Eclipse_Projects/Practice/src/InformationRetrieval/NaiveBayesClassifier/classes.txt";
		File trainingDir=new File("D:/Eclipse_Projects/Practice/src/InformationRetrieval/NaiveBayesClassifier/docs/training");
		trainingDocs=trainingDir.listFiles();
	}

	public void train(){

		HashSet<String> vocabulary=getVocabulary();
		for(String s: vocabulary) System.out.println(s);


	}

	public HashSet<String> getVocabulary(){
		BufferedReader br;
		HashSet<String> vo=new HashSet<>();
		for(File file: trainingDocs){
			try{
				br=new BufferedReader(new FileReader(file));
			}catch(FileNotFoundEXcetpion e){
				e.printStackTrace();
			}
			
			String t;
			while((t=br.readLine())!=null){
				String v[]=t.split(" ");
				for(String s: v) vo.add(s);
			}

		}
		return vo;
	}



	public static void main(String[] args) {
		


	}

}