package tv.radiotherapy.tools.dicom.xml.model.registry;

import tv.radiotherapy.tools.dicom.xml.model.OLink;
import tv.radiotherapy.tools.dicom.xml.model.UidType;

public record TemplateUid(String uid, OLink name, UidType type, OLink part) {
}
