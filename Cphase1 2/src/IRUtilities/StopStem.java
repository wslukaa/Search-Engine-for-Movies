package IRUtilities;


/*import java.io.*;
import java.util.HashSet;
*/
import IRUtilities.*;
import java.io.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
public class StopStem
{
	private Porter porter;
	private java.util.HashSet Stw;

	public StopStem(String input)
	{
		super();
		try{
			Stw = new java.util.HashSet();
			porter = new Porter();
			

			FileInputStream fstream = new FileInputStream(input);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String words;
			while ((words = br.readLine()) != null)   {
				Stw.add(words);
			}
			
			in.close();
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());}
		
		}
	
	public boolean isStopWord(String str)
	{
		return Stw.contains(str);	
	}
	
	/*
			  FileReader fr=new FileReader(str);    
		          BufferedReader br=new BufferedReader(fr);    
		  
		          int i;    
		          while((i=br.read())!=-1){  
		        	  stopWords.add(br.readLine());
		        	  //System.out.print((char)i);  
		          }  
		          br.close();    
		          fr.close(); }
		          catch (Exception e){
		  			System.err.println("Error: " + e.getMessage());}
	
		}*/
	public String stem(String str)
	{
		return porter.stripAffixes(str);
	}
	
}