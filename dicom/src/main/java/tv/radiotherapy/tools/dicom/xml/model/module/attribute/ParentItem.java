package tv.radiotherapy.tools.dicom.xml.model.module.attribute;

import org.jetbrains.annotations.NotNull;
import tv.radiotherapy.tools.dicom.xml.model.module.AttributeType;

/**
 * A special item that represents the same type as the parent that is including the item.
 * <p>
 * This enables recursive inclusion of a DICOM attribute or macro.
 */
public record ParentItem(int depth, @NotNull String name, @NotNull AttributeType type,
                         @NotNull String desc) implements Item {
}
