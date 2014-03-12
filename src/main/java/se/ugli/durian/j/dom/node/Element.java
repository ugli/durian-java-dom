package se.ugli.durian.j.dom.node;

import java.util.List;
import java.util.Set;

public interface Element extends Content {

    Element clone();

    Attribute getAttribute(String attributeName);

    Set<Attribute> getAttributes();

    String getAttributeValue(String attributeName);

    Element getElement(String elementName);

    List<? extends Element> getElements();

    List<? extends Element> getElements(String elementName);

    String getName();

    String getPath(String childPath);

    String getRelativePath(String childPath);

    List<Text> getTexts();

    String getUri();

    Set<String> getUriSet();

    boolean isSimpleTextNode();

}
