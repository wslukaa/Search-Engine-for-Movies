package IRUtilities;
import jdbm.RecordManager;
import jdbm.htree.HTree;
import jdbm.helper.FastIterator;
import java.io.IOException;

public class IndexHelper {
	private RecordManager recman;
	private HTree hashtb1;
	private HTree hashtb2;
	private int Last_Index;

	public IndexHelper(RecordManager recman1, String Objname) throws IOException {
		recman = recman1;
		long recid1 = recman.getNamedObject(Objname+"1");
		long recid2 = recman.getNamedObject(Objname+"2");
		long recid3 = recman.getNamedObject(Objname+"Size"); 
		if (recid1 == 0) { 
			hashtb1 = HTree.createInstance(recman);
			recman.setNamedObject(Objname+"1", hashtb1.getRecid());
			hashtb2 = HTree.createInstance(recman);
			recman.setNamedObject(Objname+"2", hashtb2.getRecid());
			
			long recid4 = recman.insert(new Integer(0));
			recman.setNamedObject(Objname+"Size",recid4); 
		}
		// load hashtable
		else {
			hashtb1 = HTree.load(recman, recid1);
			hashtb2 = HTree.load(recman, recid2);
			Last_Index = (Integer)recman.fetch(recid3); 
		}
	}
	
	public int getLastIndex(){
		return Last_Index;
	}

	public boolean isContain(String word) throws IOException{
		if (hashtb1.get(word) == null){
			return false;
		}
		else {
			return true;
		}
	}
	
	public String getIndex(String word) throws IOException{
		if(hashtb1.get(word) != null){
		String result = String.valueOf(hashtb1.get(word));
		return result;
		}
		else
		return "-1";
	}

	public int getValueNumber(String index) throws IOException{
		if(hashtb2.get(index) != null){
		int result = new Integer((String)hashtb2.get(index));
		return result;
		}
		else
		return -1;
	}
	
	public String getValue(String index) throws IOException{
		if(hashtb2.get(index) != null){
		String result = String.valueOf(hashtb2.get(index));
		return result;
		}
		else
		return "-1";
	}

	public int getIndexNumber(String word) throws IOException{
		if(hashtb1.get(word) != null){
		int result = new Integer((String)hashtb1.get(word));
		return result;
		}
		else
		return -1;
	}

	public void delEntry(String word) throws IOException {

		if (hashtb1.get(word)!=null)Last_Index--;{
			hashtb2.remove(hashtb1.get(word));
			hashtb1.remove(word);
			
		}

	}
	
	public int addEntry(String word, String word2) throws IOException {
		
		if (hashtb1.get(word)!=null)
		{
			return getIndexNumber(word);
		}

		hashtb1.put(word, word2);
		hashtb2.put(word2, word);
		Last_Index++;
	    recman.commit();
	    return  Integer.parseInt(word2);
	}
	
	
	public void printAll() throws IOException {
	
		FastIterator iter = hashtb1.keys();
		String htktemp;
		while ((String) iter.next() != null) {
			htktemp = (String) iter.next();
			System.out.print(htktemp);
			System.out.print(": ");
			System.out.print(hashtb1.get(htktemp)+"\n");
		}
	}
}