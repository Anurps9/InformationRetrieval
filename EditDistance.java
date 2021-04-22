package InformationRetrieval;
import java.util.*;

public class EditDistance {
	
	public void ED(Scanner sc){
		System.out.println("Enter two words:");
		char s[]=sc.next().toCharArray();
		char t[]=sc.next().toCharArray();
		
		int ed[][]=new int[s.length+1][t.length+1];
		for(int i=0; i<=s.length; ++i) {
			for(int j=0; j<=t.length; ++j) {
				if(i==0 || j==0) {
					ed[i][j]=i+j;
				} else {
					if(s[i-1]==t[j-1]) {
						ed[i][j]=ed[i-1][j-1];
					} else {
						ed[i][j]=Math.min(ed[i-1][j], ed[i][j-1]);
						ed[i][j]=Math.min(ed[i][j], ed[i-1][j-1]);
						ed[i][j]=ed[i][j]+1;
					}
				}
			}
		}
		
		System.out.println(ed[s.length][t.length]);
	}
	
	public static void main(String[] args) {
		
		Scanner sc=new Scanner(System.in);
		EditDistance ed=new EditDistance();	
		ed.ED(sc);
		sc.close();
		
	}
}
