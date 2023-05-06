package tv.radiotherapy.tools.dicom.xml.parser.registry;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import tv.radiotherapy.tools.dicom.xml.model.OLink;
import tv.radiotherapy.tools.dicom.xml.model.registry.TemplateUid;
import tv.radiotherapy.tools.dicom.xml.parser.*;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.Optional;

public class TemplateUidTableRowParser implements TableRowParser<TemplateUid> {
    private final OLinkParser olinkParser = new OLinkParser();
    private final UidTypeParser uidTypeParser = new UidTypeParser();

    private static XPathExpression olinkExpression() throws XPathExpressionException {
        return XPathFactory.newInstance().newXPath().compile(".//olink");
    }

    @Override
    public Optional<TemplateUid> parseRow(@NotNull Node row) throws XPathExpressionException, IllegalArgumentException, ParserException {
        if (row.getNodeType() != Node.ELEMENT_NODE) {
            throw new IllegalArgumentException("XML node must be of an XML element");
        }
        var tds = TableHelper.getColumns(row);
        if (tds.getLength() != 4) {
            throw new ParserException(String.format("Expected a table row with 4 columns but the actual column count is %d", tds.getLength()));
        }

        var uid = InnerText.get(tds.item(0)).replace(" ", "");
        var olinkElement1 = (Element) olinkExpression().evaluate(tds.item(1), XPathConstants.NODE);
        OLink name;
        if (olinkElement1 == null) {
            name = new OLink("", "", "", "", "");
        } else {
            name = olinkParser.parse(olinkElement1);
        }
        var uidType = uidTypeParser.parse((Element) tds.item(2));
        var olinkElement2 = (Element) olinkExpression().evaluate(tds.item(3), XPathConstants.NODE);
        OLink part;
        if (olinkElement2 == null) {
            part = new OLink("", "", "", "", "");
        } else {
            part = olinkParser.parse(olinkElement2);
        }
        return Optional.of(new TemplateUid(uid, name, uidType, part));
    }
}
