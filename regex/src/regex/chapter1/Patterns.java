package regex.chapter1;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.BeforeClass;
import org.junit.Test;

public class Patterns {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
	}
	
	@Test
	public void verifyNormalMatch(){
		// A word boundary, a lowercase a, any number of immediately trailing 
		// letters, numbers, or underscores, followed by a word boundary  
		String regex = "\\ba\\w*\\b";
		String input = "A Matcher exampines the results of applying a pattern.";

		printTitle(regex, input);
		
		Pattern pattern = Pattern.compile(regex);;
		Matcher matcher = pattern.matcher(input);
		
		String val=null;
		// examine the Matcher, and extract all words starting with a lower-case a
		while(matcher.find()){
			val = matcher.group();
			printMatch("MATCH: "+val);
		}
		
		// if there were no matches, say no
		assertTrue("No word start with lower case a", val!=null);
		
	}

	@Test
	public void verifyZipCodeMatch() throws Exception{
		String regex = "^\\d{5}([\\-]\\d{4})?$";
		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher("2211");
		boolean notValid = matcher.matches();
		assertFalse("Pattern did not validate zip code", notValid);
		
		matcher = pattern.matcher("95110-5051");
		boolean isValid = matcher.matches();
		for(int i=0;i<matcher.groupCount();i++){
			System.out.println("group"+i+": "+matcher.group(i));
		}
		assertTrue("Pattern validate zip code", isValid);

	}
	
	@Test
	public void verifyGroupMatch() throws Exception{
		String regex = "(a(b*))+(c*)";
		Pattern pattern = Pattern.compile(regex);		

		Matcher matcher = pattern.matcher("abbabcd");
		boolean matchFound = matcher.find();
		
		if(matchFound){
			System.out.println("group count = "+matcher.groupCount());
			
			// Get all groups for this match
			for(int i=0;i<=matcher.groupCount();i++){
				System.out.println("group"+i+": "+matcher.group(i));
			}
		}
	}

	@Test
	public void extract(){
		String input = "144450_Fanta_Sony_Pre_3344";
		String regex= "^([1-9][0-9]*)_.*([1-9]*)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		boolean matchFound = matcher.find();
		
		assertTrue(matchFound);
		
		System.out.println("group count = "+matcher.groupCount());
		
		if(matchFound){
			// Get all groups for this match
			for(int i=0;i<=matcher.groupCount();i++){
				System.out.println("group"+i+": "+matcher.group(i));
			}
		}

	}

	protected void printTitle(String regex, String input){
		System.out.println("RegEx: "+regex);
		System.out.println("Input: "+input);
	}
	
	protected void printMatch(String result){
		final String INDENT="    ";
		System.out.println(INDENT+result);
	}
	
	protected void printEnd(){
		System.out.println("done.");
	}
}
