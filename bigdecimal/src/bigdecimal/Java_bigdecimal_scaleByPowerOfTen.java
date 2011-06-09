package bigdecimal;

import java.math.BigDecimal;

public class Java_bigdecimal_scaleByPowerOfTen {
  public static void main(String args[]) {

    BigDecimal nc = new BigDecimal(2),
    mc = new BigDecimal(0), result = new BigDecimal(
        0);

    result = nc.scaleByPowerOfTen(2);

    System.out.println("nc object value : " + nc 
        + "\nmc object value : "
        + mc);
    System.out.println("method generated result " +
        ": " + result);

    nc = new BigDecimal(-7.0);
    mc = new BigDecimal(-1);

    result = nc.scaleByPowerOfTen(3);

    System.out.println("\nnc object value : " + nc
        + "\nmc object value : "
        + mc);
    System.out.println("method generated result " +
        ": " + result);

    nc = new BigDecimal(-02);
    mc = new BigDecimal(-032);

    result = nc.scaleByPowerOfTen(4);
    System.out.println("\nnc object value : " + 
        nc + "\nmc object value : "
        + mc);
    System.out.println("method generated result" +
        " : " + result);

  }
}