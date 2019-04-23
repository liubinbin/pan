package cn.liubinbin.experiment.linkedlistsort;

/**
 * @author liubinbin
 */
public class LinkedList {

    private final int sizeNoNeedSort = 1;
    private final int sizeJustSwap = 2;
    private Node head;
    private Node tail;
    private int size;

    public LinkedList() {
        this.setHead(null);
        this.setTail(null);
        this.setSize(0);
    }

    public static void main(String[] args) {
        {
            //case1
            LinkedList linkedList1 = new LinkedList();
            int data1[] = {4, 2, 7, 6, 8, 1, 3, 5};
            for (int i = 0; i < data1.length; i++) {
                linkedList1.add(data1[i]);
            }

            linkedList1.quickSort();
            linkedList1.print();
        }


        {
            //case2
            LinkedList linkedList2 = new LinkedList();
            int data2[] = {1, 2, 3, 4, 5, 6, 7, 8};
            for (int i = 0; i < data2.length; i++) {
                linkedList2.add(data2[i]);
            }

            linkedList2.quickSort();
            linkedList2.print();
        }

        {
            //case3
            LinkedList linkedList3 = new LinkedList();
            int data3[] = {8, 7, 6, 5, 4, 3, 2, 1};
            for (int i = 0; i < data3.length; i++) {
                linkedList3.add(data3[i]);
            }

            linkedList3.quickSort();
            linkedList3.print();
        }


    }

    public void add(int number) {
        Node temp = new Node(number);
        if (head == null) {
            head = temp;
            tail = temp;
        } else {
            tail.setNext(temp);
            tail = temp;
        }
        this.size++;
    }

    public void quickSort() {
        quickSortInternal(this);
    }

    private void quickSortInternal(LinkedList linkedList) {
        if (linkedList.getSize() <= sizeNoNeedSort) {
            return;
        } else if (linkedList.getSize() == sizeJustSwap) {
            if (linkedList.getHead().getNumber() > linkedList.getTail().getNumber()) {
                int tempNum = linkedList.getHead().getNumber();
                linkedList.getHead().setNumber(linkedList.getTail().getNumber());
                linkedList.getTail().setNumber(tempNum);
            }
            return;
        }

        int mid = (linkedList.getHead().getNumber() + linkedList.getTail().getNumber()) / 2;
        LinkedList smallPart = new LinkedList();
        LinkedList bigPart = new LinkedList();
        Node cur = linkedList.getHead();
        while (cur != null) {
            if (cur.getNumber() <= mid) {
                smallPart.add(cur.getNumber());
            } else {
                bigPart.add(cur.getNumber());
            }
            cur = cur.getNext();
        }
        smallPart.quickSort();
        bigPart.quickSort();
        LinkedList newLinkedList = merge(smallPart, bigPart);
        linkedList.setHead(newLinkedList.getHead());
        linkedList.setTail(newLinkedList.getTail());
    }

    private LinkedList merge(LinkedList smallPart, LinkedList bigPart) {
        smallPart.setTailNext(bigPart.getHead());
        smallPart.setSize(smallPart.getSize() + bigPart.getSize());
        smallPart.setTail(bigPart.getTail());
        return smallPart;
    }

    private void setTailNext(Node next) {
        this.tail.setNext(next);
    }

    public void print() {
        print("");
    }

    public void print(String tag) {
        System.out.print(tag + " size: " + this.getSize() + " data: {");
        Node cur = this.getHead();
        while (cur != null) {
            System.out.print(cur.getNumber() + ", ");
            cur = cur.getNext();
        }
        System.out.println("}");
    }

    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public Node getTail() {
        return tail;
    }

    public void setTail(Node tail) {
        this.tail = tail;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
