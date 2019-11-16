package lesson3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// Attention: comparable supported but comparator is not
public class
BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private Node<T> root = null;

    private int size = 0;

    private boolean isLeftChild;

    private static class Node<T> {
        final T value;

        Node<T> left = null;

        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        }
        else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        }
        else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    public int height() {
        return height(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     * Трудоёмкость O(log(n)), n - количество узлов
     * Ресурсоёмкость O(1)
     */
    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        T value = (T) o;
        Node<T> parent = root;
        Node<T> current = root;
        isLeftChild = true;

        if (root == null)
            return false;

        while (current.value.compareTo(value) != 0) {
            parent = current;
            if (current.value.compareTo(value) > 0) {
                isLeftChild = true;
                current = current.left;
            } else {
                isLeftChild = false;
                current = current.right;
            }
            if (current == null)
                return false;
        }
        remove(current, parent);
        size--;
        return true;
    }

    private void removeNode(Node<T> node) {
        remove(node.value);
    }

    private void remove(Node<T> current, Node<T> parent) {
        if (current.left == null && current.right == null) {
            if (current == root) {
                root = null;
            } else if (isLeftChild) {
                parent.left = null;
            } else
                parent.right = null;
        } else if (current.left == null) {
            if (current == root) {
            root = current.right;
            } else if (isLeftChild) {
                parent.left = current.right;
            } else
                parent.right = current.right;
        } else if (current.right == null) {
            if (current == root) {
                root = current.left;
            } else if (isLeftChild) {
                parent.left = current.left;
            } else
                parent.right = current.left;
        } else {
            Node<T> successor = getSuccessor(current);
            if (current == root) {
                root = successor;
            } else if (isLeftChild) {
                parent.left = successor;
            } else
                parent.right = successor;
            successor.left = current.left;
        }

    }

    private Node<T> getSuccessor(Node<T> node) {
        Node<T> successorParent = node;
        Node<T> successor = node;
        Node<T> current = node.right;

        while (current != null) {
            successorParent = successor;
            successor = current;
            current = current.left;
        }
        if (successor != node.right) {
            successorParent.left = successor.right;
            successor.right = node.right;
        }
        return successor;
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {

        private Node<T> next;
        private Stack<Node<T>> stack = new Stack<>();

        private BinaryTreeIterator() {
            next = root;
            while (next != null) {
                stack.push(next);
                next = next.left;
            }
        }

        /**
         * Проверка наличия следующего элемента
         * Средняя
         * Трудоёмкость O(1)
         * Ресурсоёмкость O(1)
         */
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        /**
         * Поиск следующего элемента
         * Средняя
         * Трудоёмкость O(1), в худшем случае O(n), n - количество узлов
         * Ресурсоёмкость O(h), h - высота дерева
         */
        @Override
        public T next() {
            Node<T> lastInStack = stack.pop();
            next = lastInStack;
            if (lastInStack.right != null) {
                lastInStack = lastInStack.right;
                while (lastInStack != null) {
                    if (!stack.contains(lastInStack))
                        stack.push(lastInStack);
                    lastInStack = lastInStack.left;
                }
            }
            return next.value;
        }

        /**
         * Удаление следующего элемента
         * Сложная
         * Трудоёмкость O(log(N))
         * Ресурсоёмкость O(1)
         */
        @Override
        public void remove() {
           removeNode(next);
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     * Трудоёмкость O(1)
     * Ресурсоёмкость O(1)
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return new SubSet<>(this, fromElement, toElement);
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     * Трудоёмкость O(1)
     * Ресурсоёмкость O(1)
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        return new SubSet<>(this, null, toElement);
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     * Трудоёмкость O(1)
     * Ресурсоёмкость O(1)
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return new SubSet<>(this, fromElement, null);
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }
}
