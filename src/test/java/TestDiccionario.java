import es.urjc.grafo.EDA.diccionarios.Diccionario;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestDiccionario {

    private static <T> List<T> asList(Iterable<T> it) {
        List<T> list = new ArrayList<>();
        for (T t : it) list.add(t);
        return list;
    }

    @Test
    void put_allowsDuplicates_and_sizeIncreases_and_getReturnsFirstInserted() {
        Diccionario<String, Integer> dic = new Diccionario<>();

        assertTrue(dic.isEmpty());
        assertEquals(0, dic.size());

        // Put several values for the same key
        Integer r1 = dic.put("a", 1);
        Integer r2 = dic.put("a", 2);
        Integer r3 = dic.put("b", 3);

        // Diccionario.put always returns null
        assertNull(r1);
        assertNull(r2);
        assertNull(r3);

        // Size counts every entry (including duplicates)
        assertEquals(3, dic.size());
        assertFalse(dic.isEmpty());

        // get retrieves the first inserted value for the key (not the last)
        assertEquals(1, dic.get("a"));
        assertEquals(3, dic.get("b"));

        // contains checks
        assertTrue(dic.containsKey("a"));
        assertTrue(dic.containsKey("b"));
        assertFalse(dic.containsKey("c"));
        assertTrue(dic.containsValue(1));
        assertTrue(dic.containsValue(2));
        assertTrue(dic.containsValue(3));
        assertFalse(dic.containsValue(99));

        // getAll returns all values for key in insertion order
        List<Integer> aValues = asList(dic.getAll("a"));
        assertEquals(Arrays.asList(1, 2), aValues);

        // keys() and values() include duplicates
        List<String> keys = asList(dic.keys());
        List<Integer> values = asList(dic.values());
        assertEquals(3, keys.size());
        assertEquals(3, values.size());
        // The multiset of values should contain the duplicates
        assertEquals(2, values.stream().filter(v -> v == 2 || v == 1).count());
    }

    @Test
    void remove_removesSingleOccurrence_inInsertionOrder_and_returnsRemovedValue() {
        Diccionario<String, Integer> dic = new Diccionario<>();
        dic.put("x", 10);
        dic.put("x", 20);
        dic.put("x", 30);
        assertEquals(3, dic.size());

        // remove removes the first occurrence (oldest) and returns its value
        assertEquals(10, dic.remove("x"));
        assertEquals(2, dic.size());
        assertTrue(dic.containsKey("x"));
        assertEquals(Arrays.asList(20, 30), asList(dic.getAll("x")));

        assertEquals(20, dic.remove("x"));
        assertEquals(1, dic.size());
        assertEquals(Collections.singletonList(30), asList(dic.getAll("x")));

        assertEquals(30, dic.remove("x"));
        assertEquals(0, dic.size());
        assertFalse(dic.containsKey("x"));
        assertNull(dic.get("x"));
        assertTrue(asList(dic.getAll("x")).isEmpty());

        // Removing again returns null
        assertNull(dic.remove("x"));
    }

    @Test
    void removeAll_removesEveryOccurrence_and_returnsAllValues_inInsertionOrder() {
        Diccionario<String, Integer> dic = new Diccionario<>();
        dic.put("k", 5);
        dic.put("other", 99);
        dic.put("k", 6);
        assertEquals(3, dic.size());

        List<Integer> removed = asList(dic.removeAll("k"));
        assertEquals(Arrays.asList(5, 6), removed);
        assertEquals(1, dic.size());
        assertFalse(dic.containsKey("k"));
        assertTrue(asList(dic.getAll("k")).isEmpty());
        assertEquals(99, dic.get("other"));
    }

    @Test
    void getAll_onAbsentKey_isEmptyIterable_and_isReusable() {
        Diccionario<Integer, String> dic = new Diccionario<>();
        dic.put(1, "a");
        assertTrue(asList(dic.getAll(2)).isEmpty());

        // Calling again stays empty
        Iterable<String> empty = dic.getAll(2);
        assertTrue(asList(empty).isEmpty());
    }

    @Test
    void iterator_visitsAllEntries_includingDuplicates_and_matchesSize() {
        Diccionario<Integer, String> dic = new Diccionario<>();
        dic.put(1, "A");
        dic.put(2, "B");
        dic.put(1, "A2");
        dic.put(3, "C");

        int count = 0;
        Set<String> seen = new HashSet<>();
        for (Map.Entry<Integer, String> e : dic) {
            count++;
            seen.add(e.getKey() + ":" + e.getValue());
        }

        assertEquals(dic.size(), count);
        // Ensure duplicates are present as separate entries
        assertTrue(seen.contains("1:A"));
        assertTrue(seen.contains("1:A2"));
        assertTrue(seen.contains("2:B"));
        assertTrue(seen.contains("3:C"));
    }
}
