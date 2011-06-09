package regex;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringMatchCase extends RegExTestCase{

	// Any \-delimited regex expression metacharacter needs to be delimited
	// once again when it is used in java code.
	
	@Test
	public void exactMatch(){
		String regex;
		// match 1-800-111-2222
		regex="(\\d-)?(\\d{3}-)?\\d{3}-\\d{4}";
		match("PhoneNumber", regex,"1-800-111-2222");
		// match 95110-4443
		regex="\\d{5}(-\\d{4})?";
		match("ZipCode",regex,"95110-4443");
		match("ZipCode",regex,"95110");
		// match date: 04-28-1999
		regex="\\d{2}-\\d{2}-\\d{4}";
		match("Date",regex,"04-28-1999");
		// match name: John Smith
		regex="\\p{Upper}(\\p{Lower})+\\s?\\p{Upper}(\\p{Lower})+";
		match("Name",regex,"John Smith");
		
	}
	
	
	@Test
	public void graceMatch(){
		String s = "144450_Fanta_Sony_Pre";
		String result = s.matches("^[1-9][0-9]*_.*")==true ? s.substring(0, s.indexOf("_")) : null;
		System.out.println(result);
		assertTrue(result.equals("144450"));
		
		s = "1234447890_Fanta_Sony_Pre";
		result = s.matches("^[0-9&&^[5-6]]*_.*")==true ? s.substring(0, s.indexOf("_")) : null;
		System.out.println(result);
		assertTrue(result.equals("1234447890"));
		
				
	}
	
}
