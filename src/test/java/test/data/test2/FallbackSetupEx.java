package test.data.test2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = FallbackSetupEx.Builder.class)
public class FallbackSetupEx<T> implements EventHandlerSetupEx {

  public static final String TYPE = "fallback";
  @JsonProperty(required = true)
  @JsonPropertyDescription("this is must be 'fallback' to setup a FallbackSetup")
  private final String type = TYPE;
  @JsonProperty(required = true)
  @JsonPropertyDescription("this is the exception classes that this fallback deals with")
  private final List<Class<? extends Throwable>> exceptionTypes;
  @JsonPropertyDescription("this is the exception handler class to instantiate, it should be thread safe")
  private final Class<? extends Function<Throwable, T>> exceptionHandlerClass;

  @SuppressWarnings("unused")
  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder<T> {
  }
}
