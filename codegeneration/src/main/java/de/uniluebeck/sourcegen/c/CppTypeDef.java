package de.uniluebeck.sourcegen.c;

import java.util.HashMap;
import java.util.Map;

public class CppTypeDef {

    // STATIC
    private static CppTypeDef instance = null;

    // NONE STATIC
    private Map<String, CppTypeGenerator> types;

    private CppTypeDef() {
        this.types = new HashMap<String, CppTypeGenerator>();
    }

    public synchronized static CppTypeDef getInstance() {
        if (instance == null) {
            return new CppTypeDef();
        } else {
            return instance;
        }
    }

    public void addTypeDef(String key, CppTypeGenerator value) {
        if (!this.types.containsKey(key)) {
            this.types.put(key, value);
        } else {
            throw new Error("TODO");
        }
    }

    public CppTypeGenerator getType(String key) {
        if(this.types.containsKey(key)) {
            return this.types.get(key);
        } else {
            throw new Error("TODO");
        }
    }
}
