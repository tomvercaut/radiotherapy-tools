package tv.radiotherapy.tools.dicom.xml.parser.module;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import tv.radiotherapy.tools.dicom.xml.model.module.Tag;
import tv.radiotherapy.tools.dicom.xml.parser.InnerText;
import tv.radiotherapy.tools.dicom.xml.parser.Parser;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

import javax.xml.xpath.XPathExpressionException;
import java.util.regex.Pattern;

public class TagParser implements Parser<Tag> {
    private final static Pattern pattern = Pattern.compile("\\(([\\dA-Fa-f]{4}),([\\dA-Fa-f]{4})\\)");

    @Override
    public Tag parse(@NotNull Element element) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException {
        var s = InnerText.get(element).trim();
        var matcher = pattern.matcher(s);
        if (!matcher.find() || matcher.groupCount() != 2) {
            throw new IllegalArgumentException("Invalid input string to extract DICOM tag from \"" + s + "\"");
        }
        var sg = matcher.group(1);
        var se = matcher.group(2);
        int g = Integer.parseInt(sg, 16);
        if (g < 0 || g > 0xFFFF) {
            throw new IllegalArgumentException("Input value must be strict positive and smaller or equal to 0xFFFF.");
        }
        int e = Integer.parseInt(se, 16);
        if (e < 0 || e > 0xFFFF) {
            throw new IllegalArgumentException("Input value must be strict positive and smaller or equal to 0xFFFF.");
        }
        return new Tag(g, e);
    }
}
