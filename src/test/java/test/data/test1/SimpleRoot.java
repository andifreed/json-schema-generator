package test.data.test1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@JsonDeserialize(builder = SimpleRoot.Builder.class)
public class SimpleRoot {

  private final List<AddNoteAppAction> _notes;
  private final Map<String, String> _properties;

  private SimpleRoot(Builder builder) {
    _notes = builder._notes;
    _properties = builder._properties;
  }

  @JsonProperty(required = true)
  public List<AddNoteAppAction> getNotes() {
    return _notes;
  }

  @JsonProperty
  public Map<String, String> getProperties() {
    return _properties;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonPOJOBuilder
  public static class Builder {
    private List<AddNoteAppAction> _notes = new ArrayList<>();
    private final Map<String, String> _properties = new TreeMap<>();

    public Builder withNotes(List<AddNoteAppAction> notes) {
      _notes.addAll(notes);
      return this;
    }

    @SuppressWarnings({"unused"})
    public Builder withProperties(Map<String, String> properties) {
      _properties.putAll(properties);
      return this;
    }

    public SimpleRoot build() {
      return new SimpleRoot(this);
    }
  }
}
