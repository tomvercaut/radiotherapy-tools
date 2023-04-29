package tv.radiotherapy.tools.dicom.xml.model.registry;

import tv.radiotherapy.tools.dicom.xml.model.OLink;
import tv.radiotherapy.tools.dicom.xml.model.UidType;

public record UidItem(
        String uid,
        String name,
        String keyword,
        UidType type,
        OLink link
) {
}
