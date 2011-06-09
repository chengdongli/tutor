package regex;

import static org.junit.Assert.assertTrue;
import static java.lang.System.out;

public class RegExTestCase {
	public String match(String name, String regex, String pattern){
		boolean ret=pattern.matches(regex);
		String result=ret?"Match":"NO MATCH";
		StringBuffer sb=new StringBuffer();
		sb.append(name+": "+result+"\n");
		sb.append("    pattern:"+pattern+"\n");
		sb.append("    regex:"+regex+"\n");
		out(sb.toString());
		assertTrue(ret);
		return sb.toString();
	}
	
	public void out(String str){
		out.println(str);
	}
}
