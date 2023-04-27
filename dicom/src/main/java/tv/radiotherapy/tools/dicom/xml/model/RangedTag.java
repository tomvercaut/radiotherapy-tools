package tv.radiotherapy.tools.dicom.xml.model;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A ranged tag is as the name implies a DICOM tag that can exist out of more than 1 group or element value.
 * <p>
 * Examples of this can be found in the registry of DICOM data elements in part 06 of the DICOM standard [ex. (60xx,4000)].
 *
 * @see <a href="https://dicom.nema.org/medical/dicom/current/output/html/part06.html#chapter_6">Registry of DICOM Data Elements</a>
 */
@Getter
public class RangedTag {
    // Group range
    private final Range group = new Range();
    // Element range
    private final Range element = new Range();

    /**
     * Creates a new instance of a ranged DICOM tag.
     * <p>
     * Initialises the group and element ranges to its default value.
     */
    public RangedTag() {
        clear();
    }

    /**
     * Remove any data stored within the group and element range and reinitialise the data to its default value.
     */
    public void clear() {
        group.clear();
        element.clear();
    }

    /**
     * Set a non ranged DICOM tag value.
     *
     * @param g DICOM tag group
     * @param e DICOM tag element
     * @throws IllegalArgumentException Thrown when the group or element value is out of range. Clears the internal state of the ranges.
     */
    public void set(int g, int e) throws IllegalArgumentException {
        try {
            group.set(g);
        } catch (IllegalArgumentException ex) {
            element.clear();
            throw ex;
        }
        try {
            element.set(e);
        } catch (IllegalArgumentException ex) {
            group.clear();
            throw ex;
        }
    }

    /**
     * Set the value of the ranged DICOM tag from a String.
     *
     * @param s Accepted input values are:
     *          <ul>
     *              <li>(gggg,eeee)</li>
     *              <li>(0xgggg,eeee)</li>
     *              <li>(gggg,0xeeee)</li>
     *              <li>(0xgggg,0xeeee)</li>
     *          </ul>
     *          where gggg and eeee are hexadecimal values representing the group and element of a DICOM tag (range).
     * @throws IllegalArgumentException Thrown when the input format is not valid or when the group or element ranges can't be set. @see {@link Range#set(String)}
     */
    public void set(@NotNull String s) throws IllegalArgumentException {
        if (!s.startsWith("(") || !s.endsWith(")")) {
            throw new IllegalArgumentException("Expected a (Ranged) DICOM tag value in the format: (gggg,eeee), (0xgggg,eeee), (gggg,0xeeee) or (0xgggg,0xeeee)");
        }
        int comma = s.indexOf(",");
        if (comma < 0) {
            throw new IllegalArgumentException("Expected a (Ranged) DICOM tag value in the format: (gggg,eeee), (0xgggg,eeee), (gggg,0xeeee) or (0xgggg,0xeeee)");
        }
        try {
            group.set(s.substring(1, comma));
        } catch (NumberFormatException nfe) {
            element.clear();
            throw nfe;
        }

        try {
            element.set(s.substring(comma + 1, s.length() - 1));
        } catch (NumberFormatException nfe) {
            group.clear();
            throw nfe;
        }

    }

    /**
     * Test if the instance is a range or not.
     *
     * @return True if the group or element is a range (@see {@link Range#isRanged()}).
     */
    public boolean isRanged() {
        return group.isRanged() || element.isRanged();
    }

    /**
     * Get the DICOM RangedTag group.
     *
     * @return DICOM RangedTag group
     */
    public Range getGroup() {
        return group;
    }

    /**
     * Get the DICOM RangedTag element.
     *
     * @return DICOM RangedTag element
     */
    public Range getElement() {
        return element;
    }

    /**
     * Check if a group and element are within range of this instance.
     *
     * @param group   DICOM tag group
     * @param element DICOM tag element
     * @return True if the group value is within the group range and the element value is within the element range.
     */
    public boolean isWithin(int group, int element) {
        return this.group.isWithin(group) && this.element.isWithin(element);
    }

    @Override
    public String toString() {
        return '(' + group.toString() + "," + element + ')';
    }

