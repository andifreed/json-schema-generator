package rmistry.schema.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, visible = true, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "action", defaultImpl = AppAction.class)
@JsonSubTypes({
  @JsonSubTypes.Type(value = AddActivityAppAction.class, name = AddActivityAppAction.ACTION),
  @JsonSubTypes.Type(value = AddNoteAppAction.class, name = AddNoteAppAction.ACTION)
})
@JsonDeserialize(builder = AppAction.Builder.class)
public class AppAction {
  private final String _action;
  private final Map<String, String> _properties;
  
  AppAction(Builder builder) {
    _action = builder._action;
    //noinspection unchecked
    _properties = builder._properties;
  }

  public static Builder<?> builder() {
    return new Builder<>();
  }

  @JsonProperty(required = true)
  public String getAction() {
    return _action;
  }

  @JsonProperty
  public Map<String, String> getProperties() {
    return _properties;
  }

  @SuppressWarnings({"WeakerAccess", "unused", "unchecked"})
  @JsonPOJOBuilder
  public static class Builder<T extends Builder<T>> {
    protected String _action;
    protected Map<String, String> _properties = new HashMap<>();

    public T withAction(String action) {
      _action = action;
      return (T) this;
    }

    public T withProperties(Map<String, String> properties) {
      _properties.putAll(properties);
      return (T) this;
    }

    public AppAction build() {
      return new AppAction(this);
    }
  }
}
