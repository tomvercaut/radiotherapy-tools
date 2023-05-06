package tv.radiotherapy.tools.dicom.xml.parser.registry;

import lombok.extern.log4j.Log4j2;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.DocumentReader;
import tv.radiotherapy.tools.dicom.xml.model.OLink;
import tv.radiotherapy.tools.dicom.xml.model.UidType;
import tv.radiotherapy.tools.dicom.xml.model.registry.Uid;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
class UidTableParserTest {

    private static String getXml() {
        @Language("XML")
        String xml = """
                <book>
                                    <table frame="box" label="A-1" rules="all" xml:id="table_A-1">
                                        <caption>UID Values</caption>
                                        <thead>
                                            <tr valign="top">
                                                <th align="center" colspan="1" rowspan="1">
                                                    <para xml:id="para_89103697-fd54-4c3f-abbe-5655ae35308b">
                                                        <emphasis role="bold">UID Value</emphasis>
                                                    </para>
                                                </th>
                                                <th align="center" colspan="1" rowspan="1">
                                                    <para xml:id="para_cb5b6b8a-9aec-44f2-8cdc-800a7cd327b7">
                                                        <emphasis role="bold">UID Name</emphasis>
                                                    </para>
                                                </th>
                                                <th align="center" colspan="1" rowspan="1">
                                                    <para xml:id="para_013510c8-4f44-4131-9319-6e72c777418c">
                                                        <emphasis role="bold">UID ​Keyword</emphasis>
                                                    </para>
                                                </th>
                                                <th align="center" colspan="1" rowspan="1">
                                                    <para xml:id="para_08eb5599-7f50-4d17-bd10-6617063c6e31">
                                                        <emphasis role="bold">UID Type</emphasis>
                                                    </para>
                                                </th>
                                                <th align="center" colspan="1" rowspan="1">
                                                    <para xml:id="para_42e221e9-efb2-49d7-8712-69266c9bc2d4">
                                                        <emphasis role="bold">Part</emphasis>
                                                    </para>
                                                </th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr valign="top">
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_de68fe97-b7cf-497e-af8f-a653669ac4e4">1.2.840.10008.1.​1</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_935df29c-2e6c-4d50-aa93-83225337b0ba">Verification SOP Class</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_5b280f39-109f-416f-bbc7-fb65409d9574">Verification</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_5e8dcea9-7c49-4751-b561-ca8a41698555">SOP Class</para>
                                                </td>
                                                <td align="center" colspan="1" rowspan="1">
                                                    <para xml:id="para_d37461b4-5537-4bf6-98b1-ded888b9b028">
                                                        <olink targetdoc="PS3.4" targetptr="PS3.4" xrefstyle="select: labelnumber"/>
                                                    </para>
                                                </td>
                                            </tr>
                                            <tr valign="top">
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_893becfb-16cd-4300-923d-e422781af211">1.2.840.10008.1.​2</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_ccbbb435-5d2e-4d4d-b2c2-a27a5a762f75">Implicit VR Little Endian: Default Transfer
                                                        Syntax for DICOM
                                                    </para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_6871172d-db56-4b45-b5d5-446f82a22c89">Implicit​VR​Little​Endian</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_6e075f3d-2392-4c57-908c-889b50ebcbcd">Transfer Syntax</para>
                                                </td>
                                                <td align="center" colspan="1" rowspan="1">
                                                    <para xml:id="para_c2a11bee-08cc-422f-a64c-01dcb4baf73b">
                                                        <olink targetdoc="PS3.5" targetptr="PS3.5" xrefstyle="select: labelnumber"/>
                                                    </para>
                                                </td>
                                            </tr>
                                            <tr valign="top">
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_ca7e6d0e-2ca6-487a-9d7a-58b1d924e8ec">1.2.840.10008.1.20.​1.​1</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_9c02cc31-08eb-4068-b742-fbdf59835e71">Storage Commitment Push Model SOP
                                                        Instance
                                                    </para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_8f31cc1c-c9f0-48da-8e8e-d301a8d775a1">Storage​Commitment​Push​Model​Instance
                                                    </para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_96c85178-928e-4c8b-8772-aed6fc340eb9">Well-known SOP Instance</para>
                                                </td>
                                                <td align="center" colspan="1" rowspan="1">
                                                    <para xml:id="para_d4654923-2dac-4f86-b7c3-0da5d2e00264">
                                                        <olink targetdoc="PS3.4" targetptr="PS3.4" xrefstyle="select: labelnumber"/>
                                                    </para>
                                                </td>
                                            </tr>
                                            <tr valign="top">
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_27db0a04-5ad3-4473-aefe-4fb25d82dcaa">1.2.840.10008.2.​6.​1</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_fae2c8a7-1dad-406b-90a2-b5b607901291">DICOM UID Registry</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_f4de83fd-a028-4051-8f24-ab8c8c683b4b">DCMUID</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_c02ce835-b1ad-4b06-8489-7e4d2507666e">DICOM UIDs as a Coding Scheme</para>
                                                </td>
                                                <td align="center" colspan="1" rowspan="1">
                                                    <para xml:id="para_49f92520-64ca-47eb-8609-8e1fbd68c2e0">
                                                        <olink targetdoc="PS3.6" targetptr="PS3.6" xrefstyle="select: labelnumber"/>
                                                    </para>
                                                </td>
                                            </tr>
                                            <tr valign="top">
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_734e46fb-610b-4026-8a01-73e680be9d4a">1.2.840.10008.2.​16.​4</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_d0b3a7d1-9a8b-47a6-a221-df20daacdfd3">DICOM Controlled Terminology</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_e34bb57e-bccf-4f65-a4e5-a92f11cf6223">DCM</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_d7116490-1d2e-47cd-a008-fdeafcac2f73">Coding Scheme</para>
                                                </td>
                                                <td align="center" colspan="1" rowspan="1">
                                                    <para xml:id="para_4b5d5e2e-18ab-42c1-9210-80584cf9635c">
                                                        <olink targetdoc="PS3.16" targetptr="PS3.16" xrefstyle="select: labelnumber"/>
                                                    </para>
                                                </td>
                                            </tr>
                                            <tr valign="top">
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_30349e28-c73f-4497-a00a-90da1dd253d5">1.2.840.10008.3.1.​1.​1</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_b25f6223-f5e4-47ba-8553-4bb3ab4c8218">DICOM Application Context Name</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_1a199c1b-aa5c-4286-b58d-7f54f900c2a6">DICOM​Application​Context</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_bc9568f3-c8b4-4cc7-ae2b-8dad3cf5bfec">Application Context Name</para>
                                                </td>
                                                <td align="center" colspan="1" rowspan="1">
                                                    <para xml:id="para_590c0d98-c535-49cf-8e88-143a57d3c48a">
                                                        <olink targetdoc="PS3.7" targetptr="PS3.7" xrefstyle="select: labelnumber"/>
                                                    </para>
                                                </td>
                                            </tr>
                                            <tr valign="top">
                                    <td align="left" colspan="1" rowspan="1">
                                        <para xml:id="para_88878ef5-39d2-4480-9804-14c8b226a58c">
                                            <emphasis role="italic">1.2.840.10008.3.1.2.​1.​4</emphasis>
                                        </para>
                                    </td>
                                    <td align="left" colspan="1" rowspan="1">
                                        <para xml:id="para_301b0fb4-b15f-4106-a50d-7e4144e16728">
                                            <emphasis role="italic">Detached Patient Management Meta SOP Class (Retired)</emphasis>
                                        </para>
                                    </td>
                                    <td align="left" colspan="1" rowspan="1">
                                        <para xml:id="para_ae6b5441-8309-4f30-8ba9-7264ae990d54">
                <emphasis role="italic">Detached​Patient​Management​Meta</emphasis>
                </para>
                                    </td>
                                    <td align="left" colspan="1" rowspan="1">
                                        <para xml:id="para_7a5f2259-0968-492f-b510-3dabec76c0f7">
                                            <emphasis role="italic">Meta SOP Class</emphasis>
                                        </para>
                                    </td>
                                    <td align="center" colspan="1" rowspan="1">
                                        <para xml:id="para_d560ee08-3cb5-44f1-ab76-a9654b893983">
                                            <emphasis role="italic">
                                                <olink targetdoc="PS3.4" targetptr="PS3.4" xrefstyle="select: labelnumber"/> (2004)</emphasis>
                                        </para>
                                    </td>
                                </tr>
                                            <tr valign="top">
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_cfeb6a87-e320-4e14-b07b-5b6494274c6d">1.2.840.10008.4.​2</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_c593cf1b-675e-419d-9816-589962470830">Storage Service Class</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_6bf63535-f24d-4bb9-afd1-8a201c1c95fb">Storage</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_a8561ab0-7c37-4975-b1b3-3c6862305964">Service Class</para>
                                                </td>
                                                <td align="center" colspan="1" rowspan="1">
                                                    <para xml:id="para_018b82db-2e63-4826-b61b-2cbedc0305d6">
                                                        <olink targetdoc="PS3.4" targetptr="PS3.4" xrefstyle="select: labelnumber"/>
                                                    </para>
                                                </td>
                                            </tr>
                                            <tr valign="top">
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_03ec0f31-5a7e-4c2e-86da-da85ea737341">1.2.840.10008.7.​1.​1</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_abbb0fbe-88e9-40e2-a0d8-c25a995bcbe7">Native DICOM Model</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_756ea077-2846-427e-8830-92a16e3607fc">Native​DICOM​Model</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_0d36980a-809a-4b46-a047-264cb6933ce9">Application Hosting Model</para>
                                                </td>
                                                <td align="center" colspan="1" rowspan="1">
                                                    <para xml:id="para_6f673d55-dd95-4801-b8f6-9c5e541ef05d">
                                                        <olink targetdoc="PS3.19" targetptr="PS3.19" xrefstyle="select: labelnumber"/>
                                                    </para>
                                                </td>
                                            </tr>
                                            <tr valign="top">
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_6eb563d0-4428-490a-bb34-4a509907f2ad">1.2.840.10008.8.​1.​1</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_d951a83b-b765-4173-8c5e-93c703ef5d8c">DICOM Content Mapping Resource</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_2c24c9fa-af7f-44ef-81f0-87ad940ca477">DICOM​Content​Mapping​Resource</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_39fcace1-b523-4b6f-a71e-4fb3c0992ecc">Mapping Resource</para>
                                                </td>
                                                <td align="center" colspan="1" rowspan="1">
                                                    <para xml:id="para_9e95f7d2-d2fe-43e3-a065-cc54163d4504">
                                                        <olink targetdoc="PS3.16" targetptr="PS3.16" xrefstyle="select: labelnumber"/>
                                                    </para>
                                                </td>
                                            </tr>
                                            <tr valign="top">
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_449bc9dc-19a2-4a59-868f-d46c45196e00">1.2.840.10008.15.0.​3.​1</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_9f5385f1-3231-4417-a6b8-0c02136705ac">dicom​Device​Name</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_40b4c3b2-2482-470f-94c1-e8a9f128abee">dicom​Device​Name</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_a37babf7-b8aa-4e8c-bfdd-f2083aa99b82">LDAP OID</para>
                                                </td>
                                                <td align="center" colspan="1" rowspan="1">
                                                    <para xml:id="para_94643b85-29b6-4fc1-93c5-8d015bbf754b">
                                                        <olink targetdoc="PS3.15" targetptr="PS3.15" xrefstyle="select: labelnumber"/>
                                                    </para>
                                                </td>
                                            </tr>
                                            <tr valign="top">
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_d9dca516-8da1-4c7c-9a07-b80d4035cc45">1.2.840.10008.15.​1.​1</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_6bd8b3fa-d1b4-466f-b8c7-f25d80acb531">Universal Coordinated Time</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_9434b170-d092-473e-8eba-df87641e65e1">UTC</para>
                                                </td>
                                                <td align="left" colspan="1" rowspan="1">
                                                    <para xml:id="para_7ec0aa3d-8e4a-4199-90bd-854656646178">Synchronization Frame of Reference</para>
                                                </td>
                                                <td align="center" colspan="1" rowspan="1">
                                                    <para xml:id="para_740a389d-ebfc-415c-89e9-14eca0b20b58">
                                                        <olink targetdoc="PS3.3" targetptr="PS3.3" xrefstyle="select: labelnumber"/>
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
        var registry = new UidTableParser().parse(root, new UidTableRowParser());
        assertNotNull(registry);

        var expected = List.of(
                new Uid("1.2.840.10008.1.1", "Verification SOP Class", "Verification", UidType.SOPClass, new OLink("PS3.4", "PS3.4", "select: labelnumber")
                ),
                new Uid("1.2.840.10008.1.2", "Implicit VR Little Endian: Default Transfer\nSyntax for DICOM", "ImplicitVRLittleEndian", UidType.TransferSyntax, new OLink("PS3.5", "PS3.5", "select: labelnumber")
                ),
                new Uid("1.2.840.10008.1.20.1.1", "Storage Commitment Push Model SOP\nInstance", "StorageCommitmentPushModelInstance", UidType.SOPInstance, new OLink("PS3.4", "PS3.4", "select: labelnumber")
                ),
                new Uid("1.2.840.10008.2.6.1", "DICOM UID Registry", "DCMUID", UidType.CodingScheme, new OLink("PS3.6", "PS3.6", "select: labelnumber")
                ),
                new Uid("1.2.840.10008.2.16.4", "DICOM Controlled Terminology", "DCM", UidType.CodingScheme, new OLink("PS3.16", "PS3.16", "select: labelnumber")),
                new Uid("1.2.840.10008.3.1.1.1", "DICOM Application Context Name", "DICOMApplicationContext", UidType.ApplicationContextName, new OLink("PS3.7", "PS3.7", "select: labelnumber")),
                new Uid("1.2.840.10008.3.1.2.1.4", "Detached Patient Management Meta SOP Class (Retired)", "DetachedPatientManagementMeta", UidType.MetaSOPClass, new OLink("PS3.4", "PS3.4", "select: labelnumber")),
                new Uid("1.2.840.10008.4.2", "Storage Service Class", "Storage", UidType.ServiceClass, new OLink("PS3.4", "PS3.4", "select: labelnumber")),
                new Uid("1.2.840.10008.7.1.1", "Native DICOM Model", "NativeDICOMModel", UidType.ApplicationHostingModel, new OLink("PS3.19", "PS3.19", "select: labelnumber")),
                new Uid("1.2.840.10008.8.1.1", "DICOM Content Mapping Resource", "DICOMContentMappingResource", UidType.MappingResource, new OLink("PS3.16", "PS3.16", "select: labelnumber")),
                new Uid("1.2.840.10008.15.0.3.1", "dicomDeviceName", "dicomDeviceName", UidType.LdapOid, new OLink("PS3.15", "PS3.15", "select: labelnumber")),
                new Uid("1.2.840.10008.15.1.1", "Universal Coordinated Time", "UTC", UidType.SynchronizationFrameOfReference, new OLink("PS3.3", "PS3.3", "select: labelnumber"))
        );

        for (var item : expected) {
            assertTrue(registry.contains(item));
        }
    }
}