package test.data.test2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = TimeoutSetupEx.Builder.class)
public class TimeoutSetupEx {
  @JsonProperty(required = true)
  private final Integer connectTimeoutMillis;
  @JsonProperty(required = true)
  private final Integer readTimeoutMillis;

  @SuppressWarnings("unused")
  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

  }
}
