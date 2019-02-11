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

    public class MyArrayListIterator<T> implements Iterator<T> {
        private int index = 0;
        private T[] array;

        MyArrayListIterator(T[] array) {
            this.array = array;
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
            return this.array[index++];
        }
    }

    public Iterator<T> iterator() {
        return new MyArrayListIterator<>(array);
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

    public int indexOf(Object o) {
        Objects.requireNonNull(o);

        for (int i = 0; i < size; i++) {
            if (array[i].equals(o)) {
                return i;
            }
        }
        return -1;
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

    public ListIterator<T> listIterator() {
        return null;
    }

    public ListIterator<T> listIterator(int index) {
        return null;
    }

    public List<T> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }

        return null;
    }

    public void print() {
        System.out.println(Arrays.toString(array));
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
