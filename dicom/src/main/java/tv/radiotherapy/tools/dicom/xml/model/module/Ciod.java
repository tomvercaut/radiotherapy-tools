package tv.radiotherapy.tools.dicom.xml.model.module;

import java.util.List;

/**
 * Composite Information Object Definition
 * @param id XML table identifier
 * @param name name of the CIOD
 * @param items list of CIOD items
 */
public record Ciod(String id, String name, List<CiodItem> items) {
}
