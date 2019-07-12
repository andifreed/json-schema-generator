package rmistry.schema.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonDeserialize(builder = Rule.Builder.class)
public class Rule {

  private final Matcher _matcher;
  private final List<AppAction> _actions;

  private Rule(Matcher matcher, List<AppAction> actions) {
    _matcher = matcher;
    _actions = actions;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonProperty(required = true)
  public Matcher getMatcher() {
    return _matcher;
  }

  @JsonProperty(required = true)
  public List<AppAction> getActions() {
    return _actions;
  }

  @JsonPOJOBuilder
  public static final class Builder {
    private Matcher _matcher;
    private List<AppAction> _actions = new ArrayList<>();

    public Builder withMatcher(Matcher matcher) {
      this._matcher = matcher;
      return this;
    }

    @SuppressWarnings("unused")
    public Builder withActions(List<AppAction> actions) {
      this._actions.addAll(actions);
      return this;
    }

    public Builder withAction(AppAction action) {
      this._actions.add(action);
      return this;
    }

    public Rule build() {
      return new Rule(_matcher, Collections.unmodifiableList(_actions));
    }
  }
}
