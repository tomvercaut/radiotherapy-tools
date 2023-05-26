package tv.radiotherapy.tools.dicom.xml.parser.module;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tv.radiotherapy.tools.dicom.xml.model.module.ModuleAttributes;
import tv.radiotherapy.tools.dicom.xml.model.module.attribute.Item;
import tv.radiotherapy.tools.dicom.xml.parser.InnerText;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;
import tv.radiotherapy.tools.dicom.xml.parser.TableHelper;
import tv.radiotherapy.tools.dicom.xml.parser.TableParser;
import tv.radiotherapy.tools.dicom.xml.parser.module.attribute.AttributeTableRowParser;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;

public class ModuleAttributesTableParser implements TableParser<ModuleAttributes, AttributeTableRowParser> {
    private static final Logger logger = LogManager.getLogger(ModuleAttributesTableParser.class);
    private final XPath xPath = XPathFactory.newInstance().newXPath();

    @Override
    public ModuleAttributes parse(@NotNull Element table, @NotNull AttributeTableRowParser rowParser) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException {
        if (!TableHelper.isTable(table)) {
            throw new ParserException("Expected an XML table element while parsing a CIOD table.");
        }
        var id = table.getAttribute("xml:id");
        var captionElement = (Element) xPath.compile(".//caption").evaluate(table, XPathConstants.NODE);
        if (captionElement == null) {
            throw new ParserException("Unable to find caption in XML table.");
        }
        var caption = InnerText.get(captionElement);
        String name = "";
        if (caption.startsWith("Example ")) {
            throw new NoModuleAttributesTableException(String.format("XML table [%s] is not a Module Attribute or Macro Attribute table but an Example.", id));
        }
        var i = caption.lastIndexOf("Macro Attributes");
        if (i != -1) {
            name = caption.substring(0, i).trim();
        } else {
            i = caption.lastIndexOf("Module Attributes");
            if (i != -1) {
                name = caption.substring(0, i).trim();
            }
        }
        if (i == -1) {
            throw new NoModuleAttributesTableException(String.format("XML table [%s] is not a Module Attribute or Macro Attribute table.", id));
        }
        if (name.isBlank()) {
            name = caption;
        }
        final NodeList rows = TableHelper.getRows(table);
        final var nrows = rows.getLength();
        final var items = new ArrayList<Item>();
        for (int r = 0; r < nrows; r++) {
            try {
                var opt = rowParser.parseRow(rows.item(r));
                if (opt.isEmpty()) {
                    continue;
                }
                var entry = opt.get();
                items.add(entry);
            } catch (NoIncludeException ex) {
                logger.debug(String.format("Table [%s] row [%d] doesn't include a link to another section in the document.", id, r));
            } catch (IncludeRecordSelectionKeysException ex) {
                logger.debug(String.format("Table [%s] row [%d] doesn't include a link but contains an \"Include Record Selection Keys\".", id, r));
            }
        }
        return new ModuleAttributes(id, name, items);
    }
}
