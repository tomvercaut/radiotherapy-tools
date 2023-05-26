package tv.radiotherapy.tools.dicom.xml.model;

import org.junit.jupiter.api.Test;
import tv.radiotherapy.tools.dicom.xml.model.module.Tag;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RangedTagTest {

    @Test
    void createFromString() {
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
    void contains() {
        var rt = RangedTag.create(0x1234, 0x5678);
        assertTrue(rt.contains(0x1234, 0x5678));
        rt = RangedTag.create("(12xx,5678)");
        assertTrue(rt.contains(0x1200, 0x5678));
        assertTrue(rt.contains(0x12FF, 0x5678));
        rt = RangedTag.create("(1234,56xx)");
        assertTrue(rt.contains(0x1234, 0x5600));
        assertTrue(rt.contains(0x1234, 0x56FF));
    }

    @Test
    void doesNotContain() {
        var rt = RangedTag.create(0x1234, 0x5678);
        assertFalse(rt.contains(0x1233, 0x5678));
        assertFalse(rt.contains(0x1234, 0x5677));

        rt = RangedTag.create("(12xx,5678)");
        assertFalse(rt.contains(0x1100, 0x5678));
        assertFalse(rt.contains(0x13FF, 0x5678));
        rt = RangedTag.create("(1234,56xx)");
        assertFalse(rt.contains(0x1234, 0x55FF));
        assertFalse(rt.contains(0x1234, 0x5700));
    }

    @Test
    void givenNonRangedTag_whenToTag_thenReturnValidTag() {
        var rt = RangedTag.create(0x1234, 0x5678);
        var ot = rt.toTag();
        assertTrue(ot.isPresent());
        var tag = ot.get();
        assertEquals(new Tag(0x1234, 0x5678), tag);
    }

    @Test
    void givenRange_whenToTag_thenOptionalTagIsEmpty() {
        var rt = RangedTag.create("(1234,56xx)");
        var ot = rt.toTag();
        assertTrue(ot.isEmpty());
    }
}