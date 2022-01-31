import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 *
 * Calculator class including core algorighm for calculating
 */
public class Calculator {
    
    /**
     * Check priority between operator <code>a</code> and <code>b</code>
     * 
     * @param a operator a
     * @param b operator b
     * @return true if operator a has high priority else return false
     */
    private static int checkPriority(char a, char b){
        int[][] priority = new int[][]{
                /* +  */{-1, -1,  1,  1, -1, -1 },
                /* -  */{-1, -1,  1,  1, -1, -1 },
                /* x  */{-1, -1, -1, -1, -1, -1 },
                /* ÷ */{-1, -1, -1, -1, -1, -1 },
                /* (  */{ 1,  1,  1,  1, -1, -1 },
                /*  . */{ 1,  1,  1,  1, -1, -1 },
                /*  ! */{ 1,  1,  1,  1,  1, -1 }};
                    /*    +   -   x   ÷  .   ! */
        Map<Character, Integer> char2idx = new HashMap<>();
        char2idx.put('+', 0);
        char2idx.put('-', 1);
        char2idx.put('*', 2);
        char2idx.put('/', 3);
        char2idx.put('(', 4);
        char2idx.put('.', 5);
        char2idx.put('!', 6);
        int result = 0;
        try {
            result = priority[char2idx.get(a)][char2idx.get(b)];
        } catch(Exception e) {
        }
        return result;
    }
    
    /**
     * Calculate by expression
     * 
     * @param exp the expression input by user
     * @return <code>ExpRes</code> including calculating resuult and message
     */
    public static ExpRes calculateExp(String exp) {
        try {            
            Stack<Character> operatorStack = new Stack<>();
            Stack<Double> numberStack = new Stack<>();
            int ptr = 0;
            while(ptr != exp.length()){
                char cur = exp.charAt(ptr++);
                if(cur >= 48 && cur <= 57){
                    String numBuffer = "" + cur;
                    while(ptr < exp.length() && ((exp.charAt(ptr) >= 48 &&  exp.charAt(ptr) <= 57) || exp.charAt(ptr) == '.')){
                        numBuffer += exp.charAt(ptr++);
                    }
                    if(ptr <= exp.length() -1 && exp.charAt(ptr) == '!') {
                        try {
                            numberStack.add(Double.valueOf(calculateFactor(Integer.valueOf(numBuffer))));
                        } catch(Exception e) {
                            throw new Exception("Number for calculating factorial should be integer.");
                        }
                        
                        ptr++;
                    } else {
                        double value = Double.parseDouble(numBuffer);
                        numberStack.add(value);
                    }
                }else if(cur == '('){
                    if(ptr < exp.length() && exp.charAt(ptr) == '-'){
                        String numBuffer = "-";
                        ptr++;
                        while(ptr < exp.length() && exp.charAt(ptr) != ')'){
                            numBuffer += exp.charAt(ptr++);
                        }
                        double value = Double.parseDouble(numBuffer);
                        numberStack.add(value);
                        ptr++;
                    }else{
                        operatorStack.add(cur);
                    }
                }else if(cur == ')'){
                    char top = 0;
                    if(!operatorStack.empty()) top = operatorStack.peek();
                    while(top != '('){
                        double num1 = numberStack.pop();
                        double num2 = numberStack.pop();
                        double res = 0;
                        char operator = operatorStack.pop();
                        if(operator == '+') {
                            res = num2 + num1;
                        } else if (operator == '-') {
                            res = num2 - num1;
                        } else if(operator == '*') { 
                                res = num2 * num1; 
                        } else if(operator == '/') {
                            if (num1 != 0) {
                                res = num2 / num1;
                            } else {
                                return new ExpRes("ERROR", "Divided by 0", 0);
                            }
                        }
                        numberStack.add(res);
                        if(!operatorStack.empty()) top = operatorStack.peek();
                        if(top == '(') top = operatorStack.pop();
                    }
                }else if(cur == '+' || cur == '-' || cur == '*' || cur == '/'){
                    if(operatorStack.empty()) operatorStack.add(cur);
                    else {
                        char top = operatorStack.peek();
                        if(checkPriority(top, cur) == -1){
                            double num1 = numberStack.pop();
                            double num2 = numberStack.pop();
                            double res = 0;
                            char operator = operatorStack.pop();
                            switch (operator) {
                                case '+' : res = num2 + num1; break;
                                case '-' : res = num2 - num1; break;
                                case '*' : res = num2 * num1; break;
                                case '/' : {
                                    if (num1 != 0) {
                                        res = num2 / num1;
                                    } else {
                                        return new ExpRes("ERROR", "Divided by 0", 0);
                                    }
                                }
                            }
                            operatorStack.add(cur);
                            numberStack.add(res);
                        }else{
                            operatorStack.add(cur);
                        }
                    }
                }
            }

            if(!operatorStack.empty()){
                while (!operatorStack.empty()){
                    if(numberStack.size() < 2) {
                        throw new Exception("Operators are more then needed!");
                    }
                    
                    double num1 = numberStack.pop();
                    double num2 = numberStack.pop();
                    double res = 0;
                    char operator = operatorStack.pop();
                    switch (operator) {
                        case '+' : res = num2 + num1; break;
                        case '-' : res = num2 - num1; break;
                        case '*' : res = num2 * num1; break;
                        case '/' : {
                            if (num1 != 0) {
                                res = num2 / num1;
                            } else {
                                return new ExpRes("ERROR", "Divided by 0", 0);
                            }
                        }
                    }
                    numberStack.add(res);
                }
            }

            double exp_res = numberStack.pop();
            return new ExpRes("OK", "", exp_res);
        
        } catch(Exception e) {
            return new ExpRes("ERROR", e.getMessage(), 0);
        }
    }
    
    /**
     * Calculate factorial with an original num <code>factor</code>
     * 
     * @param factor the original number used for calculating factorial
     * @return the factorial result
     */
    private static int calculateFactor(int factor) {
        int result = 1;
        for(int i = 1; i <= factor; i ++) {
            result*=i;
        }        
        return result;
    }

    /**
     * Basic calculating result model class.
     * It includes tag, to indicate if the calculating is successful or not
     *             msg, to show the warning message if the calculating failed
     *             res, the result value by calculating
     */
    public static class ExpRes{
        private String tag;
        private String msg;
        private double res;

        /**
         * Constructor of class <code>ExpRes</code>
         * 
         * @param tag the flag to show it fails or it is successful
         * @param msg the warning message if it fails
         * @param res the calculating result double value
         */
        public ExpRes(String tag, String msg, double res){
            this.tag = tag;
            this.msg = msg;
            this.res = res;
        }
        
        /**
         * Get the tag
         * 
         * @return the tag
         */
        public String getTag() {
            return tag;
        }
        
        /**
         * Get msg
         * 
         * @return the msg
         */
        public String getMsg() {
            return msg;
        }
        
        /**
         * Get res
         * 
         * @return res 
         */
        public Double getRes() {
            return res;
        }
    }
}
