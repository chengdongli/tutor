/**
 * Copyright (c) 2009 Tradescape Corporation.
 * All rights reserved.
 */
package format;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test for Formatter. Note, the same format applies to the
 *   System.out/err.printf(formatString,arguments);
 * @since Java 1.5.0
 * @author chengdong
 *
 */
public class FormatTestCase
{
  @Test
  public void leadingZeroTest(){
    // The format specifiers for general, character, and numeric types 
    // have the following syntax: 
    //   %[argument_index$][flags][width][.precision]conversion

    StringBuilder sb = new StringBuilder();
    // Send all output to the Appendable object sb
    Formatter formatter = new Formatter(sb, Locale.US);
    
    sb.setLength(0);
    formatter.format("%02d", 111);
    Assert.assertEquals("111", sb.toString());

    sb.setLength(0);
    formatter.format("%02d", 1);
    Assert.assertEquals("01", sb.toString());

    sb.setLength(0);
    formatter.format("%04d-%02d-%02dT%02d:%02d:%02d", 2009,3,31,1,12,13);
    Assert.assertEquals("2009-03-31T01:12:13", sb.toString());

    sb.setLength(0);
    formatter.format("%+03d", 1);
    Assert.assertEquals("+01", sb.toString());
    sb.setLength(0);
    formatter.format("%+03d", -1);
    Assert.assertEquals("-01", sb.toString());
    
  }
  
  @Test
  public void testSplit(){
	  String s = "a|b| |d";
	  String[] r = s.split("\\|");
	  Assert.assertEquals(r.length, 4);
	  Arrays.toString(r);

	  s = "a|b||d";
	  r = s.split("\\|");
	  Assert.assertEquals(r.length, 4);
	  Assert.assertNotNull(r[2]);
	  Arrays.toString(r);

	  s = "";
	  r = s.split("\\|");
	  Assert.assertEquals(r.length, 1);
	  Assert.assertNotNull(r[0]);
	  Arrays.toString(r);

	  s = "  ";
	  r = s.split("\\|");
	  Assert.assertEquals(r.length, 1);
	  Assert.assertNotNull(r[0]);
	  Arrays.toString(r);

	  s = "|";
	  r = s.split("\\|");
	  Assert.assertEquals(r.length, 0);
	  Arrays.toString(r);

	  s = "||";
	  r = s.split("\\|");
	  Assert.assertEquals(r.length, 0);
	  Arrays.toString(r);

	  s = " ||";
	  r = s.split("\\|");
	  Assert.assertEquals(r.length, 1);
	  Arrays.toString(r);

	  s = " || ";
	  r = s.split("\\|");
	  Assert.assertEquals(r.length, 3);
	  Arrays.toString(r);

	  s = " | |";
	  r = s.split("\\|");
	  Assert.assertEquals(r.length, 2);
	  Arrays.toString(r);

	  s = " | || |";
	  r = s.split("\\|");
	  Assert.assertEquals(r.length, 4);
	  Arrays.toString(r);

	  s = "| || |";
	  r = s.split("\\|");
	  Assert.assertEquals(r.length, 4);
	  Arrays.toString(r);


  }
}
