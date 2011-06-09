package bigdecimal;

import java.math.BigDecimal;

public class Java_bigdecimal_scale {
  public static void main(String args[]) {

    BigDecimal nc = new BigDecimal(0),
    mc = new BigDecimal(0.050);
    System.out.println("nc value : " + nc);
    System.out.println("method generated " +
        "result : " + nc.scale());

    System.out.println("mc value : " + mc);
    System.out.println("method generated " +
        "result : " + mc.scale());

    nc = new BigDecimal(-3.0);
    mc = new BigDecimal(-6);

    System.out.println("\nnc value : " + nc);
    System.out.println("method generated " +
        "result : " + nc.scale());

    System.out.println("mc value : " + mc);
    System.out.println("method generated " +
        "result : " + mc.scale());

    nc = new BigDecimal(-1);
    mc = new BigDecimal(-42);

    System.out.println("\nnc value : " + nc);
    System.out.println("method generated " +
        "result : " + nc.scale());

    System.out.println("mc value : " + mc);
    System.out.println("method generated " +
        "result : " + mc.scale());

  }
}