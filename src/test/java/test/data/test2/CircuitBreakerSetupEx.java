package test.data.test2;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = CircuitBreakerSetupEx.Builder.class)
public class CircuitBreakerSetupEx implements EventHandlerSetupEx {

  public static final String TYPE = "circuitBeaker";
  private final String type = TYPE;

  @JsonPropertyDescription("This is a name for logging")
  private final String name;
  @JsonPropertyDescription("This is the percentage (n/100) failure threshold, default 50")
  private final Float failureRateThreshold;
  @JsonPropertyDescription("This is the percentage (n/100) failure threshold, default 50")
  private final Float slowCallRateThreshold;
  @JsonPropertyDescription("Wait duration, decimal seconds, .5 would be 500 mill seconds, default 60")
  private final Duration slowCallDurationThreshold;
  @JsonPropertyDescription("Minimum number of calls, no action before this, default 100")
  private final Integer minimumNumberOfCalls;
  @JsonPropertyDescription("Permitted number of calls in half open, default 10")
  private final Integer permittedNumberOfCallsInHalfOpenState;
  @JsonPropertyDescription("Sliding window size, default 100")
  private final Integer slidingWindowSize;
  @JsonPropertyDescription("This is a how to determine the count, default is COUNT_BASED, alternative is TIME_BASED")
  private final String slidingWindowType;
  @JsonPropertyDescription("Write stacktrace, default true")
  private final Boolean writableStackTraceEnabled;
  private final Boolean automaticTransitionFromOpenToHalfOpenEnabled;
  @JsonPropertyDescription("This is a supplier for an Interval function, the supplier will be passed the properties below")
  private final Class<? extends Function<Map<String, String>, Function<Integer, Long>>> waitIntervalFunctionInOpenStateSupplierClass;
  private final Class<? extends Function<Map<String, String>, Predicate<Throwable>>> recordExceptionSupplierClass;
  @JsonPropertyDescription("this is a map of name/values which will be passed to the recordExceptionSupplier to"
    + " to get the recordException implementation.")
  @lombok.Singular
  private final Map<String, String> properties;

  @SuppressWarnings("unused")
  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {
  }

}
