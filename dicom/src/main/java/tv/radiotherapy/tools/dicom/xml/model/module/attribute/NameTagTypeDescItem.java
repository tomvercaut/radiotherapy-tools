package tv.radiotherapy.tools.dicom.xml.model.module.attribute;

import org.jetbrains.annotations.NotNull;
import tv.radiotherapy.tools.dicom.xml.model.RangedTag;
import tv.radiotherapy.tools.dicom.xml.model.module.AttributeType;

/**
 * Model for an attribute / macro row table containing 4 columns.
 *
 * <uL>
 * <li>The first column contains the name of the entry.</li>
 * <li>The second column contains the DICOM tag (format: (group, element)).</li>
 * <li>The third column stores the type of the attribute (1, 2, ...).</li>
 * <li>The fourth column contains a description of the attribute.</li>
 * </uL>
 *
 * @param depth relative depth of an attribute or macro item in a sequence [0 if not part of a sequence]
 * @param name  name of the attribute or macro
 * @param rtag  ranged DICOM tag of attribute or macro
 * @param type  indication of the type of attribute or macro, indicating a level or requirement of this item.
 * @param desc  description of the attribute or macro
 */
public record NameTagTypeDescItem(int depth, @NotNull String name, @NotNull RangedTag rtag, AttributeType type,
                                  String desc) implements Item {
}
