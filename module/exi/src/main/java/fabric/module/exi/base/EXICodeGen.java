package fabric.module.exi.base;

import java.util.ArrayList;
import java.util.Properties;

import fabric.wsdlschemaparser.schema.FElement;

import fabric.module.exi.java.FixValueContainer.ElementData;
import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.ListData;

/**
 * Public interface for EXICodeGen implementations.
 * 
 * @author seidel
 */
public interface EXICodeGen
{
  /**
   * Generate code to serialize and deserialize Bean objects with EXI.
   * 
   * @param fixElements XML elements, where value-tags need to be fixed
   * @param fixArrays XML arrays, where value-tags need to be fixed
   * @param fixLists XML lists, where value-tags need to be fixed
   * 
   * @throws Exception Error during code generation
   */
  public void generateCode(final ArrayList<ElementData> fixElements,
                           final ArrayList<ArrayData> fixArrays,
                           final ArrayList<ListData> fixLists) throws Exception;

  /**
   * Create source file and write it to language-specific workspace.
   * 
   * @throws Exception Error during source file write-out
   */
  public void writeSourceFile() throws Exception;

  /**
   * Handle end of XML Schema document, i.e. build EXI grammar.
   * 
   * @param pathToSchemaDocument Path to XML Schema document
   * 
   * @throws Exception Error during event handling
   */
  public void handleEndOfSchema(final String pathToSchemaDocument) throws Exception;

  /**
   * Handle top level element from XML Schema document,
   * i.e. build EXI grammar for element.
   * 
   * @param element Top level element to handle
   */
  // TODO: Update comments
  public void handleTopLevelElement(final FElement element);

  /**
   * Handle local element from XML Schema document,
   * i.e. build EXI grammar for element.
   * 
   * @param element Local element to handle
   * @param parentName Name of parent XML element
   */
  // TODO: Update comments
  public void handleLocalElement(final FElement element, final String parentName);
}
