package fabric.module.exi.cpp;

import exi.ExiException;
import exi.events.ExiEventCode;
import exi.events.ExiEventCodeGenerator;
import exi.grammar.ExiDocumentGrammar;
import exi.grammar.ExiGrammar;
import exi.grammar.ExiGrammarFactory;

import javax.xml.namespace.QName;
import java.util.ArrayList;

public class EXISchemaInformedGrammarFactory implements ExiGrammarFactory {


    private ArrayList<ElementMetadata> globalElements;
    private ArrayList<ElementMetadata> localElements;


    /**
     * Constructor.
     */
    public EXISchemaInformedGrammarFactory() {
        globalElements = new ArrayList<ElementMetadata>();
        localElements = new ArrayList<ElementMetadata>();
    }


    public void addGlobalElement(ElementMetadata element) {
        globalElements.add(element);
    }

    public void addLocalElement(ElementMetadata element) {
        localElements.add(element);
    }


    @Override
    public ExiGrammar createDocumentGrammar() throws ExiException {
        ExiDocumentGrammar eg = new ExiDocumentGrammar( );
        ExiEventCodeGenerator g = new ExiEventCodeGenerator( );

        String start   = "Document";
        String content = "DocContent";
        String end     = "DocEnd";

        eg.append(start, content, "SD", g.getNextCode(1));
        g.reset();

        java.util.Collections.sort(globalElements);
        for(ElementMetadata m : globalElements) {
            ExiEventCode c = g.getNextCode(1);
            m.setEXIEventCode(c);

            eg.append(content, end, "SE(" +m.getElementName()+ ")", c);
        }


        /*
        eg.append(content, end, "SE(*)", g.getNextCode(1));
        if (this.options.isSet(FidelityOption.PRESERVE_DTDS)) {
            eg.append(content, content, "DT", g.getNextCode(2));
        }
        if (this.options.isSet(FidelityOption.PRESERVE_COMMENTS)) {
            eg.append(content, content, "CM", g.getNextCode(3));
        }
        if (this.options.isSet(FidelityOption.PRESERVE_PROCESSING_INSTRUCTIONS)) {
            eg.append(content, content, "PI", g.getNextCode(3));
        }

        */

        g.reset();
        eg.append(end, "", "ED", g.getNextCode(1));
        /*
        if (this.options.isSet(FidelityOption.PRESERVE_COMMENTS)) {
            eg.append(end, end, "CM", g.getNextCode(2));
        }
        if (this.options.isSet(FidelityOption.PRESERVE_PROCESSING_INSTRUCTIONS)) {
            eg.append(end, end, "PI", g.getNextCode(2));
        }
        */
        eg.setInitialGroup(start);

        return eg;
    }

    @Override
    public ExiGrammar createFragmentGrammar() throws ExiException {
        return null;
    }

    @Override
    public ExiGrammar createElementGrammar(QName qName) throws ExiException {
        //ExiElementGrammar eg = new ExiElementGrammar();
        return null;
    }
}
