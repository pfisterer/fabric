package de.uniluebeck.sourcegen.c;

import java.util.LinkedList;
import java.util.List;

public class CppTemplateHelper {

    private List<CppTemplateName> elements = null;

    public CppTemplateHelper() {
        this.elements = new LinkedList<CppTemplateName>();
    }

    public void add(CppTemplateName e) {
        this.elements.add(e);
    }

    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();

        int cnt = this.elements.size();
        buffer.append("<");
        for (int i = 0; i < cnt; i++) {
            buffer.append(this.elements.get(i).getTemplateName());
            if(i != cnt-1) {
                buffer.append(", ");
            }
        }
        buffer.append(">");

        return buffer.toString();
    }

}
