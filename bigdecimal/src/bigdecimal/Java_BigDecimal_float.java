package bigdecimal;

import java.math.BigDecimal; 

/*
Java bigdecimal class floatValue() method translates bigdecimal value in to float type value. Method throws  NumberFormatException if it finds value other than a integer or float. In the example four bigdecimal objects namely: weisz_0, weisz_1, weisz_2 & weisz_3 respectively have been created. 

In the example along with method generated result, original bigdecimal value is also shown. 

Syntax for using the method: public float floatValue()
System.out.println(bigdecimal_objectName.floatValue()); 
                        or
BigDecimal x = this.object.floatValue();
*/
public class Java_BigDecimal_float {
  public static void main(String args[]) {
    
    String rachel[] = {"http://", "www.", "newstrackindia", ".com"};
    BigDecimal weisz_0 = new BigDecimal(rachel.length + 0.451245124512),
    weisz_1 = new BigDecimal(rachel[0].length() + 0.1234213421342134),
    weisz_2 = new BigDecimal(rachel[1].length() + 0.7896789678967896),
    weisz_3 = new BigDecimal(rachel[2].length() + 0.3658365836583658);  
    
    System.out.println("Default Scale="+weisz_0.scale());
    System.out.println("Default Precision="+weisz_0.precision());
    
    System.out.println("BigDecimal objects values \n'weisz_0 '\nvalue : "
      + weisz_0
      +"\nfloat value : " + weisz_0.floatValue());
    System.out.println("BigDecimal objects values \n'weisz_0 '\nvalue : "
      + weisz_0
      +"\ndouble value : " + weisz_0.doubleValue());
  
    System.out.println("\n'weisz_1 '\nvalue : " + weisz_1
                       +"\nfloat value : " + weisz_1.floatValue());

    System.out.println("\n'weisz_3'\nvalue : " + weisz_2
                        +"\nfloat value : " + weisz_2.floatValue());

    System.out.println("\n'weisz_3 '\nvalue : " + weisz_3
                        +"\nfloat value : " + weisz_3.floatValue());
  }
}