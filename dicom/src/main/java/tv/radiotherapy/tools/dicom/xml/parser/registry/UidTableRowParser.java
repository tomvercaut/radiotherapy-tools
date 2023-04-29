package tv.radiotherapy.tools.dicom.xml.parser.registry;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import tv.radiotherapy.tools.dicom.xml.model.OLink;
import tv.radiotherapy.tools.dicom.xml.model.registry.Uid;
import tv.radiotherapy.tools.dicom.xml.parser.*;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.Optional;

public class UidTableRowParser implements TableRowParser<Uid> {
    private final OLinkParser olinkParser = new OLinkParser();
    private final UidTypeParser uidTypeParser = new UidTypeParser();

    @Override
    public Optional<Uid> parseRow(@NotNull Node row) throws XPathExpressionException, IllegalArgumentException, ParserException {
        if (row.getNodeType() != Node.ELEMENT_NODE) {
            throw new IllegalArgumentException("XML node must be of an XML element");
        }
        var tds = TableHelper.getColumns(row);
        if (tds.getLength() != 5) {
            throw new ParserException(String.format("Expected a table row with 5 columns but the actual column count is %d", tds.getLength()));
        }

        var uid = InnerText.get(tds.item(0)).replace(" ", "");
        var name = InnerText.get(tds.item(1));
        var keyword = InnerText.get(tds.item(2)).replace(" ", "");
        var uidType = uidTypeParser.parse((Element) tds.item(3));
        var olinkElement = (Element) olinkExpression().evaluate(tds.item(4), XPathConstants.NODE);
        OLink olink;
        if (olinkElement == null) {
            olink = new OLink("", "", "", "", "");
        } else {
            olink = olinkParser.parse(olinkElement);
        }
        return Optional.of(new Uid(uid, name, keyword, uidType, olink));
    }

    private static XPathExpression olinkExpression() throws XPathExpressionException {
        return XPathFactory.newInstance().newXPath().compile(".//olink");
    }
}
