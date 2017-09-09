package IRUtilities;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.helper.FastIterator;

import org.htmlparser.util.ParserException;

import java.net.Authenticator;
import java.net.PasswordAuthentication;



public class spider {
	
	private static int numOfPage = 0;
	private static StopStem stopStem = new StopStem("/Users/Eric/javaworkspace/Cphase1/src/IRUtilities/stopwords.txt");
	private static Vector<String> StoringWorkToBeComplete = new Vector<String>();
	private static Vector<String> StoringWorkWhichIsFinished = new Vector<String>();
	private static IndexHelper PageIn;
	private static IndexHelper WordIndex;
	private static IndexHelper TitleIn;
	private static InvertedIndex inverted;
	private static InvertedIndex ForwardIndex;
	private static InvertedIndex Child;
	private static InvertedIndex Parent;
	private static RecordManager recman;
	private static IndexHelper mft;
	private static InvertedIndex twh;
	private static InformationOfPage InformationOfThePage;
	private static final int maximum_num_pages = 300;
	public static void main(String[] args) throws IOException {
		
		try
		{
			
			Authenticator.setDefault(new Authenticator() {
		        protected PasswordAuthentication getPasswordAuthentication() {
		            return new PasswordAuthentication("YOUR_USERNAME","YOUR_PASSWORD".toCharArray());
		        }
		    });
			
			
			//create the .db file required
			recman = RecordManagerFactory.createRecordManager("/Users/Eric/javaworkspace/Cphase1/src/IRUtilities/database");
			ForwardIndex = new InvertedIndex(recman, "ForwardIndex");
			PageIn = new IndexHelper(recman, "page");
			InformationOfThePage  = new InformationOfPage(recman, "PPT");
			WordIndex = new IndexHelper(recman, "word");
			TitleIn = new IndexHelper(recman, "title");
			Parent = new InvertedIndex(recman, "PC");
			mft = new IndexHelper(recman, "maxTermFreq");
			twh = new InvertedIndex(recman, "termWth");
			
			Child = new InvertedIndex(recman, "ParentChild");
			inverted = new InvertedIndex(recman, "invertedIndex");
			System.out.println("start");
			
			fetchPages("https://course.cse.ust.hk/comp4321/labs/TestPages/testpage.htm");
			System.out.println("page number:"+numOfPage);
			while(!StoringWorkToBeComplete.isEmpty() && numOfPage < maximum_num_pages){
				
				if(StoringWorkWhichIsFinished.contains(StoringWorkToBeComplete.firstElement())){
					StoringWorkToBeComplete.removeElementAt(0);
					continue;
				}
				fetchPages(StoringWorkToBeComplete.firstElement());
				StoringWorkToBeComplete.removeElementAt(0);
				System.out.println("page number:"+numOfPage);
			}
			
			
			FastIterator iter =  inverted.AllKey();//in order to find out the weight of the terms
			String key;
			while ((key = (String) iter.next()) != null) {
				int df = inverted.numOfElement(key);
				for(int i = 0; i < df ; i++){
					String[] temp = inverted.getElement(key, i).split(":");
					int maxTF = mft.getIndexNumber(temp[0]);
					int tf = Integer.parseInt((temp[1]));
					double weight = termWeight(tf, maxTF, df, maximum_num_pages);
					twh.addEntry2(key, temp[0]+":"+weight);
				}
			}
			
			recman.commit();
			recman.close();
			System.out.println("Finished");
		}
		catch (ParserException e)
		{
			e.printStackTrace ();
		}
	}
	
	public static double termWeight(double tf, double maxTf, double numOfDoc, double maxOfDoc){
		double idf = Math.log(maxOfDoc/numOfDoc)/Math.log(2);
		return (tf*idf)/maxTf;
	}
	
