package tv.radiotherapy.tools.dicom.xml.parser;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import tv.radiotherapy.tools.dicom.xml.model.Usage;

import javax.xml.xpath.XPathExpressionException;

public class UsageParser implements Parser<Usage> {
    @Override
    public Usage parse(@NotNull Element element) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException {
        var s = InnerText.get(element).trim();
        var i = s.indexOf(' ');
        String t = s;
        if (i > 0) {
            t = s.substring(0, i);
        }
        switch (t) {
            case "M" -> {
                return Usage.M;
            }
            case "U" -> {
                return Usage.U;
            }
            case "C" -> {
                return Usage.C;
            }
            default -> throw new ParserException("Unable to parse Usage from " + s);
        }
    }
}
