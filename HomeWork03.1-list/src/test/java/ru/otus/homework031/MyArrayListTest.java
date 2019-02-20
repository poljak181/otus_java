package ru.otus.homework031;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.*;

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
    void testAddAllCollections() {
        Collections.addAll(list, 15, 16);
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8 , 9, 15, 16}, list.toArray());
        final var sublist = list.subList(0, 2);
        Collections.addAll(sublist, 33, 44);
        assertArrayEquals(new Integer[]{0, 1, 33, 44, 2, 3, 4, 5, 6, 7, 8 , 9, 15, 16}, list.toArray());
    }

    @Test
    void testCopyCollections() {
        var destList = new ArrayList<Integer>(Collections.nCopies(11, 7));
        Collections.copy(destList, list);
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8 , 9, 7}, destList.toArray());
    }

    @Test
    void testSortCollections() {
        Collections.sort(list, (lhs, rhs) -> {
            if (lhs < rhs) {
                return 1;
            } else if (lhs > rhs) {
                return -1;
            } else {
                return 0;
            }
        });

        assertArrayEquals(new Integer[]{9, 8, 7, 6, 5, 4, 3, 2, 1, 0}, list.toArray());
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
    void testToArrayCast() {
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
    void testToArraySubListCast() {
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
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 888, 8, 9}, list.toArray());
        assertArrayEquals(new Integer[]{3, 4, 5, 6, 7, 888}, sublist1.toArray());

        final var sublist2 = sublist1.subList(2, 6); // 5, 6, 7, 888
        sublist2.add(666);

        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 888, 666, 8, 9}, list.toArray());
        assertArrayEquals(new Integer[]{3, 4, 5, 6, 7, 888, 666}, sublist1.toArray());
        assertArrayEquals(new Integer[]{5, 6, 7, 888, 666}, sublist2.toArray());
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
        final var sublist2 = sublist1.subList(0, 1);
        sublist2.remove(0);
        assertArrayEquals(new Integer[]{}, sublist2.toArray());
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 6, 8, 9}, list.toArray());
    }

    @Test
    void removeObject() {
        list.remove(Integer.valueOf(0));
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, list.toArray());
        list.remove(Integer.valueOf(8));
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6, 7, 9}, list.toArray());
    }

    @Test
    void removeSubListObject() {
        final var sublist1 = list.subList(4, 8); // 4, 5, 6, 7
        sublist1.remove(Integer.valueOf(4));
        sublist1.remove(Integer.valueOf(7));
        assertArrayEquals(new Integer[]{5, 6}, sublist1.toArray());
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 5, 6, 8, 9}, list.toArray());
        final var sublist2 = sublist1.subList(0, 1);
        sublist2.remove(Integer.valueOf(5));
        assertArrayEquals(new Integer[]{}, sublist2.toArray());
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 6, 8, 9}, list.toArray());
    }

    @Test
    void testContainsAll() {
        assertEquals(true, list.containsAll(Arrays.asList(2, 5, 7)));
        assertEquals(false, list.containsAll(Arrays.asList(22, 5, 7)));
    }

    @Test
    void testContainsAllSublist() {
        final var sublist1 = list.subList(1, list.size() - 1);         // 1, 2, 3, 4, 5, 6, 7, 8
        final var sublist2 = sublist1.subList(1, sublist1.size() - 1); // 2, 3, 4, 5, 6, 7
        final var sublist3 = sublist2.subList(1, sublist2.size() - 1); // 3, 4, 5, 6
        final var sublist4 = sublist3.subList(1, sublist3.size() - 1); // 4, 5
        assertEquals(true, sublist1.containsAll(Arrays.asList(1, 2, 3, 4)));
        assertEquals(false, sublist2.containsAll(Arrays.asList(1, 2, 3, 4)));
        assertEquals(false, sublist3.containsAll(Arrays.asList(3, 4, 5, 6, 7)));
        assertEquals(true, sublist3.containsAll(Arrays.asList(3, 4, 5, 6)));
        assertEquals(true, sublist4.containsAll(Arrays.asList(4, 5)));
    }

    @Test
    void testAddAll() {
        list.addAll(Arrays.asList(22, 33, 44));
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 22, 33, 44}, list.toArray());
    }

    @Test
    void testAddAllSublist() {
        final var sublist1 = list.subList(1, list.size() - 1);         // 1, 2, 3, 4, 5, 6, 7, 8
        final var sublist2 = sublist1.subList(1, sublist1.size() - 1); // 2, 3, 4, 5, 6, 7
        sublist2.addAll(Arrays.asList(100, 200));
        assertArrayEquals(new Integer[]{2, 3, 4, 5, 6, 7, 100, 200}, sublist2.toArray());
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6, 7, 100, 200, 8}, sublist1.toArray());
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 100, 200, 8, 9}, list.toArray());
    }

    @Test
    void testAddAllIndex() {
        assertEquals(true, list.addAll(3, Arrays.asList(88, 99)));
        assertArrayEquals(new Integer[]{0, 1, 2, 88, 99, 3, 4, 5, 6, 7, 8, 9}, list.toArray());
    }

    @Test
    void testAddAllIndexSublist() {
        final var sublist = list.subList(3, 6); // 3, 4, 5
        assertEquals(true, sublist.addAll(0, Arrays.asList(666, 999)));
        assertArrayEquals(new Integer[]{666, 999, 3, 4, 5}, sublist.toArray());
        assertArrayEquals(new Integer[]{0, 1, 2, 666, 999, 3, 4, 5, 6, 7, 8, 9}, list.toArray());
    }

    @Test
    void testRemoveAll() {
        assertEquals(false, list.removeAll(Arrays.asList(77, 88)));
        assertEquals(true, list.removeAll(Arrays.asList(6, 8)));
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 5, 7, 9}, list.toArray());
    }

    @Test
    void testRemoveAllSubList() {
        final var sublist1 = list.subList(1, list.size() - 1);         // 1, 2, 3, 4, 5, 6, 7, 8
        final var sublist2 = sublist1.subList(1, sublist1.size() - 1); // 2, 3, 4, 5, 6, 7
        sublist2.removeAll(Arrays.asList(3, 5, 7));
        assertArrayEquals(new Integer[]{2, 4, 6}, sublist2.toArray());
        assertArrayEquals(new Integer[]{1, 2, 4, 6, 8}, sublist1.toArray());
        assertArrayEquals(new Integer[]{0, 1, 2, 4, 6, 8, 9}, list.toArray());
    }

    @Test
    void testRetainAll() {
        Collection<Integer> c = Arrays.asList(4, 2, 1);
        list.retainAll(c);
        assertArrayEquals(new Integer[] {1, 2, 4}, list.toArray());
    }

    @Test
    void testRetainAllSubList() {
        final var sublist1 = list.subList(2, 8); // 2, 3, 4, 5, 6, 7
        Collection<Integer> c = Arrays.asList(4, 2, 1);
        sublist1.retainAll(c);
        assertArrayEquals(new Integer[] {2, 4}, sublist1.toArray());
        assertArrayEquals(new Integer[] {0, 1, 2, 4, 8, 9}, list.toArray());
    }

    @Test
    void testClear() {
        list.clear();
        assertEquals(true, list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void testClearSubList() {
        final var sublist1 = list.subList(1, list.size() - 1);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8}, sublist1.toArray());
        final var sublist2 = sublist1.subList(1, sublist1.size() - 1);
        assertArrayEquals(new Integer[]{2, 3, 4, 5, 6, 7}, sublist2.toArray());
        final var sublist3 = sublist2.subList(1, sublist2.size() - 1);
        assertArrayEquals(new Integer[]{3, 4, 5, 6}, sublist3.toArray());
        final var sublist4 = sublist3.subList(1, sublist3.size() - 1);
        assertArrayEquals(new Integer[]{4, 5}, sublist4.toArray());

        sublist4.clear();
        assertEquals(0, sublist4.size());
        assertArrayEquals(new Integer[]{3, 6}, sublist3.toArray());
        assertEquals(2, sublist3.size());
        assertArrayEquals(new Integer[]{2, 3, 6, 7}, sublist2.toArray());
        assertEquals(4, sublist2.size());
        assertArrayEquals(new Integer[]{1, 2, 3, 6, 7, 8}, sublist1.toArray());
        assertEquals(6, sublist1.size());
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 6, 7, 8, 9}, list.toArray());
        assertEquals(8, list.size());
    }

    @Test
    void testGet() {
        assertEquals(4, list.get(4));
        assertEquals(9, list.get(9));
    }

    @Test
    void testGetSubList() {
        final var sublist1 = list.subList(2, 8);
        assertEquals(2, sublist1.get(0));
        assertEquals(7, sublist1.get(5));
    }

    @Test
    void testSet() {
        list.set(5, 555);
        assertEquals(555, list.get(5));
    }

    @Test
    void testSetSubList() {
        final var sublist1 = list.subList(2, 8);
        sublist1.set(5, 777);
        assertEquals(777, sublist1.get(5));
        assertEquals(777, list.get(7));
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
    void testLastIndexOf() {
        assertEquals(7, list.lastIndexOf(7));
    }

    @Test
    void testLastIndexOfSubList() {
        final var sublist1 = list.subList(2, 8);
        sublist1.add(1, 3);
        assertEquals(2, sublist1.lastIndexOf(3));
        assertEquals(4, list.lastIndexOf(3));
    }

    @Test
    void testListIterator() {
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
    }

    @Test
    void testListIteratorWithIndex() {
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
    void testListIteratorPrevious() {
        final var iter1 = list.listIterator(0);
        assertEquals(false, iter1.hasPrevious());

        final var iter2 = list.listIterator(10);
        assertEquals(true, iter2.hasPrevious());
        assertEquals(9, iter2.previous());
    }

    @Test
    void testListIteratorPreviousSublist() {
        final var sublist = list.subList(3, 6);
        assertEquals(false, sublist.listIterator().hasPrevious());
        assertThrows(NoSuchElementException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                sublist.listIterator().previous();
            }
        });

        final var iter = sublist.listIterator(1);
        assertEquals(true, iter.hasPrevious());
        assertEquals(true, iter.hasNext());
        assertEquals(3, iter.previous());
    }

    @Test
    void testListIteratorNextPreviousIndex() {
        final var iter1 = list.listIterator(0);
        assertEquals(-1, iter1.previousIndex());
        assertEquals(0, iter1.nextIndex());
        final var iter2 = list.listIterator(10);
        assertEquals(9, iter2.previousIndex());
        assertEquals(10, iter2.nextIndex());
    }

    @Test
    void testListIteratorNextPreviousIndexSublist() {
        final var sublist = list.subList(3, 6); // 3, 4, 5
        final var iter1 = sublist.listIterator();
        assertEquals(-1, iter1.previousIndex());
        assertEquals(0, iter1.nextIndex());

        assertThrows(IndexOutOfBoundsException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                sublist.listIterator(9);
            }
        });

        final var iter2 = sublist.listIterator(3);
        assertEquals(2, iter2.previousIndex());
        assertEquals(3, iter2.nextIndex());
    }

    @Test
    void testListIteratorAddToEmpty() {
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
    void testListIteratorAddToEmptySubList() {
        var sublist = list.subList(3, 3);
        var iter = sublist.listIterator();
        assert(sublist.isEmpty());
        assertEquals(-1, iter.previousIndex());
        assertEquals(0, iter.nextIndex());
        iter.add(777);
        assertEquals(1, sublist.size());
        assertEquals(0, iter.previousIndex());
        assertEquals(1, iter.nextIndex());
    }

    @Test
    void testListIteratorAdd() {
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

        assertArrayEquals(new Integer[]{777, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 666}, list.toArray());
    }

    @Test
    void testListIteratorAddSubList() {
        final var sublist = list.subList(3, 6); // 3, 4, 5
        final var iter = sublist.listIterator(1);
        iter.add(32167);

        assertArrayEquals(new Integer[]{3, 32167, 4, 5}, sublist.toArray());
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 32167, 4, 5, 6, 7, 8, 9}, list.toArray());
    }

    @Test
    void testListIteratorSet() {
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
    void testListIteratorSetSublist() {
        final var sublist = list.subList(3, 6); // 3, 4, 5
        final var iter = sublist.listIterator(1);

        assertThrows(IllegalStateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                iter.set(444);
            }
        });
        iter.next();
        iter.set(444);
        assertArrayEquals(new Integer[]{3, 444, 5}, sublist.toArray());
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 444, 5, 6, 7, 8, 9}, list.toArray());

        final var sublist1 = list.subList(1, list.size() - 1);         // 1, 2, 3, 4, 5, 6, 7, 8
        final var sublist2 = sublist1.subList(1, sublist1.size() - 1); // 2, 3, 4, 5, 6, 7
        final var it = sublist2.listIterator(1);
        it.next();
        it.set(555);
        assertArrayEquals(new Integer[]{1, 2, 555, 444, 5, 6, 7, 8}, sublist1.toArray());
        assertArrayEquals(new Integer[]{2, 555, 444, 5, 6, 7}, sublist2.toArray());
        assertArrayEquals(new Integer[]{0, 1, 2, 555, 444, 5, 6, 7, 8, 9}, list.toArray());
    }

    @Test
    void testListIteratorAddCheckNext() {
        var iter = list.listIterator();
        iter.next(); // between 0 and 1
        iter.add(777);
        assertEquals(1, iter.next()); //  subsequent call to previous would return the new element.
        assertEquals(2, iter.next()); //  subsequent call to previous would return the new element.
    }

    @Test
    void testListIteratorRemoveBegin() {
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
    void testListIteratorRemoveBeginSubList() {
        final var sublist1 = list.subList(1, list.size() - 1);         // 1, 2, 3, 4, 5, 6, 7, 8
        final var sublist2 = sublist1.subList(1, sublist1.size() - 1); // 2, 3, 4, 5, 6, 7
        final var iter = sublist2.listIterator(1);

        assertThrows(IllegalStateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                iter.remove();
            }
        });

        assertEquals(2, iter.previous());
        assertEquals(2, iter.next());
        assertEquals(2, iter.previous());
        iter.remove();
        assertArrayEquals(new Integer[]{3, 4, 5, 6, 7}, sublist2.toArray());
        assertArrayEquals(new Integer[]{1, 3, 4, 5, 6, 7, 8}, sublist1.toArray());
        assertArrayEquals(new Integer[]{0, 1, 3, 4, 5, 6, 7, 8, 9}, list.toArray());
    }
}