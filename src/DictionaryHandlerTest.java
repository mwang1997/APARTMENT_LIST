import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Random;

import org.junit.jupiter.api.Test;

class DictionaryHandlerTest{

	//Tests the checkFriends method
	@Test
	public void checkFriendsTest(){
		//Tests checkFriends with a 1 length string and a non-friend
		assertFalse(DictionaryHandler.checkFriends("H", "HELLO"));
		
		//Tests checkFriends with a 1 length string and a friend with an extra character
		assertTrue(DictionaryHandler.checkFriends("H", "HE"));
		
		//Tests checkFriends with a 1 length string and a friend with a different character
		assertTrue(DictionaryHandler.checkFriends("H", "A"));
		
		//Tests checkFriends with a multiple length string and a non-friend
		assertFalse(DictionaryHandler.checkFriends("HELLO", "COW"));
		
		//Tests checkFriends with a multiple length string and a friend with an extra character
		assertTrue(DictionaryHandler.checkFriends("HELLO", "HELLON"));
		
		//Tests checkFriends with a multiple length string and a friend with a different character
		assertTrue(DictionaryHandler.checkFriends("HELLO", "JELLO"));
		
		//Tests checkFriends with the same string
		assertFalse(DictionaryHandler.checkFriends("SIMIAN", "SIMIAN"));
	}

	//Tests the DictionaryHandler constructor
	@Test
	public void dictionaryHandlerConstructorTest() throws IOException {
		//Delete the graph
		File f = new File("test_one_dictionary_graph.txt");
		if(f.exists()) {
			f.delete();
		}
		
		//Create a dictionary with 1 entry
		DictionaryHandler dh = new DictionaryHandler("test_one_dictionary");
		
		//The size of the dictionary should be 1
		assertTrue(dh.getDictionary().size() == 1);
		
		//Create a graph_file to test building with a graph
		dh.writeGraph();
		
		//Create a dictionary with a small amount of entries
		dh = new DictionaryHandler("test_one_dictionary");
		
		//The size of the dictionary should be 1
		assertTrue(dh.getDictionary().size() == 1);
		
		//Delete the graph
		f = new File("test_two_dictionary_graph.txt");
		if(f.exists()) {
			f.delete();
		}
		
		//DictionaryHandler with a small amount of entries
		dh = new DictionaryHandler("test_two_dictionary");
		
		//The size of the dictionary should be 8
		assertTrue(dh.getDictionary().size() == 8);
		
		//Tests the constructor's dictionary's friends
		for(node entry: dh.getDictionary().values()){
			//Amount of friends
			int i = 0;
			
			//Checks the amount of friends from the  dictionary
			for(node compEntry: dh.getDictionary().values()) {
				if(DictionaryHandler.checkFriends(entry.getString(), compEntry.getString())){
					i++;
				}
			}
			
			//Assert the amount of friends in the data structure is the same as the checked
			assertTrue(i == entry.getFriends().size());
			
			//Checks that all the strings in friends are friends
			for(node friend: entry.getFriends()){
				System.out.print(entry.getString() + ": " + friend.getString() + '\n');
				assertTrue(DictionaryHandler.checkFriends(entry.getString(), friend.getString()));
			}
		}
		
		//Create a graph_file to test building with a graph
		dh.writeGraph();
		
		//Re-create DictionaryHandler with a graph
		dh = new DictionaryHandler("test_two_dictionary");
		
		//The size of the dictionary should be 8
		assertTrue(dh.getDictionary().values().size() == 8);
		
		//Tests the constructor's dictionary's friends
		for(node entry: dh.getDictionary().values()){
			//Amount of friends
			int i = 0;
			
			//Checks the amount of friends from the  dictionary
			for(node compEntry: dh.getDictionary().values()) {
				if(DictionaryHandler.checkFriends(entry.getString(), compEntry.getString())){
					i++;
				}
			}
			
			//Assert the amount of friends in the data structure is the same as the checked
			assertTrue(i == entry.getFriends().size());
			
			//Checks that all the strings in friends are friends
			for(node friend: entry.getFriends()){
				System.out.print(entry.getString() + ": " + friend.getString() + '\n');
				assertTrue(DictionaryHandler.checkFriends(entry.getString(), friend.getString()));
			}
		}
	}
	
	//Tests the getSocialNetwork method
	@Test
	public void getSocialNetworkTest() throws IOException{
		//Constructor for a small sized dictionary
		DictionaryHandler dh = new DictionaryHandler("test_three_dictionary");
		
		//The size for LISTY should be 5
		assertEquals(dh.getSocialNetwork("LISTY").size(), 5);
		
		//The size for anything in the social network of LISTY should be the same as LISTY
		for(String s: dh.getSocialNetwork("LISTY")) {
			assertEquals(dh.getSocialNetwork(s).size(), 5);
		}
		
		//Generate a random file from dictionary
		LinkedList<String> dictionary = new LinkedList<String>();
		//FileReader for the dictionary
		FileReader fr = new FileReader("test_four_dictionary.txt");
		//StringBuilder for each entry
		StringBuilder sb = new StringBuilder();
		
		// Goes through the text file and add to the dictionary
		for(int i = fr.read(); i != -1; i = fr.read()){
			if(i != '\n' && i != ' ' && i != '\r'){
				sb.append((char)i);
			}
			else if(i == '\r'){
				dictionary.add(sb.toString());
				sb = new StringBuilder();
			}
		}
		
		dictionary.add(sb.toString());
		Object[] arrayDictionary = dictionary.toArray();
		fr.close();
		
		//Create a test file from nothing
		File f = new File("test_four_sample_dictionary.txt");
		//New FileWriter to clear the file
		FileWriter fw = new FileWriter(f);
		fw.write("");
		fw.close();
		
		//Randomly picking from test_four_dictionary and getting sample size
		Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test_four_sample_dictionary.txt"), "utf-8"));
		//Random number generator
		Random r = new Random();
		//LinkedList of sample words
		LinkedList<String> sampleWords = new LinkedList<String>();
		//500 of the words from test_four_dictionary are going to be in the file
		while(sampleWords.size() < 1000){
			int x = r.nextInt(arrayDictionary.length - 1);
			if(!sampleWords.contains((String)arrayDictionary[x])){
				sampleWords.add((String)arrayDictionary[x]);
				w.write((String)arrayDictionary[x] + "\r\n");
			}
		}
		
		w.close();
		
		//The randomly generated dictionary
		dh = new DictionaryHandler("test_four_sample_dictionary");

		//The size for anything in the social network should be the same as it's social network
		for(node entry: dh.getDictionary().values()) {
			int size = dh.getSocialNetwork(entry.getString()).size();
			for(node friend: entry.getFriends()){
				assertEquals(dh.getSocialNetwork(friend.getString()).size(), size);
			}
		}
	}
}
