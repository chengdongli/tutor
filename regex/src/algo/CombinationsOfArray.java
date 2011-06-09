package algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//http://stackoverflow.com/questions/127704/algorithm-to-return-all-combinations-of-k-elements-from-n
//http://answers.yahoo.com/question/index?qid=20100116114107AAftIlu
/**
 * The problem:
 *  <ul>Given an array of N elements, e.g. A={0,1,2,3,4} 
 *    <li> print all combinations of the array. Or </li>
 *    <li> print all combinations of K elements of the array. </li>
 *  </ul>
 * Solution:
 *  <ul> <b>direct method (generate K out of N combinations)</b> 
 *     <li>pick up K leftmost elements from array A to array C</li>
 *     <li>shift the C[K-1] to the right, until it reach the end of A</li>
 *     <li>shift the C[K-2],C[K-1] to the right, until it reach the end of A<li>
 *     <li>...</li>
 *     <li>shift the C[0],...C[K-1] to the right, until reach the end of A<li>
 *  </ul> 
 *  Or
 *  <ul> <b>recursive method (generate all combinations)</b> 
 *     <li>pick array A as start combination</li>
 *     <li>for each element A[i], create array C with sub elements, C=A[0..i-1,i+1..N-1], A=C, repeat</li>
 *  </ul>
 *  <ul>Note:
 *    <li>there is no redundant combinations in direct method </li>
 *    <li>there are redundant combinations in recursive method, so when adding combinations C to final result, you have to check the uniqueness of the combination</li>
 *  </ul>
 *  
 */
public class CombinationsOfArray
{
    public static void main(String[] args)
    {
      directMethod();
      recursiveMethod();
    }

    private static void recursiveMethod()
    {
      // TODO Auto-generated method stub
      List<int[]> combinations = new ArrayList<int[]>();
      int[] array = new int[]{0,1,2,3,4,5};
      generateCombinations(array,combinations);
      
//      Collections.sort(combinations, new Comparator(){
//
//        @Override
//        public int compare(Object o1, Object o2)
//        {
//          int[] a1=(int[]) o1;
//          int[] a2=(int[]) o2;
//          if(a1.length<a2.length)
//            return -1;
//          else if(a1.length==a2.length){
//            return 0;
//          }else{
//            return 1;
//          }
//        }
//        
//      });
      
      System.out.println("\nGenerate all combinations for array:"+Arrays.toString(array));
      
      for(int[] list: combinations){
        for (int j = 0; j < list.length; j++)
        {
            System.out.print(list[j] + " ");
        }
        System.out.println();
      }
      System.out.println("Total: "+combinations.size());

    }
    
    public static void addUniqueArrayToList(int[] array, List<int[]> list){
      for(int[] ex: list){
        if(Arrays.equals(ex, array))
          return;
      }
      list.add(array);
    }
    
    public static void generateCombinations(int[] array, List<int[]> combinations){
      addUniqueArrayToList(array,combinations);
      
      if(array.length==1){
        return;
      }
      
      for(int i=0;i<array.length;i++){
        int[] newArray= new int[array.length-1];
        System.arraycopy(array, 0, newArray, 0, i);
        System.arraycopy(array, i+1, newArray, i, array.length-1-i);
        generateCombinations(newArray,combinations);
      }
    }

    private static void directMethod()
    {
      int K = 5;
      int N = 8;
      int[][] combinations = getnerateKoutN(K, N);

      System.out.println("\nGenerate combinations: ["+K+"] out of ["+N+"]");

      for (int i = 0; i < combinations.length; i++)
      {
          for (int j = 0; j < combinations[i].length; j++)
          {
              System.out.print(combinations[i][j] + " ");
          }
          System.out.println();
      }
      System.out.println("Total: "+combinations.length);
    }

    private static int[][] getnerateKoutN(int K, int N)
    {
        int possibilities = getNumberOfCombinations(K,N);
        int[][] combinations = new int[possibilities][K];
        int arrayPointer = 0;

        int[] counter = new int[K];

        for (int i = 0; i < K; i++)
        {
            counter[i] = i;
        }
        breakLoop: while (true)
        {
            // Initializing part
            for (int i = 1; i < K; i++)
            {
                if (counter[i] >= N - (K - 1 - i))
                    counter[i] = counter[i - 1] + 1;
            }

            // CombinationsOfArray part
            for (int i = 0; i < K; i++)
            {
                if (counter[i] < N)
                {
                    continue;
                } else
                {
                    break breakLoop;
                }
            }

            // Innermost part
            combinations[arrayPointer++] = counter.clone();

            // Incrementing part
            counter[K - 1]++;
            for (int i = K - 1; i >= 1; i--)
            {
                if (counter[i] >= N - (K - 1 - i))
                    counter[i - 1]++;
            }
        }

        return combinations;
    }

    private static int getNumberOfCombinations(int K, int N)
    {
        if(K > N)
        {
            throw new ArithmeticException("K is greater then N");
        }
        long numerator = 1;
        long denominator = 1;

        for (int i = 1; i <=K; i++)
        {
            denominator *= i;
        }
        for (int i = N; i > N - K; i--)
        {
            numerator *= i;
        }

        return (int) (numerator / denominator);
    }
}
//Python yield will return a generator, which make the code more concise.
//def choose_iter(elements, length):
//  for i in xrange(len(elements)):
//      if length == 1:
//          yield (elements[i],)
//      else:
//          for next in choose_iter(elements[i+1:len(elements)], length-1):
//              yield (elements[i],) + next
//def choose(l, k):
//  return list(choose_iter(l, k))
// 
//Example usage:
//   len(list(choose_iter("abcdefgh",3)))
// or 
//   print(list(choose_iter("abcdefgh",3)))
//


