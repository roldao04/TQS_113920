package tqs.euromillions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.Iterator;

class CuponEuromillionsTest {
    private CuponEuromillions coupon;
    private Dip dip1, dip2;

    @BeforeEach
    public void setUp() {
        coupon = new CuponEuromillions();
        dip1 = new Dip(new int[]{10, 20, 30, 40, 50}, new int[]{1, 2});
        dip2 = new Dip(new int[]{5, 15, 25, 35, 45}, new int[]{3, 4});
    }

    @AfterEach
    public void tearDown() {
        coupon = null;
        dip1 = dip2 = null;
    }

    @Test
    public void testAppendDip() {
        coupon.appendDip(dip1);
        coupon.appendDip(dip2);
        assertEquals(2, coupon.countDips());
    }

    @Test
    public void testGetDipByIndex() {
        coupon.appendDip(dip1);
        coupon.appendDip(dip2);
        assertEquals(dip1, coupon.getDipByIndex(0));
        assertEquals(dip2, coupon.getDipByIndex(1));
    }

    @Test
    public void testFormat() {
        coupon.appendDip(dip1);
        String formatted = coupon.format();
        assertTrue(formatted.contains("Dip #1"));
        assertTrue(formatted.contains("N[ 10 20 30 40 50] S[  1  2]"));
    }

    @Test
    public void testIterator() {
        coupon.appendDip(dip1);
        Iterator<Dip> iterator = coupon.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(dip1, iterator.next());
    }

    @Test
    public void testCountDips() {
        assertEquals(0, coupon.countDips());
        coupon.appendDip(dip1);
        assertEquals(1, coupon.countDips());
    }
}
