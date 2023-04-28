package tv.radiotherapy.tools.dicom.xml.model;

import java.util.List;

public record DataElementRegistryItem(
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
