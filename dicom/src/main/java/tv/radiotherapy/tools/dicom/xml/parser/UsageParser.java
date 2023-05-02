package tv.radiotherapy.tools.dicom.xml.parser;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import tv.radiotherapy.tools.dicom.xml.model.Usage;

import javax.xml.xpath.XPathExpressionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsageParser implements Parser<Usage> {
    @SuppressWarnings("EscapedSpace")
    private final static Pattern pattern = Pattern.compile("[\s\n]");

    @Override
    public Usage parse(@NotNull Element element) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException {
        final var s = InnerText.get(element).trim();
        final Matcher matcher = pattern.matcher(s);
        String t = s;
        if (matcher.find()) {
            final var i = matcher.start();
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
