package bigdecimal;

import java.math.BigDecimal;

public class Java_bigdecimal_precision {
  public static void main(String args[]) {
    BigDecimal honey = new BigDecimal("12"),
    storm = new BigDecimal(0000);
    System.out.println("honey object value :" +
        " " + honey);
    System.out.println("method generated " +
        "result : " + honey.precision());

    System.out.println("\nstorm object value" +
        " : " + storm);
    System.out.println("method generated " +
        "result : " + storm.precision());

    honey = new BigDecimal(-2321);
    storm = new BigDecimal(-5.1);

    System.out.println("\nhoney object value " +
        ": " + honey);
    System.out.println("method generated " +
        "result : " + honey.precision());

    System.out.println("\nstorm object value " +
        ": " + storm);
    System.out.println("method generated " +
        "result : " + storm.precision());

    storm = new BigDecimal(-5.1);
    storm = new BigDecimal(storm.floatValue());
    System.out.println("\nstorm object value " +
        ": " + storm);
    System.out.println("method generated " +
        "result : " + storm.precision());

  }
}