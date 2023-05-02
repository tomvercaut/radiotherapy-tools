package tv.radiotherapy.tools.dicom.xml.model.module;

/**
 * DICOM tag consisting of a group and element value.
 *
 * @param group DICOM group
 * @param element DICOM element
 */
public record Tag(int group, int element) {
    public Tag {
        if (group < 0 || group > 0xFFFF) {
            throw new IllegalArgumentException("The tag group must be 0 <= group <= 0xFFFF");
        }
        if (element < 0 || element > 0xFFFF) {
            throw new IllegalArgumentException("The tag element must be 0 <= group <= 0xFFFF");
        }
    }
}
