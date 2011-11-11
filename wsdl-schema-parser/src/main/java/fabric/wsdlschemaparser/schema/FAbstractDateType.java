package fabric.wsdlschemaparser.schema;

import java.util.Arrays;
import java.util.List;

import org.apache.xmlbeans.SchemaType;

public abstract class FAbstractDateType extends FSimpleType {

    public FAbstractDateType(String typeName) {
        super(typeName);
        addRestriction(SchemaType.FACET_WHITE_SPACE, SchemaType.WS_COLLAPSE);
    }

    @Override
    public List<Integer> getValidFacets() {

        return Arrays.asList(new Integer[] {
                SchemaType.FACET_ENUMERATION,
                SchemaType.FACET_MIN_INCLUSIVE,
                SchemaType.FACET_MIN_EXCLUSIVE,
                SchemaType.FACET_MAX_INCLUSIVE,
                SchemaType.FACET_MAX_EXCLUSIVE,
                SchemaType.FACET_WHITE_SPACE,
                SchemaType.FACET_PATTERN});
    }

}
