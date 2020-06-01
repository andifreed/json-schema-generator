package test.data.test1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = SimpleSubClassRoot.Builder.class)
public class SimpleSubClassRoot {

  private final AppAction _action;

  private SimpleSubClassRoot(Builder builder) {
    _action = builder._action;
  }

  @JsonProperty(required = true)
  public AppAction getAction() {
    return _action;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonPOJOBuilder
  public static class Builder {
    private AppAction _action;

    public Builder withAction(AppAction action) {
      _action = action;
      return this;
    }

    public SimpleSubClassRoot build() {
      return new SimpleSubClassRoot(this);
    }
  }
}
