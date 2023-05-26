package tv.radiotherapy.tools.dicom.xml.model.module.attribute;

import org.jetbrains.annotations.NotNull;
import tv.radiotherapy.tools.dicom.xml.model.RangedTag;

/**
 * Model for an attribute / macro row table containing 3 columns.
 *
 * <uL>
 * <li>The first column contains the name of the entry.</li>
 * <li>The second column contains the DICOM tag (format: (group, element)).</li>
 * <li>The third column contains a description of the attribute.</li>
 * </uL>
 *
 * @param depth relative depth of an attribute or macro item in a sequence [0 if not part of a sequence]
 * @param name  name of the attribute or macro
 * @param rtag  ranged DICOM tag of attribute or macro
 * @param desc  description of the attribute or macro
 */
public record NameTagDescItem(int depth, @NotNull String name, @NotNull RangedTag rtag, String desc) implements Item {
}
