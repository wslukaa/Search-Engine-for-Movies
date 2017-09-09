
package IRUtilities;
import java.io.IOException;

import org.htmlparser.util.ParserException;



import java.io.*;


import jdbm.RecordManager;
import jdbm.RecordManagerFactory;


public class testing {
	private static final int  maximum_num_pages  = 30;

	private static IndexHelper PageIndexer;
	private static IndexHelper WordIndexer;
	
	private static InvertedIndex inverted;
	private static InvertedIndex ForwardIndex;
	
	private static FileWriter fstream;
	private static BufferedWriter out;
	private static RecordManager recman;
	private static InvertedIndex ParentChild;
	private static InformationOfPage Pageppt;

	
	
	public static void main(String[] args) throws IOException, ParserException {
		recman = RecordManagerFactory.createRecordManager("/Users/Eric/javaworkspace/comp4321_Phase1/comp4321_Phase1/src/IRUtilities/dasebase");
		//recman = RecordManager("/Users/lukwingsan/Documents/comp4321/comp4321_Phase1/database");
		//Scanner input = new Scanner(new FileInputStream("/Users/lukwingsan/Documents/comp4321/comp4321_Phase1/database"));
		//String getInput = input.nextLine();
		PageIndexer = new IndexHelper(recman, "page");
		//PageIndexer = new Indexer(getInput, "page");
		WordIndexer = new IndexHelper(recman, "word");
		//TitleIndexer = new Indexer(recman, "title");
		
		//create objects
		inverted = new InvertedIndex(recman, "invertedIndex");
		ForwardIndex = new InvertedIndex(recman, "ForwardIndex");
		//Child = new InvertedIndex(recman, "ParentChild");
		ParentChild = new InvertedIndex(recman, "PC");
		Pageppt  = new InformationOfPage(recman, "PPT");
		fstream = new FileWriter("spider_result.txt");
		out = new BufferedWriter(fstream);
		//maxTermFreq = new Indexer(recman, "maxTermFreq");
		//termWth = new InvertedIndex(recman, "termWth");
		//titleForwardIndex = new InvertedIndex(recman, "titleFI");
		//titleInverted = new InvertedIndex(recman, "titleI");
		//titleMaxTermFreq = new Indexer(recman, "titleMaxTermFreq");
		

	
		
		for(int i = 0; i< maximum_num_pages ; i++){
			String stringurl = PageIndexer.getValue(String.valueOf(i));
			fetch(stringurl);
		}
				
		
		//closed the test
		out.close();
		recman.close();
	}
	
	public static void fetch(String url) throws IOException{
		int index = PageIndexer.getIndexNumber(url);
		String title = Pageppt.getTitle(Integer.toString(index));
		String Pageurl =  Pageppt.getUrl(Integer.toString(index));
		String DateAndPageSize=Pageppt.getLastDate(Integer.toString(index))+","+Pageppt.getPageSize(Integer.toString(index));
		
		System.out.println("Title: "+title);
		out.append("Title: "+title).append("\n");
		
		System.out.println("URL: "+Pageurl);
		out.append("URL: "+Pageurl).append("\n");
		
		
		System.out.println("Last modification date and size of page: "+DateAndPageSize);
		out.append("Last modification date and size of page: "+DateAndPageSize).append("\n");


		
		String strindex = String.valueOf(index);
		String WordList = ForwardIndex.getValue(strindex);
		String[] childtemp = WordList.split(" ");
		
		for(int i = 0; i<childtemp.length;i++){
			
			System.out.print(childtemp[i]);
			System.out.print(" ");
			out.append(childtemp[i]+" ");
			
			String tempID = WordIndexer.getIndex(childtemp[i]);
			String tempstr = inverted.getValue(tempID);
			
			String[] temp2 = tempstr.split(" ");
			
			for(int j = 0 ; j < temp2.length; j++){
				String[] temp3=temp2[j].split(":");
				
				int num = Integer.parseInt(temp3[0]);
				
				if(index-num==0){
					
					System.out.print(temp3[1]);
					System.out.print("; ");
					out.append(" ").append(temp3[1]).append("; ");
					
					break;
				}
			}
		}
		System.out.println();
		out.append("\n");
		
		
		
		String strchild = String.valueOf(index);
		String child = ParentChild.getValue(strchild);
		childtemp = child.split(" ");
		
		int i = 0;
		while(i<childtemp.length){
			String childpage = PageIndexer.getValue(childtemp[i]);
			
			System.out.println(childpage);
			out.append(childpage).append("\n");
			i++;
		 }


		System.out.println("-------------------------------------------------------------------------------------------");
		out.append("-------------------------------------------------------------------------------------------").append("\n");
	}
}
