package test.data.test2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(builder = InterceptorSetupEx.Builder.class)
@Builder(builderClassName = "Builder", toBuilder = true)
public class InterceptorSetupEx {

  @JsonProperty(required = true)
  @JsonPropertyDescription("This is the supplier class, it will be passed the properties to get a RequestInterceptor implementation")
  private final Class<? extends Function<Map<String, String>, Consumer<Object>>> interceptorSupplierClass;
  @JsonPropertyDescription("This is a map of name/value that will be passed to the supplier")
  @lombok.Singular
  private final Map<String, String> properties;

  @SuppressWarnings({"unused"})
  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {
  }
}
