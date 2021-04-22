import java.util.*;
import java.io.*;

public class VBCode{


	public static void main(String[] args) {
		
		PrintStream out=System.out;
		Scanner sc=new Scanner(System.in);
		out.print("Enter number of docIds: ");
		int n=sc.nextInt();
		out.print("Enter docIDS: ");
		ArrayList<Integer> numbers=new ArrayList<>();

		for(int i=0; i<n; ++i){
			numbers.add(sc.nextInt());
		}

		ArrayList<Integer> res=encode(numbers);
		out.println("VB code: "+showBytes(res));

		out.println("docIds: "+decode(res));

	}

	static ArrayList<Integer> encodeNumber(int n){
		ArrayList<Integer> bytes=new ArrayList<>();
		while(true){
			bytes.add(n%128);
			if(n<128){
				break;
			}
			n=n/128;
		}
		int len=bytes.size();
		Collections.reverse(bytes);
		bytes.set(len-1, bytes.get(len-1)+128);
		return bytes;
	}

	static ArrayList<Integer> decode(ArrayList<Integer> byteStream){
		ArrayList<Integer> numbers=new ArrayList<>();
		int n=0;
		for(int i=0; i<byteStream.size(); ++i){
			if(byteStream.get(i)<128){
				n=128*n+byteStream.get(i);
			}else{
				n=128*n+(byteStream.get(i)-128);
				numbers.add(n);
				n=0;
			}
		}
		return numbers;
	}

	static ArrayList<Integer> encode(ArrayList<Integer> numbers){
		ArrayList<Integer> byteStream=new ArrayList<>();
		for(int n : numbers){
			ArrayList<Integer> bytes=encodeNumber(n);
			byteStream=extend(byteStream, bytes);
		}
		return byteStream;
	}

	static ArrayList<Integer> extend(ArrayList<Integer> a, ArrayList<Integer> b){
		ArrayList<Integer> c=new ArrayList<>();
		for(int i: a) c.add(i);
		for(int i: b) c.add(i);
		return c;
	}

	static String showBytes(ArrayList<Integer> bytes){
		String res="";
		for(int i: bytes){
			res+=String.format("%8s", Integer.toBinaryString(i)).replace(' ', '0');
		}
		return res;
	}

}