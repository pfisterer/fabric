/** 29.03.2012 00:12 */
package fabric.module.exi.cpp;

import com.siemens.ct.exi.grammar.EventInformation;
import com.siemens.ct.exi.grammar.SchemaInformedGrammar;
import com.siemens.ct.exi.grammar.XSDGrammarBuilder;
import com.siemens.ct.exi.grammar.event.EventType;
import com.siemens.ct.exi.grammar.event.StartElement;
import com.siemens.ct.exi.grammar.rule.Rule;

/**
 * @author seidel
 */
// TODO: Add class comment
public class GrammarBuilder
{
  // TODO: Add comment
  public void buildGrammar(final String pathToSchemaDocument) throws Exception
  {
    if (null != pathToSchemaDocument)
    {
      // Create EXIficient grammar builder
      XSDGrammarBuilder builder = XSDGrammarBuilder.newInstance();
      
      // Build schema-informed EXI grammar
      builder.loadGrammar(pathToSchemaDocument);
      SchemaInformedGrammar schemaInformedGrammar = builder.toGrammar();
      
      // Get document grammar and look for SD event (= start document)
      Rule documentGrammar = schemaInformedGrammar.getDocumentGrammar();
      EventInformation nextEvent = documentGrammar.lookForEvent(EventType.START_DOCUMENT);
      handleEvent(nextEvent);
      
      // Get next rule and look for SE(root) event (= start of root element)
      if (null != nextEvent)
      {
        Rule rootGrammar = nextEvent.next;
        nextEvent = rootGrammar.lookForStartElement("", "root"); // TODO: Set root name dynamically!
        handleEvent(nextEvent);
      }
      
      // Get next rule and look for ED event (= end document)
      if (null != nextEvent)
      {
        documentGrammar = nextEvent.next;
        nextEvent = documentGrammar.lookForEvent(EventType.END_DOCUMENT);
        handleEvent(nextEvent);
      }
    }  
  }
  
  // TODO: Implement and add comment
  private void handleEvent(final EventInformation eventInfo)
  {
    if (null != eventInfo)
    {
      System.out.println(">>> Grammar builder: " + eventInfo.event + "\t" + eventInfo.getEventCode()); // TODO: Remove

      // Handle local elements
      if (eventInfo.event.isEventType(EventType.START_ELEMENT))
      {
        StartElement element = (StartElement)eventInfo.event;

        // Get first production rile
        EventInformation nextEvent = element.getRule().lookFor(0);

        // Handle lefthand side of the grammar production rule
        // (in this case an element grammar)
        handleEvent(nextEvent);

        // Handle first production rule fo the righthand side
        // of the grammar production rule (either another
        // element grammar or an EE event)
        handleEvent(nextEvent.next.lookFor(0));
      }
    }
  }
}
