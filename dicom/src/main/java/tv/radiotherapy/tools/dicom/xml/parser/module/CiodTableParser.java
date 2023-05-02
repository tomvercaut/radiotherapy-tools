package tv.radiotherapy.tools.dicom.xml.parser.module;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tv.radiotherapy.tools.dicom.xml.model.module.Ciod;
import tv.radiotherapy.tools.dicom.xml.model.module.CiodItem;
import tv.radiotherapy.tools.dicom.xml.parser.InnerText;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;
import tv.radiotherapy.tools.dicom.xml.parser.TableHelper;
import tv.radiotherapy.tools.dicom.xml.parser.TableParser;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;

public class CiodTableParser implements TableParser<Ciod, CiodTableRowParser> {

    private final XPath xPath = XPathFactory.newInstance().newXPath();

    @Override
    public Ciod parse(@NotNull Element table, @NotNull CiodTableRowParser rowParser) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException {
        if (!TableHelper.isTable(table)) {
            throw new ParserException("Expected an XML table element while parsing a CIOD table.");
        }
        var id = table.getAttribute("xml:id");
        var captionElement = (Element) xPath.compile(".//caption").evaluate(table, XPathConstants.NODE);
        if (captionElement == null) {
            throw new ParserException("Unable to find caption in XML table.");
        }
        var caption = InnerText.get(captionElement);
        final NodeList rows = TableHelper.getRows(table);
        final var nrows = rows.getLength();
        final var ciods = new ArrayList<CiodItem>();
        for (int r = 0; r < nrows; r++) {
            var opt = rowParser.parseRow(rows.item(r));
            if (opt.isEmpty()) {
                continue;
            }
            var entry = opt.get();
            if (entry.hasIe()) {
                ciods.add(entry);
            } else {
                var idx = ciods.size() - 1;
                if (idx < 0) {
                    continue;
                }
                var last = ciods.get(idx);
                last.modules().addAll(entry.modules());
            }
        }
        var idx = caption.lastIndexOf("IOD Modules");
        String name;
        if (idx > 0) {
            name = caption.substring(0, idx).trim();
        } else {
            name = caption;
        }
        return new Ciod(id, name, ciods);
    }
}
