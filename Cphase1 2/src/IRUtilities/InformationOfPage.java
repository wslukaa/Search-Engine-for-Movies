package IRUtilities;


import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;
import jdbm.helper.FastIterator;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.text.html.parser.Parser;

 class Property implements Serializable {
	public String title;
	public String url;
	public String lastModifyDate;
	public int size;

	Property(String _title, String _url, String _lastModifyDate, int _size){
		title = _title;
		url = _url;
		lastModifyDate = _lastModifyDate;
		size = _size;
	}
}

public class InformationOfPage {
	private RecordManager recman;
	private HTree hashtable;

	public InformationOfPage(RecordManager _recman, String objectname) throws IOException {
		recman = _recman;
		long recid = recman.getNamedObject(objectname);

		if (recid == 0) {
			hashtable = HTree.createInstance(recman);
			recman.setNamedObject(objectname, hashtable.getRecid());
		}
		else // If not, load hashtable;
		{
			hashtable = HTree.load(recman, recid);
		}
	}
	
	public boolean isContain(String word) throws IOException{
		if (hashtable.get(word)!=null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void finalize() throws IOException {
		recman.commit();
		recman.close();
	}
	
	public void newElementToAdd(String _key, String _title, String _url, String _lastModifyDate, int _size) throws IOException {
		if (hashtable.get(_key) == null){
			hashtable.put(_key, new Property(_title, _url, _lastModifyDate, _size));
		}
		else {
			return;
		}
	}
	
	public Property gethastable(String _key) throws IOException{
		Property curr_page = (Property)hashtable.get(_key);
		return curr_page;
	}
	
	public String getTitle(String _key) throws IOException{
		Property curr_page = gethastable(_key);
		return curr_page.title;
	}
	
	public String getUrl(String _key) throws IOException{
		Property curr_page = gethastable(_key);
		return curr_page.url;
	}
	
	public String getLastDate(String _key) throws IOException{
		Property curr_page = gethastable(_key);
		return curr_page.lastModifyDate;
	}
	
	public int getPageSize(String _key) throws IOException{
		Property curr_page = gethastable(_key);
		return curr_page.size;
	}
	
	public void update(String _key, String _title, String _url, String _lastModifyDate, int _size) throws IOException{
		if (hashtable.get(_key) == null){
			return;
		}
		else {
			Property curr_page = gethastable(_key);
			curr_page.title = _title;
			curr_page.url = _url;
			curr_page.lastModifyDate = _lastModifyDate;
			curr_page.size = _size;
		}
	}
	
	public void delEntry(String word) throws IOException {
		hashtable.remove(word);
	}
	
	public void printAll() throws IOException {
		FastIterator iter = hashtable.keys();
		String key;
		while (((String) iter.next()) != null) {
			key = (String) iter.next();
			Property curr_page = gethastable(key);
			System.out.println(key + ": " + curr_page.title+ " , "+ curr_page.url + " , "+ curr_page.lastModifyDate+ " , " + curr_page.size);
		}
	}
}
