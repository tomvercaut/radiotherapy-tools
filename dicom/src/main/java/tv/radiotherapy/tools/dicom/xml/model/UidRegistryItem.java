package tv.radiotherapy.tools.dicom.xml.model;

public record UidRegistryItem(
        String uid,
        String name,
        String keyword,
        UidType type,
        OLink link
) {
}
