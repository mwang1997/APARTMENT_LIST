import java.io.IOException;
import java.util.LinkedList;

public class main{

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//List of social network
		LinkedList<String> socialnetwork;
		
		//The String to analyze
		String s = "LISTY";
		
		//The dictionary to check
		String fn = "dictionary";
		
		//DictionaryHandler to check
		DictionaryHandler dh = new DictionaryHandler(fn);
		
		System.out.println(dh.getDictionary().values().size());
		
		//Write a graph for n time searching next time
		dh.writeGraph();
		
		socialnetwork = dh.getSocialNetwork(s);
		
		//Size of the social network 
		System.out.println(socialnetwork.size());
	}

}
