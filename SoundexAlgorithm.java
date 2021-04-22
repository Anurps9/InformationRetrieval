package InformationRetrieval;
import java.util.*;

public class SoundexAlgorithm {

	public void SA (Scanner sc){
		System.out.println("Enter a word:");
		char s[]=sc.next().toCharArray();
		
		ArrayList<Character> al=new ArrayList<>();
		al.add(s[0]);
		for(int i=1; i<s.length; ++i) {
			if(s[i]=='a' || s[i]=='e' || s[i]=='i' || s[i]=='o' || 
					s[i]=='u' || s[i]=='y' || s[i]=='h' || s[i]=='w') {
				al.add('0');
			}else if(s[i]=='b' || s[i]=='f' || s[i]=='p' || s[i]=='v') {
				al.add('1');
			} else if(s[i]=='c' || s[i]=='g' || s[i]=='j' || s[i]=='k' 
					|| s[i]=='k' || s[i]=='q' || s[i]=='s' || s[i]=='x' || s[i]=='z') {
				al.add('2');
			}else if(s[i]=='d' || s[i]=='t') {
				al.add('3');
			}else if(s[i]=='l') {
				al.add('4');
			}else if(s[i]=='m' || s[i]=='n') {
				al.add('5');
			}else if(s[i]=='r'){
				al.add('6');
			}
			
		}
		
		//remove consecutive digits
		char prev='?';
		for(int i=0; i<al.size(); ++i) {
			if(al.get(i)!=prev) {
				prev=al.get(i);
			} else {
				al.remove(i);
			}
		}
		//remove zeroes
		for(int i=0; i<al.size(); ++i) {
			if(al.get(i)=='0') {
				al.remove(i);
			}
		}
		
		
		if(al.size()<4) {
			for(char c: al) System.out.print(c);
			for(int i=0; i<4-al.size(); ++i) System.out.print(0);
		}else {
			for(int i=0; i<4; ++i) {
				System.out.print(al.get((i)));
			}
			System.out.println();
		}
		
		sc.close();
		
	}
	
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		SoundexAlgorithm sa=new SoundexAlgorithm();
		sa.SA(sc);
	}
}
