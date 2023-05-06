package tv.radiotherapy.tools.dicom.xml.parser;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.util.List;

public interface ElementConsumer<T> extends Runnable {

    @NotNull
    List<T> getResults();

    public void add(@NotNull Element element);

    public void clearResults();

    public boolean hasErrors();
}
