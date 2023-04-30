package tv.radiotherapy.tools.dicom.xml.parser.module;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import tv.radiotherapy.tools.dicom.xml.XRef;
import tv.radiotherapy.tools.dicom.xml.model.Usage;
import tv.radiotherapy.tools.dicom.xml.model.module.CiodItem;
import tv.radiotherapy.tools.dicom.xml.model.module.ciod.IodModule;
import tv.radiotherapy.tools.dicom.xml.parser.*;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CiodTableRowParser implements TableRowParser<CiodItem> {
    private final XRefParser xRefParser = new XRefParser();
    private final UsageParser usageParser = new UsageParser();

    private static XPathExpression xrefExpression() throws XPathExpressionException {
        return XPathFactory.newInstance().newXPath().compile(".//xref");
    }

    @Override
    public Optional<CiodItem> parseRow(@NotNull Node row) throws XPathExpressionException, IllegalArgumentException, ParserException {
        if (row.getNodeType() != Node.ELEMENT_NODE) {
            throw new IllegalArgumentException("XML node must be of an XML element");
        }
        var tds = TableHelper.getColumns(row);
        var ntds = tds.getLength();
        if (ntds != 4 && ntds != 3) {
            throw new ParserException(String.format("Expected a table row with 3 or 4 columns but the actual column count is %d", ntds));
        }

        String ie = "";
        String smodule;
        XRef xref = null;
        Usage usage = null;
        int c = 0;
        if (ntds == 4) {
            ie = InnerText.get(tds.item(c++));
        }
        smodule = InnerText.get(tds.item(c++));
        var xrefElem = (Element) xrefExpression().evaluate(tds.item(c++), XPathConstants.NODE);
        if (xrefElem != null) {
            xref = xRefParser.parse(xrefElem);
        }
        var usageElem = (Element) tds.item(c);
        if (usageElem != null) {
            usage = usageParser.parse((Element) tds.item(c));
        }

        List<IodModule> modules = new ArrayList<>();
        modules.add(new IodModule(smodule, xref, usage));
        return Optional.of(new CiodItem(ie, modules));
    }
}
