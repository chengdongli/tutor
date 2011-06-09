package bigdecimal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class Java_bigdecimal_valueOf_long    {
  public static void main(String args[]) {

    BigDecimal obj = new BigDecimal(0);

    long ln = 1256953443;
    System.out.println("long value : " + ln + 
        "\nmethod generated result :"
        + obj.valueOf(ln));

    int in = 120;
    System.out.println("\ninteger value : " 
        + in
        + "\nmethod generated result :"
        + obj.valueOf(in));

    ln = 30022543;
    System.out.println("\nlong value : " + ln
        + "\nmethod generated result :" 
        + obj.valueOf(ln));

    in = 256;
    System.out.println("\ninteger value : " + in
        + "\nmethod generated result :"
        + obj.valueOf(in));
  }
}