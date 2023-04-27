package tv.radiotherapy.tools.dicom.xml.model;

/**
 * Different types of unique identifiers (UIDs).
 * <p>
 * These UIDs are described in the DICOM standard part 06:
 * <ul>
 *     <li>table A-1. UID Values</li>
 * </ul>
 */
public enum UidType {
    None,
    TransferSyntax,
    SOPClass,
    SOPInstance,
    CodingScheme,
    ApplicationContextName,
    MetaSOPClass,
    ServiceClass,
    ApplicationHostingModel,
    MappingResource,
    LdapOid,
    SynchronizationFrameOfReference,
}
