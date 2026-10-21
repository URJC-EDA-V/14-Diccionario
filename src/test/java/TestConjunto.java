import es.urjc.grafo.EDA.conjuntos.ConjuntoTablaHash;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestConjunto {

    @Test
    void startsEmpty() {
        ConjuntoTablaHash<String> set = new ConjuntoTablaHash<>();

        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
        assertFalse(set.contains("missing"));
        assertFalse(set.remove("missing"));
        assertIterableEquals(Set.of(), set.elements());
    }

    @Test
    void addReportsWhetherSetChangedAndUsesEquality() {
        ConjuntoTablaHash<String> set = new ConjuntoTablaHash<>();

        assertTrue(set.add(new String("value")));
        assertFalse(set.add(new String("value")));
        assertEquals(1, set.size());
        assertFalse(set.isEmpty());
        assertTrue(set.contains(new String("value")));
    }

    @Test
    void removeOnlyChangesSetWhenElementExists() {
        ConjuntoTablaHash<String> set = new ConjuntoTablaHash<>();
        set.add("value");

        assertFalse(set.remove("missing"));
        assertEquals(1, set.size());
        assertTrue(set.remove(new String("value")));
        assertFalse(set.remove("value"));
        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
        assertTrue(set.add("value"));
    }

    @Test
    void supportsNullElement() {
        ConjuntoTablaHash<String> set = new ConjuntoTablaHash<>();

        assertTrue(set.add(null));
        assertFalse(set.add(null));
        assertTrue(set.contains(null));
        assertEquals(1, set.size());
        assertTrue(set.remove(null));
        assertFalse(set.contains(null));
        assertEquals(0, set.size());
    }

    @Test
    void storesDistinctElementsWithTheSameHashCode() {
        ConjuntoTablaHash<String> set = new ConjuntoTablaHash<>(2);
        Set<String> collisions = Set.of("Aa", "BB", "AaAa", "BBBB", "BBAa", "AaBB");

        for (String element : collisions) {
            assertTrue(set.add(element));
        }

        assertEquals(collisions.size(), set.size());
        for (String element : collisions) {
            assertTrue(set.contains(element));
        }
        assertEquals(collisions, setContents(set.elements()));

        for (String element : collisions) {
            assertTrue(set.remove(element));
        }
        assertTrue(set.isEmpty());
    }

    @Test
    void resizesAndRetainsSetContents() {
        ConjuntoTablaHash<Integer> set = new ConjuntoTablaHash<>(2);
        Set<Integer> expected = new HashSet<>();

        for (int i = 0; i < 1_000; i++) {
            assertEquals(expected.add(i), set.add(i));
        }
        assertMatches(expected, set);

        for (int i = 0; i < 1_000; i += 2) {
            assertEquals(expected.remove(i), set.remove(i));
        }
        assertMatches(expected, set);

        for (int i = 1_000; i < 1_500; i++) {
            assertEquals(expected.add(i), set.add(i));
        }
        assertMatches(expected, set);
    }

    @Test
    void iteratorTraversesEveryElementOnceAndObeysIteratorContract() {
        ConjuntoTablaHash<Integer> set = new ConjuntoTablaHash<>(2);
        Set<Integer> expected = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            set.add(i);
            expected.add(i);
        }

        Iterator<Integer> iterator = set.elements().iterator();
        Set<Integer> actual = new HashSet<>();
        while (iterator.hasNext()) {
            assertTrue(actual.add(iterator.next()));
        }
        assertEquals(expected, actual);
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
        assertThrows(UnsupportedOperationException.class, () -> set.elements().iterator().remove());
    }

    @Test
    void iterableCanCreateIndependentIteratorsAndBeTraversedRepeatedly() {
        ConjuntoTablaHash<Integer> set = new ConjuntoTablaHash<>(2);
        for (int i = 0; i < 20; i++) {
            set.add(i);
        }
        Iterable<Integer> elements = set.elements();
        Set<Integer> expected = new HashSet<>(setContents(elements));

        assertEquals(expected, setContents(elements));
        Iterator<Integer> first = elements.iterator();
        Iterator<Integer> second = elements.iterator();
        assertEquals(first.next(), second.next());
        assertEquals(expected, setContents(elements));
    }

    @Test
    void matchesHashSetAcrossDeterministicOperationSequence() {
        ConjuntoTablaHash<Integer> actual = new ConjuntoTablaHash<>(3);
        Set<Integer> expected = new HashSet<>();
        Random random = new Random(0xEDA);

        for (int i = 0; i < 5_000; i++) {
            Integer element = random.nextInt(25) == 0 ? null : random.nextInt(250);
            switch (random.nextInt(4)) {
                case 0 -> assertEquals(expected.add(element), actual.add(element));
                case 1 -> assertEquals(expected.remove(element), actual.remove(element));
                case 2 -> assertEquals(expected.contains(element), actual.contains(element));
                case 3 -> assertMatches(expected, actual);
                default -> fail("Unexpected operation");
            }
            assertMatches(expected, actual);
        }
    }

    private static <T> void assertMatches(Set<T> expected, ConjuntoTablaHash<T> actual) {
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.isEmpty(), actual.isEmpty());
        Set<T> actualElements = new HashSet<>();
        for (T element : actual.elements()) {
            assertTrue(actualElements.add(element), "Set iteration must not yield duplicates");
        }
        assertEquals(expected, actualElements);
        for (T element : expected) {
            assertTrue(actual.contains(element));
        }
    }

    private static <T> Set<T> setContents(Iterable<T> iterable) {
        Set<T> contents = new HashSet<>();
        for (T element : iterable) {
            contents.add(element);
        }
        return contents;
    }

}
