package test.data.test2;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, visible = true, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = RetrySetupEx.class, name = RetrySetupEx.TYPE),
  @JsonSubTypes.Type(value = FallbackSetupEx.class, name = FallbackSetupEx.TYPE),
  @JsonSubTypes.Type(value = CircuitBreakerSetupEx.class, name = CircuitBreakerSetupEx.TYPE),
})
public interface EventHandlerSetupEx {
  String getType();
}
