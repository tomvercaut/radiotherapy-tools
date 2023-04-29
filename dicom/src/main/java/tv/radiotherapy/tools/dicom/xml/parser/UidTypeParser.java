package tv.radiotherapy.tools.dicom.xml.parser;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import tv.radiotherapy.tools.dicom.xml.model.UidType;

import javax.xml.xpath.XPathExpressionException;

import static org.apache.logging.log4j.util.Strings.quote;

public class UidTypeParser implements Parser<UidType> {
    @Override
    public UidType parse(@NotNull Element element) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException {
        var value = InnerText.get(element);
        if (value.equals("SOP Class")) {
            return UidType.SOPClass;
        } else if (value.equals("Transfer Syntax")) {
            return UidType.TransferSyntax;
        } else if (value.equals("Well-known SOP Instance")) {
            return UidType.SOPInstance;
        } else if (value.contains("Coding Scheme")) {
            return UidType.CodingScheme;
        } else if (value.equals("Application Context Name")) {
            return UidType.ApplicationContextName;
        } else if (value.equals("Meta SOP Class")) {
            return UidType.MetaSOPClass;
        } else if (value.equals("Service Class")) {
            return UidType.ServiceClass;
        } else if (value.equals("Application Hosting Model")) {
            return UidType.ApplicationHostingModel;
        } else if (value.equals("Mapping Resource")) {
            return UidType.MappingResource;
        } else if (value.equals("LDAP OID")) {
            return UidType.LdapOid;
        } else if (value.equals("Synchronization Frame of Reference")) {
            return UidType.SynchronizationFrameOfReference;
        } else if (value.equals("Document TemplateID")) {
            return UidType.DocumentTemplateId;
        } else if (value.equals("Section TemplateID")) {
            return UidType.SectionTemplateId;
        } else if (value.equals("Entry TemplateID")) {
            return UidType.EntryTemplateId;
        } else if (value.equals("Element Set TemplateID")) {
            return UidType.ElementSetTemplateId;
        } else {
            throw new ParserException("Unknown UID type: " + quote(value));
        }
    }
}
