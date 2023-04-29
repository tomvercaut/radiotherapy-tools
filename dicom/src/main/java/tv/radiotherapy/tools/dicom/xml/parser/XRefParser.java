package tv.radiotherapy.tools.dicom.xml.parser;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import tv.radiotherapy.tools.dicom.xml.XRef;

import javax.xml.xpath.XPathExpressionException;

import static org.apache.logging.log4j.util.Strings.quote;

public class XRefParser implements Parser<XRef> {
    private final static String requiredElementName = "xref";

    @Override
    public XRef parse(@NotNull Element element) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException {
        var name = element.getTagName();
        if (!name.equals(requiredElementName)) {
            throw new ParserException("Expected an element with the name " + quote(requiredElementName) + " but found an element with the name " + quote(name));
        }

        var linkend = element.getAttribute("linkend");
        var xrefstyle = element.getAttribute("xrefstyle");

        if (linkend.isBlank()) {
            throw new ParserException("Element: " + quote(requiredElementName) + ", attribute: " + quote("linkend") + " is missing or empty");
        }
        if (xrefstyle.isBlank()) {
            throw new ParserException("Element: " + quote(requiredElementName) + ", attribute: " + quote("xrefstyle") + " is missing or empty");
        }
        return new XRef(linkend, xrefstyle);
    }
}
