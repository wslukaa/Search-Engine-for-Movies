package IRUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.helper.FastIterator;

public class Filtering {
	private static InvertedIndex ForwardIndex;
	private static ArrayList<String> keywords;
	private static RecordManager recman;

	public Filtering() throws IOException{
		recman = RecordManagerFactory.createRecordManager("/Users/Eric/javaworkspace/Cphase1/src/IRUtilities/database");
		ForwardIndex = new InvertedIndex(recman, "ForwardIndex");
		keywords = new ArrayList<String>();
	}
	
	//give keywords of the database
	public static ArrayList<String> getkeywords() throws IOException{
		FastIterator iter = ForwardIndex.AllKey();
		String key, index;
		while ((index = (String) iter.next()) != null) {
			key = ForwardIndex.getValue(index);
			String[] keyword = key.split(" ");
			for (int  i = 0 ; i < keyword.length; i++){
				keywords.add(keyword[i]);	
			}
		}
		ArrayList<String> RandomBlock = new ArrayList<String>();
		for (int i = 0; i < 10; i++){
			int random = (int)(Math.random()*10000)%keywords.size();
			RandomBlock.add(keywords.get(random));
		}
		return RandomBlock;
	}
}
