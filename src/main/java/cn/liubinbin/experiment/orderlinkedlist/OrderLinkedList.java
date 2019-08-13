package cn.liubinbin.experiment.orderlinkedlist;

public class OrderLinkedList<T> {
    public Node<Comparable> first;
    public Node<Comparable> last;

    class Node<T extends Comparable> {
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

    public OrderLinkedList() {
        first = null;
        last = null;
    }

    public void add(T numToAdd) {
        Node<Comparable> temp = new Node<Comparable>((Comparable) numToAdd);
        if (first == null) {
            first = temp;
            last = temp;
            return;
        }
        Node<Comparable> current = first;
        if (first.getValue().compareTo(numToAdd) > 0) {
            last = first;
            first = temp;
            first.next = last;
            return;
        }
        while (current.getNext() != null && current.next.getValue().compareTo(numToAdd) < 0) {
            current = current.next;
        }
        temp.next = current.next;
        current.next = temp;
    }

    public void printAll() {
        Node<Comparable> current = first;
        System.out.print(current.getValue());
        current = current.next;
        while (current != null) {
            System.out.print("->" + current.getValue());
            current = current.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        OrderLinkedList<Integer> orderLinkedList = new OrderLinkedList<Integer>();
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
