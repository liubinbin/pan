package cn.liubinbin.experiment.calexpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 前几天面试时被问到表达式求值，当时困的原因脑子没转过来，现在写个简单的实现一下想法。
 * TODO 有很多bug未改，以后修复
 *
 * @author liubinbin
 */
public class CalExpression {

    public static void main(String[] args) {
        String expr = "3+4*5-2*4";
        System.out.println(String.format("expr: [%s] result: [%s]", expr, (new CalExpression()).outPutCalResult(expr)));
    }

    public int outPutCalResult(String expr) {
        List<Item> infix = getItemsFromExpr(expr);
        List<Item> postfix = getPostfixFromInfix(infix);
        int result = getResultFromPostfix(postfix);
        return result;
    }

    /**
     * 将一个字符串转化为一个个item，为中缀表达式
     *
     * @param expr
     * @return
     */
    private List<Item> getItemsFromExpr(String expr) {
        List<Item> results = new ArrayList<Item>();
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) > '1' && expr.charAt(i) < '9') {
                results.add(new Item(true, Integer.parseInt(expr.charAt(i) + ""), Op.NULL));
            } else {
                results.add(new Item(false, 0, Op.getOpFromStr(expr.charAt(i) + "")));
            }
        }
        return results;
    }

    /**
     * 将中缀表达式转化为后缀表达式
     *
     * @param items
     * @return
     */
    private List<Item> getPostfixFromInfix(List<Item> infix) {
        List<Item> postfix = new ArrayList<Item>();
        Stack<Op> opStack = new Stack<Op>();
        for (int i = 0; i < infix.size(); i++) {
            if (infix.get(i).isNumber) {
                postfix.add(infix.get(i));
            } else if (opStack.isEmpty()) {
                opStack.push(infix.get(i).getOp());
            } else if (Op.isHigher(infix.get(i).getOp()) && !Op.isHigher(opStack.peek())) { //遇到的优先级高于栈顶的
                postfix.add(infix.get(i + 1));
                postfix.add(infix.get(i));
                i++;
            } else if (!Op.isHigher(infix.get(i).getOp()) && Op.isHigher(opStack.peek())) { //遇到的优先级低于栈顶的
                postfix.add(new Item(false, 0, opStack.pop()));
                opStack.push(infix.get(i).getOp());
            } else { //遇到的优先级一样
                postfix.add(new Item(false, 0, opStack.pop()));
                opStack.push(infix.get(i).getOp());
            }
        }
        while (!opStack.isEmpty()) {
            postfix.add(new Item(false, 0, opStack.pop()));
        }
        return postfix;
    }

    /**
     * 从后缀表达式中计算结果
     *
     * @param items
     * @return
     */
    private int getResultFromPostfix(List<Item> items) {
        Stack<Integer> numbers = new Stack<Integer>();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isNumber) {
                numbers.push(items.get(i).number);
            } else {
                int post = numbers.pop();
                int pre = numbers.pop();
                int result = calresult(pre, items.get(i).getOp(), post);
                numbers.push(result);
            }
        }
        return numbers.pop();
    }

    private int calresult(int pre, Op op, int post) {
        if (op == Op.MULTI) {
            return pre * post;
        } else if (op == Op.DEVIDE) {
            return pre / post;
        } else if (op == Op.MINUS) {
            return pre - post;
        } else if (op == Op.PLUS) {
            return pre + post;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    class Item {
        private boolean isNumber;
        private int number;
        private Op op;

        public Item(boolean isNumber, int number, Op op) {
            this.isNumber = isNumber;
            this.number = number;
            this.op = op;
        }

        public boolean isNumber() {
            return isNumber;
        }

        public void setNumber(boolean isNumber) {
            this.isNumber = isNumber;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public Op getOp() {
            return op;
        }

        public void setOp(Op op) {
            this.op = op;
        }

        public String toString() {
            return String.format("isNumber{%s} number{%d} op {%s}", isNumber, number, op.toString());
        }
    }

    private void debug(List<Item> fix) {
        System.out.println("fix is following");
        for (int j = 0; j < fix.size(); j++) {
            System.out.println("item: " + fix.get(j));
        }
    }
}
