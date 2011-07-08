package fabric.wsdlschemaparser.schema;

import java.util.Arrays;
import java.util.List;

import org.apache.xmlbeans.SchemaType;

public class FBase64Binary extends FSimpleType {

    public FBase64Binary() {
        this(null);
    }

    public FBase64Binary(String typeName) {
        super(typeName);
    }

    @Override
    public List<Integer> getValidFacets() {

        return Arrays.asList(new Integer[] { SchemaType.FACET_LENGTH, SchemaType.FACET_MIN_LENGTH,
                SchemaType.FACET_MAX_LENGTH, SchemaType.FACET_PATTERN, SchemaType.FACET_ENUMERATION,
                SchemaType.FACET_WHITE_SPACE });

    }

}
