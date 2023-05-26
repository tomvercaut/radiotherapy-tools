package tv.radiotherapy.tools.dicom.xml.model.module.attribute;

import org.jetbrains.annotations.NotNull;
import tv.radiotherapy.tools.dicom.xml.XRef;

/**
 * Model for an attribute / macro row table containing 2 columns.
 * <p>
 * The first column contains an XML reference to another section or table in the standard.
 * The second column stores a description.
 *
 * @param depth relative depth of an attribute or macro item in a sequence [0 if not part of a sequence]
 * @param xRef  references an XML node within the same XML document
 * @param desc  description of what is being included
 */
public record Include(int depth, @NotNull XRef xRef, @NotNull String desc) implements Item {
}
