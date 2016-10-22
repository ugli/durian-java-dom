package se.ugli.durian.j.fpd;

import static se.ugli.durian.j.schema.SchemaType.W3C_XML_SCHEMA;
import static se.ugli.durian.j.validation.Validator.validator;

import se.ugli.durian.Durian;
import se.ugli.durian.Source;
import se.ugli.durian.j.dom.node.Element;
import se.ugli.durian.j.validation.Validator;

public class FpdParser {

    private static final Validator validator = validator(W3C_XML_SCHEMA, FpdParser.class.getResource("/durian/xsd/fpd.xsd"));

    public static FpdParser apply(final byte[] defBytes) {
        return new FpdParser(defBytes);
    }

    private final Struct struct;
    private final String targetNamespace;

    private FpdParser(final byte[] definition) {
        validator.validate(definition);
        final Element element = Durian.parseXml(Source.apply(definition));
        final boolean includeEmptyValues = Boolean.parseBoolean(element.attributeValue("includeEmptyValues").get());
        targetNamespace = element.attributeValue("targetNamespace").orElse(null);
        struct = Struct.apply(element, targetNamespace, includeEmptyValues);
    }

    public Element parse(final String data) {
        return struct.createNode(data).as(Element.class);
    }

}