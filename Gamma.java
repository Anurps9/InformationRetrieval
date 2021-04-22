import java.util.*;
import java.io.*;

public class Gamma{
	public static void main(String[] args) {
		
		PrintStream out=System.out;
		Scanner sc=new Scanner(System.in);
		out.print("Enter number of docIds: ");
		int n=sc.nextInt();
		ArrayList<Integer> docIds=new ArrayList<>();
		for(int i=0; i<n; ++i) docIds.add(sc.nextInt());
		out.println("Gamma codes: "+encode(docIds));
		out.println("docIds: "+decode(encode(docIds)).toString());

	}

	static int log2(int n){
		return (int)(Math.log(n)/Math.log(2));
	}

	static String showBytes(int bytes, int n){
		String res=String.format("%"+n+"s", Integer.toBinaryString(bytes)).replace(' ', '0');
		return res;
	}

	static String encode(ArrayList<Integer> docIds){
		String res="";
		for(int i: docIds){
			res+=encodeNumber(i);
		}
		return res;
	}

	static String encodeNumber(int n){
		int pow=log2(n);
		String res=unary(pow);
		int offset=n-(int)Math.pow(2,pow);
		res+=showBytes(offset,pow);
		return res;
	}

	static ArrayList<Integer> decode(String s){
		ArrayList<Integer> docIds=new ArrayList<>();
		int offset=0;
		for(int i=0; i<s.length(); ++i){
			if(s.charAt(i)=='0'){
				docIds.add(Integer.parseInt('1'+s.substring(i+1,i+offset+1),2));
				i+=offset;
				offset=0;
			}else{
				offset++;
			}
		}
		return docIds;
	}

	static String unary(int n){
		String s="";
		for(int i=0; i<n; ++i){
			s+='1';
		}
		s+='0';
		return s;
	}

}