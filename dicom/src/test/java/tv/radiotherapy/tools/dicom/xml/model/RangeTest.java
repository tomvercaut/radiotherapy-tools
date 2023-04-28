package tv.radiotherapy.tools.dicom.xml.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RangeTest {
    @Test
    void contains() {
        var range = Range.create(0x1234);
        assertTrue(range.contains(0x1234));
        range = Range.create("12xx");
        assertTrue(range.contains(0x1200));
        assertTrue(range.contains(0x1201));
        assertTrue(range.contains(0x12FF));
    }

    @Test
    void doesNotContain() {
        var range = Range.create(0x1234);
        assertFalse(range.contains(0x1233));
        assertFalse(range.contains(0x1235));
        range = Range.create("12xx");
        assertFalse(range.contains(0x11FF));
        assertFalse(range.contains(0x1301));
    }
}