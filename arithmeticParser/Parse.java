import java.util.*;
import java.lang.*;

public class Parse{


    public static void main(String args[]){
        
        ArrayList test;

        Scanner scan = new Scanner(System.in);

        String expression;


        System.out.println("\nEnter expression (Enter 'quit' to quit): ");
        expression = scan.nextLine();
        while(!expression.equals("quit")){
            expression = expression.replaceAll("\\s","");

            test = Expression.parse(expression);

            System.out.println("ANSWER: "+Expression.calculate(test));

            System.out.println("\nEnter expression (Enter 'quit' to quit): ");
            expression = scan.nextLine();
        
        }
    }
}

