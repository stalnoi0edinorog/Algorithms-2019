package lesson3;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class SubSet<T extends Comparable<T>> extends BinaryTree<T> {
    private BinaryTree<T> tree;
    private T fromElement;
    private T toElement;

    SubSet(BinaryTree<T> tree, T fromElement, T toElement) {
        this.tree = tree;
        this.fromElement = fromElement;
        this.toElement = toElement;
    }

    @Override
    public boolean contains(Object o) {
        return tree.contains(o) && inRange((T) o);
    }

    @Override
    public boolean add(T t) {
        if (inRange(t)) {
            return tree.add(t);
        } else throw new IllegalArgumentException();
    }

    @Override
    public boolean remove(Object o) {
        return inRange((T)o) && tree.remove(o);
    }


    private boolean inRange(T t) {
        return (fromElement == null || fromElement.compareTo(t) <= 0) && (toElement == null || toElement.compareTo(t) > 0);
    }

    @Override
    public int size() {
        int size = 0;
        for (T t: this) {
            if (t != null)
                size++;
        }
        return size;
    }

    private class SubTreeIterator implements Iterator<T> {
        Iterator<T> iterator = tree.iterator();

        private SubTreeIterator() {

        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            while (iterator.hasNext()) {
                T t = iterator.next();
                if (inRange(t))
                    return t;
            }
            return null;
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new SubTreeIterator();
    }
}