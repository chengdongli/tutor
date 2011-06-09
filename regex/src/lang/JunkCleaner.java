package lang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Read a text file and remove the junk characters.
 * <p/>
 * This will check the junk code who is unicoded in a text file. For detail of
 * valid code, see isJunk(c).
 * 
 * <p/>
 * To Run:
 *   cp test.txt /tmp 
 *   java -cp . biz.tradescape.util.JunkCleaner test.txt test.out UTF-8
 * 
 * Referecne:
 * http://java.sun.com/developer/technicalArticles/Intl/Supplementary/
 * 
 * @author chengdong
 * 
 */
public class JunkCleaner {
	/*
	 * Usage: JunkCleaner in.txt out.txt UTF-8
	 */
	public static void main(String[] args) {

		if (args.length != 3) {
			echoUsage();
			return;
		}
		final String INPUT = args[0];
		final String OUTPUT = args[1];
		final String ENCODE = args[2];

		BufferedReader in;// = new BufferedReader(isr);
		PrintWriter out;// = new OutputStreamWriter(fos, "UTF8");

		try {
			// prepare the stream
			FileInputStream fis = new FileInputStream(INPUT);
			InputStreamReader isr = new InputStreamReader(fis, ENCODE);
			in = new BufferedReader(isr);

			FileOutputStream fos = new FileOutputStream(OUTPUT);
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					fos, ENCODE)));

			StringBuilder sb0 = new StringBuilder();
			StringBuilder sb = new StringBuilder();

			// String buf;
			// while ((buf = in.readLine()) != null)
			// {
			// sb0.append(buf);
			//
			// if (buf.isEmpty())
			// {
			// sb.append(buf);
			// out.println();
			// continue;
			// }
			//
			// for(int i=0;i<buf.length();i++){
			// char c=buf.charAt(i);
			// if(!isJunk(c)){
			// sb.append(c);
			// out.print(c);
			// }
			// }
			// // // StringBuilder sb = new StringBuilder(buf);
			// // // buf.offsetByCodePoints(index, codePointOffset)
			// // int total = buf.codePointCount(0, buf.length() - 1);
			// // for (int i = 0; i < total; i++)
			// // {
			// // int cpOffset = buf.offsetByCodePoints(0, i);
			// // int cp = buf.codePointAt(cpOffset);
			// // if (!isJunk(cp))
			// // {
			// // out.write(cp);
			// // sb.append(Character.toChars(cp));
			// // }
			// // }
			// // out.println();
			// // out.write(buf);
			// }
			int c;
			while ((c = in.read()) > -1) {
				sb0.append(Character.toChars(c));

				if (!isJunk((char) c)) {
					sb.append(Character.toChars(c));
					out.write(c);
				}
			}

			in.close();
			out.close();

			System.out.println("sb0=\n" + sb0.toString());
			System.out.println("sb=\n" + sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void echoUsage() {
		System.out.println("Usage:\nJunkCleaner in.txt out.txt UTF-8\n");
	}

	/**
	 * Check if c is a junk character or not. A Character is UTF-16 (which is
	 * java internal storage format for unicode), and we only treat certain BMP
	 * (Basic Multilingual Plane) unicode as a valid character.
	 * <p/>
	 * Currently, it will keep the line feed character and removed return
	 * character.
	 * 
	 * @param c
	 *            character
	 * @return true if c is a junk character.
	 */
	public static boolean isJunk(char c) {
		if (Character.isHighSurrogate(c) || Character.isLowSurrogate(c)) {
			// we discard all supplementary character \u10000~\u10FFFF
			return true;
		} else { // BMP characters \u0000~\uFFFF
			if (c == '@'
			// || c == 0x0a // <LF \n ^@>
					// || c == 0x0d // <LR \r ^M>
					|| c == '\n' // <LF \n ^@ 0x0a>
					|| c == 32 // <space>
					|| c == 37 // <%>
					|| c == 42 // <*>
					|| c == 43 // <+>
					|| c == 45 // <->
					|| c == 46 // <.>
					|| c == 47 // </>
					|| c == 58 // <:>
					|| c == 92 // <\>
					|| c == 95 // <_>
					|| c == 124 // <|>
					|| (c >= 48 && c <= 57) // <number> // c.isDigit(ch)
					|| (c >= 65 && c <= 90) // <upper case> // c.isUpperCase(ch)
					|| (c >= 97 && c <= 122) // <lower case> //
												// c.isLowerCase(ch)
			)
				return false;
			else
				return true;
		}
	}

	@Test
	public void testJavaLangStringBuilderSupportRemoveSurrogate() {
	  // remove control code: in the range '\u0000' through '\u001F' or in the range '\u007F' through '\u009F'.
	  // but keep \t.
//	  \u3042\uD840\uDC0B\u3042
	  // \u0090 Control[Cc]
		StringBuffer src = new StringBuffer("\u3042 \u0090\t\nabcd\u3042");
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < src.length(); i = src.offsetByCodePoints(i,
				1)) {
			int codePoint = src.codePointAt(i);
			if('\t'==codePoint || !Character.isISOControl(codePoint))
			  sb.appendCodePoint(codePoint);
		}

		String expected = "\u3042 \tabcd\u3042";
		System.out.println(expected.equals(sb.toString()));
		Assert.assertEquals(expected, sb.toString());
	}

}
