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
        private Node<E> previo;
        private boolean next_invocado;

        public SimpleListIterator() {
            actual = null;
            previo = null;
            next_invocado = false;
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
                throw new NoSuchElementException("No quedan elementos por recorrer");
            }

            if (actual == null) {
                actual = frente;
            } else {
                previo = actual;
                actual = actual.getNext();
            }
            next_invocado = true;

            return actual.getInfo();
        }

        @Override
        public void remove() {
            if (!next_invocado) {
                throw new IllegalStateException("Debe invocar a next() antes de remove()...");
            }

            if (previo == null) {
                frente = actual.getNext();
            } else {
                previo.setNext(actual.getNext());
            }

            actual = previo;
            next_invocado = false;
            cantidad--;
        }
    }
}
