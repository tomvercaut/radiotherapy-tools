package tv.radiotherapy.tools.dicom.xml.model.module;

import tv.radiotherapy.tools.dicom.xml.model.module.attribute.Item;

import java.util.List;

/**
 * Model for an attribute / macro table row containing.
 * <p>
 * Since each row in the table can store different types of data.
 * <p>
 * The data types share a common interface {@link Item}:
 * <uL>
 * <li>{@link tv.radiotherapy.tools.dicom.xml.model.module.attribute.NameTagTypeDescItem}</li>
 * <li>{@link tv.radiotherapy.tools.dicom.xml.model.module.attribute.NameTagDescItem}</li>
 * <li>{@link tv.radiotherapy.tools.dicom.xml.model.module.attribute.Include}</li>
 * <li>{@link tv.radiotherapy.tools.dicom.xml.model.module.attribute.Description}</li>
 * </uL>
 *
 * @param id    XML id of the table from which the data was generated
 * @param name  name of the module attributes
 * @param items list of items (representing the rows) defining each ModuleAttribute item
 */
public record ModuleAttributes(String id, String name, List<Item> items) {
}
