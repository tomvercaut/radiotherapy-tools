package tv.radiotherapy.tools.dicom.xml.model;

import org.jetbrains.annotations.NotNull;

/**
 * A ranged DICOM tag consists of a group and element Range.
 * <p>
 * Ranged DICOM tags are used within the registry of DICOM data elements.
 *
 * @param group
 * @param element
 * @see <a href="https://dicom.nema.org/medical/dicom/current/output/html/part06.html#chapter_6">Registry of DICOM Data Elements</a>
 */
public record RangedTag(
        // Group range
        Range group,
        // Element range
        Range element) {

    /**
     * Create a DICOM tag from a (non-ranged) group and element.
     *
     * @param group   DICOM tag group
     * @param element DICOM element group
     * @return A non-ranged DICOM tag.
     */
    public static RangedTag create(int group, int element) {
        return new RangedTag(Range.create(group), Range.create(element));
    }

    /**
     * Create a RangedTag from a String with a well-defined format.
     *
     * @param s Accepted input values are:
     *          <ul>
     *              <li>(gggg,eeee)</li>
     *              <li>(0xgggg,eeee)</li>
     *              <li>(gggg,0xeeee)</li>
     *              <li>(0xgggg,0xeeee)</li>
     *          </ul>
     *          where gggg and eeee are hexadecimal values representing the group and element of a DICOM tag (range).
     * @throws IllegalArgumentException Thrown when the input format is not valid or when the group or element ranges can't be set. @see {@link Range#create(String)}
     */
    public static RangedTag create(@NotNull String s) throws IllegalArgumentException {
        if (!s.startsWith("(") || !s.endsWith(")")) {
            throw new IllegalArgumentException("Expected a (Ranged) DICOM tag value in the format: (gggg,eeee), (0xgggg,eeee), (gggg,0xeeee) or (0xgggg,0xeeee)");
        }
        int comma = s.indexOf(",");
        if (comma < 0) {
            throw new IllegalArgumentException("Expected a (Ranged) DICOM tag value in the format: (gggg,eeee), (0xgggg,eeee), (gggg,0xeeee) or (0xgggg,0xeeee)");
        }
        return new RangedTag(
                Range.create(s.substring(1, comma)),
                Range.create(s.substring(comma + 1, s.length() - 1))
        );
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
     * Check if a group and element are within range of this instance.
     *
     * @param group   DICOM tag group
     * @param element DICOM tag element
     * @return True if the group value is within the group range and the element value is within the element range.
     */
    public boolean contains(int group, int element) {
        return this.group.contains(group) && this.element.contains(element);
    }
}
