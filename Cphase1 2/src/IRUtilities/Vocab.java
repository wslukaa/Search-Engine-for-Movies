package IRUtilities;

public class Vocab implements Comparable<Vocab> {
	private String text;
	private int freq;
	
	public Vocab(){
	}
	
	public Vocab(String text, int freq){
		this.setText(text);
		this.setFreq(freq);
	}
	
	public int compareTo(Vocab a) {
	       //return either 1, 0, or -1
	       //that you compare between this object and object a
		return  a.getFreq() - freq ;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}
}
