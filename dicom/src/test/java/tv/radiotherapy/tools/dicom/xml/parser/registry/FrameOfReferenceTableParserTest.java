package tv.radiotherapy.tools.dicom.xml.parser.registry;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.DocumentReader;
import tv.radiotherapy.tools.dicom.xml.model.registry.FrameOfReference;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FrameOfReferenceTableParserTest {

    private static final char ZWSP = '\u200B';

    private static String getXml() {
        @Language("XML")
        String xml = """
                <book>
                    <table frame="box" label="A-2" rules="all" xml:id="table_A-2">
                        <caption>Well-known Frames of Reference</caption>
                        <thead>
                            <tr valign="top">
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_6b2f66e8-4232-407f-88b9-06ac0bfc17ec">UID Value</para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_9bebba6d-8bc7-4a06-b87e-68dd2ab7be42">UID Name</para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_08e37fae-e7b0-4c25-b153-638ab9ce90d2">
                                        <emphasis role="bold">UID """;
        xml += ZWSP;
        xml += """
                Keyword</emphasis>
                                                </para>
                                            </th>
                                            <th align="center" colspan="1" rowspan="1">
                                                <para xml:id="para_2ca9a076-9357-4809-b6d0-9b785557e11f">Normative Reference</para>
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr valign="top">
                                            <td align="left" colspan="1" rowspan="1">
                                                <para xml:id="para_398f82f3-0659-4c0a-b4fa-080261fa8808">1.2.840.10008.1.4.""";
        xml += ZWSP + "1." + ZWSP;
        xml += """
                1</para>
                                            </td>
                                            <td align="left" colspan="1" rowspan="1">
                                                <para xml:id="para_5c73178a-e314-4f5f-8812-331d2c13a045">Talairach Brain Atlas Frame of Reference
                                                </para>
                                            </td>
                                            <td align="left" colspan="1" rowspan="1">
                                                <para xml:id="para_059a178a-a88f-4ffd-9adf-a885121c3a20">Talairach""";
        xml += ZWSP + "Brain" + ZWSP;
        xml += """
                Atlas</para>
                                            </td>
                                            <td align="left" colspan="1" rowspan="1">
                                                <para xml:id="para_cdb89db0-3271-460d-bf73-b1a8504bc74e">Talairach J. and Tournoux P. Co-Planar
                                                    stereotactic atlas of the human brain. Stuttgart: Georg Thieme Verlag, 1988.
                                                </para>
                                            </td>
                                        </tr>
                                        <tr valign="top">
                                            <td align="left" colspan="1" rowspan="1">
                                                <para xml:id="para_b961d129-b9c6-4fe0-9d99-b09775bf2540">1.2.840.10008.1.4.""";
        xml += ZWSP + "1." + ZWSP;
        xml += """
                2</para>
                                            </td>
                                            <td align="left" colspan="1" rowspan="1">
                                                <para xml:id="para_f407fce3-a31f-4bf6-adb1-65f4ef42a401">SPM2 T1 Frame of Reference</para>
                                            </td>
                                            <td align="left" colspan="1" rowspan="1">
                                                <para xml:id="para_67196c86-d0c4-483c-b42d-a405618c3386">SPM2""";
        xml += ZWSP;
        xml += """
                T1</para>
                                            </td>
                                            <td align="left" colspan="1" rowspan="1">
                                                <para xml:id="para_9e14ab04-d31c-4ff0-8e9e-d73674aa7d6f">
                                                    <link xl:href="http://github.com/spm/spm2/blob/master/templates/T1.mnc?raw=true">
                                                        http://""";
        xml += ZWSP + "github.com/" + ZWSP + "spm/" + ZWSP + "spm2/" + ZWSP + "blob/" + ZWSP;
        xml += """
                master/templates/T1.mnc
                                                    </link>
                                                </para>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </book>""";
        return xml;
    }

    @Test
    void parse() throws ParserException, XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        var doc = DocumentReader.readXmlString(getXml());
        var root = doc.getDocumentElement();
        var registry = new FrameOfReferenceTableParser().parse(root, new FrameOfReferenceTableRowParser());
        assertNotNull(registry);

        var expected = List.of(
                new FrameOfReference("1.2.840.10008.1.4.1.1", "Talairach Brain Atlas Frame of Reference", "TalairachBrainAtlas"),
                new FrameOfReference("1.2.840.10008.1.4.1.2", "SPM2 T1 Frame of Reference", "SPM2T1")
        );
        for (var item : expected) {
            assertTrue(registry.contains(item));
        }
    }
}
