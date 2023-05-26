package tv.radiotherapy.tools.dicom.xml.model.module.attribute;

import org.jetbrains.annotations.NotNull;
import tv.radiotherapy.tools.dicom.xml.model.module.AttributeType;

/**
 * Represents any UID attribute type.
 *
 * @param depth relative depth of an attribute or macro item in a sequence [0 if not part of a sequence]
 * @param type  indication of the type of attribute or macro, indicating a level or requirement of this item.
 * @param desc  description of what is being included
 */
public record AnyUid(int depth, @NotNull AttributeType type, @NotNull String desc) implements Item {
}
