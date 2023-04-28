package tv.radiotherapy.tools.dicom.xml.parser;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.DocumentReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InnerTextTest {

    @Test
    void paraToString() throws ParserConfigurationException, IOException, SAXException {
        @Language("XML")
        String xml = """
                <book>
                    <para>
                        <emphasis>Length\u200bTo\u200bEnd</emphasis>
                    </para>
                </book>
                """;
        var doc = DocumentReader.readXmlString(xml);
        var root = doc.getDocumentElement();
        var list = root.getElementsByTagName("para");
        assertNotNull(list);
        assertEquals(1, list.getLength());
        var para = list.item(0);
        var value = InnerText.para(para);
        var expected = "LengthToEnd";
        assertEquals(expected, value);
    }

    @Test
    void paraToString2() throws ParserConfigurationException, IOException, SAXException {
        @Language("XML") String xml = """
                <book>
                    <copyright>
                        <para>
                            <year>2023</year>
                            <holder>ABC</holder>
                        </para>
                    </copyright>
                </book>
                               \s""";
        var doc = DocumentReader.readXmlString(xml);
        var root = doc.getDocumentElement();
        var list = root.getElementsByTagName("para");
        assertNotNull(list);
        assertEquals(1, list.getLength());
        var para = list.item(0);
        var value = InnerText.para(para);
        var expected = "2023\nABC";
        assertEquals(expected, value);
    }

    @Test
    void paraToString3() throws ParserConfigurationException, IOException, SAXException {
        @Language("XML") String xml = """
                <book>
                    <copyright>
                        <para>
                            <year>2023</year>
                            <holder> ABC</holder>
                        </para>
                    </copyright>
                </book>
                               \s""";
        var doc = DocumentReader.readXmlString(xml);
        var root = doc.getDocumentElement();
        var list = root.getElementsByTagName("para");
        assertNotNull(list);
        assertEquals(1, list.getLength());
        var para = list.item(0);
        var value = InnerText.para(para);
        var expected = "2023\nABC";
        assertEquals(expected, value);
    }

    @Test
    void innerText() throws ParserConfigurationException, IOException, SAXException {
        @Language("XML") String xml = """
                <book>
                    <copyright>
                        <para>
                            <year>2023</year>
                            <holder> ABC\t DEF</holder>
                        </para>
                    </copyright>
                </book>
                               \s""";
        var doc = DocumentReader.readXmlString(xml);
        var root = doc.getDocumentElement();
        var list = root.getElementsByTagName("para");
        assertNotNull(list);
        assertEquals(1, list.getLength());
        var para = list.item(0);
        var value = InnerText.get(para);
        var expected = "2023\nABC\t DEF";
        assertEquals(expected, value);
    }
}