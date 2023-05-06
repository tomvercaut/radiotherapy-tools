package tv.radiotherapy.tools.dicom.xml.parser.registry;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.DocumentReader;
import tv.radiotherapy.tools.dicom.xml.model.OLink;
import tv.radiotherapy.tools.dicom.xml.model.UidType;
import tv.radiotherapy.tools.dicom.xml.model.registry.TemplateUid;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemplateUidTableParserTest {

    private static String getXml() {
        @Language("XML")
        String xml = """
                <book>
                    <table frame="box" label="A-4" rules="all" xml:id="table_A-4">
                        <caption>Template UID Values</caption>
                        <thead>
                            <tr valign="top">
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_4947c7ad-8d2a-44ae-bdab-e63e62944801">UID Value</para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_84f3a62c-feb2-4b51-ae84-acd1eb25b1fd">UID Name</para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_1514b1f5-b123-4614-b122-e072fc7b931e">UID Type</para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_aaf4fb64-c41d-411f-a14d-491e93153a63">Part</para>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr valign="top">
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_18028211-43e3-4be6-a904-23e1fb500ea9">1.2.840.10008.9.1</para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_ba33bab1-3d99-44a9-af4f-4ae8028741b0">
                                        <olink targetdoc="PS3.20" targetptr="sect_7.1" xrefstyle="select: title"/>
                                    </para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_13e8ff89-a9ba-4412-ae75-4582cf042ef0">Document TemplateID</para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_ab8920ae-9acd-417e-8026-d760f6203603">
                                        <olink targetdoc="PS3.20" targetptr="PS3.20" xrefstyle="select: labelnumber"/>
                                    </para>
                                </td>
                            </tr>
                            <tr valign="top">
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_a94773cd-303b-4c06-ac49-3f781efa01c9">1.2.840.10008.9.2</para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_e5bafd73-75da-4e16-b856-fc826b4d606f">
                                        <olink targetdoc="PS3.20" targetptr="sect_9.2" xrefstyle="select: title"/>
                                    </para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_d2e96b09-9d86-4aa6-a710-c966a92437e6">Section TemplateID</para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_d3aec29b-7711-43a4-a05e-bff2108803ba">
                                        <olink targetdoc="PS3.20" targetptr="PS3.20" xrefstyle="select: labelnumber"/>
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
        var registry = new TemplateUidTableParser().parse(root, new TemplateUidTableRowParser());
        assertNotNull(registry);

        var expected = List.of(
                new TemplateUid("1.2.840.10008.9.1", new OLink("PS3.20", "sect_7.1", "select: title"), UidType.DocumentTemplateId, new OLink("PS3.20", "PS3.20", "select: labelnumber")),
                new TemplateUid("1.2.840.10008.9.2", new OLink("PS3.20", "sect_9.2", "select: title"), UidType.SectionTemplateId, new OLink("PS3.20", "PS3.20", "select: labelnumber"))
        );
        for (var item : expected) {
            assertTrue(registry.contains(item));
        }
    }
}