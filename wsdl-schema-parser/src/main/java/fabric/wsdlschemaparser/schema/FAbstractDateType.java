package fabric.wsdlschemaparser.schema;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.apache.xmlbeans.SchemaType;

public abstract class FAbstractDateType extends FSimpleType {

    public FAbstractDateType(String typeName) {
        super(typeName);
        addRestriction(SchemaType.FACET_MAX_INCLUSIVE, BigInteger.ZERO);
        addRestriction(SchemaType.FACET_MIN_INCLUSIVE, BigInteger.valueOf(Long.MIN_VALUE));
        addRestriction(SchemaType.FACET_WHITE_SPACE, SchemaType.WS_COLLAPSE);
    }

    @Override
    public List<Integer> getValidFacets() {

        // TODO Check for correctness
        return Arrays.asList(new Integer[] {
                SchemaType.FACET_MIN_INCLUSIVE,
                SchemaType.FACET_MIN_EXCLUSIVE,
                SchemaType.FACET_MAX_INCLUSIVE,
                SchemaType.FACET_MAX_EXCLUSIVE,
                SchemaType.FACET_WHITE_SPACE,
                SchemaType.FACET_PATTERN});
    }

}
