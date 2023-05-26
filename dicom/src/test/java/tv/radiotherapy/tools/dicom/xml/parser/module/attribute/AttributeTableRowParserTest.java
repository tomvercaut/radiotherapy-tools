package tv.radiotherapy.tools.dicom.xml.parser.module.attribute;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.DocumentReader;
import tv.radiotherapy.tools.dicom.xml.XRef;
import tv.radiotherapy.tools.dicom.xml.model.module.AttributeType;
import tv.radiotherapy.tools.dicom.xml.model.module.Tag;
import tv.radiotherapy.tools.dicom.xml.model.module.attribute.*;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AttributeTableRowParserTest {

    private static String scol4() {
        // Snippet from part03 table C.11.11-1b
        @Language("XML")
        String xml = """
                <tr valign="top">
                    <td align="left" colspan="1" rowspan="1">
                        <para>Referenced Series Sequence</para>
                    </td>
                    <td align="center" colspan="1" rowspan="1">
                        <para>(0008,1115)</para>
                    </td>
                    <td align="center" colspan="1" rowspan="1">
                        <para>1</para>
                    </td>
                    <td align="left" colspan="1" rowspan="1">
                        <para>Sequence of Items where each Item includes the
                            Attributes of one Series to which the Presentation applies.
                        </para>
                        <para>One or more Items shall be included in this Sequence.</para>
                    </td>
                </tr>""";
        return xml;
    }

    private static String scol3() {
        // Snippet from part03 table C.11-4
        @Language("XML")
        String xml = """
                <tr valign="top">
                    <td align="left" colspan="1" rowspan="1">
                        <para>Presentation LUT Sequence</para>
                    </td>
                    <td align="center" colspan="1" rowspan="1">
                        <para>(2050,0010)</para>
                    </td>
                    <td align="left" colspan="1" rowspan="1">
                        <para>Defines a Sequence of Presentation LUTs.</para>
                        <para>Only a single Item shall be included in this
                            Sequence.
                        </para>
                    </td>
                </tr>""";
        return xml;
    }

    private static String scol3SeqItem() {
        // Snippet from part03 table C.11-4
        @Language("XML")
        String xml = """
                <tr valign="top">
                    <td align="left" colspan="1" rowspan="1">
                        <para>&gt;LUT Descriptor</para>
                    </td>
                    <td align="center" colspan="1" rowspan="1">
                        <para>(0028,3002)</para>
                    </td>
                    <td align="left" colspan="1" rowspan="1">
                        <para>Specifies the format of the LUT Data in this Sequence.</para>
                        <para>Required if Presentation LUT Sequence (2050,0010) is present.</para>
                        <para>See
                            <xref linkend="sect_C.11.4.1" xrefstyle="select: label"/>
                            for further explanation.
                        </para>
                    </td>
                </tr>""";
        return xml;
    }

    private static String scol3AttributeIsTop() {
        // Snippet from part03 table C.12.1.1.9-1
        @Language("XML")
        String xml = """
                <tr valign="top">
                    <td align="left" colspan="2" rowspan="1">
                        <para>
                            <emphasis>&gt;&gt;Any Attribute from the top level Data Set that was modified or removed.
                            </emphasis>
                        </para>
                    </td>
                    <td align="center" colspan="1" rowspan="1">
                        <para>2</para>
                    </td>
                    <td align="left" colspan="1" rowspan="1">
                        <para>May include Sequence Attributes and their Items.</para>
                    </td>
                </tr>""";
        return xml;
    }

    private static String scol3AnyDateTimeAttributes() {
        @Language("XML")
        String xml = """
                <tr valign="top">
                    <td align="left" colspan="2" rowspan="1">
                        <para>
                            <emphasis role="italic">&gt;&gt;Any DA, DT, or TM Attributes</emphasis>
                        </para>
                    </td>
                    <td align="center" colspan="1" rowspan="1">
                        <para>2</para>
                    </td>
                    <td align="left" colspan="1" rowspan="1">
                        <para>Key Attribute values for matching.</para>
                        <para>Multiple Attributes may be present.</para>
                    </td>
                </tr>""";
        return xml;
    }

    private static String scol3AnyUidAttributes() {
        @Language("XML")
        String xml = """
                <tr valign="top">
                    <td align="left" colspan="2" rowspan="1">
                        <para>
                            <emphasis role="italic">&gt;&gt;Any UI Key Attribute</emphasis>
                        </para>
                    </td>
                    <td align="center" colspan="1" rowspan="1">
                        <para>1</para>
                    </td>
                    <td align="left" colspan="1" rowspan="1">
                        <para>Key Attribute value for matching.</para>
                    </td>
                </tr>""";
        return xml;
    }
    private static String scol3AnyAttributes() {
        @Language("XML")
        String xml = """
                <tr valign="top">
                    <td align="left" colspan="2" rowspan="1">
                        <para>
                            <emphasis role="italic">&gt;&gt;Any Attributes</emphasis>
                        </para>
                    </td>
                    <td align="center" colspan="1" rowspan="1">
                        <para>2</para>
                    </td>
                    <td align="left" colspan="1" rowspan="1">
                        <para>Key Attributes with zero-length values.</para>
                        <para>Multiple Attributes may be present.</para>
                    </td>
                </tr>""";
        return xml;
    }

    private static String scol2Include() {
        @Language("XML")
        String xml = """
                <tr valign="top">
                    <td align="left" colspan="3" rowspan="1">
                        <para>
                            <emphasis role="italic">&gt;Include
                                <xref linkend="table_C.11.11-1b" xrefstyle="select: label quotedtitle"/>
                            </emphasis>
                        </para>
                    </td>
                    <td align="left" colspan="1" rowspan="1">
                        <para>abc</para>
                    </td>
                </tr>""";
        return xml;
    }

    @Test
    void parseRow4Columns() throws ParserException, XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        var doc = DocumentReader.readXmlString(scol4());
        var root = doc.getDocumentElement();
        var opt = new AttributeTableRowParser().parseRow(root);
        assertNotNull(opt);
        assertTrue(opt.isPresent());
        var value = opt.get();
        assertInstanceOf(NameTagTypeDescItem.class, value);
        var item = (NameTagTypeDescItem) value;
        assertEquals(0, item.depth());
        assertEquals("Referenced Series Sequence", item.name());
        var optTag= item.rtag().toTag();
        assertTrue(optTag.isPresent());
        assertEquals(new Tag(0x0008, 0x1115), optTag.get());
        assertEquals(AttributeType.ONE, item.type());
        assertTrue(item.desc().startsWith("Sequence of Items where"));
        assertTrue(item.desc().endsWith("in this Sequence."));
    }

    @Test
    void parseRow3Columns() throws ParserException, XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        var doc = DocumentReader.readXmlString(scol3());
        var root = doc.getDocumentElement();
        var opt = new AttributeTableRowParser().parseRow(root);
        assertNotNull(opt);
        assertTrue(opt.isPresent());
        var value = opt.get();
        assertInstanceOf(NameTagDescItem.class, value);
        var item = (NameTagDescItem) value;
        assertEquals(0, item.depth());
        assertEquals("Presentation LUT Sequence", item.name());
        var optTag = item.rtag().toTag();
        assertTrue(optTag.isPresent());
        assertEquals(new Tag(0x2050, 0x0010), optTag.get());
        assertTrue(item.desc().startsWith("Defines a Sequence of Presentation"));
        assertTrue(item.desc().endsWith("in this\nSequence."));
    }

    @Test
    void parseRow3ColumnsSeqItem() throws ParserException, XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        var doc = DocumentReader.readXmlString(scol3SeqItem());
        var root = doc.getDocumentElement();
        var opt = new AttributeTableRowParser().parseRow(root);
        assertNotNull(opt);
        assertTrue(opt.isPresent());
        var value = opt.get();
        assertInstanceOf(NameTagDescItem.class, value);
        var item = (NameTagDescItem) value;
        assertEquals(1, item.depth());
        assertEquals("LUT Descriptor", item.name());
        var optTag = item.rtag().toTag();
        assertTrue(optTag.isPresent());
        assertEquals(new Tag(0x0028, 0x3002), optTag.get());
        assertTrue(item.desc().startsWith("Specifies the format"));
        assertTrue(item.desc().endsWith("further explanation."));
    }

    @Test
    void parseRow3ColumnsAttributeIsTop() throws ParserException, XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        var doc = DocumentReader.readXmlString(scol3AttributeIsTop());
        var root = doc.getDocumentElement();
        var opt = new AttributeTableRowParser().parseRow(root);
        assertNotNull(opt);
        assertTrue(opt.isPresent());
        var value = opt.get();
        assertInstanceOf(ParentItem.class, value);
        var item = (ParentItem) value;
        assertEquals(2, item.depth());
        assertEquals("Any Attribute from the top level Data Set that was modified or removed.", item.name());
        assertEquals("May include Sequence Attributes and their Items.", item.desc());
    }


    @Test
    void given3Columns_whenParse_thenAnyDateTime() throws ParserException, XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        var doc = DocumentReader.readXmlString(scol3AnyDateTimeAttributes());
        var root = doc.getDocumentElement();
        var opt = new AttributeTableRowParser().parseRow(root);
        assertNotNull(opt);
        assertTrue(opt.isPresent());
        var value = opt.get();
        assertInstanceOf(AnyDateTime.class, value);
        var item = (AnyDateTime) value;
        assertEquals(2, item.depth());
        assertEquals(AttributeType.TWO, item.type());
        assertTrue(item.desc().startsWith("Key Attribute values for matching."));
    }


    @Test
    void given3Columns_whenParse_thenAnyUid() throws ParserException, XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        var doc = DocumentReader.readXmlString(scol3AnyUidAttributes());
        var root = doc.getDocumentElement();
        var opt = new AttributeTableRowParser().parseRow(root);
        assertNotNull(opt);
        assertTrue(opt.isPresent());
        var value = opt.get();
        assertInstanceOf(AnyUid.class, value);
        var item = (AnyUid) value;
        assertEquals(2, item.depth());
        assertEquals(AttributeType.ONE, item.type());
        assertTrue(item.desc().startsWith("Key Attribute value for matching."));
    }

    @Test
    void given3Columns_whenParse_thenAny() throws ParserException, XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        var doc = DocumentReader.readXmlString(scol3AnyAttributes());
        var root = doc.getDocumentElement();
        var opt = new AttributeTableRowParser().parseRow(root);
        assertNotNull(opt);
        assertTrue(opt.isPresent());
        var value = opt.get();
        assertInstanceOf(Any.class, value);
        var item = (Any) value;
        assertEquals(2, item.depth());
        assertEquals(AttributeType.TWO, item.type());
        assertTrue(item.desc().startsWith("Key Attributes with zero-length values."));
    }

    @Test
    void parseRow2ColumnsInclude() throws ParserException, XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        var doc = DocumentReader.readXmlString(scol2Include());
        var root = doc.getDocumentElement();
        var opt = new AttributeTableRowParser().parseRow(root);
        assertNotNull(opt);
        assertTrue(opt.isPresent());
        var value = opt.get();
        assertInstanceOf(Include.class, value);
        var item = (Include) value;
        assertEquals(1, item.depth());
        assertEquals(new Include(1, new XRef("table_C.11.11-1b", "select: label quotedtitle"), "abc"), item);
    }
}