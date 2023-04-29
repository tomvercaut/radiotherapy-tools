package tv.radiotherapy.tools.dicom.xml.model.registry;

import tv.radiotherapy.tools.dicom.xml.model.RangedTag;
import tv.radiotherapy.tools.dicom.xml.model.VR;

import java.util.List;

public record DataElement(
        RangedTag tag,
        String name,
        String keyword,
        VR vr,
        List<VR> ovr,
        String vm,
        String desc) {

    public boolean isRetired() {
        return desc.contains("RET");
    }
}
