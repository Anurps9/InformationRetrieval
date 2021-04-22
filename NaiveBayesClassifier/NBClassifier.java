import java.io.*;
import java.util.*;

public class NBClassifier{

	ArrayList<String> classes;
	HashMap<String, Integer> classIndex;
	ArrayList<String> docs;
	ArrayList<Integer> classLabel;
	ArrayList<String> vocab;
	HashSet<String> vocabSet;
	ArrayList<String> [] classWiseDocuments;
	double condProb[][];
	double prior[];

	void initClasses(String trainDir){
		classes=new ArrayList<>();
		File dir=new File(trainDir);
		classIndex=new HashMap<>();
		File [] trainFiles=dir.listFiles();
		int index=0;
		for(File file: trainFiles){
			String s=file.getPath().substring(trainDir.length()+1);
			classes.add(s);
			index++;
			classIndex.put(s,index);
		}
	}

	void initDocs(String trainDir) {
		classWiseDocuments=new ArrayList[classes.size()];
		for(int i=0; i<classes.size(); ++i) classWiseDocuments[i]=new ArrayList<>();
		classLabel=new ArrayList<>();
		docs=new ArrayList<>();
		int i=0;
		for(String cls: classes){
			File clsDir=new File(trainDir+"/"+cls);
			File [] files=clsDir.listFiles();
			for(File file: files){
				docs.add(file.getAbsolutePath());
				classLabel.add(classIndex.get(cls));
				classWiseDocuments[i].add(file.getAbsolutePath());
			}
			i++;
		}
	}

	void  extractVocab() throws Exception{
		vocab=new ArrayList<>();
		vocabSet=new HashSet<>();
		for(String doc: docs){
			File file=new File(doc);
			String t;
			BufferedReader br=new BufferedReader(new FileReader(file));
			while((t=br.readLine())!=null){
				String tokens[]=t.split(" ");
				for(String token: tokens) vocabSet.add(token);
			}
		}
		for(String s: vocabSet) vocab.add(s);
	}

	void train(String trainDir) throws Exception{
		
		initClasses(trainDir);
		initDocs(trainDir);
		extractVocab();
		int n=docs.size();
		condProb=new double[vocab.size()][classes.size()];
		prior=new double[classes.size()];

		for(int i=0; i<classes.size(); ++i){
			ArrayList<String> docsInClass=classWiseDocuments[i];
			int nc=docsInClass.size();
			prior[i]=(double)nc/n;
			// System.out.println(docsInClass.toString());
			ArrayList<String> text=getTextOfClass(docsInClass);
			// System.out.println(text.toString());
			int TCT[]=new int[vocab.size()];
			int sum=0;

			for(int j=0; j<vocab.size(); ++j){
				String t=vocab.get(j);
				for(String term: text){
					if(t.equals(term)){
						TCT[j]++;
					}
				}
			}

			for(int j: TCT) sum+=j+1;

			for(int j=0; j<vocab.size(); ++j){
				condProb[j][i]=(double)(TCT[j]+1)/sum;
			}
		}
		for(int j=0; j<vocab.size(); ++j){
			System.out.println(Arrays.toString(condProb[j]));
		}
		System.out.println(Arrays.toString(prior));

	}

	ArrayList<String> getTextOfClass(ArrayList<String> docsInClass) throws Exception{
		ArrayList<String> text=new ArrayList<>();
		for(String doc: docsInClass){
			File file=new File(doc);
			String t;
			BufferedReader br=new BufferedReader(new FileReader(file));
			while((t=br.readLine())!=null){
				String tokens[]=t.split(" ");
				for(String token: tokens) text.add(token);
			}
		}
		return text;
	}

	String classify(String d){
		String [] tokens=d.split(" ");
		ArrayList<String> selectTokens=new ArrayList<>();
		for(String t: tokens){
			if(vocabSet.contains(t)){
				selectTokens.add(t);
			}
		}

		double score[]=new double[classes.size()];
		for(int i=0; i<classes.size(); ++i){
			score[i]=Math.log(prior[i]);
			for(String t: selectTokens){
				score[i]+=Math.log(condProb[getIndex(t)][i]);
			}
		}

		double maxScore=Integer.MIN_VALUE;
		int optimalClass=0;
		for(int i=0; i<classes.size(); ++i){
			if(maxScore<score[i]){
				maxScore=score[i];
				optimalClass=i;
			}
		}
		System.out.println(Arrays.toString(score));
		System.out.println(optimalClass);
		return classes.get(optimalClass);

	}

	int getIndex(String s){
		for(int i=0; i<vocab.size(); ++i){
			if(s.equals(vocab.get(i))){
				return i;
			}
		}
		return -1;
	}

	public static void main(String[] args) throws Exception {
		NBClassifier nb=new NBClassifier();
		nb.train("D:/Eclipse_Projects/Practice/src/InformationRetrieval/NaiveBayesClassifier/docs/train");	
		String res=nb.classify("");
		System.out.println(res);
	}

}