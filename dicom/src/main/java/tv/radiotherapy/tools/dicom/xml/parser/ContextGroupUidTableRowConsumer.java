package tv.radiotherapy.tools.dicom.xml.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import tv.radiotherapy.tools.dicom.xml.model.registry.ContextGroupUid;
import tv.radiotherapy.tools.dicom.xml.parser.registry.ContextGroupUidTableParser;
import tv.radiotherapy.tools.dicom.xml.parser.registry.ContextGroupUidTableRowParser;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ContextGroupUidTableRowConsumer implements ElementConsumer<ContextGroupUid> {
    private static final Logger logger = LogManager.getLogger(CiodTableConsumer.class);
    private final Queue<Element> queue = new LinkedList<>();
    private final ContextGroupUidTableParser tableParser = new ContextGroupUidTableParser();
    private final ContextGroupUidTableRowParser rowParser = new ContextGroupUidTableRowParser();
    private final List<ContextGroupUid> cguids = new ArrayList<>();
    private final List<Throwable> throwables = new ArrayList<>();

    @Override
    @NotNull
    public List<ContextGroupUid> getResults() {
        return cguids;
    }

    @Override
    public void add(@NotNull Element element) {
        queue.add(element);
    }

    @Override
    public void clearResults() {
        cguids.clear();
    }

    public List<Throwable> getErrors() {
        return throwables;
    }

    @Override
    public boolean hasErrors() {
        return !throwables.isEmpty();
    }

    @Override
    public void run() {
        Element row;
        while ((row = queue.poll()) != null) {
            try {
                var opt = rowParser.parseRow(row);
                opt.ifPresent(cguids::add);
            } catch (XPathExpressionException | ParserException e) {
                throwables.add(e);
            }
        }
    }
}
