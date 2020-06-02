package rmistry.schema;

import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import test.data.test1.AddActivityAppAction;
import test.data.test1.AssignTo;
import test.data.test1.Matcher;
import test.data.test1.NotMatcher;
import test.data.test1.SimpleRoot;
import test.data.test1.SimpleSubClassRoot;
import test.data.test2.ConfigEx;

import java.io.IOException;

public class SchemaGeneratorTest {

  @Rule
  public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

  @Test
  public void testSimple() throws IOException {
    String content = doAClass(SimpleRoot.class);
    softly.assertThat(content).contains("urn:jsonschema:test:data:test1:AddNoteAppAction");
    softly.assertThat(content).doesNotContain("test.data.test1.AddNoteAppAction");
  }

  @Test
  public void testClassName() throws IOException {
    String content = GenerateSchemas.genJsonForAClass(SimpleRoot.class, true, null, true, null);
    softly.assertThat(content).doesNotContain("urn:jsonschema:test:data:test1:AddNoteAppAction");
    softly.assertThat(content).contains("test.data.test1.AddNoteAppAction");
  }

  @Test
  public void testSimplePropertySubclass() throws IOException {
    doAClass(SimpleSubClassRoot.class);
  }

  @Test
  public void testComplex() throws IOException {
    String content = GenerateSchemas.genJsonForAClass(ConfigEx.class, true, this.getClass().getClassLoader(), true,
        () -> softly.fail("The schema has changed\n" +
            "Replaced the content in test/resources for " + ConfigEx.class.getSimpleName() + "\n"));
    softly.assertThat(content).doesNotContain("urn:jsonschema");
    softly.assertThat(content).contains(
        "\"id\": \"test.data.test2.ApiKeyAuthEx\"",
        "\"id\": \"test.data.test2.AuthMethodEx\"",
        "\"id\": \"test.data.test2.BackoffEx\"",
        "\"id\": \"test.data.test2.CircuitBreakerSetupEx\"",
        "\"id\": \"test.data.test2.ConfigEx\"",
        "\"id\": \"test.data.test2.ConstantExponentialBackoffEx\"",
        "\"id\": \"test.data.test2.CredentialsEx\"",
        "\"id\": \"test.data.test2.EventHandlerSetupEx\"",
        "\"id\": \"test.data.test2.FallbackSetupEx\"",
        "\"id\": \"test.data.test2.HttpBasicAuthEx\"",
        "\"id\": \"test.data.test2.InterceptorSetupEx\"",
        "\"id\": \"test.data.test2.OAuthEx\"",
        "\"id\": \"test.data.test2.RandomExponentialBackoffEx\"",
        "\"id\": \"test.data.test2.RetrySetupEx\"",
        "\"id\": \"test.data.test2.SSLSetupEx\"",
        "\"id\": \"test.data.test2.TimeoutSetupEx\"");
  }

  @Test
  public void testSimpleSubClass() throws IOException {
    doAClass(AssignTo.class);
  }

  @Test
  public void testSimplePropertySubClass() throws IOException {
    doAClass(AddActivityAppAction.class);
  }

  @Test
  public void testNotMatcherClass() throws IOException {
    doAClass(NotMatcher.class);
  }

  @Test
  public void testRecursiveSubClass() throws IOException {
    doAClass(Matcher.class);
  }

  private String doAClass(Class<?> classToGenerate) throws IOException {
    return GenerateSchemas.genJsonForAClass(classToGenerate, false, this.getClass().getClassLoader(), true,
        () -> softly.fail("The schema has changed\n" +
            "Replaced the content in test/resources for " + classToGenerate.getSimpleName() + "\n"));
  }

}
