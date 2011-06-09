package bigdecimal;

import java.math.BigDecimal;

public class Java_bigdecimal_remainder {
  public static void main(String args[]) {
    BigDecimal dividend
 = new BigDecimal(625),
    divisor = new BigDecimal(5);
    System.out.println("dividend & divisor value" +
        " : " + dividend + "  &  "
        + divisor);
    System.out.println("method generated result : "
        + dividend.remainder(divisor));

    dividend = new BigDecimal(100.100);
    divisor = new BigDecimal(25);

    dividend = new BigDecimal(dividend.floatValue());
    divisor = new BigDecimal(25);

    System.out.println("\ndividend & divisor value" +
        " : " + dividend + "  &  "
        + divisor);
    System.out.println("method generated result : "
        + dividend.remainder(divisor));

    dividend = new BigDecimal(81);
    divisor = new BigDecimal(dividend.floatValue());

    System.out.println("\ndividend & divisor value" +
        " : " + dividend + "  &  "
        + divisor);
    System.out.println("method generated result : "
        + dividend.remainder(divisor));

  }
}