package javaslang.text;

public class Token {

	public final String text;
	public final int start;
	public final int end;
	
	Token(String text, int start, int end) {
		this.text = text;
		this.start = start;
		this.end = end;
	}
	
}
