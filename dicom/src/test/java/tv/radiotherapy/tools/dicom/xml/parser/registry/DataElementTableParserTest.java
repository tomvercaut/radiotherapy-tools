package tv.radiotherapy.tools.dicom.xml.parser.registry;

import lombok.extern.log4j.Log4j2;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.DocumentReader;
import tv.radiotherapy.tools.dicom.xml.model.RangedTag;
import tv.radiotherapy.tools.dicom.xml.model.VR;
import tv.radiotherapy.tools.dicom.xml.model.registry.DataElement;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
public class DataElementTableParserTest {

    private static String getXml() {
        @Language("XML")
        String xml = """
                <book>
                    <table frame="box" label="6-1" rules="all" xml:id="table_6-1">
                        <caption>Registry of DICOM Data Elements</caption>
                        <thead>
                            <tr valign="top">
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_3e8c52d5-3582-4ec4-a7e6-acd7604caf32">
                                        <emphasis role="bold">Tag</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_ac298c3b-bcdd-462b-9240-c38e738bfb7c">
                                        <emphasis role="bold">Name</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_99f6b0ff-e1b0-484e-83b5-c3003d61704c">
                                        <emphasis role="bold">Keyword</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_42b3621d-8b30-46eb-8992-10bb21310ac7">
                                        <emphasis role="bold">VR</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_551ea829-a949-4bfd-a201-bb102024e64c">
                                        <emphasis role="bold">VM</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_3a18713f-2e81-4664-b9a1-e4e3d41957ab"/>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr valign="top">
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_a3b3bd4c-066d-47ca-9cfc-310e017063df">
                                        <emphasis role="italic">(0008,0001)</emphasis>
                                    </para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_98dadd68-a5fc-4f46-9590-cf129f76a96f">
                                        <emphasis role="italic">Length to End</emphasis>
                                    </para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_d4f02df2-6cdf-4324-afb8-5d033d1d63a2">
                                        <emphasis role="italic">Length​To​End</emphasis>
                                    </para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_40ad40bf-10cb-42fe-bf58-10794de1d4e7">
                                        <emphasis role="italic">UL</emphasis>
                                    </para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_f0c1b432-1911-4196-ac5e-c50217908dd9">
                                        <emphasis role="italic">1</emphasis>
                                    </para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_b8e51f4b-5e85-4337-b2a7-14eecec3d1f5">
                                        <emphasis role="italic">RET</emphasis>
                                    </para>
                                </td>
                            </tr>
                            <tr valign="top">
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_6069751d-e1fb-4740-ae70-a5af57b85778">(0008,0005)</para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_0e3d7a81-72c1-4a28-b456-9765b8e0b848">Specific Character Set</para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_1b7575f0-152e-4f8b-a5a5-b434d3644682">Specific​Character​Set</para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_ab146458-275c-48d0-8b58-228df69ded71">CS</para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_a46f6de2-e592-4f99-aa91-3cd5b24f5262">1-n</para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_755031d6-eebd-409d-b07d-129b603eb54a"/>
                                </td>
                            </tr>
                            <tr valign="top">
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_ba2a311b-753c-4692-a56a-cfb7c137f6ad">(0008,0006)</para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_ac1de38b-0bbf-4f6c-ad21-280e68dbf6e4">Language Code Sequence</para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_5165d4d7-dbc1-4c4f-81ba-7a18a64f2ec9">Language​Code​Sequence</para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_1e1535d7-3875-45e8-8f2a-5e0f146626f3">SQ</para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_01867379-4b83-4f55-a436-2ae58d012d38">1</para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_c7394e55-2146-4907-9745-2af4085a6152"/>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <table frame="box" label="7-1" rules="all" xml:id="table_7-1">
                        <caption>Registry of DICOM File Meta Elements</caption>
                        <thead>
                            <tr valign="top">
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_7ba3db66-cfea-4381-b97d-b40577cc038d">
                                        <emphasis role="bold">Tag</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_b83af89e-d42d-434e-92d3-2cad321d1e22">
                                        <emphasis role="bold">Name</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_4cd18e86-8130-4893-98b8-fc14564383c1">
                                        <emphasis role="bold">Keyword</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_a71a5f1a-6f3b-44ec-b552-54a010c2ca45">
                                        <emphasis role="bold">VR</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_c069f9b2-5a34-4440-9be6-1b24928e1dba">
                                        <emphasis role="bold">VM</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_8d17b545-7512-4ccf-a03a-9a794d1122e6"/>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr valign="top">
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_53f4fbd2-9ffe-48cb-91a5-d3a8ef68052d">(0002,0000)</para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_4f4b326c-2f5e-48b4-ad16-eebaacc201c9">File Meta Information Group Length</para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_20a40d43-308c-4e7f-bcd7-ed9345eae21f">File​Meta​Information​Group​Length</para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_c2e5147d-c2b0-43ab-91d6-adb62f6cc960">UL</para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_264e5678-bdb3-4352-b92a-72ac5cd252e6">1</para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_7f9ab037-08d9-4fd4-af21-4346421d3914"/>
                                </td>
                            </tr>
                            <tr valign="top">
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_1597fac2-21fb-4039-9bd8-2acaa9d54d5a">(0002,0001)</para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_b4160ea8-7777-4df4-9527-2289de23fd68">File Meta Information Version</para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_c6967354-be33-44c6-a437-eec710111335">File​Meta​Information​Version</para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_5384115e-3709-427f-97d6-e33c357a835e">OB</para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_5ddcc164-e1cf-44dc-8d4d-3c0a27523c1b">1</para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_2692f4cb-5f45-4e8d-90f4-3c1ccaeb994d"/>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <table frame="box" label="8-1" rules="all" xml:id="table_8-1">
                        <caption>Registry of DICOM Directory Structuring Elements</caption>
                        <thead>
                            <tr valign="top">
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_5d8e0337-9da2-4a38-bdbf-5143ab91f2b3">
                                        <emphasis role="bold">Tag</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_6af49d19-b45d-4d4a-a025-6456a93b1e5a">
                                        <emphasis role="bold">Name</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_f299e0a0-8bcd-44ee-bc9e-f92bd71e393a">
                                        <emphasis role="bold">Keyword</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_9ae72fa3-9f27-425d-8aa0-cefba94507fb">
                                        <emphasis role="bold">VR</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_343b92e6-b0b3-4827-8810-fdd336fa2aab">
                                        <emphasis role="bold">VM</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_2c3ca515-9c1a-4329-8453-0c302b966cf0"/>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr valign="top">
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_13613462-1269-4231-8722-c9ded470e25f">(0004,151A)</para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_c21c8219-63c2-459f-b26a-a0727c64e967">Referenced Related General SOP Class UID in
                                        File
                                    </para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_47678435-c2a0-4c81-892d-9b1ba23739a6">
                                        Referenced​Related​General​SOP​Class​UID​In​File
                                    </para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_858f854a-a7c9-45fd-9744-9dc080d1d444">UI</para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_a9f8f5ec-9b79-42a3-889d-61d658d6d21a">1-n</para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_4acd494f-10fc-4a29-ab39-7874d63afede"/>
                                </td>
                            </tr>
                            <tr valign="top">
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_16eca443-193f-46ba-a117-10c052e1bc0c">
                                        <emphasis role="italic">(0004,1600)</emphasis>
                                    </para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_2d9e2855-7937-413f-a799-0a2445b767e2">
                                        <emphasis role="italic">Number of References</emphasis>
                                    </para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_50889854-e734-4729-a744-2216df96de6c">
                                        <emphasis role="italic">Number​Of​References</emphasis>
                                    </para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_2f976649-caef-4090-8e07-ee4834e83a3d">
                                        <emphasis role="italic">UL</emphasis>
                                    </para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_be6f627f-918e-47bb-b657-b624ff6ae8ca">
                                        <emphasis role="italic">1</emphasis>
                                    </para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_2ce6defe-772b-44ac-8a47-2e66969336b9">
                                        <emphasis role="italic">RET (2004)</emphasis>
                                    </para>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <table frame="box" label="9-1" rules="all" xml:id="table_9-1">
                        <caption>Registry of DICOM Dynamic RTP Payload Elements</caption>
                        <thead>
                            <tr valign="top">
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_64979e15-0679-4d13-8673-fb0e8a2e7707">
                                        <emphasis role="bold">Tag</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_f759657a-3df8-43cd-a378-dc6ca87472f8">
                                        <emphasis role="bold">Name</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_d68ddc35-03fe-4314-9bd1-ac7308973096">
                                        <emphasis role="bold">Keyword</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_b2ad1a2d-6ff2-4ee9-aa80-cf553f208066">
                                        <emphasis role="bold">VR</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_b54afc28-7f87-490a-8fa0-504059be4961">
                                        <emphasis role="bold">VM</emphasis>
                                    </para>
                                </th>
                                <th align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_5c65433c-7afe-4c81-83a9-3a31c4e9752a"/>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr valign="top">
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_0e85f0a2-3b6f-489e-8ea8-493e0bdaea39">(0006,0001)</para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_d289b6e9-c5f0-45cd-9f67-8b3e34e3d8a2">Current Frame Functional Groups Sequence
                                    </para>
                                </td>
                                <td align="left" colspan="1" rowspan="1">
                                    <para xml:id="para_034cd69b-fd80-42f8-aebe-908bf0a27c9b">Current​Frame​Functional​Groups​Sequence
                                    </para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_1aef10ad-d2d1-45c6-91ec-59784133807d">SQ</para>
                                </td>
                                <td align="center" colspan="1" rowspan="1">
                                    <para xml:id="para_991a7547-45ec-4fc5-aaf9-244cb3e7a431">1</para>
                                </td>
                                <td align="left" colspan="1" rowspan="1"/>
                            </tr>
                        </tbody>
                    </table>
                </book>""";
        return xml;
    }

