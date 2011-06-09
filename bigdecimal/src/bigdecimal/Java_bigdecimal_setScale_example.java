package bigdecimal;

import java.math.BigDecimal;

public class Java_bigdecimal_setScale_example {
  public static void main(String args[]) {

    BigDecimal neo = new BigDecimal(88),
    result = new BigDecimal(0);

    result = neo.setScale(2, BigDecimal.ROUND_UP);

    System.out.println("\nneo object value : "
        + neo);
    System.out.println("method generated result " +
        ": " + result);

    neo = new BigDecimal(452);

    result = neo.setScale(4, BigDecimal.ROUND_DOWN);

    System.out.println("\nneo object value : "
        + neo);
    System.out.println("method generated result : " +
        "" + result);

    neo = new BigDecimal(183);

    result = neo.setScale(0, BigDecimal.ROUND_FLOOR);

    System.out.println("\nneo object value : " 
        + neo);
    System.out.println("method generated result :" +
        " " + result);

  }
}