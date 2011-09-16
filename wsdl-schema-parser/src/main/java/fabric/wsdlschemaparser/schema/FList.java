package fabric.wsdlschemaparser.schema;

import org.apache.xmlbeans.SchemaType;

import java.util.Arrays;
import java.util.List;

public class FList extends FSimpleType {
    /**
     * The maximum list length if no restrictions are defined.
     */
    public static final int MAX_LENGTH = 65535;

    /**
     * The item type of this list.
     */
    private FSimpleType itemType;

    /**
     * Creating a new unnamed FList.
     */
    public FList() {
        this(null);
    }

    /**
     * @param typeName
     */
    protected FList(String typeName) {
        super(typeName);
        initialize();
    }

    @Override
    public final boolean isList() {
        return true;
    }

    public void setItemType(FSimpleType st) {
        itemType = st;
    }

    public FSimpleType getItemType() {
        return itemType;
    }

    /**
     * Initialises the list. Mostly here the initial restrictions are being
     * set.
     */
    private void initialize() {
        addRestriction(SchemaType.FACET_MIN_LENGTH, 0);
        addRestriction(SchemaType.FACET_MAX_LENGTH, MAX_LENGTH);
        addRestriction(SchemaType.FACET_WHITE_SPACE, SchemaType.WS_COLLAPSE);
    }

    /**
     * @see fabric.wsdlschemaparser.schema.FSchemaType#getValidFacets()
     */
    @Override
    public List<Integer> getValidFacets() {
        return Arrays.asList(new Integer[]{
                SchemaType.FACET_LENGTH,
                SchemaType.FACET_MIN_LENGTH,
                SchemaType.FACET_MAX_LENGTH,
                SchemaType.FACET_ENUMERATION,
                SchemaType.FACET_PATTERN,
                SchemaType.FACET_WHITE_SPACE
        });
    }
}
