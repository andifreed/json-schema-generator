package test.data.test2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = ConstantExponentialBackoffEx.Builder.class)
public class ConstantExponentialBackoffEx implements BackoffEx {
  public static final String TYPE = "exponential";
  @JsonProperty(required = true)
  @JsonPropertyDescription("This is required to be 'exponential' for ConstantExponentialBackoffEx")
  private final String type = TYPE;
  @JsonPropertyDescription("This is the back off period, its a duration, json treats this as float seconds."
    + " Default is 500 millisecs, which would appear at 0.5.")
  private final Duration interval;
  @JsonPropertyDescription("this is the back off multiple, default is 1.5")
  private final Double multiplier;

  @SuppressWarnings("unused")
  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {
  }
}
