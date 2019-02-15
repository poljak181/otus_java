package ru.otus.homework031;

import java.util.*;

public class MyArrayList<T> implements List<T> {
    private static int INITIAL_CAPACITY = 5;
    private int capacity = INITIAL_CAPACITY;
    private int size = 0;
    private T[] array = (T[]) new Object[capacity];

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public class MyIterator implements Iterator<T> {
        int index = 0;
        int lastIndex = -1;

        public MyIterator() {
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastIndex = index;
            return (T)MyArrayList.this.array[index++];
        }
    }

    public Iterator<T> iterator() {
        return new MyIterator();
    }

    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    public <T1> T1[] toArray(T1[] a) {
        if (a == null) {
            throw new NullPointerException();
        }

        if (a.length < size) {
            return (T1[]) Arrays.copyOf(array, size, a.getClass());
        } else {
            System.arraycopy(array, 0, a, 0, size);
            for (int i = size; i < a.length; i++) {
                a[i] = null;
            }
            return a;
        }
    }

    public boolean add(T t) { // +
        try {
            add(size, t);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }

        for (int i = 0; i < size; i++) {
            if (array[i].equals(o)) {
                while (i < size - 1) {
                    swap(i, i + 1);
                    ++i;
                }
                array[size - 1] = null;
                --size;
                System.out.println(Arrays.toString(array));
                return true;
            }
        }
        return false;
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

        if (size + c.size() > capacity) {
            capacity = size + c.size();
            array = Arrays.copyOf(array, capacity);
        }

        final var inputArray = c.toArray();
        if (index == size) {
            System.arraycopy(inputArray, 0, array, index, inputArray.length);
        } else {
            final var tmpArray = Arrays.copyOfRange(array, index, size);
            System.arraycopy(inputArray, 0, array, index, inputArray.length);
            System.arraycopy(tmpArray, 0, array, index + c.size(), tmpArray.length);
        }
        size += c.size();
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
        for (int i = 0; i < size; i++) {
            if (c.contains(array[i])) {
                for (int j = i; j > newSize; j--) {
                    swap(j, j - 1);
                }
                newSize++;
            }
        }
        size = newSize;
        return newSize > 0;
    }

    public void clear() {
        size = 0;
        capacity = INITIAL_CAPACITY;
        array = (T[]) new Object[capacity];
    }

    public T get(int index) {
        Objects.checkIndex(index, size);
        return array[index];
    }

    public T set(int index, T element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        var objectToreturn = array[index];
        array[index] = element;
        return objectToreturn;
    }

    public void add(int index, T element) { // +
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        if (element == null) {
            throw new NullPointerException();
        }

        if (size >= capacity) {
            capacity *= 2;
            array = Arrays.copyOf(array, capacity);
        }

        if (index == size) {
            array[size] = element;
        } else {
            for (int i = size; i > index; i--) {
                swap(i, i - 1);
            }
            array[index] = element;
        }

        ++size;

        System.out.println(Arrays.toString(array));
    }

    public T remove(int index) {
        if (index >= size || index < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }

        for (int i = index; i < size - 1; i++) {
            swap(i, i + 1);
        }

        T removedObject = array[size - 1];
        array[size - 1] = null;
        --size;
        System.out.println(Arrays.toString(array));
        return removedObject;
    }

    private int privateIndexOf(Object o, int startIndex, int size) {
        Objects.requireNonNull(o);

        for (int i = startIndex; i < startIndex + size; i++) {
            if (array[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(Object o) {
        return privateIndexOf(o, 0, size);
    }

    public int lastIndexOf(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }

        for (int i = size - 1; i >= 0; i--) {
            if (array[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    class MyListIterator extends MyIterator implements ListIterator<T>{
        public MyListIterator(int index) {
            super();
            this.index = index;
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            lastIndex = index;
            return array[--index];
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            if (lastIndex == -1) {
                throw new IllegalStateException();
            }

            if (index >= 0 || index < size) {
                MyArrayList.this.remove(lastIndex);
                index--;
                lastIndex = -1;
            }
        }

        @Override
        public void set(T t) {
            if (lastIndex == -1) {
                throw new IllegalStateException();
            }
            if (lastIndex >= 0 || lastIndex < size) {
                MyArrayList.this.set(lastIndex, t);
            }
        }

        @Override
        public void add(T t) {
            if (index >= 0 || index < size) {
                MyArrayList.this.add(index, t);
                lastIndex = -1;
                ++index;
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

    class SubList implements List<T> {
        private final MyArrayList<T> root;
        private final SubList parent;
        private final int offset;
        private int size;

        public SubList(MyArrayList<T> root, int startIndex, int endIndex) {
            this.root = root;
            this.parent = null;
            this.offset = startIndex;
            this.size = endIndex - startIndex;
        }

        public SubList(SubList parent, int startIndex, int endIndex) {
            this.root = parent.root;
            this.parent = parent;
            this.offset = parent.offset + startIndex;
            this.size = endIndex - startIndex;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        public boolean contains(Object o) {
            return indexOf(o) != -1;
        }

        @Override
        public Iterator<T> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T1> T1[] toArray(T1[] a) {
            return null;
        }

        @Override
        public boolean add(T t) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends T> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public T get(int index) {
            return null;
        }

        @Override
        public T set(int index, T element) {
            return null;
        }

        @Override
        public void add(int index, T element) {

        }

        @Override
        public T remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            final var result = privateIndexOf(o, offset, size);
            return result == -1 ? -1 : result - offset;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<T> listIterator() {
            return null;
        }

        @Override
        public ListIterator<T> listIterator(int index) {
            return null;
        }

        @Override
        public List<T> subList(int fromIndex, int toIndex) {
            return null;
        }
    }


    public List<T> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }

        return new SubList(this, fromIndex, toIndex);
    }

    public void print() {
        System.out.println(Arrays.toString(array) + ", size:" + size);

    }

    private void swap(int i, int j) {
        if (i >= capacity || j >= capacity || i == j) {
            System.out.println("Can't swap elements");
            return;
        }

        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
