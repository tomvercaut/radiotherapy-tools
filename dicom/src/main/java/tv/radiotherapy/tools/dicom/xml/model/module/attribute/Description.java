package tv.radiotherapy.tools.dicom.xml.model.module.attribute;

import org.jetbrains.annotations.NotNull;

/**
 * Model for an attribute / macro row table containing 1 column.
 * <p>
 * The column contains a general description but nothing that
 * can easily be interpreted.
 *
 * @param desc description of the attribute or macro
 */
public record Description(@NotNull String desc) implements Item {
}
