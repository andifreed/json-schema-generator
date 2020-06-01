package test.data.test1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = OrMatcher.class, name = "Or"),
  @JsonSubTypes.Type(value = NotMatcher.class, name = "Not"),
  @JsonSubTypes.Type(value = LongMatcher.class, name = "Score")
})
public interface Matcher {
  @JsonProperty(required = true)
  String getType();
}
