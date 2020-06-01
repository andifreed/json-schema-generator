package test.data.test2;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Duration;

@SuppressWarnings("unused")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, visible = true, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = ConstantExponentialBackoffEx.class, name = ConstantExponentialBackoffEx.TYPE),
  @JsonSubTypes.Type(value = RandomExponentialBackoffEx.class, name = RandomExponentialBackoffEx.TYPE),
})
public interface BackoffEx {
  String getType();

  Duration getInterval();

  Double getMultiplier();
}
