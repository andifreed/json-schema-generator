package test.data.test2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.util.Map;
import java.util.function.Function;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(builder = SSLSetupEx.Builder.class)
@Builder(builderClassName = "Builder", toBuilder = true)
public class SSLSetupEx {

  @JsonProperty(required = true)
  @JsonPropertyDescription("this is a function class that accepts a Map<String,String> and returns an object that implements HostnameVerifier")
  private final Class<? extends Function<Map<String, String>, HostnameVerifier>> hostnameVerifierSupplierClass;
  @JsonProperty(required = true)
  @JsonPropertyDescription("this is a function class that accepts a Map<String,String> and returns an object that implements SSLSocketFactory")
  private final Class<? extends Function<Map<String, String>, SSLSocketFactory>> factorySupplierClass;
  @lombok.Singular
  @JsonPropertyDescription("this is the map passed to the functions")
  private final Map<String, String> properties;

  @SuppressWarnings("unused")
  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {
  }
}
