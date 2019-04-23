package cn.liubinbin.experiment.linkedlistsort;

/**
 * @author liubinbin
 */
public class Node {

    private int number = 0;
    private Node next = null;

    public Node(int number) {
        this.setNumber(number);
        this.setNext(null);
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
