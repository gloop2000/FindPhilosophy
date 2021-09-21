import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JumpToPhilosophy {
	final static int MAX_COUNT = 100;
	final static String targetUrl = "/wiki/Philosophy";
	final static String defaultUrl = "https://en.wikipedia.org";
	
	//Removes child tag elements from parent tag.
	private static void removeTag(Element currentElement, char parentTag, char childTag) {
		currentElement.select(parentTag + " > " + childTag).remove();
	} 
	
	//Removes child tag elements from parent tag.
	private static void removeTag(Element currentElement, String parentTag, String childTag) {
		currentElement.select(parentTag + " > " + childTag).remove();
	} 
	
	//Removes Brackets from html, including characters enclosed in brackets
	private static void removeBrackets(Element currentElement) {
		for (Element removeBraces : currentElement.select(":matchesOwn(\\((.*?)\\))")) { // Removes characters in brackets
			removeBraces.html(removeBraces.html().replaceAll("\\([^()]*\\)", ""));
		}
	}
	
	//Finds the first link in an Element
	private static String findFirstLink(Element currentElement) {
		Element newLink = currentElement.select("p > a").first();
		if (newLink.attr("href") != null) {
			return newLink.attr("href");
		}			
		return null;
	}
	
	public static void main(String[] args) throws IOException {
		
		String initUrl = "/wiki/Data_(computing)";
		String currentUrl = initUrl;
		int count = 0;
		while ((!currentUrl.equals(targetUrl)) && count < MAX_COUNT) {
			Document doc = Jsoup.connect(defaultUrl + currentUrl).get();
			System.out.println("Current Website:" + currentUrl);
			Elements paragraphs = doc.select("div.mw-parser-output p");
			for (Element sentence : paragraphs) {
				// Check if paragraph is empty
				if (sentence.text().equals(""))
					continue;
				else {
					removeTag(sentence,'p','i');	//Removes Italics
					removeTag(sentence,"p","sup");	//Removes superscripts/meta-links
					removeBrackets(sentence);		//Removes brackets
					currentUrl = findFirstLink(sentence);	//Finds first link
					if(currentUrl!=null) {
						count++;
						break;
					}else {
						continue;
					}
				}
			}
		}
		System.out.println("Number of Websites jumped: " + count);
	}
}
