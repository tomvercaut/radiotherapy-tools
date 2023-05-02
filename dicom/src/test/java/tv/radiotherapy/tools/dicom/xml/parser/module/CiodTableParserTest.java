package tv.radiotherapy.tools.dicom.xml.parser.module;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import tv.radiotherapy.tools.dicom.xml.SharedResources;
import tv.radiotherapy.tools.dicom.xml.XRef;
import tv.radiotherapy.tools.dicom.xml.model.Usage;
import tv.radiotherapy.tools.dicom.xml.model.module.Ciod;
import tv.radiotherapy.tools.dicom.xml.model.module.CiodItem;
import tv.radiotherapy.tools.dicom.xml.model.module.ciod.IodModule;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;
import tv.radiotherapy.tools.dicom.xml.parser.TableHelper;
import tv.radiotherapy.tools.dicom.xml.parser.XmlHelper;

import javax.xml.xpath.XPathExpressionException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CiodTableParserTest {

    @Test
    void parse() throws ExecutionException, InterruptedException, ParserException, XPathExpressionException {
        var doc = SharedResources.getInstance().getPart03();
        assertNotNull(doc);
        var root = doc.getDocumentElement();
        var chapterA = (Element) XmlHelper.findById(root, "chapter_A");
        var tableA2_1Element = (Element) TableHelper.findById(chapterA, "table_A.2-1");
        var tableParser = new CiodTableParser();
        var ciod = tableParser.parse(tableA2_1Element, new CiodTableRowParser());
        assertEquals("Computed Radiography Image", ciod.name());
        var eciod = new Ciod("table_A.2-1","Computed Radiography Image", List.of(
                new CiodItem(
                        "Patient", List.of(
                                new IodModule("Patient", new XRef("sect_C.7.1.1", "select: labelnumber"), Usage.M),
                                new IodModule("Clinical Trial Subject", new XRef("sect_C.7.1.3", "select: labelnumber"), Usage.U)
                )),
                new CiodItem(
                        "Study", List.of(
                        new IodModule("General Study", new XRef("sect_C.7.2.1", "select: labelnumber"), Usage.M),
                        new IodModule("Patient Study", new XRef("sect_C.7.2.2", "select: labelnumber"), Usage.U),
                        new IodModule("Clinical Trial Study", new XRef("sect_C.7.2.3", "select: labelnumber"), Usage.U)
                )),
                new CiodItem(
                        "Series", List.of(
                        new IodModule("General Series", new XRef("sect_C.7.3.1", "select: labelnumber"), Usage.M),
                        new IodModule("CR Series", new XRef("sect_C.8.1.1", "select: labelnumber"), Usage.M),
                        new IodModule("Clinical Trial Series", new XRef("sect_C.7.3.2", "select: labelnumber"), Usage.U)
                )),
                new CiodItem(
                        "Equipment", List.of(
                        new IodModule("General Equipment", new XRef("sect_C.7.5.1", "select: labelnumber"), Usage.M)
                )),
                new CiodItem(
                        "Acquisition", List.of(
                        new IodModule("General Acquisition", new XRef("sect_C.7.10.1", "select: labelnumber"), Usage.M)
                )),
                new CiodItem(
                        "Image", List.of(
                        new IodModule("General Image", new XRef("sect_C.7.6.1", "select: labelnumber"), Usage.M),
                        new IodModule("General Reference", new XRef("sect_C.12.4", "select: labelnumber"), Usage.U),
                        new IodModule("Image Pixel", new XRef("sect_C.7.6.3", "select: labelnumber"), Usage.M),
                        new IodModule("Contrast/Bolus", new XRef("sect_C.7.6.4", "select: labelnumber"), Usage.C),
                        new IodModule("Display Shutter", new XRef("sect_C.7.6.11", "select: labelnumber"), Usage.U),
                        new IodModule("Device", new XRef("sect_C.7.6.12", "select: labelnumber"), Usage.U),
                        new IodModule("Specimen", new XRef("sect_C.7.6.22", "select: labelnumber"), Usage.U),
                        new IodModule("CR Image", new XRef("sect_C.8.1.2", "select: labelnumber"), Usage.M),
                        new IodModule("Overlay Plane", new XRef("sect_C.9.2", "select: labelnumber"), Usage.U),
                        new IodModule("Modality LUT", new XRef("sect_C.11.1", "select: labelnumber"), Usage.U),
                        new IodModule("VOI LUT", new XRef("sect_C.11.2", "select: labelnumber"), Usage.U),
                        new IodModule("SOP Common", new XRef("sect_C.12.1", "select: labelnumber"), Usage.M),
                        new IodModule("Common Instance Reference", new XRef("sect_C.12.2", "select: labelnumber"), Usage.U)
                )
                )
        ));
        assertEquals(eciod, ciod);
    }
}