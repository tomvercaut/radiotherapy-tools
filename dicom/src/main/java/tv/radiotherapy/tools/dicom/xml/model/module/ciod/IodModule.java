package tv.radiotherapy.tools.dicom.xml.model.module.ciod;

import tv.radiotherapy.tools.dicom.xml.XRef;
import tv.radiotherapy.tools.dicom.xml.model.Usage;

/**
 * Information Object Definition module
 *
 * @param module name of the IOD module
 * @param xRef   cross-reference to another part of the XML document
 * @param usage  module usage requirements
 */
public record IodModule(String module, XRef xRef, Usage usage) {
}
