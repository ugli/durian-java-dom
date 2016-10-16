package se.ugli.durian.j.dom.mutable;

import java.util.Optional;

import se.ugli.durian.j.dom.node.Element;
import se.ugli.durian.j.dom.node.NodeFactory;
import se.ugli.durian.j.dom.node.Text;
import se.ugli.durian.j.dom.node.TextCloneApi;
import se.ugli.durian.j.dom.utils.Id;

public class MutableText implements Text, MutableNode {

    private Optional<Element> parent = Optional.empty();
    private final String value;
    private final String id = Id.create();
    private final NodeFactory nodeFactory;

    @Override
    public String id() {
        return id;
    }

    public MutableText(final String value, final NodeFactory nodeFactory) {
        this.value = value;
        this.nodeFactory = nodeFactory;
    }

    @Override
    public TextCloneApi clone() {
        return new MutableTextCloneApiImpl(this);
    }

    @Override
    public NodeFactory nodeFactory() {
        return nodeFactory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Element> Optional<T> getParent() {
        return (Optional<T>) parent;
    }

    @Override
    public String getPath() {
        final String selfPath = "/text()";
        if (parent.isPresent())
            return parent.get().getPath() + selfPath;
        return selfPath;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setParent(final Element parent) {
        this.parent = Optional.ofNullable(parent);
    }

    @Override
    public String toString() {
        return "MutableText [value=" + value + "]";
    }

}
