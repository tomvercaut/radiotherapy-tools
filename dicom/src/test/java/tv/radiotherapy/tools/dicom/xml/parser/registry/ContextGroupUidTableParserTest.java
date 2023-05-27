package tv.radiotherapy.tools.dicom.xml.parser.registry;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.DocumentReader;
import tv.radiotherapy.tools.dicom.xml.model.OLink;
import tv.radiotherapy.tools.dicom.xml.model.registry.ContextGroupUid;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContextGroupUidTableParserTest {
    private static final char ZWSP = '\u200B';

    private static String getXml() {
        @Language("XML")
        String xml = """
                <book>
                    <table frame="box" label="A-3" rules="all" xml:id="table_A-3">
                    <caption>Context Group UID Values</caption>
                            <thead>
                                <tr valign="top">
                                    <th align="center" colspan="1" rowspan="1">
                                        <para xml:id="para_3322bcc5-7ea4-44d1-9b17-499a6a5e512c">
                                            <emphasis role="bold">Context UID</emphasis>
                                        </para>
                                    </th>
                                    <th align="center" colspan="1" rowspan="1">
                                        <para xml:id="para_225c7fe6-91ef-424b-812b-249ecd55f194">
                                            <emphasis role="bold">Context Identifier</emphasis>
                                        </para>
                                    </th>
                                    <th align="center" colspan="1" rowspan="1">
                                        <para xml:id="para_25349958-e4d1-4bb0-aaa9-33d5cb03df93">
                                            <emphasis role="bold">Context Group Name</emphasis>
                                        </para>
                                    </th>
                                    <th align="center" colspan="1" rowspan="1">
                                        <para xml:id="para_21c2633a-2b79-49c4-baa8-d930649479d0">
                                            <emphasis role="bold">Comment</emphasis>
                                        </para>
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr valign="top">
                                    <td align="left" colspan="1" rowspan="1">
                                        <para xml:id="para_a61be4bd-e095-42e3-a495-ba9619ba5c8d">1.2.840.10008.6.""";
        xml += ZWSP + "1." + ZWSP;
        xml += """
                1</para>
                                                    </td>
                                                    <td align="left" colspan="1" rowspan="1">
                                                        <para xml:id="para_63982966-ed69-44ad-965e-96bd990525f5">
                                                            <olink targetdoc="PS3.16" targetptr="sect_CID_2" xrefstyle="select: labelnumber"/>
                                                        </para>
                                                    </td>
                                                    <td align="left" colspan="1" rowspan="1">
                                                        <para xml:id="para_7f76a133-67d7-4b7e-9457-b477713b940c">
                                                            <olink targetdoc="PS3.16" targetptr="sect_CID_2" xrefstyle="select: title"/>
                                                        </para>
                                                    </td>
                                                    <td align="left" colspan="1" rowspan="1">
                                                        <para xml:id="para_1d4c9d73-ac2c-40e5-b62d-922e5e9d3c16"/>
                                                    </td>
                                                </tr>
                                                <tr valign="top">
                                                    <td align="left" colspan="1" rowspan="1">
                                                        <para xml:id="para_e88b945b-0f4b-4c8e-ab80-a392bfa04d77">1.2.840.10008.6.""";
        xml += ZWSP + "1." + ZWSP;
        xml += """
                2</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_faceabc6-c107-4bc2-ac87-02ef734fa134">
                                                        <olink targetdoc="PS3.16" targetptr="sect_CID_4" xrefstyle="select: labelnumber"/>
                                                    </para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_eec7e204-4006-4a18-a5eb-e593a97ce483">
                                                        <olink targetdoc="PS3.16" targetptr="sect_CID_4" xrefstyle="select: title"/>
                                                    </para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_7566a522-df22-4b5c-ad16-89ba21054b60"/>
                                                </td>
                                            </tr>
                                            <tr valign="top">
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_2b11e719-a926-48ce-aaca-37938ae4e91e">1.2.840.10008.6.""";
        xml += ZWSP + "1." + ZWSP;
        xml += """
                3</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_d533cde1-c4bc-4d44-8c8b-c2a8ec0abc8a">
                                                        <olink targetdoc="PS3.16" targetptr="sect_CID_5" xrefstyle="select: labelnumber"/>
                                                    </para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_255e194b-ae7c-40ad-9223-67a86fc89a17">
                                                        <olink targetdoc="PS3.16" targetptr="sect_CID_5" xrefstyle="select: title"/>
                                                    </para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_0a6173b7-15cd-4e92-a1dc-a18b127c39f5"/>
                                                </td>
                                            </tr>
                                           \s
                                            <tr valign="top">
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_1281b4af-53c2-49f0-b409-cbe84505683f">
                            <emphasis role="italic">1.2.840.10008.6.""";
        xml += ZWSP + "1." + ZWSP;
        xml += """
                945</emphasis>
                            </para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_867bb42e-0f39-4409-aa19-9c87b707c01b"/>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_aeff10ec-5ecd-4780-bd8d-67f59fbbef6d"/>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_ba7b3a9f-1133-4099-bbe9-03027e88804d">
                            <emphasis role="italic">Retired</emphasis>
                            </para>
                                                </td>
                                            </tr>
                                            <tr valign="top">
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_5b50ad34-fcc8-4970-99a5-1dd0fc4b2d37">
                            <emphasis role="italic">1.2.840.10008.6.""";
        xml += ZWSP + "1." + ZWSP;
        xml += """
                946</emphasis>
                            </para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_9ea083e3-8175-40a5-be84-31a39e09705f"/>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_024f236d-510f-45ba-ab5b-1aae9a13ba6a"/>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_64b5da9d-d892-4127-be1c-0fd9e54d1371">
                            <emphasis role="italic">Retired</emphasis>
                            </para>
                                                </td>
                                            </tr>
                                            </tbody>
                                            </table>
                                           </book>\s""";
        return xml;
    }

    @Test
    void parse() throws ParserException, XPathExpressionException, InterruptedException, IOException, ParserConfigurationException, SAXException {
        var doc = DocumentReader.readXmlString(getXml());
        var root = doc.getDocumentElement();
        var registry = new ContextGroupUidTableParser().parse(root, new ContextGroupUidTableRowParser());
        assertNotNull(registry);

        var expected = List.of(
                new ContextGroupUid("1.2.840.10008.6.1.1", new OLink("PS3.16", "sect_CID_2", "select: labelnumber"), new OLink("PS3.16", "sect_CID_2", "select: title"), ""),
                new ContextGroupUid("1.2.840.10008.6.1.2", new OLink("PS3.16", "sect_CID_4", "select: labelnumber"), new OLink("PS3.16", "sect_CID_4", "select: title"), ""),
                new ContextGroupUid("1.2.840.10008.6.1.945", new OLink("", "", ""), new OLink("", "", ""), "Retired"),
                new ContextGroupUid("1.2.840.10008.6.1.946", new OLink("", "", ""), new OLink("", "", ""), "Retired")
        );
        for (var item : expected) {
            assertTrue(registry.contains(item));
        }
    }
}