    @Test
    void build() throws ParserException, XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        var doc = DocumentReader.readXmlString(getXml());
        var root = doc.getDocumentElement();

        var registry = new DataElementTableParser().parse(root, new DataElementTableRowParser());
        assertNotNull(registry);
        var expected = List.of(
                new DataElement(RangedTag.create(0x0008, 0x0001), "Length to End", "LengthToEnd", VR.UL, List.of(), "1", "RET"),
                new DataElement(RangedTag.create(0x0008, 0x0005), "Specific Character Set", "SpecificCharacterSet", VR.CS, List.of(), "1-n", ""),
                new DataElement(RangedTag.create(0x0008, 0x0006), "Language Code Sequence", "LanguageCodeSequence", VR.SQ, List.of(), "1", ""),
                new DataElement(RangedTag.create(0x0002, 0x0000), "File Meta Information Group Length", "FileMetaInformationGroupLength", VR.UL, List.of(), "1", ""),
                new DataElement(RangedTag.create(0x0002, 0x0001), "File Meta Information Version", "FileMetaInformationVersion", VR.OB, List.of(), "1", ""),
                new DataElement(RangedTag.create(0x0004, 0x151A), "Referenced Related General SOP Class UID in File", "ReferencedRelatedGeneralSOPClassUIDInFile", VR.UI, List.of(), "1-n", ""),
                new DataElement(RangedTag.create(0x0004, 0x1600), "Number of References", "NumberOfReferences", VR.UL, List.of(), "1", "RET (2004)"),
                new DataElement(RangedTag.create(0x0006, 0x0001), "Current Frame Functional Groups Sequence", "CurrentFrameFunctionalGroupsSequence", VR.SQ, List.of(), "1", "")
        );
        final var n = expected.size();
        assertEquals(n, registry.size());
        for (var e : expected) {
            assertTrue(registry.contains(e));
        }
    }
}
