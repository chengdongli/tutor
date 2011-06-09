package bigdecimal;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Java_bigdecimal_setScale {
  public static void main(String args[]) {

    BigDecimal b1 = new BigDecimal(2),
    result = new BigDecimal(0);

    result = b1.setScale(2);

    System.out.println("\nb1 object value :" +
        " " + b1);
    System.out.println("method generated " +
        "result : " + result);

    b1 = new BigDecimal(0.111111,new MathContext(0,RoundingMode.UP));

    result = b1.setScale(3,RoundingMode.UP);

    System.out.println("\nb1 object value : " +
        "" + b1);
    System.out.println("method generated " +
        "result : " + result);

    b1 = new BigDecimal(78);

    result = b1.setScale(8);

    System.out.println("\nb1 object value : " +
        "" + b1);
    System.out.println("method generated " +
        "result : " + result);

    b1 = new BigDecimal(180);
    result = b1.setScale(4);
    
    System.out.println("\nb1 object value : " +
      "" + b1);
    System.out.println("method generated " +
        "result : " + result);
  
    b1 = new BigDecimal(180.0000);
    result = b1.setScale(4);
    System.out.println("\nb1 object value : " +
      "" + b1);
    System.out.println("method generated " +
        "result : " + result);

  }
}