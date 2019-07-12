package rmistry.schema.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = NotMatcher.Builder.class)
public class NotMatcher implements Matcher {

  private final Matcher _matcher;

  private NotMatcher(Matcher matcher) {
    _matcher = matcher;
  }

  @JsonProperty(required = true)
  public Matcher getMatcher() {
    return _matcher;
  }

  @JsonProperty(required = true)
  @Override
  public String getType() {
    return "Not";
  }

  public static NotMatcher.Builder builder() {
    return new NotMatcher.Builder();
  }

  @JsonPOJOBuilder
  public static final class Builder {

    private Matcher _matcher;

    public Builder withMatcher(Matcher matcher) {
      _matcher = matcher;
      return this;
    }

    public NotMatcher build() {
      return new NotMatcher(_matcher);
    }
  }
}
