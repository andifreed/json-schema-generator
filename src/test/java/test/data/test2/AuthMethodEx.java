package test.data.test2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("unused")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, visible = true, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "method")
@JsonSubTypes({
  @JsonSubTypes.Type(value = ApiKeyAuthEx.class, name = ApiKeyAuthEx.METHOD),
  @JsonSubTypes.Type(value = HttpBasicAuthEx.class, name = HttpBasicAuthEx.METHOD),
  @JsonSubTypes.Type(value = HttpBearerAuthEx.class, name = HttpBearerAuthEx.METHOD),
  @JsonSubTypes.Type(value = OAuthEx.class, name = OAuthEx.METHOD)
})
public interface AuthMethodEx {

  interface CredentialSupplier extends BiFunction<String, String, String> {
    @Override
    String apply(String credentialName, String defaultValue);
  }

  interface CredentialSupplierSupplier extends Function<Map<String, String>, CredentialSupplier> {
    CredentialSupplier apply(Map<String, String> properties);
  }
}
