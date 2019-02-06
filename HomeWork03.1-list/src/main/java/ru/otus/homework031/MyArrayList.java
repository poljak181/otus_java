package ru.otus.homework031;

import java.util.*;

public class MyArrayList<T> implements List<T> {
    private int capacity = 5;
    private int size = 0;
    private T[] array = (T[]) new Object[capacity];

    public int size() { // +
        return size;
    }

    public boolean isEmpty() { // +
        return size == 0;
    }

    public boolean contains(Object o) { // +
        for (int i = 0; i < size; i++) {
            if (array[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    public Iterator<T> iterator() {
        return null;
    }

    public Object[] toArray() {
        return array;
    }

    public <T1> T1[] toArray(T1[] a) {
        return null;
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
        return false;
    }

    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        T[] newArray = (T[]) new Object[capacity]; // TODO: we can use only origin array (remove - erase)
        int newArrayIndex = 0;
        int removedCount = 0;
        for (int i = 0; i < size; i++) {
            var iter = c.iterator();
            while (iter.hasNext()) {
                if (!array[i].equals(iter.next())) {
                    newArray[newArrayIndex++] = array[i];
                } else {
                    ++removedCount;
                }
            }
        }
        size -= removedCount;
        array = newArray;
        return removedCount != 0;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public void clear() {

    }

    public T get(int index) {
        if (index >= size || index < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return array[index];
    }

    public T set(int index, T element) {
        return null;
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
        return 0;
    }

    public int lastIndexOf(Object o) {
        return 0;
    }

    public ListIterator<T> listIterator() {
        return null;
    }

    public ListIterator<T> listIterator(int index) {
        return null;
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return null;
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
