package lang;

import static java.lang.System.out;

/**
 * The break and continue can explicitly jump to specified loop. 
 * @author chengdong
 *
 */
public class BreakAndContinue {
	
	public static void main(String[] args) {
		BreakAndContinue target = new BreakAndContinue();
		target.testBreak();
		target.testContinue();
		target.testContinue2();
		target.testContinue3();
	}

	
	   public void testBreak(){
	     System.out.println("============== break ===============");
	     
	     loop1:
	     for(int i=0;i<5;i++){
	       loop2:
	       for(int j=0;j<5;j++){
	         out.println("i="+i+", j="+j);
	         if(i==1)
	           break loop1;
	         if(j==2)
	           break loop2;
	       }
	     }
	     out.println("OK");     
	     
	   }

	   public void testContinue(){
	     System.out.println("============== continue ===============");
	     
	     loop1: for(int i=0;i<5;i++){
	       loop2: for(int j=0;j<5;j++){
	         out.println("i="+i+", j="+j);
	         if(i==1)
	           continue loop1;
	         if(j==2)
	           continue loop2;
	       }
	     }
	     out.println("OK");     
	   }

	   public void testContinue2(){
	     System.out.println("============== continue2 ===============");
	     
	     for(int i=0;i<5;i++){
	       loop2: for(int j=0;j<5;j++){
	         out.println("i="+i+", j="+j);
	         if(j==2)
	           continue loop2;
	       }
	     }
	     out.println("OK");     
	   }
	   
	   public void testContinue3(){
	     System.out.println("============== continue3 ===============");
	     
	     loop1: for(int i=0;i<5;i++){
	       for(int j=0;j<5;j++){
	         out.println("i="+i+", j="+j);
	         if(j==2)
	           continue loop1;
	       }
	     }
	     out.println("OK");     
	   }

}

