package se.ugli.durian.j.fpd;

import java.util.ArrayList;
import java.util.List;

import se.ugli.durian.j.dom.mutable.MutableElement;
import se.ugli.durian.j.dom.mutable.MutableNodeFactory;
import se.ugli.durian.j.dom.node.Element;
import se.ugli.durian.j.dom.node.Node;
import se.ugli.durian.j.dom.node.Prefixmapping;

class Struct implements Definition {

    static Struct apply(final Element element, final String targetNamespace, final boolean includeEmptyValues) {
        return new Struct(element, targetNamespace, includeEmptyValues, true);
    }

    private final String name;
    private final String targetNamespace;
    private final boolean includeEmptyValues;
    private final boolean root;
    private final List<Definition> definitions = new ArrayList<Definition>();
    private final int numOfChars;

    private Struct(final Element element, final String targetNamespace, final boolean includeEmptyValues, final boolean root) {
        name = element.getAttributeValue("name");
        this.targetNamespace = targetNamespace;
        this.includeEmptyValues = includeEmptyValues;
        this.root = root;
        int _numOfChars = 0;
        for (final Element e : element.getElements()) {
            final Definition definition = definition(e);
            definitions.add(definition);
            _numOfChars += definition.numOfChars();
        }
        numOfChars = _numOfChars;
    }

    @Override
    public Node createNode(final String data) {
        final ArrayList<Prefixmapping> prefixmappings = new ArrayList<Prefixmapping>();
        if (root && targetNamespace != null)
            prefixmappings.add(new Prefixmapping(null, targetNamespace));
        final MutableElement element = new MutableElement(name, targetNamespace, new MutableNodeFactory(), prefixmappings);
        int beginIndex = 0;
        for (final Definition definition : definitions) {
            final int endIndex = beginIndex + definition.numOfChars();
            final Node node = definition.createNode(data.substring(beginIndex, endIndex));
            element.add(node);
            beginIndex = endIndex;
        }
        return element;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int numOfChars() {
        return numOfChars;
    }

    @Override
    public String toString() {
        return "Struct [name=" + name + ", targetNamespace=" + targetNamespace + ", includeEmptyValues=" + includeEmptyValues + ", root="
                + root + ", definitions=" + definitions + ", numOfChars=" + numOfChars + "]";
    }

    private Definition definition(final Element element) {
        final String type = element.getAttributeValue("type");
        if ("Struct".equals(type))
            return new Struct(element, targetNamespace, includeEmptyValues, false);
        if ("Array".equals(type))
            return new Array(element, targetNamespace, includeEmptyValues);
        if ("Field".equals(type))
            return new Field(element, targetNamespace, includeEmptyValues);
        throw new IllegalStateException();
    }

}