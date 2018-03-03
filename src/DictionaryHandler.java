import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Hashtable;
import java.util.LinkedList;

/*
 * A class to take in a text document and create a graph and encode it into a new text document.
 */
public class DictionaryHandler {
	//Linked List of all dictionary entries
	private Hashtable<String, node> dictionary = new Hashtable<String, node>();
	//Path of textFile
	String textFile;
	//Path of graphFile
	String graphFile;
	
	
	/*
	 * Constructor to create a graph structure if it doesn't exist and open it if it does
	 */
	public DictionaryHandler(String textFile) throws IOException{
		//Assigning the textFile
		this.textFile = textFile + ".txt";
		//Assigning the graphFile
		this.graphFile = textFile + "_graph.txt";
		
		//If there already exists a textGraph of the dictionary
		if(new File(graphFile).isFile()){
			//FilerReader for the graph
			FileReader gr = new FileReader(this.graphFile);
			//FileReader for the dictionary
			FileReader fr = new FileReader(this.textFile);
			//StringBuilder for each entry
			StringBuilder sb = new StringBuilder();
			
			// Goes through the text file and add to the dictionary
			for(int i = fr.read(); i != -1; i = fr.read()){
				if(i != '\n' && i != ' ' && i != '\r'){
					sb.append((char)i);
				}
				else if(i == '\r'){
					dictionary.put(sb.toString(), new node(sb.toString()));
					sb = new StringBuilder();
				}
			}
			
			dictionary.put(sb.toString(), new node(sb.toString()));
			fr.close();
			
			//Refresh the StringBuilder
			sb = new StringBuilder();
			//Boolean to confirm a new line
			boolean newLine = true;
			//Node to store friend information
			node tempNode = null;
			
			//Goes through the graph file and adds each entry and it's friends
			for(int i = gr.read(); i != -1; i = gr.read()){
				//If the word is not done building
				if(i <= 90 && i >= 65){
					sb.append((char)i);
				}
				//When a String is completed
				else if(sb.length() != 0){
					//If its not first line and therefore not the entry node
					if(!newLine){
						tempNode.setFriend(dictionary.get(sb.toString()));
					}
					//Or else set the tempNode and continue
					else if(newLine){
						newLine = false;
						tempNode = dictionary.get(sb.toString());
					}
					//Refresh the StringBuilder;
					sb = new StringBuilder();
				}
				//New line creates a new entry
				if(i == '\n'){
					newLine = true;
				}
			}
			gr.close();
		}
		//Else create a new File for the graph structure
		else{
			//FileReader for the dictionary
			FileReader fr = new FileReader(this.textFile);
			//StringBuilder for each entry
			StringBuilder sb = new StringBuilder();
			
			// Goes through the text file and add to the dictionary
			for(int i = fr.read(); i != -1; i = fr.read()){
				if(i != '\n' && i != ' ' && i != '\r'){
					sb.append((char)i);
				}
				else if(i == '\r'){
					dictionary.put(sb.toString(), new node(sb.toString()));
					sb = new StringBuilder();
				}
			}
			
			dictionary.put(sb.toString(), new node(sb.toString()));
			fr.close();
			
			/*
			 * Goes through and finds all the friends of entries in the dictionary
			 */
			for(node entry: dictionary.values()){
				for(node friend: dictionary.values()){
					//If they are friends set them as friends
					if(checkFriends(entry.getString(), friend.getString())){
						entry.setFriend(friend);
					}
				}
			}
		}
	}
	
	/*
	 * Method to check if two separate strings have a Levenshtein distance of one
	 */
	public static boolean checkFriends(String entry, String friend){
		//StringBuilder for comparing two different Strings
		StringBuilder sb;
		//If the two entries are one character length apart
		if(entry.length() - friend.length() == 1){
			//This loop goes through the word entry and removes a single word to check if entry is a friend
			for(int i = 0; i < entry.length(); i++){
				sb = new StringBuilder(entry);
				sb.deleteCharAt(i);
				if(sb.toString().equals(friend)){
					return true;
				}
			}
		}
		else if(friend.length() - entry.length() == 1){
			//This loop goes through the word friend and removes a single word to check if entry is a friend
			for(int i = 0; i < friend.length(); i++){
				sb = new StringBuilder(friend);
				sb.deleteCharAt(i);
				if(sb.toString().equals(entry)){
					return true;
				}
			}
		}
		//If the entries are the same length
		else if(entry.length() == friend.length()){
			//This loop goes through each character in both words and compare for more than one difference
			int j = 0;
			for(int i = 0; i < friend.length(); i++){
				if(entry.charAt(i) != friend.charAt(i)){
					j++;
				}
			}
			if(j == 1){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Method to return the social network of any given word
	 */
	public LinkedList<String> getSocialNetwork(String s){
		//The entire social network
		LinkedList<String> socialNetwork = new LinkedList<String>();
		//The two different friend groups, friends of friends and friends
		LinkedList<node> friends1 = new LinkedList<node>();
		LinkedList<node> friends2 = new LinkedList<node>();
		//The initial node
		node initialNode = null;
		
		//Checks if a entry exists in the dictionary
		if(dictionary.containsKey(s)){
			initialNode = dictionary.get(s);
			friends1 = initialNode.getFriends();
		}
		
		//If an entry for the given string does not exist
		if(initialNode == null){
			initialNode = new node(s);
			//Find all the friends of initial node
			for(node entry: dictionary.values()){
				if(checkFriends(s, entry.getString())){
					friends1.add(entry);
				}
			}
		}
		
		//Add the initial node to the social network
		socialNetwork.add(initialNode.getString());
		//Add the friends of the initial node to the social network
		for(node friend: friends1){
			socialNetwork.add(friend.getString());
		}
		//the previous size of the social network
		int size = -1;
		
		//Until the socialNetwork no longer expands
		while(socialNetwork.size() != size) {
			//Size Update
			size = socialNetwork.size();
			//For each entry of friends1
			for(node entry: friends1){
				//Check all their friends
				for(node friend: entry.getFriends()){
					//If they are not already added into the social network add them into friends2
					if(!socialNetwork.contains(friend.getString())) {
						socialNetwork.add(friend.getString());
						friends2.add(friend);
					}
				}
			}
			//Size update
			size = socialNetwork.size();
			//Refresh friends1
			friends1 = new LinkedList<node>();
			
			//For each entry of friends2
			for(node entry: friends2){
				//Check all their friends
				for(node friend: entry.getFriends()){
					//If they are not already added into the social network add them into friends2
					if(!socialNetwork.contains(friend.getString())){
						socialNetwork.addFirst(friend.getString());
						friends1.add(friend);
					}
				}
			}
			//Refresh friends2
			friends2 = new LinkedList<node>();
		}
		
		return socialNetwork;
	}

	/*
	 * Method to return the dictionary
	 */
	public Hashtable<String, node> getDictionary(){
		return dictionary;
	}
	
	/*
	 * Method to create a text document given a complete graph to save the information
	 */
	public void writeGraph() throws IOException{
		//If a graph already exists
		if(new File(graphFile).isFile()){
			return;
		}
		
		//If the graph has not been created yet
		Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(graphFile), "utf-8"));
		
		//Write the entry word in each line, with friends separated by spaces and a new entry on each line
		for(node entry: dictionary.values()) {
			w.write(entry.getString());
			for(node friend: entry.getFriends()) {
				w.write(' ');
				w.write(friend.getString());
			}
			w.write("\r\n");
		}
		w.close();
	}
}
