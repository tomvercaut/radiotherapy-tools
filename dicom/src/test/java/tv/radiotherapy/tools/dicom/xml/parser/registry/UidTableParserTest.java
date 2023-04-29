package tv.radiotherapy.tools.dicom.xml.parser.registry;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.DocumentReader;
import tv.radiotherapy.tools.dicom.xml.model.OLink;
import tv.radiotherapy.tools.dicom.xml.model.registry.UidItem;
import tv.radiotherapy.tools.dicom.xml.model.UidType;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UidTableParserTest {

    @Test
    void parse() throws ParserConfigurationException, IOException, SAXException, ParserException, XPathExpressionException {
        var classLoader = getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream("dicom_standard/part06/part06.xml");
        assertNotNull(inputStream);
        var doc = DocumentReader.read(inputStream);
        var root = doc.getDocumentElement();
        var registry = new UidTableParser().parse(root, new UidTableRowParser());
        assertNotNull(registry);

        var expected = List.of(
                new UidItem("1.2.840.10008.1.1", "Verification SOP Class", "Verification", UidType.SOPClass, new OLink("PS3.4", "PS3.4", "select: labelnumber")
                ),
                new UidItem("1.2.840.10008.1.2", "Implicit VR Little Endian: Default Transfer Syntax for DICOM", "ImplicitVRLittleEndian", UidType.TransferSyntax, new OLink("PS3.5", "PS3.5", "select: labelnumber")
                ),
                new UidItem("1.2.840.10008.1.20.1.1", "Storage Commitment Push Model SOP Instance", "StorageCommitmentPushModelInstance", UidType.SOPInstance, new OLink("PS3.4", "PS3.4", "select: labelnumber")
                ),
                new UidItem("1.2.840.10008.2.6.1", "DICOM UID Registry", "DCMUID", UidType.CodingScheme, new OLink("PS3.6", "PS3.6", "select: labelnumber")
                ),
                new UidItem("1.2.840.10008.2.16.4", "DICOM Controlled Terminology", "DCM", UidType.CodingScheme, new OLink("PS3.16", "PS3.16", "select: labelnumber")),
                new UidItem("1.2.840.10008.3.1.1.1", "DICOM Application Context Name", "DICOMApplicationContext", UidType.ApplicationContextName, new OLink("PS3.7", "PS3.7", "select: labelnumber")),
                new UidItem("1.2.840.10008.3.1.2.1.4", "Detached Patient Management Meta SOP Class (Retired)", "DetachedPatientManagementMeta", UidType.MetaSOPClass, new OLink("PS3.4", "PS3.4", "select: labelnumber")),
                new UidItem("1.2.840.10008.4.2", "Storage Service Class", "Storage", UidType.ServiceClass, new OLink("PS3.4", "PS3.4", "select: labelnumber")),
                new UidItem("1.2.840.10008.7.1.1", "Native DICOM Model", "NativeDICOMModel", UidType.ApplicationHostingModel, new OLink("PS3.19", "PS3.19", "select: labelnumber")),
                new UidItem("1.2.840.10008.8.1.1", "DICOM Content Mapping Resource", "DICOMContentMappingResource", UidType.MappingResource, new OLink("PS3.16", "PS3.16", "select: labelnumber")),
                new UidItem("1.2.840.10008.15.0.3.1", "dicomDeviceName", "dicomDeviceName", UidType.LdapOid, new OLink("PS3.15", "PS3.15", "select: labelnumber")),
                new UidItem("1.2.840.10008.15.1.1", "Universal Coordinated Time", "UTC", UidType.SynchronizationFrameOfReference, new OLink("PS3.3", "PS3.3", "select: labelnumber"))
        );

        for (UidItem item : expected) {
            assertTrue(registry.contains(item));
        }
    }
}