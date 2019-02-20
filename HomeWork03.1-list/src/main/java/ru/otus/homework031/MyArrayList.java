package ru.otus.homework031;

import java.util.*;

public class MyArrayList<T> implements List<T> {
    private static int INITIAL_CAPACITY = 5;
    private int capacity = INITIAL_CAPACITY;
    private int offset = 0;
    private int size = 0;
    private T[] array = null;
    private final MyArrayList<T> parent;

    public MyArrayList() {
        this.parent = null;
        this.array = (T[]) new Object[capacity];
    }

    private MyArrayList(MyArrayList<T> parent, int startIndex, int endIndex) {
        this.offset = startIndex;
        this.size = endIndex - startIndex;
        this.parent = parent;
        this.array = parent.array;
        this.capacity = parent.capacity;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int indexOf(Object o) {
        Objects.requireNonNull(o);

        for (int i = offset; i < offset + size; i++) {
            if (array[i].equals(o)) {
                return i - offset;
            }
        }
        return -1;
    }

    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public class MyIterator implements Iterator<T> {
        int curIndex = 0;
        int lastReturnedIndex = -1;

        public MyIterator() {
        }

        @Override
        public boolean hasNext() {
            return curIndex < size; // max index
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturnedIndex = curIndex;
            ++curIndex;
            return MyArrayList.this.array[offset + lastReturnedIndex];
        }
    }

    public Iterator<T> iterator() {
        return new MyIterator();
    }

    public Object[] toArray() {
        return Arrays.copyOfRange(array, offset, offset + size);
    }

    public <T1> T1[] toArray(T1[] a) {
        if (a == null) {
            throw new NullPointerException();
        }

        if (a.length < size) {
            return (T1[]) Arrays.copyOfRange(array, offset, offset + size, a.getClass());
        } else {
            System.arraycopy(array, offset, a, 0, size);
            for (int i = size; i < a.length; i++) {
                a[i] = null;
            }
            return a;
        }
    }

    public boolean add(T t) {
        try {
            add(size, t);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public T remove(int index) {
        if (index >= size || index < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }

        MyArrayList root = this;
        while (root.parent != null) {
            --root.size;
            root = root.parent;
        }

        final int rootIndex = offset + index;
        for (int i = rootIndex; i < root.size - 1; i++) {
            swap(i, i + 1);
        }

        T removedObject = array[root.size - 1];
        array[root.size - 1] = null;
        --root.size;
//        System.out.println(Arrays.toString(array));
        return removedObject;
    }

    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        final var indexToRemove = indexOf(o);
        if (indexToRemove != -1) {
            remove(indexToRemove);
            return true;
        } else {
            return false;
        }
    }

    public boolean containsAll(Collection<?> c) {
        int counter = 0;
        for (var element : c) {
            if (indexOf(element) >= 0) {
                counter++;
            }
        }
        return counter == c.size();
    }

    public boolean addAll(Collection<? extends T> c) {
        return addAll(size, c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (c.contains(null)) {
            throw new NullPointerException();
        }
        if (c.isEmpty()) {
            return false;
        }

        int i = 0;
        for (var elem : c) {
            add(index + i, elem);
            ++i;
        }

        return true;
    }

    public boolean removeAll(Collection<?> c) {
        if (c.contains(null) || c == null) {
            throw new NullPointerException();
        }
        final var iterator = c.iterator();
        boolean result = false;
        while (iterator.hasNext()) {
            if (remove(iterator.next())) {
                result = true;
            }
        }

        return result;
    }

    public boolean retainAll(Collection<?> c) {
        if (c.contains(null) || c == null) {
            throw new NullPointerException();
        }

        int newSize = 0;
        for (int i = offset; i < offset + size; i++) {
            if (c.contains(array[i])) {
                for (int j = i; j > offset + newSize; j--) {
                    swap(j, j - 1);
                }
                newSize++;
            }
        }

        if (parent == null) {
            size = newSize;
        } else {
            subList(newSize, size).clear();
        }
        return newSize > 0;
    }

    public void clear() {
        if (parent == null) {
            size = 0;
        } else {
            final int elementsToRemove = size;

            MyArrayList root = this; // find root
            while (root.parent != null) {
                root.size -= elementsToRemove; // change size of sublists
                root = root.parent;
            }

            for (int i = 0; i < elementsToRemove; i++) {
                root.remove(offset);
            }
        }
    }

    public T get(int index) {
        Objects.checkIndex(index, size);
        return array[offset + index];
    }

    public T set(int index, T element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        var objectToreturn = array[offset + index];
        array[offset + index] = element;
        return objectToreturn;
    }

    public void add(int index, T element) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        if (element == null) {
            throw new NullPointerException();
        }

        MyArrayList root = this;
        while (root.parent != null) {
            ++root.size;
            root = root.parent;
        }

        if (root.size >= root.capacity) {
            int newCapacity = root.capacity * 2;
            var newArray = Arrays.copyOf(array, newCapacity);

            // need update ref to array and capacity through all hierarchy
            MyArrayList curList = this;
            do {
                curList.capacity = newCapacity;
                curList.array = newArray;
                curList = curList.parent;

            } while (curList != null);
        }

        final int rootIndex = offset + index;
        if (rootIndex == root.size) {
            array[root.size] = element;
        } else {
            for (int i = root.size; i > rootIndex; i--) {
                swap(root.capacity, i, i - 1);
            }
            array[rootIndex] = element;
        }

        ++root.size;
//        System.out.println(Arrays.toString(array) + ", root size: " + root.size);
    }

    public int lastIndexOf(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }

        for (int i = offset + size - 1; i >= 0; i--) {
            if (array[i].equals(o)) {
                return i - offset;
            }
        }
        return -1;
    }

    class MyListIterator extends MyIterator implements ListIterator<T>{
        public MyListIterator(int index) {
            super();
            this.curIndex = index;
        }

        @Override
        public boolean hasPrevious() {
            return curIndex > 0;
        }

        @Override
        public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            --curIndex;
            lastReturnedIndex = curIndex;
            return array[offset + curIndex];
        }

        @Override
        public int nextIndex() {
            return curIndex;
        }

        @Override
        public int previousIndex() {
            return curIndex - 1;
        }

        @Override
        public void remove() {
            if (lastReturnedIndex == -1) {
                throw new IllegalStateException();
            }

            if (curIndex >= 0 || curIndex < size) {
                MyArrayList.this.remove(lastReturnedIndex);
                curIndex--;
                lastReturnedIndex = -1;
            }
        }

        @Override
        public void set(T t) {
            if (lastReturnedIndex == -1) {
                throw new IllegalStateException();
            }
            if (lastReturnedIndex >= 0 || lastReturnedIndex < size) {
                MyArrayList.this.set(lastReturnedIndex, t);
            }
        }

        @Override
        public void add(T t) {
            if (curIndex >= 0 || curIndex < size) {
                MyArrayList.this.add(curIndex, t);
                lastReturnedIndex = -1;
                ++curIndex;
            }
        }
    }

    public ListIterator<T> listIterator() {
        return new MyListIterator(0);
    }

    public ListIterator<T> listIterator(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        return new MyListIterator(index);
    }


    public List<T> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }

        return new MyArrayList<>(this, offset + fromIndex, offset + toIndex);
    }

    public void print() {
        System.out.println(Arrays.toString(array) + ", size:" + size);
    }

    private void swap(int i, int j) {
        if (i >= capacity|| j >= capacity || i == j) {
            System.out.println("Can't swap elements");
            return;
        }

        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private void swap(int rootCapacity, int i, int j) {
        if (i >= rootCapacity || j >= rootCapacity || i == j) {
            System.out.println("Can't swap elements");
            return;
        }

        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
