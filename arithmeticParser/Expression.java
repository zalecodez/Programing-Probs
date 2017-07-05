import java.util.*;
import java.lang.*;

public class Expression{
    public static enum OpType {OPERATION, EXPRESSION, VALUE}

    public OpType type;
    public String expression;
    public float value;
    public char operation;
    public boolean negate;

    public Expression(OpType type, String expr){
        this(type, expr, false);
    }
    public Expression(OpType type, String expr, boolean negate){
        this.type = type;
        this.expression = expr;
        this.negate = negate;

        switch(type){
            case OPERATION:
                this.operation = expr.charAt(0);
                break;
            case VALUE:
                this.value = Float.parseFloat(expr);
                break;
            default:
                this.expression = expr;
                break;
        }
    }
    public void setNegate(boolean negate){
        this.negate = negate;
    }
    public static float operate(float op1, char f, float op2){
        System.out.println(op1 +" "+f+" "+op2);
        switch(f){
            case '+':
                return op1 + op2;
            case '-':
                return op1 - op2;
            case 'x':
                return op1 * op2;
            case '*':
                return op1 * op2;
            case '/':
                return op1 / op2;
            case '^':
                int ans = 1;
                for(int i = 1; i <= op2; i ++){
                    ans *= op1;
                }
                return ans;
            default:
                return op1;
        }
    }

    public static float calculate(ArrayList<Expression> whole){

        System.out.println("List");
        for(int k = 0; k < whole.size(); k++){
            System.out.print(whole.get(k).toString()+" ");
        }
        System.out.println("\nEndList");

        //base case
        if(whole.size() == 1 && whole.get(0).type == OpType.VALUE){
            if(whole.get(0).negate){
                return -1*whole.get(0).value;
            }
            else{
                return whole.get(0).value;
            }
        }
        if(whole.size() == 3 && whole.get(0).type == OpType.VALUE 
                && whole.get(2).type == OpType.VALUE)
        {
            float node1, node2;

            if(whole.get(0).negate){
                node1 = -1*whole.get(0).value;
            }
            else{
                node1 = whole.get(0).value;
            }
                System.out.println(whole.get(0).toString());

            if(whole.get(2).negate){
                node2 = -1*whole.get(2).value;
            }
            else{
                node2 = whole.get(2).value;
            }
                System.out.println(whole.get(2).toString());

            return operate(
                    node1, 
                    whole.get(1).operation, 
                    node2
                    );
        }

        if(whole.size() == 1){
            //expression
                System.out.println(whole.get(0).toString());
            if(whole.get(0).negate){
                return -1*calculate(parse(whole.get(0).toString()));
            }
            else{
                return calculate(parse(whole.get(0).toString()));
            }

        }

        if(whole.size() == 3){
            float node1, node2;
            
                System.out.println(whole.get(0).toString());
            if(whole.get(0).negate){
                node1 = -1*calculate(parse(whole.get(0).toString()));
            }
            else{
                node1 = calculate(parse(whole.get(0).toString()));
            }
                System.out.println(whole.get(2).toString());
 
            if(whole.get(2).negate){
                node2 = -1*calculate(parse(whole.get(2).toString()));
            }
            else{
                node2 = calculate(parse(whole.get(2).toString()));
            }

            return operate(
                    node1,
                    whole.get(1).operation,
                    node2
                    );
        }

        float node1, node2;
        float part1, part2;
        char finalOp;

        char op1 = whole.get(1).operation;
        char op2 = whole.get(3).operation;
        if(priority(op1) > priority(op2)){
            System.out.println(op1+" > "+op2);
                System.out.println(whole.get(0).toString());
            if(whole.get(0).negate){
                part1 = -1*calculate(parse(whole.get(0).toString()));
            }
            else{
                part1 = calculate(parse(whole.get(0).toString()));
            }

                System.out.println(whole.get(2).toString());
            if(whole.get(2).negate){
                part2 = -1*calculate(parse(whole.get(2).toString()));
            }
            else{
                part2 = calculate(parse(whole.get(2).toString()));
            }

            node1 = operate(
                    part1,
                    op1,
                    part2
                    );
            ArrayList<Expression> temp = from(whole, 3);
            temp.add(0, new Expression(OpType.VALUE,""+node1));
            return calculate(temp);
        }
        else{
            System.out.println(op1+" <= "+op2);
                System.out.println(whole.get(0).toString());
            if(whole.get(0).negate){
                node1 = -1*calculate(parse(whole.get(0).toString()));
            }
            else{
                node1 = calculate(parse(whole.get(0).toString()));
            }
            node2 = calculate(from(whole,2));
            finalOp = whole.get(1).operation;
            return operate(node1, finalOp, node2);
        }
    }

