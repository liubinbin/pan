package cn.liubinbin.experiment.calexpression;

public enum Op {
    PLUS("+"), MINUS("-"), MULTI("*"), DEVIDE("/"), NULL("n");
    private String operator;

    Op(String operator) {
        this.operator = operator;
    }

    public String toString() {
        return operator;
    }

    public static Op getOpFromStr(String opStr) {
        if (opStr.equalsIgnoreCase("+")) {
            return Op.PLUS;
        } else if (opStr.equalsIgnoreCase("-")) {
            return Op.MINUS;
        } else if (opStr.equalsIgnoreCase("*")) {
            return Op.MULTI;
        } else if (opStr.equalsIgnoreCase("/")) {
            return Op.DEVIDE;
        }
        return Op.NULL;
    }

    public static boolean isHigher(String opertor) {
        if (opertor.equalsIgnoreCase("*") || opertor.equalsIgnoreCase("/")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isHigher(Op op) {
        if (op == Op.MULTI || op == Op.DEVIDE) {
            return true;
        } else {
            return false;
        }
    }

}
