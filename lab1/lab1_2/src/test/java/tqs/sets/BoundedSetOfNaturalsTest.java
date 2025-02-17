package tqs.sets;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;

import org.junit.jupiter.api.*;

class BoundedSetOfNaturalsTest {
    private BoundedSetOfNaturals setA;
    private BoundedSetOfNaturals setB;
    private BoundedSetOfNaturals emptySet;

    @BeforeEach
    public void setUp() {
        setA = new BoundedSetOfNaturals(1);
        setB = BoundedSetOfNaturals.fromArray(new int[]{10, 20, 30, 40, 50, 60});
        emptySet = new BoundedSetOfNaturals(5);

    }

    @AfterEach
    public void tearDown() {
        setA = setB = emptySet = null;
    }

    @Test
    public void testInvalidConstructorSize() {
        assertThrows(IllegalArgumentException.class, () -> new BoundedSetOfNaturals(0));
        assertThrows(IllegalArgumentException.class, () -> new BoundedSetOfNaturals(-5));
    }

    @Test
    public void testAddElement() {
        setA.add(99);
        assertTrue(setA.contains(99), "add: added element not found in set.");
        assertEquals(1, setA.size());
    }

    @Test
    public void testAddDuplicate() {
        assertThrows(IllegalArgumentException.class, () -> setB.add(10));
    }

    @Test
    public void testAddFromBadArray() {
        int[] elems = new int[]{10, -20, -30};

        // must fail with exception
        assertThrows(IllegalArgumentException.class, () -> setA.add(elems));
    }

    @Test
    public void testSetSizeLimit() {
        setA.add(42);
        assertThrows(IllegalArgumentException.class, () -> setA.add(50), "bounded set is full. no more elements allowed.");
    }

    @Test
    public void testCreateFromArray() {
        BoundedSetOfNaturals setC = BoundedSetOfNaturals.fromArray(new int[]{1, 2, 3});
        assertEquals(3, setC.size());
        assertTrue(setC.contains(1));
        assertTrue(setC.contains(2));
        assertTrue(setC.contains(3));
    }

    @Test
    public void testIntersects() {
        BoundedSetOfNaturals setC = BoundedSetOfNaturals.fromArray(new int[]{20, 30, 70});
        assertTrue(setB.intersects(setC), "Expected intersection between sets.");
        
        BoundedSetOfNaturals setD = BoundedSetOfNaturals.fromArray(new int[]{100, 200});
        assertFalse(setB.intersects(setD), "Expected no intersection between sets.");
    }

    @Test
    public void testSelfIntersection() {
        assertTrue(setB.intersects(setB));
    }

    @Test
    public void testEmptySetIntersection() {
        BoundedSetOfNaturals newSet = new BoundedSetOfNaturals(3);
        assertFalse(setB.intersects(newSet));
    }

    @Test
    public void testIllegalNaturalNumbers() {
        assertThrows(IllegalArgumentException.class, () -> setA.add(0), "Expected exception for zero.");
        assertThrows(IllegalArgumentException.class, () -> setA.add(-5), "Expected exception for negative number.");
    }

    @Test
    public void testContains() {
        assertTrue(setB.contains(10), "Expected set to contain 10.");
        assertFalse(setB.contains(99), "Expected set not to contain 99.");
    }

    @Test
    public void testSize() {
        assertEquals(6, setB.size(), "Expected size mismatch.");
    }

    @Test
    public void testEquals() {
        BoundedSetOfNaturals setC = BoundedSetOfNaturals.fromArray(new int[]{10, 20, 30, 40, 50, 60});
        assertEquals(setB, setC, "Expected sets to be equal.");

        BoundedSetOfNaturals setD = BoundedSetOfNaturals.fromArray(new int[]{1, 2, 3});
        assertNotEquals(setB, setD, "Expected sets to be different.");
    }

    @Test
    public void testEqualsWithDifferentObject() {
        assertNotEquals(setB, "Not a BoundedSetOfNaturals");
    }

    @Test
    public void testHashCode() {
        BoundedSetOfNaturals setC = BoundedSetOfNaturals.fromArray(new int[]{10, 20, 30, 40, 50, 60});
        assertEquals(setB.hashCode(), setC.hashCode(), "Expected same hash codes for equal sets.");
    }

    @Test
    public void testIterator() {
        Iterator<Integer> iterator = setB.iterator();
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
    }

    @Test
    public void testEmptyIterator() {
        Iterator<Integer> iterator = emptySet.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testStream() {
        assertEquals(6, setB.stream().count());
    }

    @Test
    public void testEmptyStream() {
        assertEquals(0, emptySet.stream().count());
    }
}
