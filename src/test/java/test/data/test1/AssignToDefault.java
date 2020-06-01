package test.data.test1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@JsonDeserialize(builder = AssignToDefault.Builder.class)
public class AssignToDefault implements AssignTo {
  private final String _target;
  private final Map<String, String> _properties;

  private AssignToDefault(String target, Map<String, String> properties) {
    _target = target;
    _properties = Collections.unmodifiableMap(properties);
  }

  public static Builder<?> builder() {
    return new Builder<>();
  }

  @JsonProperty(required = true)
  public String getTarget() {
    return _target;
  }

  @JsonProperty
  public Map<String, String> getProperties() {
    return _properties;
  }

  @JsonPOJOBuilder
  public static class Builder<T extends Builder<T>> {
    private String _target;
    private Map<String, String> _properties = new HashMap<>();

    @SuppressWarnings({"unchecked", "unused"})
    public T withTarget(String value) {
      _target = value;
      return (T) this;
    }

    @SuppressWarnings({"unused", "unchecked"})
    public T withProperties(Map<String, String> properties) {
      _properties.putAll(properties);
      return (T) this;
    }

    public AssignToDefault build() {
      return new AssignToDefault(_target, _properties);
    }
  }
}
