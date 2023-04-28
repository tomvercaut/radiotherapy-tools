package tv.radiotherapy.tools.dicom.xml.parser;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import tv.radiotherapy.tools.dicom.xml.model.VR;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VRParser implements Parser<List<VR>> {
    @Override
    public List<VR> parse(@NotNull Element element) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException {
        var s = InnerText.get(element).trim();
        if (s.isBlank() || s.contains("See Note")) {
            return List.of(VR.NONE);
        } else if (s.contains("or")) {
            var vrs = Arrays.stream(s.split("or")).toList();
            var nvrs = vrs.size();
            var list = new ArrayList<VR>(nvrs);
            for (String s1 : vrs) {
                list.add(VR.valueOf(s1.trim()));
            }
            return list;
        } else {
            return List.of(VR.valueOf(s));
        }
    }
}
