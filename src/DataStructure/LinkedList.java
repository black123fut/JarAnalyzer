package DataStructure;

public class LinkedList<T> {
    private Node<T> head;
    private int size;

    public LinkedList(){
        this.head = null;
        this.size = 0;
    }

    public void add(T data){
        if (head != null){
            Node<T> tmp = head;

            while (tmp.getNext() != null){
                tmp = tmp.getNext();
            }
            tmp.setNext(new Node<>(data));
        }else {
            head = new Node<>(data);
        }
        size++;
    }

    public void remove(int index){
        if (head != null){
            Node<T> tmp = head;
            int counter = 0;

            while (tmp.getNext() != null){
                if (index == counter){
                    size--;
                    tmp.setNext(tmp.getNext().getNext());
                    return;
                }
                tmp = tmp.getNext();
                counter++;
            }
        }
    }

    public void remove(T data){
        if (head != null){
            if (data == head.getData()){
                size--;
                head = head.getNext();
            } else {
                Node<T> tmp = head;

                while(tmp.getNext() != null){
                    if (data == tmp.getNext().getData()){
                        size--;
                        tmp.setNext(tmp.getNext().getNext());
                        return;
                    }
                    tmp = tmp.getNext();
                }
            }
        }
    }

    public void intercambiar(int pos1, int pos2) {
        if (pos1 <= length() - 1 && pos2 <= length() - 1) {
            Node<T> copia1 = head;
            for (int f = 1; f <= pos1; f++)
                copia1 = copia1.getNext();
            Node<T> copia2 = head;
            for (int f = 1; f <= pos2; f++)
                copia2 = copia2.getNext();
            T aux = copia1.getData();
            copia1.setData(copia2.getData());
            copia2.setData(aux);
        } else {
            throw new IllegalArgumentException("Posicion no exite");
        }
    }

    public T get(int index){
        if (head != null && index <= size){
            Node<T> tmp = head;

            for (int i = 0; i < index; i++) {
                tmp = tmp.getNext();
            }
            return tmp.getData();
        }
        return null;
    }

    public boolean contains(T data){
        if (head != null){
            Node<T> tmp = head;

            while (tmp != null){
                if (tmp.getData() == data){
                    return true;
                }
                tmp = tmp.getNext();
            }
        }
        return false;
    }

    public Node<T> getNode(int index){
        if (this.head != null && index <= size){
            Node<T> tmp = head;

            for (int i = 0; i < index; i++) {
                tmp = tmp.getNext();
            }
            return tmp;
        }
        return null;
    }

    public boolean vacia(){
        return head == null;
    }

    public int length(){
        return size;
    }
}

