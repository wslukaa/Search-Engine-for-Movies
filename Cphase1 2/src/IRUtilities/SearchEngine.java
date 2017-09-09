package IRUtilities;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

public class SearchEngine {
	//private String query;
	private  StopStem stopStem;
	private  Vector<String> TaskList;
	private  Vector<String> DoneList;
	private static IndexHelper PageIndexer;
	private static  IndexHelper WordIndexer;
	private static IndexHelper TitleIndexer;
	private static  InvertedIndex inverted;
	private static  InvertedIndex ForwardIndex;
	private static InvertedIndex pagePro;
	private static InvertedIndex ChildParent;
	private static RecordManager recman;
	private static InvertedIndex ParentChild;
	private static  InformationOfPage Pageppt;
	private static IndexHelper maxTermFreq;
	private static InvertedIndex termWth;
	private static InvertedIndex titleForwardIndex;
	private static InvertedIndex titleInverted;
	private static IndexHelper titleMaxTermFreq;
	
	public SearchEngine() throws IOException{
		stopStem = new StopStem("/Users/Eric/javaworkspace/Cphase1/src/IRUtilities/stopwords.txt");
		
		TaskList = new Vector<String>();
		DoneList = new Vector<String>();
		
		recman = RecordManagerFactory.createRecordManager("/Users/Eric/javaworkspace/Cphase1/src/IRUtilities/database");

		PageIndexer = new IndexHelper(recman, "page");
		WordIndexer = new IndexHelper(recman, "word");
		TitleIndexer = new IndexHelper(recman, "title");
		
		//word index
		inverted = new InvertedIndex(recman, "invertedIndex");
		ForwardIndex = new InvertedIndex(recman, "ForwardIndex");
		ChildParent = new InvertedIndex(recman, "ParentChild");
		ParentChild = new InvertedIndex(recman, "PC");
		Pageppt  = new InformationOfPage(recman, "PPT");
		maxTermFreq = new IndexHelper(recman, "maxTermFreq");
		termWth = new InvertedIndex(recman, "termWth");
	}
	
	public Vector<Link> search(Vector<String> keywords) throws IOException{
		//find word index
		Vector<String> keywordValue = new Vector<String>();
		for(int i = 0; i < keywords.size(); i++){
			String word = keywords.elementAt(i);
			if (!stopStem.isStopWord(word)){
				String temp = stopStem.stem(word);
				keywordValue.add(WordIndexer.getIndex(temp));
			}
		}
		
		//loop weight
		Hashtable<String, Double> map = new Hashtable<String,Double>();
		Hashtable<String, Double> map2 = new Hashtable<String,Double>();
		for(int i = 0; i< keywordValue.size(); i++){
			String[] temp = termWth.getValue(keywordValue.elementAt(i)).split(" ");
			for(int j = 0; j < temp.length; j++){
				String[] temp2 = temp[j].split(":");
				if(!map.containsKey(temp2[0])){
					double v2 = Double.parseDouble(temp2[1])*Double.parseDouble(temp2[1]);
					double v1 = Double.parseDouble(temp2[1]);
					map.put(temp2[0], v1);
					map2.put(temp2[0], v2);
				}else{
					double v2 = map2.get(temp2[0]) + Double.parseDouble(temp2[1])*Double.parseDouble(temp2[1]);
					double v1 = map2.get(temp2[0]) + Double.parseDouble(temp2[1]);
					map.put(temp2[0], v1);
					map2.put(temp2[0], v2);
					
				}
			}
		}
		Set<String> set = map2.keySet();
	    Iterator<String> itr = set.iterator();
	    while (itr.hasNext()) {
	    	String index = itr.next();
	    	map2.put(index, Math.sqrt(map2.get(index)));
	    }
	    set = map2.keySet();
	    itr = set.iterator();
	    Vector<Link> result = new Vector<Link>();
	    while (itr.hasNext()) {
	    	String index = itr.next();
	    	double totalScore = map.get(index)/(map2.get(index) * Math.sqrt(keywordValue.size()));
	    	//System.out.println(map.get(index)+", "+map2.get(index)+", "+map3.get(index)+"\n"+totalScore);
	    	//System.out.println(totalScore);
	    	result.add(toLink(index,totalScore));
	    }
	    Collections.sort(result);
	    return result;
	}
	
	public static Link toLink(String index, Double score) throws IOException{
		Link result = new Link();
		result.setScore(score);
		result.setTitle(Pageppt.getTitle(index));
		result.setUrl(Pageppt.getUrl(index));
		result.setLastUpdate(Pageppt.getLastDate(index));
		result.setPageSize(Pageppt.getPageSize(index));
		
		//keywords
		String WordList = ForwardIndex.getValue(index);
		String[] temp = WordList.split(" ");
		for(int i = 0; i < temp.length;i++){
			Vocab a = new Vocab();
			a.setText(temp[i]);
			String str = inverted.getValue(WordIndexer.getIndex(temp[i]));
			String[] temp2 = str.split(" ");
			for(int j = 0 ; j < temp2.length;j++){
				String[] temp3 = temp2[j].split(":");
				if(index.compareTo(temp3[0])==0){
					a.setFreq(Integer.parseInt(temp3[1]));
					result.addKeyword(a);
					break;
				}
			}
		}
		result.sortKeyword();
		
		//child Links
		String child = ParentChild.getValue(index);
		temp = child.split(" ");
		for(int i = 0; i < temp.length;i++){
			result.addChildLink(PageIndexer.getValue(temp[i]));
		}
		
		//parent Links
		String par = ChildParent.getValue(index);
		temp = par.split(" ");
		for(int i = 0; i < temp.length;i++){
			result.addParentLink(PageIndexer.getValue(temp[i]));
		}
		
		
		return result;
	}
}
