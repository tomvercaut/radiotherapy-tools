package tv.radiotherapy.tools.dicom.xml.parser;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.DocumentReader;
import tv.radiotherapy.tools.dicom.xml.model.UidType;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UidTypeParserTest {

    @Test
    void parse() throws IOException, ParserConfigurationException, SAXException, ParserException, XPathExpressionException {
        @Language("XML")
        String xml = """
                <book>
                    <para>SOP Class</para>
                    <para>Transfer Syntax</para>
                    <para>Well-known SOP Instance</para>
                    <para>DICOM UIDs as a Coding Scheme</para>
                    <para>Coding Scheme</para>
                    <para>Application Context Name</para>
                    <para>Meta SOP Class</para>
                    <para>Service Class</para>
                    <para>Application Hosting Model</para>
                    <para>Mapping Resource</para>
                    <para>LDAP OID</para>
                    <para>Synchronization Frame of Reference</para>
                </book>""";
        var expected = List.of(
                UidType.SOPClass,
                UidType.TransferSyntax,
                UidType.SOPInstance,
                UidType.CodingScheme,
                UidType.CodingScheme,
                UidType.ApplicationContextName,
                UidType.MetaSOPClass,
                UidType.ServiceClass,
                UidType.ApplicationHostingModel,
                UidType.MappingResource,
                UidType.LdapOid,
                UidType.SynchronizationFrameOfReference
        );
        var doc = DocumentReader.readXmlString(xml);
        var xpath = XPathFactory.newInstance().newXPath();
        var list = (NodeList) xpath.compile("/book/para").evaluate(doc, XPathConstants.NODESET);
        assertNotNull(list);
        var n = list.getLength();
        assertEquals(expected.size(), n);
        var parser = new UidTypeParser();
        for (int i = 0; i < n; i++) {
            var node = list.item(i);
            assertEquals(Node.ELEMENT_NODE, node.getNodeType());
            var uidType = parser.parse((Element) node);
            assertEquals(expected.get(i), uidType);
        }
    }

    @Test
    void parseUnknownUidType() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        @Language("XML")
        String xml = """
                <book>
                    <para>Unknown</para>
                </book>""";
        var doc = DocumentReader.readXmlString(xml);
        var xpath = XPathFactory.newInstance().newXPath();
        var element = (Node) xpath.compile("/book/para").evaluate(doc, XPathConstants.NODE);
        assertNotNull(element);
        var parser = new UidTypeParser();
        assertThrows(ParserException.class, () -> parser.parse((Element) element));
    }
}