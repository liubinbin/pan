package cn.liubinbin.experiment.jmx;

/**
 * Created by bin on 2019/5/9.
 */
public interface HelloMBean {
    public String getName();
    public void setName(String name);
    public void printHello();
    public void printHello(String whoName);
}
