import java.util.LinkedList;

public class node {
	//String entry of the dictionary
	private String entry;
	
	//Friends of the entry
	private LinkedList<node> friends = new LinkedList<node>();
	
	/*
	 * Constructor only establishes the string assciated with the node
	 */
	public node(String s){
		entry = s;
	}
	
	/*
	 * Return the string that the node represents
	 */
	public String getString(){
		return entry;
	}
	
	/*
	 * Return the Linked List friend group
	 */
	public LinkedList<node> getFriends(){
		return friends;
	}
	
	/*
	 * Adds another node to the friend group
	 */
	public void setFriend(node friend){
		//If friends does not contain this node, add it and ensure the reciprocal relationship of friends
		if(!friends.contains(friend)){
			friends.add(friend);
			friend.setFriend(this);
		}
	}
}