    public static ArrayList<Expression> from(ArrayList<Expression> whole, int inclusive){
        ArrayList remainder = new ArrayList<Expression>();
        
        for(int i = inclusive; i < whole.size(); i++){
            remainder.add(whole.get(i));
        }
        return remainder;
    }

    public static int priority(char f){
        //greater number => greater priority
        switch(f){
            case '^':
                return 5;
            case 'x':
                return 4;
            case '*':
                return 4;
            case '/':
                return 3;
            case '+':
                return 2;
            case '-':
                return 1;
            default:
                return 99;
        }

    }



    public static boolean isDigit(char c){
        if(c <= '9' && c >= '0' || c == '.'){
            return true;
        }
        return false;
    }

    public static ArrayList parse(String ex){
       ArrayList opList = new ArrayList<String>();
       ArrayList<Expression> wholeExpression = new ArrayList<Expression>();
       OpType type;

       type = popNextOp(ex, opList);

       while(opList.size() == 2){
           wholeExpression.add(new Expression(type, (String)opList.get(0)));
           ex = (String)opList.get(1);
           type = popNextOp(ex, opList);
       }
       wholeExpression.add(new Expression(type, (String)opList.get(0)));

       boolean negate = true;
       for(int i = 0; i < wholeExpression.size()-1; i++){
           Expression e = wholeExpression.get(i);
           Expression next = wholeExpression.get(i+1);

           //assumes only unary operator is -
           if(e.type == OpType.OPERATION){
              if(negate == true){
                  wholeExpression.remove(i);
                  wholeExpression.add(i, new Expression(OpType.VALUE, "1", true));
                  wholeExpression.add(i+1, new Expression(OpType.OPERATION, "*"));
                  negate = false;
              }
              else{
                  negate = true;
              }
           }
           else{
               negate = false;
           }
       }

       return wholeExpression;

        
    }

    public static OpType popNextOp(String expr, ArrayList<String> ops){
 
        char[] expArr = expr.toCharArray();
        char curr = expArr[0];

        StringBuilder valueBuilder = new StringBuilder();
        float value;

        char operation;

        StringBuilder subExpr;
        
        ops.clear();
        //ops = new ArrayList<String>();
        
        if(isDigit(curr)){
            int j = 1;
            while(j < expArr.length && isDigit(expArr[j])){
                j++;
            }

            valueBuilder = new StringBuilder(); 
            for(int i = 0; i < j; i++){
                valueBuilder.append(expArr[i]);
            }
            ops.add(valueBuilder.toString());

            valueBuilder = new StringBuilder();
            for(int i = j; i < expr.length(); i++){
                valueBuilder.append(expArr[i]);
            }
            if(valueBuilder.length() > 0){
                ops.add(valueBuilder.toString());
            }
            return OpType.VALUE;
        }

        if(curr == '+' || curr == '-' || curr == '*' || curr=='x' || curr == '/' || curr == '^'){
            ops.add(""+curr);

            valueBuilder = new StringBuilder();
            for(int i = 1; i < expr.length(); i++){
                valueBuilder.append(expArr[i]);
            }

            if(valueBuilder.length() > 0){
                ops.add(valueBuilder.toString());
            }

            return OpType.OPERATION;
        }

        if(curr == '('){
            int b=1;
            subExpr = new StringBuilder();

            int j;
            for(j = 1; j < expArr.length; j++){
                char ch = expArr[j];
                if(ch == '('){
                    b++;
                }
                if(ch == ')'){
                    b--;
                }
                if(b == 0){
                    break;
                }
                else{
                    subExpr.append(ch);
                }
            }

            ops.add(subExpr.toString());

            for(int i = j+1; i < expr.length(); i++){
                valueBuilder.append(expArr[i]);
            }

            if(valueBuilder.length() > 0){
                ops.add(valueBuilder.toString());
            }
            
            return OpType.EXPRESSION;
        }

        return OpType.VALUE;
                                  
        
    }       

    public String toString(){
        String out = "";
        switch(type){
            case OPERATION:
                out +=this.operation;
                break;
            case VALUE:
                out +=this.value;
                break;
            default:
                out += this.expression;
                break;
        }
        return out;
    }
}
 
