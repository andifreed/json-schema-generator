package rmistry.schema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import rmistry.schema.test.AssignTo;
import rmistry.schema.test.Matcher;
import rmistry.schema.test.Root;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

public class SchemaGeneratorTest {

  @Test
  public void testComplex() throws IOException {
    doAClass(Root.class);
  }

  @Test
  public void testSimpleSubClass() throws IOException {
    doAClass(AssignTo.class);
  }

  @Test
  public void testRecursiveSubClass() throws IOException {
    doAClass(Matcher.class);
  }

  private void doAClass(Class<?> classToGenerate) throws IOException {
    String resName = "/" + classToGenerate.getSimpleName() + ".schema.json";
    InputStream resource = this.getClass().getResourceAsStream(resName);
    JsonSchema schema = GenerateSchemas.generateSchemaFromJavaClass(classToGenerate);
    String content = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(schema);
    String expected = resource == null ? "" : IOUtils.toString(resource, "utf-8");
    assertThat(content)
      .as("The schema has changed\n" +
        "Please replace the content in test/resources" + resName + ":\n" +
        content + "\n")
      .isEqualToIgnoringNewLines(expected);
  }
}