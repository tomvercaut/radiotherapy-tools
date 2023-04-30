package tv.radiotherapy.tools.dicom.xml.model.module;

import tv.radiotherapy.tools.dicom.xml.model.module.ciod.IodModule;

import java.util.List;

/**
 * Composite Information Object Definition
 *
 * @param ie      information entity
 * @param modules IOD modules
 */
public record CiodItem(String ie, List<IodModule> modules) {
    /**
     * Check if this instance has a information entity.
     *
     * @return True if this instance has a information entity.
     */
    public boolean hasIe() {
        return !ie.isBlank();
    }
}
