package tv.radiotherapy.tools.dicom.xml.parser.module;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tv.radiotherapy.tools.dicom.xml.model.module.Ciod;
import tv.radiotherapy.tools.dicom.xml.parser.Parser;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;
import tv.radiotherapy.tools.dicom.xml.parser.XmlHelper;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

public class CiodTablesParser implements Parser<List<Ciod>> {
    private static final Logger logger = LogManager.getLogger(CiodTablesParser.class);

    @Override
    public List<Ciod> parse(@NotNull Element element) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException {
        final var chapterA = (Element) XmlHelper.findById(element, "chapter_A");
        final var tables = (NodeList) XmlHelper.findTables(chapterA);
        final int ntables = tables.getLength();
        if (ntables == 0) {
            throw new ParserException("Failed to find any CIOD tables, something went wrong.");
        }

        final List<Ciod> list = new ArrayList<>();
        final var tableParser = new CiodTableParser();
        final var ciodRowParser = new CiodTableRowParser();
        for (int i = 0; i < ntables; i++) {
            final var table = tables.item(i);
            try {
                final var ciod = tableParser.parse((Element) table, ciodRowParser);
                if (ciod!=null) list.add(ciod);
            } catch (ParserException ex) {
                final var et = (Element) table;
                var id = et.getAttribute("xml:id");
                logger.warn("Table [" + id + "] could not be converted to a CIOD.");
            }
        }
        return list;
    }
}
