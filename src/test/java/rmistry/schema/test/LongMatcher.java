package rmistry.schema.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = LongMatcher.Builder.class)
public class LongMatcher implements Matcher {

  private final Long _greaterThan;
  private final Long _lessThan;
  private final Long _greaterThanEquals;
  private final Long _lessThanEquals;

  private LongMatcher(Long greaterThan, Long lessThan, Long greaterThanEquals, Long lessThanEquals) {
    _greaterThan = greaterThan;
    _lessThan = lessThan;
    _greaterThanEquals = greaterThanEquals;
    _lessThanEquals = lessThanEquals;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonProperty
  public Long getGreaterThan() {
    return _greaterThan;
  }

  @JsonProperty
  public Long getLessThan() {
    return _lessThan;
  }

  @JsonProperty
  public Long getLessThanEquals() {
    return _lessThanEquals;
  }

  @JsonProperty
  public Long getGreaterThanEquals() {
    return _greaterThanEquals;
  }

  @JsonProperty(required = true)
  @Override
  public String getType() {
    return "Score";
  }

  @JsonPOJOBuilder
  public static final class Builder {
    private Long _greaterThan;
    private Long _lessThan;
    private Long _greaterThanEquals;
    private Long _lessThanEquals;

    public Builder withGreaterThan(Long greaterThan) {
      this._greaterThan = greaterThan;
      return this;
    }

    public Builder withLessThan(Long lessThan) {
      this._lessThan = lessThan;
      return this;
    }

    public Builder withGreaterThanEquals(Long greaterThanEquals) {
      this._greaterThanEquals = greaterThanEquals;
      return this;
    }

    public Builder withLessThanEquals(Long lessThanEquals) {
      this._lessThanEquals = lessThanEquals;
      return this;
    }

    public LongMatcher build() {
      return new LongMatcher(_greaterThan, _lessThan, _greaterThanEquals, _lessThanEquals);
    }

    private void assertMaxMin() {
      if (_greaterThan != null && _lessThan != null && _greaterThan > _lessThan) {
        throw new IllegalArgumentException("Invalid matcher: " + _greaterThan + ">" + _lessThan);
      }
    }
  }
}
