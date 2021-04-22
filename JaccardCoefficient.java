package InformationRetrieval;
import java.util.*;

public class JaccardCoefficient {
	
	public void JC(Scanner sc){
		System.out.print("Enter query: ");
		String query=sc.next();
		System.out.print("Enter term: ");
		String term=sc.next();
		System.out.print("Enter value of k: ");
		int k=sc.nextInt();
		Set<String> intersection=new HashSet<>();
		Set<String> union=new HashSet<>();
		System.out.print(query+": ");
		for(int i=0; i<query.length()-k+1; ++i) {
			String tmp=query.substring(i,i+k);
			union.add(tmp);
			System.out.print(tmp+" ");
			if(term.contains(tmp)) intersection.add(tmp);
		}
		System.out.println();
		System.out.print(term+": ");
		for(int i=0; i<term.length()-k+1; ++i) {
			String tmp=term.substring(i,i+k);
			System.out.print(tmp+" ");
			union.add(tmp);
		}
		System.out.println();
		
		System.out.println((double)intersection.size()/union.size());
	}
	
	public static void main(String[] args) {
		
		Scanner sc=new Scanner(System.in);
		JaccardCoefficient jc=new JaccardCoefficient();
		jc.JC(sc);
		sc.close();
		
	}

}
