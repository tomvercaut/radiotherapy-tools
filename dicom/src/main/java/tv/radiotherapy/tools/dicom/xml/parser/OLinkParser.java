package tv.radiotherapy.tools.dicom.xml.parser;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import tv.radiotherapy.tools.dicom.xml.model.OLink;

import static org.apache.logging.log4j.util.Strings.quote;

public class OLinkParser implements Parser<OLink> {
    private final static String requiredElementName = "olink";

    @Override
    public OLink parse(@NotNull Element element) throws ParserException {
        var name = element.getTagName();
        if (!name.equals(requiredElementName)) {
            throw new ParserException("Expected an element with the name " + quote(requiredElementName) + " but found an element with the name " + quote(name));
        }

        var link = new OLink();
        link.setDocument(element.getAttribute("targetdoc"));
        link.setPtr(element.getAttribute("targetptr"));
        link.setStyle(element.getAttribute("xrefstyle"));
        link.setLocalInfo(element.getAttribute("localinfo"));
        link.setType(element.getAttribute("type"));

        if (link.getDocument().isBlank()) {
            throw new ParserException("Element: " + quote(requiredElementName) + ", attribute: " + quote("targetdoc") + " is missing or empty");
        }
        if (link.getPtr().isBlank()) {
            throw new ParserException("Element: " + quote(requiredElementName) + ", attribute: " + quote("targetptr") + " is missing or empty");
        }
        if (link.getStyle().isBlank()) {
            throw new ParserException("Element: " + quote(requiredElementName) + ", attribute: " + quote("xrefstyle") + " is missing or empty");
        }
        return link;
    }
}
