package rmistry.schema.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = AssignToGroup.Builder.class)
public class AssignToGroup implements AssignTo {

  @SuppressWarnings("WeakerAccess")
  public static final String TARGET = "GROUP";
  private final String _groupName;

  private AssignToGroup(String groupName) {
    _groupName = groupName;
  }

  @JsonProperty(required = true)
  @Override
  public String getTarget() {
    return TARGET;
  }

  @JsonProperty(required = true)
  public String getGroupName() {
    return _groupName;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonPOJOBuilder
  public static class Builder {
    private String _groupName;

    public Builder withTarget(String value) {
      if (!TARGET.equals(value)) {
        throw new IllegalArgumentException("Expected " + TARGET + " attempted " + value);
      }
      return this;
    }

    public Builder withGroupName(String groupName) {
      _groupName = groupName;
      return this;
    }

    public AssignToGroup build() {
      return new AssignToGroup(_groupName);
    }
  }
}
