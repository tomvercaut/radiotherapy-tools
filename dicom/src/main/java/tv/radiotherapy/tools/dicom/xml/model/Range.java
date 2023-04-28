package tv.radiotherapy.tools.dicom.xml.model;

import org.jetbrains.annotations.NotNull;

/**
 * A Range consists of a minimum and a maximum value over which it is defined. It finds it's use in DICOM tag group and element ranges. These are used as part of the <a href="https://dicom.nema.org/medical/dicom/current/output/html/part06.html#chapter_6">registry of DICOM Data Elements</a>.
 * <p>
 * The minimum and maximum values of a Range must be within 0 &le; value &le; 0xFFFF; inorder to be valid.
 *
 * @param min   minimum value of the range
 * @param max   maximum value of the range
 * @param value String representation of either the DICOM tag range '12xx' or a non-ranged DICOM tag '1234'.
 */
public record Range(
        int min,
        int max,
        String value
) {
    public Range(int min, int max, String value) {
        if (min < 0) {
            throw new IllegalArgumentException("The minimum value must be larger or equal to 0.");
        }
        if (min > max) {
            throw new IllegalArgumentException("The minimum value must be smaller or equal to the maximum value.");
        }
        if (max > 0xFFFF) {
            throw new IllegalArgumentException("The maximum value must be smaller or equal to 0xFFFF.");
        }

        this.min = min;
        this.max = max;
        this.value = value;
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
     * If this instance is:
     * <ul>
     *     <li><b>is not a range</b>: test if the value is equal to the minimum value (which should be equal to the internally stored maxinum)</li>
     *     <li><b>is a range</b>: test if the value is larger or equal to the minimum and smaller or equal to the maximum.</li>
     * </ul>
     *
     * @param value group or element value of a DICOM tag
     * @return True if the group or element value provided is within this range.
     */
    public boolean contains(int value) {
        return (!isRanged()) ? min == value : (min <= value && value <= max);
    }

    /**
     * Create a Range where the minimum and maximum value are determined by the input value.
     *
     * @param value non-range integer value
     * @return Range in which the min and max value is equal.
     * @throws IllegalArgumentException Thrown when the input hexadecimal integer representation is outside the expected range [0 &le; value &le; 0xFFFF].
     */
    public static Range create(int value) throws IllegalArgumentException {
        if (value < 0 || value > 0xFFFF) {
            throw new IllegalArgumentException("Input value must be strict positive and smaller or equal to 0xFFFF.");
        }
        return new Range(value, value, String.format("%04x", value));
    }

    /**
     * Create a Range from a String.
     *
     * @param s Accepted input values are:
     *          <ul>
     *              <li>dddd: 4 characters representing a hexadecimal value.</li>
     *              <li>0xdddd: 6 characters representing a hexadecimal value.</li>
     *          </ul>
     *          If the last 4 characters contain an 'x', this is interpreted as a range where 'x' varies between '0' and 'F'.
     * @return Range in which the min and max value are derived from the input String
     * @throws IllegalArgumentException Thrown when the input string has an invalid format or when the resulting hexadecimal integer is outside the expected range [0 &le; value &le; 0xFFFF].
     * @throws NumberFormatException    Thrown when the String can't be parsed to a hexadecimal integer.
     */
    public static Range create(@NotNull String s) throws IllegalArgumentException, NumberFormatException {
        String t;
        if (s.length() == 4) {
            t = s;
        } else if (s.length() == 6 && s.toLowerCase().startsWith("0x")) {
            t = s.toLowerCase().substring(2);
        } else {
            throw new IllegalArgumentException("Expected a (Ranged) DICOM tag group or element value in the format: dddd or 0xdddd");
        }
        if (t.contains("x")) {
            var mi = t.replace("x", "0");
            int min = Integer.parseInt(mi, 16);
            if (min < 0 || min > 0xFFFF) {
                throw new IllegalArgumentException("Input value must be strict positive and smaller or equal to 0xFFFF.");
            }
            var ma = t.replace("x", "f");
            int max = Integer.parseInt(ma, 16);
            if (max < 0 || max > 0xFFFF) {
                throw new IllegalArgumentException("Input value must be strict positive and smaller or equal to 0xFFFF.");
            }
            return new Range(min, max, t);
        } else {
            int min = Integer.parseInt(t, 16);
            if (min < 0 || min > 0xFFFF) {
                throw new IllegalArgumentException("Input value must be strict positive and smaller or equal to 0xFFFF.");
            }
            return new Range(min, min, t);
        }
    }
}
