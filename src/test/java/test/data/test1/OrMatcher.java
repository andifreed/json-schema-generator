package test.data.test1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonDeserialize(builder = OrMatcher.Builder.class)
public class OrMatcher implements Matcher {

  private final List<Matcher> _matchers;

  private OrMatcher(List<Matcher> matchers) {
    _matchers = Collections.unmodifiableList(matchers);
  }

  @JsonProperty(required = true)
  public List<Matcher> getMatchers() {
    return _matchers;
  }

  @Override
  @JsonProperty
  public String getType() {
    return "Or";
  }

  public static OrMatcher.Builder builder() {
    return new OrMatcher.Builder();
  }

  @JsonPOJOBuilder
  public static final class Builder {

    private List<Matcher> _values = new ArrayList<>();

    @SuppressWarnings("unused")
    public OrMatcher.Builder withMatchers(List<Matcher> matchers) {
      _values.addAll(matchers);
      return this;
    }

    public OrMatcher build() {
      return new OrMatcher(_values);
    }
  }
}
