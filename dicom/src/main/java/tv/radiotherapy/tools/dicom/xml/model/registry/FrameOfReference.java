package tv.radiotherapy.tools.dicom.xml.model.registry;

/**
 * Unique identifier of a frame of reference.
 *
 * @param uid     unique identifier
 * @param name    name of the frame of reference
 * @param keyword keyword of the frame of reference
 */
public record FrameOfReference(
        String uid,
        String name,
        String keyword
) {
}
