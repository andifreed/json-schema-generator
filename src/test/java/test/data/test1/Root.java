package test.data.test1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(builder = Root.Builder.class)
public class Root {

  private final List<Rule> _rules;

  private Root(Builder builder) {
    _rules = builder._rules;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonProperty(required = true)
  public List<Rule> getRules() {
    return _rules;
  }

  @JsonPOJOBuilder
  public static class Builder {
    private List<Rule> _rules = new ArrayList<>();

    public Builder withRules(List<Rule> rules) {
      _rules.addAll(rules);
      return this;
    }

    public Root build() {
      return new Root(this);
    }
  }
}
