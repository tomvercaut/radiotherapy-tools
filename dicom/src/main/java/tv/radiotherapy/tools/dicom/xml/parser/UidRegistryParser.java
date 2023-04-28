package tv.radiotherapy.tools.dicom.xml.parser;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tv.radiotherapy.tools.dicom.xml.model.OLink;
import tv.radiotherapy.tools.dicom.xml.model.UidRegistryItem;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;

public class UidRegistryParser implements Parser<List<UidRegistryItem>> {
    @Override
    public List<UidRegistryItem> parse(@NotNull Element element) throws ParserException, XPathExpressionException {
        var registry = new ArrayList<UidRegistryItem>();
        build(element, registry, "table_A-1");
        return registry;
    }

    private void build(@NotNull Element root, @NotNull List<UidRegistryItem> registry, @NotNull String tableId) throws XPathExpressionException, IllegalArgumentException, NullPointerException, ParserException {
        // Find table by id.
        final Node table = TableHelper.findById(root, tableId);
        // Extract table rows and iterate the rows.
        final NodeList rows = TableHelper.getRows(table);
        final var nrows = rows.getLength();
        // UID type parser
        var uidTypeParser = new UidTypeParser();
        // OLink parser and XPath expression
        var olinkParser = new OLinkParser();
        var olinkExpression = XPathFactory.newInstance().newXPath().compile(".//olink");
        for (int r = 0; r < nrows; r++) {

            // Extract row columns and iterate them
            var tds = TableHelper.getColumns(rows.item(r));
            if (tds.getLength() != 5) {
                throw new ParserException(String.format("Expected a table row[%d] with 5 columns but the actual column count is %d", r, tds.getLength()));
            }

            var uid = InnerText.get(tds.item(0)).replace(" ", "");
            var name = InnerText.get(tds.item(1));
            var keyword = InnerText.get(tds.item(2)).replace(" ", "");
            var uidType = uidTypeParser.parse((Element) tds.item(3));
            var olinkElement = (Element) olinkExpression.evaluate(tds.item(4), XPathConstants.NODE);
            OLink olink;
            if (olinkElement == null) {
                olink = new OLink("", "", "", "", "");
            } else {
                olink = olinkParser.parse(olinkElement);
            }
            registry.add(new UidRegistryItem(uid, name, keyword, uidType, olink));
        }
    }
}
