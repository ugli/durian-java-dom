package se.ugli.durian.j.dom.mutable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import se.ugli.durian.j.dom.node.Attribute;
import se.ugli.durian.j.dom.node.Content;
import se.ugli.durian.j.dom.node.NodeFactory;
import se.ugli.durian.j.dom.node.Text;

public abstract class AbstractElement implements MutableElement {

    private MutableElement parent;
    private final String name;
    private final String uri;
    private final NodeFactory nodeFactory;

    public AbstractElement(final String name, final String uri, final NodeFactory nodeFactory) {
        this.name = name;
        this.uri = uri;
        this.nodeFactory = nodeFactory;
    }

    @Override
    public MutableElement getParent() {
        return parent;
    }

    @Override
    public boolean isSimpleTextNode() {
        return getTexts().size() == 1 && getContent().size() == 1;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public String getAttributeValue(final String attributeName) {
        final Attribute attribute = getAttribute(attributeName);
        if (attribute != null) {
            return attribute.getValue();
        }
        return null;
    }

    @Override
    public Attribute getAttribute(final String attributeName) {
        for (final Attribute attribute : getAttributes()) {
            if (attribute.getName().equals(attributeName)) {
                return attribute;
            }
        }
        return null;
    }

    @Override
    public void setAttributeValue(final String attributeName, final String value) {
        final Attribute attribute = getAttribute(attributeName);
        if (attribute != null) {
            attribute.setValue(value);
        }
        else {
            getAttributes().add(nodeFactory.createAttribute(attributeName, uri, this, value));
        }
    }

    @Override
    public List<MutableElement> getElements(final String elementName) {
        final List<MutableElement> result = new ArrayList<MutableElement>();
        for (final MutableElement element : getElements()) {
            if (element.getName().equals(elementName)) {
                result.add(element);
            }
        }
        return Collections.unmodifiableList(result);
    }

    @SuppressWarnings("unchecked")
    public <T extends MutableElement> List<T> getTypedElements(final String elementName) {
        return (List<T>) getElements(elementName);
    }

    @Override
    public MutableElement getElement(final String elementName) {
        for (final MutableElement element : getElements()) {
            if (element.getName().equals(elementName)) {
                return element;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends MutableElement> T getTypedElement(final String elementName) {
        return (T) getElement(elementName);
    }

    public void setElement(final MutableElement element) {
        if (element != null) {
            final MutableElement element2 = getElement(element.getName());
            if (element2 != null) {
                getElements().remove(element2);
            }
            getElements().add(element);
        }
    }

    public MutableElement clone(final String elementName) {
        final MutableElement element = (MutableElement) nodeFactory.createElement(elementName, uri, null);
        for (final Attribute attribute : getAttributes()) {
            element.getAttributes()
                    .add(nodeFactory.createAttribute(attribute.getName(), attribute.getUri(), element,
                            attribute.getValue()));
        }
        for (final Content content : getContent()) {
            if (content instanceof Text) {
                final Text text = (Text) content;
                element.getTexts().add(nodeFactory.createText(element, text.getValue()));
            }
            else if (content instanceof MutableElement) {
                final MutableElement child = (MutableElement) content;
                element.getElements().add(child.clone());
            }
        }
        return element;
    }

    @Override
    public MutableElement clone() {
        return clone(name);
    }

    @Override
    public void setParent(final MutableElement parent) {
        this.parent = parent;
    }

    @Override
    public String getPath() {
        final String elementPath = "/" + name;
        if (parent == null) {
            return elementPath;
        }
        return parent.getPath() + elementPath;
    }

    @Override
    public String getPath(final String childPath) {
        if (childPath.startsWith("/")) {
            return getPath() + childPath;
        }
        return getPath() + "/" + childPath;
    }

    @Override
    public String getRelativePath(final String childPath) {
        if (childPath.startsWith("/")) {
            return name + childPath;
        }
        return name + "/" + childPath;
    }

    @Override
    public Set<String> getUriSet() {
        final Set<String> result = new LinkedHashSet<String>();
        if (uri != null) {
            result.add(uri);
        }
        for (final MutableElement element : getElements()) {
            result.addAll(element.getUriSet());
        }
        return result;
    }

}
