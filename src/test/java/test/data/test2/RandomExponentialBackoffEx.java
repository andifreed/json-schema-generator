package test.data.test2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = RandomExponentialBackoffEx.Builder.class)
public class RandomExponentialBackoffEx implements BackoffEx {
  public static final String TYPE = "random";
  @JsonProperty(required = true)
  @JsonPropertyDescription("This is required to be 'random' for RandomExponentialBackoffEx")
  private final String type = TYPE;

  private final Double intervalSecs;
  private final Double multiplier;
  private final Double randomizationFactor;

  @SuppressWarnings("unused")
  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {
  }
}