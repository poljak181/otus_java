package ru.otus.homework031;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MyArrayListTest {
    private MyArrayList<Integer> list = null;

    private static <T> MyArrayList<T> prepareList(T... t) {
        var list = new MyArrayList<T>();
        for (var e : t)
            list.add(e);
        return list;
    }

    @BeforeEach
    void setUp() {
        list = new MyArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
    }

    @AfterEach
    void tearDown() {
        list = null;
    }

    @Test
    void size() {
    }

    @Test
    void isEmpty() {
    }

    @Test
    void contains() {
    }

    @Test
    void iterator() {
    }

    @Test
    void toArray() {
    }

    @Test
    void toArray1() {
    }

    @Test
    void add() {
    }

    @Test
    void remove() {
    }

    @Test
    void containsAll() {
    }

    @Test
    void addAll() {
    }

    @Test
    void addAll1() {
    }

    @Test
    void removeAll() {
    }

    @Test
    void retainAll() {
    }

    @Test
    void clear() {
    }

    @Test
    void get() {
    }

    @Test
    void set() {
    }

    @Test
    void add1() {
    }

    @Test
    void remove1() {
    }

    @Test
    void indexOf() {
    }

    @Test
    void lastIndexOf() {
    }

    @Test
    void listIterator() {
        var iter = list.listIterator();
        assertEquals(true, iter.hasNext());
        assertEquals(false, iter.hasPrevious());

        assertThrows(NoSuchElementException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                iter.previous();
            }
        });

        assertEquals(-1, iter.previousIndex());
        assertEquals(0, iter.next());
        assertEquals(1, iter.next());
        assertEquals(1, iter.previous());
        assertEquals(0, iter.previous());
        assertEquals(-1, iter.previousIndex());

        Integer object = null;
        while (iter.hasNext()) {
            object = iter.next();
        }

        assertEquals(9, object);
        assertEquals(10, iter.nextIndex());

        assertEquals(10, list.size());
//        iter.add(10);
//        assertEquals(11, list.size());
//        assertEquals(10, iter.previousIndex());
//        assertEquals(11, iter.nextIndex());

    }

    @Test
    void listIteratorAddToEmpty() {
        var newList = new MyArrayList<Integer>();
        var iter = newList.listIterator();
        assert(newList.isEmpty());
        assertEquals(-1, iter.previousIndex());
        assertEquals(0, iter.nextIndex());
        iter.add(777);
        assertEquals(1, newList.size());
        assertEquals(0, iter.previousIndex());
        assertEquals(1, iter.nextIndex());
    }

    @Test
    void listIteratorAdd() {
        var iter = list.listIterator();
        iter.add(777);
        assertEquals(777, list.get(0));
        assertEquals(0, iter.previousIndex());
        assertEquals(1, iter.nextIndex());
        assertEquals(0, iter.next());

        while (iter.hasNext()) {
            iter.next();
        }
        iter.add(666);
        assertEquals(666, list.get(list.size() - 1));
        assertEquals(iter.nextIndex(), list.size());
        assertEquals(iter.previousIndex(), list.size() - 1);
    }

    @Test
    void listIteratorSet() {
        var iter = list.listIterator();

        assertThrows(IllegalStateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                iter.set(100);
            }
        });
        iter.next();
        iter.set(777); // 0 = 777
        assertEquals(777, list.get(0));
        iter.set(111);
        assertEquals(111, list.get(0));
        iter.add(12);
        assertEquals(12, list.get(1));
        assertEquals(12, list.get(iter.previousIndex()));
        assertEquals(1, list.get(iter.nextIndex()));

        assertThrows(IllegalStateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                iter.set(100);
            }
        });
        assertEquals(1, iter.next());
        iter.remove();
    }

    @Test
    void listIteratorAddCheckNext() {
        var iter = list.listIterator();
        iter.next(); // between 0 and 1
        iter.add(777);
        assertEquals(1, iter.next()); //  subsequent call to previous would return the new element.
        assertEquals(2, iter.next()); //  subsequent call to previous would return the new element.
    }

    @Test
    void listIteratorRemoveBegin() {
        var iter = list.listIterator();

        assertThrows(IllegalStateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                iter.remove();
            }
        });
        iter.next();
        iter.remove();
        assertEquals(1, iter.next());
        iter.add(17);

        assertThrows(IllegalStateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                iter.remove();
            }
        });
    }


    @Test
    void listIteratorWithIndex() {
        var iter1 = list.listIterator(0);
        assertEquals(0, iter1.next());

        assertThrows(IndexOutOfBoundsException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                var iter2 = list.listIterator(11);
            }
        });

        var iter3 = list.listIterator(5);
        assertEquals(4, iter3.previousIndex());
        assertEquals(5, iter3.nextIndex());
        assertEquals(5, iter3.next());

    }

    @Test
    void subList() {
    }

    @Test
    void subListSizeTest() {
        final var l = list.subList(0, 3);
        assertEquals(3, l.size());
    }
}