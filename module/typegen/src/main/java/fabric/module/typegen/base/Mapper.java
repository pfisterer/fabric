package fabric.module.typegen.base;

import java.util.HashMap;

import fabric.wsdlschemaparser.schema.FSchemaType;
import fabric.wsdlschemaparser.schema.FSimpleType;

/**
 * Abstract base class for all language mapper implementations.
 * This class defines various methods, which are being used for
 * mapping creation and datatype lookup.
 *
 * @author seidel
 */
abstract public class Mapper {
    /**
     * Mapping between XSD build-in datatypes and language-specific ones
     */
    protected HashMap<String, String> types = new HashMap<String, String>();

    /**
     * Constructor initializes the internal datatype mapping.
     */
    public Mapper() {
        createMapping();
    }

    /**
     * Look up the language-specific representation of an FSchemaType object.
     *
     * @param type FSchemaType object for lookup
     * @return String with the name of the corresponding
     *         language-specific datatype
     * @throws IllegalArgumentException No matching mapping found
     */
    public String lookup(FSchemaType type) throws IllegalArgumentException {
        String className = type.getClass().getCanonicalName();
        if (types.containsKey(className)) {
            return types.get(className);
        }

        throw new IllegalArgumentException(String.format("No mapping found for datatype '%s'.", type.getName()));
    }

    /**
     * This method populates the internal map with
     * language-specific datatype mappings.
     */
    abstract protected void createMapping();
}
