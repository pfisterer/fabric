package fabric.module.exi.cpp;

import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Method;

/**
 * Unit test for CppEXIConverter class.
 *
 * @author seidel
 */
public class CppEXIConverterTest
{
  /**
   * Test method for nice comment delimiters.
   */
  @Test(timeout = 1000)
  public void testNiceCommentDelimiter() throws Exception
  {
    // Test comment with even text length
    String text = "Even";
    this.testComment(text);
    
    // Test comment with odd text length
    text = "Odd";
    this.testComment(text);
    
    // Test comment with text length > 52 characters
    text = "";
    for (int i = 0; i < 100; ++i)
    {
      text += "A";
    }
    this.testComment(text);
  }

  /**
   * Private helper method to test text length of return value.
   * 
   * @param comment Comment to test
   */
  private void testComment(final String comment) throws Exception
  {
    // Use reflection API to get method for nice comment delimiters
    Method mapping = CppEXIConverter.class.getDeclaredMethod("createNiceCommentDelimiter", String.class);
    mapping.setAccessible(true); // Make private method accessible

    // Output return value and test length
    System.out.println(mapping.invoke(null, comment));
    assertEquals("Message", mapping.invoke(null, comment).toString().length(), 52);
  }
}
