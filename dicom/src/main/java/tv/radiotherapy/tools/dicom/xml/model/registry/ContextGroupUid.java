package tv.radiotherapy.tools.dicom.xml.model.registry;

import tv.radiotherapy.tools.dicom.xml.model.OLink;

/**
 * Context group UID values
 *
 * @param uid               context UID
 * @param contextIdentifier context identifier
 * @param contextGroupName  context group name
 * @param comment           comment
 */
public record ContextGroupUid(String uid, OLink contextIdentifier, OLink contextGroupName, String comment) {
}
