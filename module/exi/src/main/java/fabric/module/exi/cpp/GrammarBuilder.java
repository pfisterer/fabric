/** 30.03.2012 03:23 */
package fabric.module.exi.cpp;

import com.siemens.ct.exi.EXIFactory;
import com.siemens.ct.exi.FidelityOptions;
import com.siemens.ct.exi.GrammarFactory;
import com.siemens.ct.exi.grammar.EventInformation;
import com.siemens.ct.exi.grammar.Grammar;
import com.siemens.ct.exi.grammar.event.EventType;
import com.siemens.ct.exi.grammar.event.StartElement;
import com.siemens.ct.exi.grammar.rule.Rule;
import com.siemens.ct.exi.helpers.DefaultEXIFactory;

/**
 * @author seidel
 */
// TODO: Add class comment
public class GrammarBuilder
{
  // TODO: Add comment
  public Grammar buildGrammar(final String pathToSchemaDocument) throws Exception
  {
    Grammar schemaInformedGrammar = null;

    if (null != pathToSchemaDocument)
    {
//      // Create EXIficient grammar builder
//      XSDGrammarBuilder builder = XSDGrammarBuilder.newInstance();
//
//      // Build schema-informed EXI grammar
//      builder.loadGrammar(pathToSchemaDocument);
//      SchemaInformedGrammar schemaInformedGrammar = builder.toGrammar();

      // TODO: Remove block
      EXIFactory exiFactory = DefaultEXIFactory.newInstance();
      GrammarFactory grammarFactory = GrammarFactory.newInstance();
      schemaInformedGrammar = grammarFactory.createGrammar(pathToSchemaDocument);
      exiFactory.setGrammar(schemaInformedGrammar);
      exiFactory.setFidelityOptions(FidelityOptions.createStrict());
      schemaInformedGrammar = exiFactory.getGrammar();
      // Block end

      // Get document grammar and look for SD event (= start document)
      Rule documentGrammar = schemaInformedGrammar.getDocumentGrammar();
      System.out.println(">>> Grammar builder: Looking for SD event..."); // TODO: Remove
      System.out.println(">>> Document grammar: " + documentGrammar);
      System.out.println(">>> Grammar builder: Document grammar has " + documentGrammar.getNumberOfEvents() + " events.");
      EventInformation nextEvent = documentGrammar.lookForEvent(EventType.START_DOCUMENT);
      handleEvent(nextEvent);

      // Get next rule and look for SE(root) event (= start of root element)
      if (null != nextEvent)
      {
        Rule rootGrammar = nextEvent.next;
        System.out.println(">>> Root grammar: " + rootGrammar);
        System.out.println(">>> Grammar builder: Root grammar has " + rootGrammar.getNumberOfEvents() + " events.");

        System.out.println(">>> Grammar builder: Handle first child:");
        handleEvent(rootGrammar.lookFor(0));

        System.out.println(">>> Grammar builder: Handle second child:");
        handleEvent(rootGrammar.lookFor(1));

//        for (int i = 0; i < rootGrammar.getNumberOfEvents(); ++i)
//        {
//          System.out.println(">>> Grammar builder: Looking for SE(root) event..."); // TODO: Remove
//          nextEvent = rootGrammar.lookFor(i);
////          nextEvent = rootGrammar.lookForEvent(EventType.START_ELEMENT);
////          nextEvent = rootGrammar.lookForStartElement("http://www.itm.uni-luebeck.de/fabrictest", "WeatherForecast"); // TODO: namespace and local name dynamically!
//          handleEvent(nextEvent);
//        }
      }

      // Get next rule and look for ED event (= end document)
      if (null != nextEvent)
      {
        documentGrammar = nextEvent.next;
        System.out.println(">>> Grammar builder: Looking for ED event..."); // TODO: Remove
        nextEvent = documentGrammar.lookForEvent(EventType.END_DOCUMENT);
        handleEvent(nextEvent);
      }

    }

    return schemaInformedGrammar;
  }
  
  // TODO: Implement and add comment
  private void handleEvent(final EventInformation eventInfo)
  {
    if (null != eventInfo)
    {     
      // Handle local elements
      if (eventInfo.event.isEventType(EventType.START_ELEMENT))
      {
        StartElement element = (StartElement)eventInfo.event;
        System.out.println(">>> Grammar builder: Element " + element.getQName().getLocalPart()
                + " has " + element.getRule().getNumberOfEvents() + " events.");

        // Get first production rule
        EventInformation nextEvent = element.getRule().lookFor(0);

        // TODO: Left-hand side is state, that we are in?

        // Handle left-hand side of the grammar production rule
        // (in this case an element grammar)
        System.out.println(">>> Grammar builder: Lefthand side of " + element.getQName().getLocalPart() +
                " is of type " + nextEvent.event.getEventType() + ".");
        handleEvent(nextEvent);

        // TODO: Right-hand side is a set of states that we can get to via state transition?

        // Handle first production rule for the right-hand side
        // of the grammar production rule (either another
        // element grammar or an EE event)
        System.out.println(">>> Grammar builder: Righthand side of " + element.getQName().getLocalPart() +
                " has " +  nextEvent.next.getNumberOfEvents() + " events.");
        for (int i = 0; i < nextEvent.next.getNumberOfEvents(); ++i)
        {
          System.out.println(">>> Grammar builder: Righthand side of " + element.getQName().getLocalPart() +
                " is of type " + nextEvent.next.lookFor(i).event.getEventType() + ".");
          handleEvent(nextEvent.next.lookFor(i));
        }
      }
      else
      {
        System.out.println(">>> Grammar builder: Element is of type " + eventInfo.event.getEventType());
      }
    }
  }
}
