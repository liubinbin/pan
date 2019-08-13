package cn.liubinbin.pan.module;

/**
 * GET
 * PUT
 * DELETE
 * <p>
 * Created by bin on 2019/5/20.
 */
public enum OpEnum {

    GET("GET", 0), PUT("PUT", 1), DELETE("DELETE", 2), ALL("ALL", 3);

    private String op;
    private int idx;

    OpEnum(String op, int idx) {
        this.op = op;
        this.idx = idx;
    }

    public String getOp() {
        return op;
    }
}
