package soporte;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleList<E extends Comparable> implements Iterable<E> {

    private Node<E> frente;
    private int cantidad;

    public SimpleList() {
        frente = null;
        cantidad = 0;
    }
    
    public E get(E x) {
        if (x == null) {
            return null;
        }
        for (Node<E> p = frente; p != null; p = p.getNext()) {
            if (x.compareTo(p.getInfo()) == 0) {
                return p.getInfo();
            }
        }
        return null;
    }

    public void addFirst(E x) {
        if (x == null) {
            return;
        }

        Node< E> p = new Node<>(x, frente);
        frente = p;

        cantidad++;
    }

    public boolean isEmpty() {
        return (frente == null);
    }

    @Override
    public Iterator<E> iterator() {
        return new SimpleListIterator();
    }

    public int size() {
        return cantidad;
    }

    @Override
    public String toString() {
        Node<E> p = frente;
        StringBuilder res = new StringBuilder();
        res.append("[");
        while (p != null) {
            res.append(p.toString());
            if (p.getNext() != null) {
                res.append(", ");
            }
            p = p.getNext();
        }
        res.append("]");
        return res.toString();
    }

    private class SimpleListIterator implements Iterator<E> {

        private Node<E> actual;

        public SimpleListIterator() {
            actual = null;
        }

        @Override
        public boolean hasNext() {
            if (frente == null) {
                return false;
            }
            return !(actual != null && actual.getNext() == null);
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No quedan elementos");
            }

            if (actual == null) {
                actual = frente;
            } else {
                actual = actual.getNext();
            }
            return actual.getInfo();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("No se permite remover");
        }
    }
}
