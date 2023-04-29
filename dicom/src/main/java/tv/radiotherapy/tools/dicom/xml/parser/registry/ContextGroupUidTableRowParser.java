package tv.radiotherapy.tools.dicom.xml.parser.registry;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import tv.radiotherapy.tools.dicom.xml.model.OLink;
import tv.radiotherapy.tools.dicom.xml.model.registry.ContextGroupUid;
import tv.radiotherapy.tools.dicom.xml.parser.*;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.Optional;

public class ContextGroupUidTableRowParser implements TableRowParser<ContextGroupUid> {
    private final OLinkParser oLinkParser = new OLinkParser();

    @Override
    public Optional<ContextGroupUid> parseRow(@NotNull Node row) throws XPathExpressionException, IllegalArgumentException, ParserException {
        if (row.getNodeType() != Node.ELEMENT_NODE) {
            throw new IllegalArgumentException("XML node must be of an XML element");
        }
        // Extract row columns and iterate them
        var tds = TableHelper.getColumns(row);
        if (tds.getLength() != 4) {
            throw new ParserException(String.format("Expected a table row with 4 columns but the actual column count is %d", tds.getLength()));
        }
        var uid = InnerText.get(tds.item(0));
        var olinkElement1 = (Element) olinkExpression().evaluate(tds.item(1), XPathConstants.NODE);
        OLink ctx;
        if (olinkElement1 != null) {
            ctx = oLinkParser.parse(olinkElement1);
        } else {
            ctx = new OLink("", "", "", "", "");
        }
        var olinkElement2 = (Element) olinkExpression().evaluate(tds.item(2), XPathConstants.NODE);
        OLink grp;
        if (olinkElement2 != null) {
            grp = oLinkParser.parse(olinkElement2);
        } else {
            grp = new OLink("", "", "", "", "");
        }
        var comment = InnerText.get(tds.item(3));
        return Optional.of(new ContextGroupUid(uid, ctx, grp, comment));
    }

    private static XPathExpression olinkExpression() throws XPathExpressionException {
        return XPathFactory.newInstance().newXPath().compile(".//olink");
    }
}
