package cn.liubinbin.experiment.orderlinkedlist;

public class OrderLinkedList2<T> {
    public Node<T> first;
    public Node<T> last;

    class Node<T> {
        private T value;
        private Node<T> next;

        Node(T num) {
            this.value = num;
            next = null;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }
    }

    public OrderLinkedList2() {
        first = null;
        last = null;
    }

    public void add(T numToAdd) {
        Node<T> temp = new Node<T>(numToAdd);
        if (first == null) {
            first = temp;
            last = temp;
            return;
        }
        Node<T> current = first;
        if (compare(first.getValue(), numToAdd) > 0) {
            last = first;
            first = temp;
            first.next = last;
            return;
        }
        while (current.getNext() != null && compare(current.next.getValue(), numToAdd) < 0) {
            current = current.next;
        }
        temp.next = current.next;
        current.next = temp;
    }

    private int compare(T a1, T a2) {
        return ((Comparable) a1).compareTo((Comparable) a2);
    }

    public void printAll() {
        Node<T> current = first;
        System.out.print(current.getValue());
        current = current.next;
        while (current != null) {
            System.out.print("->" + current.getValue());
            current = current.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        OrderLinkedList2<Integer> orderLinkedList = new OrderLinkedList2<Integer>();
        orderLinkedList.add(3);
        orderLinkedList.printAll();

        orderLinkedList.add(1);
        orderLinkedList.printAll();

        orderLinkedList.add(2);
        orderLinkedList.printAll();

        orderLinkedList.add(4);
        orderLinkedList.printAll();

        orderLinkedList.add(4);
        orderLinkedList.printAll();
    }

}
