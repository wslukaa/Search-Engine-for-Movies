package IRUtilities;

//COMP4321 Lab2 
import java.text.*;
import java.util.*;

import org.htmlparser.beans.StringBean;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.*;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.beans.LinkBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.htmlparser.tags.*;

public class Crawler
{private static StopStem stopStem = new StopStem("/Users/Eric/javaworkspace/comp4321_Phase1/comp4321_Phase1/src/IRUtilities/stopwords.txt");
	private String url;
	public Crawler(String _url)
	{
		url = _url;
	}

	public String lastUpdate() throws IOException{
		String[] t = this.url.split("://");
		URL page = new URL ("http", t[1], 80, "/"); 
		URLConnection pageConnection = page.openConnection();
		
		Date date = new Date(pageConnection.getLastModified());
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		return dateFormat.format(date);
	}
	public int pageSize() throws IOException{
		URL pageUrl = new URL(url);
        URLConnection urlconnect = pageUrl.openConnection();
        BufferedReader read = new BufferedReader(new InputStreamReader( urlconnect.getInputStream()));
        
        String input = "";
        String t = "";
        while (read.readLine() != null){
        	input = read.readLine();
        	t = t + input;
        }
        read.close();
        return t.length();
	}
	
	
	// HTML parser in getting the title of the page
	// reference to http://www.cnblogs.com/dlutxm/archive/2011/12/13/2286862.html
	public Vector<String> extractTitle() throws ParserException{
		Parser parser = new Parser (this.url);
		NodeFilter ff = new NodeClassFilter(TitleTag.class);  //ff = filter
		NodeList nodeList = parser.parse(ff);
		Node[] nodes = nodeList.toNodeArray();
		String line = " ";
		int index = 0;
		while(index <nodes.length){
			
		   Node nn = nodes[index];  //nn = array of node title
		   if(nn instanceof TitleTag)
		   {
			   TitleTag Node_title = (TitleTag) nn;
			   line = Node_title.getTitle();
		   }
		   index++;
		}
		String[] s = line.split(" ");
		Vector<String> result = new Vector<String>();
		int j = 0;
		while(j < s.length){
			result.add(s[j]);
			j++;
		}
		return result;
	}
	
	public Vector<String> extractWords() throws ParserException
	{
		Vector<String> result = new Vector<String>();
		StringBean bean = new StringBean();
		bean.setURL(url);
		bean.setLinks(false);
		
		String contents = bean.getStrings();
		if (!stopStem.isStopWord(contents)){
			StringTokenizer  st = new StringTokenizer(contents);


		while (st.hasMoreTokens()) {
		    result.add(st.nextToken());
		}}
		return result;
	}
	public Vector<String> extractLinks() throws ParserException

	{
		
		Vector<String> result = new Vector<String>();
		LinkBean bean = new LinkBean();
		bean.setURL(url);
		URL[] urls = bean.getLinks();
		for (URL s : urls) {
		    result.add(s.toString());
		}
		return result;

	}

	
	public static void main (String[] args)
	{
		try
		{
			
			Crawler crawler = new Crawler("https://course.cse.ust.hk/comp4321/labs/TestPages/testpage.htm");
			Vector<String> words = crawler.extractWords();		
			System.out.println("Words in "+crawler.url+":");
			for(int i = 0; i < words.size(); i++)
				System.out.print(words.get(i)+" ");
			System.out.println("\n\n");
			Vector<String> links = crawler.extractLinks();
			System.out.println("Links in "+crawler.url+":");
			for(int i = 0; i < links.size(); i++)		
				System.out.println(links.get(i));
			System.out.println("");
			
		}
		catch (ParserException e)
        {
            e.printStackTrace ();
        }

	}

}
	
