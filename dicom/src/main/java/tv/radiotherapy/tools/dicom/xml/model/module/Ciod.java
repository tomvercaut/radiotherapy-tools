package tv.radiotherapy.tools.dicom.xml.model.module;

import java.util.List;

public record Ciod(String id, String name, List<CiodItem> items) {
}
