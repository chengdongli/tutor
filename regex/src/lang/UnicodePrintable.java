package lang;

import org.junit.Test;

public class UnicodePrintable {
	public boolean isPrintableChar( char c ) {
	    Character.UnicodeBlock block = Character.UnicodeBlock.of( c );
	    return (!Character.isISOControl(c)) &&
	            block != null &&
	            block != Character.UnicodeBlock.SPECIALS;
	}
	
	@Test
	public void testNonPrintable(){
		String s="11234\tabc de\r   ddd\n";
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<s.length();i++){
			char c = s.charAt(i);
			if(isPrintableChar(c)){
				sb.append(c);
			}
		}
		System.out.println(sb.toString());
	}
}

//StringBuilder sb=new StringBuilder();
//for(int i=0;i<s.length();i++){
//	char c = s.charAt(i);
//    Character.UnicodeBlock block = Character.UnicodeBlock.of( c );
//    if((!Character.isISOControl(c)) &&
//            block != null &&
//            block != Character.UnicodeBlock.SPECIALS){
//		sb.append(c);
//	}
//}
