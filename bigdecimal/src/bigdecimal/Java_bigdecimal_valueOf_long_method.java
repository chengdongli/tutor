package bigdecimal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class Java_bigdecimal_valueOf_long_method {

  public static void main(String args[]) {

    BigDecimal obj = new BigDecimal(0);

    long ln = 1345444456;
    System.out.println("long value : " + ln + 
        "\nmethod generated result :"
        + obj.valueOf(ln, 2));

    int in = 4545;
    System.out.println("\ninteger value " +
        ": " + in
        + "\nmethod generated result" +
            " :" + obj.valueOf(in, 5));

    ln = 2120022;
    System.out.println("\nlong value : " + ln
        + "\nmethod generated result " +
            ":" + obj.valueOf(ln, 4));

    in = 256;
    System.out.println("\ninteger value : " + in
        + "\nmethod generated result " +
            ":" + obj.valueOf(in, 2));
  }
}