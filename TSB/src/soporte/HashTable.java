package soporte;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashTable<E extends Comparable> 
        implements Serializable, Iterable<E> {

    private SimpleList<E>[] items;
    private int cantidad;

    public HashTable() {
        this(11);
    }

    public HashTable(int n) {
        if (n <= 0) {
            n = 11;
        }

        items = new SimpleList[n];
        for (int i = 0; i < n; i++) {
            items[i] = new SimpleList<>();
        }

        cantidad = 0;
    }

    public boolean isEmpty() {
        return (cantidad == 0);
    }
    
    public E get(E x) {
        if (x == null) {
            return null;
        }
        int y = h(x);
        if (items[y].isEmpty()) {
            return null;
        }
        return items[y].get(x);
    }

    public boolean put(E x) {
        if (x == null) {
            return false;
        }

        if (averageLength() >= 8) {
            rehash();
        }

        int y = h(x);
        items[y].addFirst(x);
        cantidad++;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder cad = new StringBuilder("");
        for (int i = 0; i < items.length; i++) {
            cad.append("\nLista ").append(i).append(":\n\t").append(items[i].toString());
        }
        return cad.toString();
    }

    protected void rehash() {
        int n = (int) (1.5 * items.length);

        n = siguientePrimo(n);

        SimpleList<E> temp[] = new SimpleList[n];
        for (int j = 0; j < temp.length; j++) {
            temp[j] = new SimpleList<>();
        }

        for (SimpleList<E> item : items) {

            for (E x : item) {
                int k = x.hashCode();
                int y = k % temp.length;
                temp[y].addFirst(x);
            }
        }

        items = temp;
    }

    public static boolean esPrimo(int n) {
        if (n == 2) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }
        long raiz = (long) Math.sqrt(n);
        for (long div = 3; div <= raiz; div += 2) {
            if (n % div == 0) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("empty-statement")
    public static int siguientePrimo(int n) {
        if (n <= 1) {
            return 3;
        }
        if (n % 2 == 0) {
            n++;
        }
        for (; !esPrimo(n); n += 2) ;
        return n;
    }

    private int h(int k) {
        k = Math.abs(k);
        return k % items.length;
    }

    private int h(Comparable x) {
        return h(x.hashCode());
    }

    private int averageLength() {
        return cantidad / items.length;
    }
    
    @Override
    public Iterator<E> iterator() {
        return new HashTableIterator();
    }
    
    private class HashTableIterator implements Iterator<E> {

        private int pos;
        private int contados;
        private Iterator<E> it;

        public HashTableIterator() {
            pos = 0;
            contados = 0;
            it = null;
        }

        @Override
        public boolean hasNext() {
            if (contados >= cantidad) {
                return false;
            }
            if (it == null) {
                return true;
            }
            Iterator<E> tempIt = it;
            int tempPos = pos;
            while (!tempIt.hasNext() && tempPos < items.length) {
                tempPos++;
                tempIt = items[tempPos].iterator();
            }
            return tempIt.hasNext();
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No quedan elementos");
            }

            if (it == null) {
                it = items[pos].iterator();
            }
            while (!it.hasNext()) {
                pos++;
                it = items[pos].iterator();
            }
            contados++;
            return it.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("No se permite remover");
        }
    }
}