	public static void fetchPages(String url) throws ParserException, IOException{
		System.out.println(url);
		StoringWorkWhichIsFinished.add(url);
		numOfPage=numOfPage+1;
		
		
		Crawler crawler = new Crawler(url);//for the purpose to crawl
		Vector<String> website = crawler.extractLinks();
		for(int i = 0; i < website.size(); i++){
			if(StoringWorkWhichIsFinished.contains(website.elementAt(i))==false){
				StoringWorkToBeComplete.add(website.elementAt(i));
			}else{
				website.removeElementAt(i);
			}
		}
		int pageIndex;
		
		// to find out the url is contained or not
		if(PageIn.isContain(url) && InformationOfThePage.isContain(PageIn.getIndex(url))){
			pageIndex = PageIn.getIndexNumber(url);
			String date = crawler.lastUpdate();
			String date2 = InformationOfThePage.getLastDate(Integer.toString(pageIndex));
			
			if(date.compareTo(date2)==0){
				System.out.println("no change");
				//return;
			}else{
				System.out.println("last modification date changed");
				//update if last modification date are not same
				String text = ForwardIndex.getValue(Integer.toString(pageIndex));
				String[] temp = text.split(" ");
				for(int i = 0; i < temp.length; i++){
					System.out.println(temp[i]);
				}
				ForwardIndex.delEntry(Integer.toString(pageIndex));
				InformationOfThePage.delEntry(Integer.toString(pageIndex));
			}
		}else{
			System.out.println("new page found");
			pageIndex = PageIn.addEntry(url, Integer.toString(PageIn.getLastIndex()));
		}
		
		
		//extract word
		Vector<String> words = crawler.extractWords();
		Hashtable<Integer, Integer> map = new Hashtable<Integer,Integer>(); 
		for(int i = 0; i < words.size(); i++){
			
			if (!stopStem.isStopWord(words.get(i))){

				
				String temp = stopStem.stem(words.get(i));
				int index = WordIndex.addEntry(temp, Integer.toString(WordIndex.getLastIndex()));
				//Inverted-file index
				if(!map.containsKey(index)){
					map.put(index, 1);
				}else{
					
					map.put(index, map.get(index) + 1);
				}
				//forward index
				ForwardIndex.addEntry2(pageIndex+"", temp);
			}
			//else 
			//{	System.out.println("stopword detected");}
		}
		Set<Integer> set = map.keySet();
	    Iterator<Integer> itr = set.iterator();
	    int maximum = 0;
	    while (itr.hasNext())
	    {
	      int index = itr.next();
	      int  number = map.get(index);
	      inverted.addEntry(index+"", pageIndex,  number);
	     if( number > maximum) {
	    maximum = number;
	     }
	     
	    }
	   
	    
	    
		//to get the title of the web
		StopStem stopStem = new StopStem("/Users/Eric/javaworkspace/Cphase1/src/IRUtilities/stopwords.txt");
		Vector<String> titleWords = crawler.extractTitle();
		String title = "";
		for(int j = 0; j < titleWords.size(); j++){
			title += titleWords.elementAt(j);
			if (!stopStem.isStopWord(titleWords.get(j))){
				
				int index= TitleIn.addEntry(stopStem.stem(titleWords.get(j)), Integer.toString(TitleIn.getLastIndex()));
				if(!map.containsKey(index)){
					map.put(index, 1);
				}else{
				
					map.put(index, map.get(index) + 1);
				}}
				
		}
		
		//to get the latest update
		String date = crawler.lastUpdate();
		
		//to get the size of page
		int pageSize = crawler.pageSize();
		InformationOfThePage.newElementToAdd(Integer.toString(pageIndex), title, url, date, pageSize);
		for(int j = 0; j < website.size(); j++){
			int pageId = PageIn.addEntry(website.elementAt(j),Integer.toString(PageIn.getLastIndex()) );
			Child.addEntry2(Integer.toString(pageId),Integer.toString(pageIndex));
			Parent.addEntry2(Integer.toString(pageIndex), Integer.toString(pageId));
		}
		StoringWorkToBeComplete.addAll(website);
		
	}
}
