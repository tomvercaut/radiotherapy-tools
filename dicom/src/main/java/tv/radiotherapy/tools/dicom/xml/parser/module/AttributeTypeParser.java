package tv.radiotherapy.tools.dicom.xml.parser.module;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import tv.radiotherapy.tools.dicom.xml.model.module.AttributeType;
import tv.radiotherapy.tools.dicom.xml.parser.InnerText;
import tv.radiotherapy.tools.dicom.xml.parser.Parser;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

import javax.xml.xpath.XPathExpressionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttributeTypeParser implements Parser<AttributeType> {
    @SuppressWarnings("EscapedSpace")
    private final static Pattern pattern = Pattern.compile("[\s\n]");

    @Override
    public AttributeType parse(@NotNull Element element) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException {
        var s = InnerText.get(element).trim();
        if (s.isBlank()) {
            return AttributeType.NONE;
        }
        final Matcher matcher = pattern.matcher(s);
        String t = s;
        if (matcher.find()) {
            final var i = matcher.start();
            t = s.substring(0, i);
        }
        switch (t) {
            case "1" -> {
                return AttributeType.ONE;
            }
            case "1C" -> {
                return AttributeType.ONE_C;
            }
            case "2" -> {
                return AttributeType.TWO;
            }
            case "3" -> {
                return AttributeType.THREE;
            }
            default -> {
                return AttributeType.NONE;
            }
        }
    }
}