    /**
     * DICOM group or element range as used in the <a href="https://dicom.nema.org/medical/dicom/current/output/html/part06.html#chapter_6">Registry of DICOM Data Elements</a>.
     * <p>
     * An instance of the Range can be created either by integer or by passing a String into the {@link Range#set(String)}.
     * <p>
     * It's also possible to test if a group or element value is within this Range by using {@link Range#isWithin(int)}.
     */
    public static class Range {
        // String representation of either the DICOM tag range '12xx' or a non-ranged value '1234'.
        private String value = "";
        // minimum value of the range
        private int min = 0;
        // maximum value of the range
        private int max = 0;

        Range() {
            clear();
        }

        Range(int value) {
            clear();
            set(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Range range = (Range) o;
            return min == range.min && max == range.max && Objects.equals(value, range.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, min, max);
        }

        /**
         * Clears the internal data to its default values.
         */
        public void clear() {
            value = "";
            min = 0;
            max = 0;
        }

        /**
         * Set the value of the minimum and maximum value based on an integer.
         *
         * @param value non-range integer value
         * @throws IllegalArgumentException Thrown when the hexadecimal integer is outside the expected range [0 &le; value &le; 0xFFFF].
         */
        public void set(int value) throws IllegalArgumentException {
            if (value < 0 || value > 0xFFFF) {
                throw new IllegalArgumentException("Input value must be strict positive and smaller or equal to 0xFFFF.");
            }
            min = value;
            max = value;
            this.value = String.format("%04x", value);
        }

        /**
         * Set the internal value of the range based on a String.
         *
         * @param s Accepted input values are:
         *          <ul>
         *              <li>dddd: 4 characters representing a hexadecimal value.</li>
         *              <li>0xdddd: 6 characters representing a hexadecimal value.</li>
         *          </ul>
         *          If the last 4 characters contain an 'x', this is interpreted as a range where 'x' varies between '0' and 'F'.
         * @throws IllegalArgumentException Thrown when the input string has an invalid format or when the resulting hexadecimal integer is outside the expected range [0 &le; value &le; 0xFFFF].
         * @throws NumberFormatException    Thrown when the String can't be parsed to a hexadecimal integer.
         */
        public void set(@NotNull String s) throws IllegalArgumentException, NumberFormatException {
            String t;
            if (s.length() == 4) {
                t = s;
            } else if (s.length() == 6 && s.toLowerCase().startsWith("0x")) {
                t = s.toLowerCase().substring(2);
            } else {
                throw new IllegalArgumentException("Expected a (Ranged) DICOM tag group or element value in the format: dddd or 0xdddd");
            }
            try {
                if (t.contains("x")) {
                    var mi = t.replace("x", "0");
                    min = Integer.parseInt(mi, 16);
                    if (min < 0 || min > 0xFFFF) {
                        clear();
                        throw new IllegalArgumentException("Input value must be strict positive and smaller or equal to 0xFFFF.");
                    }
                    var ma = t.replace("x", "f");
                    max = Integer.parseInt(ma, 16);
                    if (max < 0 || max > 0xFFFF) {
                        clear();
                        throw new IllegalArgumentException("Input value must be strict positive and smaller or equal to 0xFFFF.");
                    }
                } else {
                    min = Integer.parseInt(t, 16);
                    if (min < 0 || min > 0xFFFF) {
                        clear();
                        throw new IllegalArgumentException("Input value must be strict positive and smaller or equal to 0xFFFF.");
                    }
                    max = min;
                }
                value = t;
            } catch (NumberFormatException nfe) {
                clear();
                throw nfe;
            }
        }

        /**
         * Test if the minimum and maxmimum value differs (indicating a range).
         *
         * @return True if the minimum and maximum value differ.
         */
        public boolean isRanged() {
            return min != max;
        }

        /**
         * Get the minimum value of the range.
         *
         * @return Minimum value of the range.
         */
        public int getMin() {
            return min;
        }

        /**
         * Get the maximum value of the range.
         *
         * @return Maximum value of the range.
         */
        public int getMax() {
            return max;
        }

        @Override
        public String toString() {
            return value;
        }

        /**
         * If this instance is:
         * <ul>
         *     <li><b>is not a range</b>: test if the value is equal to the minimum value (which should be equal to the internally stored maxinum)</li>
         *     <li><b>is a range</b>: test if the value is larger or equal to the minimum and smaller or equal to the maximum.</li>
         * </ul>
         *
         * @param value group or element value of a DICOM tag
         * @return True if the group or element value provided is within this range.
         */
        public boolean isWithin(int value) {
            return (!isRanged()) ? min == value : (min <= value && value <= max);
        }
    }


}
