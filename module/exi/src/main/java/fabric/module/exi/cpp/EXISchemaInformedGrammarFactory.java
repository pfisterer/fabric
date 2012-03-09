package fabric.module.exi.cpp;

import exi.ExiException;
import exi.events.ExiEventCodeGenerator;
import exi.grammar.ExiDocumentGrammar;
import exi.grammar.ExiElementGrammar;
import exi.grammar.ExiGrammar;
import exi.grammar.ExiGrammarFactory;
import fabric.wsdlschemaparser.schema.FElement;

import javax.xml.namespace.QName;
import java.util.ArrayList;

public class EXISchemaInformedGrammarFactory implements ExiGrammarFactory {


    private ArrayList<String> globalElements;


    /**
     * Constructor.
     */
    public EXISchemaInformedGrammarFactory() {
        globalElements = new ArrayList<String>();
    }


    /**
     *
     * @param element
     */
    public void addGlobalElement(FElement element) {
        globalElements.add(element.toString());
    }

    public void addLocalElement(FElement element) {
        // TODO
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
        for(int i=0; i< globalElements.size(); i++) {
            eg.append(content, end, "SE("+ globalElements.get(i)+")", g.getNextCode(i+1));
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
