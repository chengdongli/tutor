package bigdecimal;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Java_bigdecimal_remainder_example {
  public static void main(String args[]) {

    MathContext mc = 
      new MathContext(0, RoundingMode.DOWN);
    mc = mc.DECIMAL32;
    BigDecimal dividend = new BigDecimal(555), 
    divisor = new BigDecimal(5);
    System.out.println("dividend & divisor value" +
        " : " + dividend + "  &  "
        + divisor);
    System.out.println("method generated result : "
        + dividend.remainder(divisor, mc));

    dividend = new BigDecimal(169.02);
    divisor = new BigDecimal(13);

    dividend = new BigDecimal(dividend.floatValue());
    divisor = new BigDecimal(13);

    System.out.println("\ndividend & divisor value " +
        ": " + dividend + "  &  "
        + divisor);
    System.out.println("method generated result : "
        + dividend.remainder(divisor, mc));

    dividend = new BigDecimal(999);
    divisor = 
      new BigDecimal(dividend.floatValue() + 0.0000);

    System.out.println("\ndividend & divisor value" +
        " : " + dividend + "  &  "
        + divisor);
    System.out.println("method generated result : "
        + dividend.remainder(divisor, mc));

  }
}