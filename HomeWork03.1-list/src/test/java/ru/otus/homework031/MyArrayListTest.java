package ru.otus.homework031;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MyArrayListTest {
    private MyArrayList<Integer> list = null;

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
    void testSize() {
        assertEquals(10, list.size());
    }

    @Test
    void testSizeSubList() {
        var sublist = list.subList(4, 8);
        assertEquals(4, sublist.size());
    }

    @Test
    void testIsEmpty() {
        assertEquals(false, list.isEmpty());
    }

    @Test
    void testIsEmptySubList() {
        var sublist = list.subList(3, 3);
        assertEquals(true, sublist.isEmpty());
    }

    @Test
    void testIndexOf() {
        assertEquals(0, list.indexOf(0));
        assertEquals(3, list.indexOf(3));
    }

    @Test
    void testIndexOfSubList() {
        var sublist = list.subList(1, 5);
        assertEquals(-1, sublist.indexOf(0));
        assertEquals(-1, sublist.indexOf(5));
        assertEquals(0, sublist.indexOf(1));
        assertEquals(3, sublist.indexOf(4));
    }

    @Test
    void testContains() {
        assertEquals(true, list.contains(7));
        assertEquals(false, list.contains(11));
    }

    @Test
    void testContainsSubList() {
        final var sublist = list.subList(0, 5);
        assertEquals(true, sublist.contains(0));
        assertEquals(false, sublist.contains(5));
    }

    @Test
    void testIteratorHasNext() {
        final var iter = list.iterator();
        assertEquals(true, iter.hasNext());
        final var emptyList = new MyArrayList();
        assertEquals(false, emptyList.iterator().hasNext());
    }

    @Test
    void testIteratorHasNextSubList() {
        final var sublist = list.subList(5, 9);
        assertEquals(true, sublist.iterator().hasNext());
        final var sublist2 = list.subList(1, 1);
        assertEquals(false, sublist2.iterator().hasNext() );
    }
    @Test

    void testIteratorNext() {
        final var iter = list.iterator();
        for (int i = 0; i < 3; i++) {
            iter.next();
        }
        assertEquals(3, iter.next());
        while (iter.hasNext()) {
            iter.next();
        }
        assertThrows(NoSuchElementException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                iter.next();
            }
        });
    }

    @Test
    void testIteratorNextSubList() {
        final var sublist = list.subList(9, 10);
        final var iter = sublist.iterator();
        assertEquals(9, iter.next());
        assertThrows(NoSuchElementException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                iter.next();
            }
        });
    }

    @Test
    void testToArray() {
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, list.toArray());
    }

    @Test
    void testToArraySubList() {
        final var sublist = list.subList(1, 5);
        assertArrayEquals(new Integer[]{1, 2, 3, 4}, sublist.toArray());
    }

    @Test
    void testToArray1() {
        final var arr1 = new Integer[]{1, 2, 3};
        var arr1result = list.toArray(arr1);
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, arr1result);

        final var arr2 = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        var arr2result = list.toArray(arr2);
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, null, null, null}, arr2result);

        final var arr3 = new Integer[]{1, 1, 2, 3, 4, 5, 6, 7, 8, 8};
        var arr3result = list.toArray(arr3);
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, arr3result);
    }

    @Test
    void testToArraySubList1() {
        final var sublist1 = list.subList(0, 6);
        final var sublist2 = sublist1.subList(2, 5);

        final var arr1 = new Integer[]{1, 2, 3};
        assertArrayEquals(new Integer[]{2, 3, 4}, sublist2.toArray(arr1));

        final var arr2 = new Integer[]{0, 0, 0, 1, 2, 3};
        assertArrayEquals(new Integer[]{2, 3, 4, null, null, null}, sublist2.toArray(arr2));
    }

    @Test
    void testAdd() {
        list.add(777);
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 777}, list.toArray());

        list.add(0, 17);
        assertArrayEquals(new Integer[]{17, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 777}, list.toArray());

        list.add(6, 27);
        assertArrayEquals(new Integer[]{17, 0, 1, 2, 3, 4, 27, 5, 6, 7, 8, 9, 777}, list.toArray());
    }

    @Test
    void testAddSubList() {
        final var sublist1 = list.subList(3, 8); // 3, 4, 5, 6, 7
        sublist1.add(888);
        System.out.println("list: " + Arrays.toString(list.toArray()) + ", size: " + list.size());
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 888, 8, 9}, list.toArray());
        assertArrayEquals(new Integer[]{3, 4, 5, 6, 7, 888}, sublist1.toArray());

        final var sublist2 = sublist1.subList(2, 6); // 5, 6, 7, 888
        sublist2.add(666);

        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 888, 666, 8, 9}, list.toArray());
        assertArrayEquals(new Integer[]{3, 4, 5, 6, 7, 888, 666}, sublist1.toArray());
        assertArrayEquals(new Integer[]{5, 6, 7, 888, 666}, sublist2.toArray());
    }

    @Test
    void testAddIndex() {
        list.add(7, 15);
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 5, 6, 15, 7, 8, 9}, list.toArray());
        list.add(0, 111);
        assertArrayEquals(new Integer[]{111, 0, 1, 2, 3, 4, 5, 6, 15, 7, 8, 9}, list.toArray());
    }

    @Test
    void testAddIndexSubList() {
        final var sublist1 = list.subList(0, 4); // 0, 1, 2, 3
        sublist1.add(0, 333);

        assertArrayEquals(new Integer[]{333, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, list.toArray());
        assertArrayEquals(new Integer[]{333, 0, 1, 2, 3}, sublist1.toArray());

        final var sublist2 = sublist1.subList(2, 5); // 1, 2, 3
        sublist2.add(1, 42);

        assertArrayEquals(new Integer[]{333, 0, 1, 42, 2, 3, 4, 5, 6, 7, 8, 9}, list.toArray());
        assertArrayEquals(new Integer[]{333, 0, 1, 42, 2, 3}, sublist1.toArray());
        assertArrayEquals(new Integer[]{1, 42, 2, 3}, sublist2.toArray());
    }

    @Test
    void remove() {
        list.remove(0);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, list.toArray());
        list.remove(8);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8}, list.toArray());
    }

    @Test
    void removeSubList() {
        final var sublist1 = list.subList(4, 8); // 4, 5, 6, 7
        sublist1.remove(0);
        sublist1.remove(2);
        assertArrayEquals(new Integer[]{5, 6}, sublist1.toArray());
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 5, 6, 8, 9}, list.toArray());
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
    void testSubListSize() {
        final var l = list.subList(0, 3);
        assertEquals(3, l.size());
    }

    @Test
    void testSubListIsEmpty() {
        final var sublist1 = list.subList(0, 1);
        assertEquals(false, sublist1.isEmpty());

        final var sublist2 = list.subList(1, 1);
        assertEquals(true, sublist2.isEmpty());
    }

    @Test
    void testSubListIndexOf() {
        final var subList = list.subList(3, 8);
        assertEquals(1, subList.indexOf(4));
        assertEquals(-1, subList.indexOf(444));
        assertEquals(4, subList.indexOf(7));
    }

    @Test
    void testSubListContains() {
        final var subList = list.subList(7, 9);
        assertEquals(true, subList.contains(7));
        assertEquals(true, subList.contains(8));
        assertEquals(false, subList.contains(9));
    }

}