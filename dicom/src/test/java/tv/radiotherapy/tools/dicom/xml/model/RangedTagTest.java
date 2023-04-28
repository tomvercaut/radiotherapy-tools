package tv.radiotherapy.tools.dicom.xml.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RangedTagTest {

    @Test
    void set() {
        var exp = List.of("(1234,5678)", "(12xx,5678)", "(1234,56xx)");
        var groupMins = List.of(
                0x1234,
                0x1200,
                0x1234
        );
        var groupMaxs = List.of(
                0x1234,
                0x12FF,
                0x1234
        );
        var elementsMins = List.of(
                0x5678,
                0x5678,
                0x5600
        );
        var elementsMaxs = List.of(
                0x5678,
                0x5678,
                0x56FF
        );
        var ranges = List.of(false, true, true);
        int n = exp.size();
        assertEquals(n, groupMins.size());
        assertEquals(n, groupMaxs.size());
        assertEquals(n, ranges.size());
        for (int i = 0; i < n; i++) {
            var rt = RangedTag.create(exp.get(i));
            assertEquals(groupMins.get(i), rt.group().min());
            assertEquals(groupMaxs.get(i), rt.group().max());
            assertEquals(elementsMins.get(i), rt.element().min());
            assertEquals(elementsMaxs.get(i), rt.element().max());
            assertEquals(ranges.get(i), rt.isRanged());
        }
    }

    @Test
    void getGroupRanged() {
        var rt = RangedTag.create("(12xx,5678)");
        var rng = Range.create("0x12xx");
        assertEquals(rng, rt.group());
    }

    @Test
    void getElement() {
        var rt = RangedTag.create(0x1234, 0x5678);
        var rng = Range.create(0x5678);
        assertEquals(rng, rt.element());
    }

    @Test
    void getElementRanged() {
        var rt = RangedTag.create("(1234,56xx)");
        var rng = Range.create("0x56xx");
        assertEquals(rng, rt.element());
    }

    @Test
    void isWithin() {
        var rt = RangedTag.create(0x1234, 0x5678);
        assertTrue(rt.isWithin(0x1234,0x5678));
        rt = RangedTag.create("(12xx,5678)");
        assertTrue(rt.isWithin(0x1200, 0x5678));
        assertTrue(rt.isWithin(0x12FF, 0x5678));
        rt = RangedTag.create("(1234,56xx)");
        assertTrue(rt.isWithin(0x1234,0x5600));
        assertTrue(rt.isWithin(0x1234,0x56FF));
    }
    @Test
    void isNotWithin() {
        var rt = RangedTag.create(0x1234, 0x5678);
        assertFalse(rt.isWithin(0x1233,0x5678));
        assertFalse(rt.isWithin(0x1234,0x5677));

        rt = RangedTag.create("(12xx,5678)");
        assertFalse(rt.isWithin(0x1100, 0x5678));
        assertFalse(rt.isWithin(0x13FF, 0x5678));
        rt = RangedTag.create("(1234,56xx)");
        assertFalse(rt.isWithin(0x1234,0x55FF));
        assertFalse(rt.isWithin(0x1234,0x5700));
    }

    @Test
    void rangeIsWithin() {
        var range = Range.create(0x1234);
        assertTrue(range.isWithin(0x1234));
        range = Range.create("12xx");
        assertTrue(range.isWithin(0x1200));
        assertTrue(range.isWithin(0x1201));
        assertTrue(range.isWithin(0x12FF));
    }
    @Test
    void rangeIsNotWithin() {
        var range = Range.create(0x1234);
        assertFalse(range.isWithin(0x1233));
        assertFalse(range.isWithin(0x1235));
        range = Range.create("12xx");
        assertFalse(range.isWithin(0x11FF));
        assertFalse(range.isWithin(0x1301));
    }
}