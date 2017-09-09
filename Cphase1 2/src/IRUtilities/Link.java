package IRUtilities;

import java.util.Collections;
import java.util.Vector;

public class Link implements Comparable<Link>{
	private double score;
	private String title;
	private String url;
	private String lastUpdate;
	private int pageSize;
	private Vector<String> PLink;
	private Vector<String> CLink;
	private Vector<Vocab> keywords;
	
	public int compareTo(Link a) {
	       //return either 1, 0, or -1
	       //that you compare between this object and object a
		double temp = a.getScore() - score;
		if(temp < 0.0){
			return -1;
		}else if(temp > 0.0){
			return 1;
		}else{
			return 0;
		}
	}
	
	public String toString(){
		String result = score+"\n";
		result += title+"\n";
		result += url+"\n";
		result += lastUpdate+":"+pageSize+"\n";
		for(int i = 0; i < keywords.size(); i++){
			Vocab temp = keywords.elementAt(i);
			result += temp.getText()+" "+temp.getFreq()+"; ";
		}
		result+="\n";
		for(int i = 0; i < PLink.size(); i++){
			result += PLink.elementAt(i)+"\n";
		}
		for(int i = 0; i < CLink.size(); i++){
			result += CLink.elementAt(i)+"\n";
		}
		return result+"\n";
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Link(){
		PLink = new Vector<String>();
		CLink = new Vector<String>();
		keywords = new Vector<Vocab>();
	}
	
	public void setScore(double score){
		this.score = score;
	}
	
	public double getScore(){
		return score;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public void addParentLink(String link){
		PLink.add(link);
	}
	
	public Vector<String> getParentLink(){
		return PLink;
	}
	
	public void addChildLink(String link){
		CLink.add(link);
	}
	
	public Vector<String> getChildLink(){
		return CLink;
	}

	public void addKeyword(Vocab keyword){
		keywords.add(keyword);
	}
	
	public Vector<Vocab> getKeywords(){
		return keywords;
	}
	
	public void sortKeyword(){
		Collections.sort(keywords);
	}
}
