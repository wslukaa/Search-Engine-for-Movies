
package IRUtilities;


import jdbm.RecordManager;
import jdbm.htree.HTree;
import jdbm.helper.FastIterator;
import java.io.IOException;
import java.io.Serializable;

/*
 * RecordManager can be interpreted as a manager of database, it responses for the communication with the hard disk, 
 * under which the objects created will not be lost after powering off. The content stored in recman is object, including
 * hashtable. Hashtable is only one implementation of indexing (others like Btree), it can store the records into memory but cannot write them into hard disk,
 * and it needs the help of record manager. That is why hash table has to be provided with an argument of record manager
 * during the creation. After being created, hash table will automatically be stored in the corresponding record manager.  */

public class InvertedIndex {
	private RecordManager recman;
	private HTree hashtable;

	public InvertedIndex(RecordManager recman1, String objectname) throws IOException {
		recman = recman1;
		long recid = recman.getNamedObject(objectname);

		if (recid != 0) {
			// If the object has already been recorded in record
			// manager;
			hashtable = HTree.load(recman, recid);
		}
		else // If not, create a new hashtable;
		{
			hashtable = HTree.createInstance(recman);
			recman.setNamedObject(objectname, hashtable.getRecid()); // Store object
																// hashtable ht1
																// into recman
																// as the name
																// "ht1";
		}
	}

	public void addEntry(String word, int x, int y) throws IOException {
		// Add a "docX Y" entry for the key "word" into hashtable
		// ADD YOUR CODES HERE
		// Test if a term has already existed in the inverted index file. If so, stop insertion and return directly.
		// If not, insert to the inverted file;
		// If there is no the following "if" block, the posting lists will accumulate after several time's running the code.
		
		if (hashtable.get(word)!=null && ((String) hashtable.get(word)).contains( "doc" + x+ " " + y))
		{
			return;
		}
		String new_entry = x + ":" + y + " ";
		// Since "put" will cover the previous insertion, it is necessary to
		// extract the existed data first;
		String existed_entry = "";
		if (hashtable.get(word) != null) {
			existed_entry = (String) hashtable.get(word);
		}

		hashtable.put(word, existed_entry + new_entry);
	    recman.commit();
	}
	
	public String getValue(String index) throws IOException{
		if(hashtable.get(index) == null){
			return "-1";
		}
		return String.valueOf(hashtable.get(index));
	}
	
	public void addEntry2(String word, String value) throws IOException {
		// Add a "docX Y" entry for the key "word" into hashtable
		// ADD YOUR CODES HERE
		// Test if a term has already existed in the inverted index file. If so, stop insertion and return directly.
		// If not, insert to the inverted file;
		// If there is no the following "if" block, the posting lists will accumulate after several time's running the code.
		
		if (hashtable.get(word)!=null && ((String) hashtable.get(word)).contains(value))
		{
			return;
		}
		String new_entry = value + " ";
		// Since "put" will cover the previous insertion, it is necessary to
		// extract the existed data first;
		String existed_entry = "";
		if (hashtable.get(word) != null) {
			existed_entry = (String) hashtable.get(word);
		}

		hashtable.put(word, existed_entry + new_entry);
	    //recman.commit();
	}
	
	
	public void delElement(String word, String new_word) throws IOException{
		String existed_entry = "";
		if (hashtable.get(word) != null) {
			existed_entry = (String) hashtable.get(word);
		}
		String newstr = existed_entry.replaceAll("\\s+["+ new_word +"]*\\s+", " ");
		hashtable.put(word, newstr);
	}
	
	public String getElement(String word, int index) throws IOException{
		if (hashtable.get(word) != null) {
			String existed_entry = (String) hashtable.get(word);
			String[] temp = existed_entry.split(" ");
			return temp[index];
		}
		return null;
	}
	
	public int numOfElement(String word) throws IOException{
		if (hashtable.get(word) != null) {
			String existed_entry = (String) hashtable.get(word);
			String[] temp = existed_entry.split(" ");
			return temp.length;
		}
		return 0;
	}

	public void delEntry(String word) throws IOException {
		hashtable.remove(word);
	}
	public FastIterator AllKey() throws IOException{
		return hashtable.keys();
	}
//missing fast iterator for final project//////////////////////////////////////
	
	public void printAll() throws IOException {
		// Print all the data in the hashtable
		// ADD YOUR CODES HERE
		FastIterator iter = hashtable.keys();
		String key;
		while ((key = (String) iter.next()) != null) {
			System.out.println(key + ": " + hashtable.get(key));
			
		}
	}
}