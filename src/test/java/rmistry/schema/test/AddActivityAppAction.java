package rmistry.schema.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = AddActivityAppAction.Builder.class)
public final class AddActivityAppAction extends AppAction {

  @SuppressWarnings("WeakerAccess")
  public static final String ACTION = "ADD_ACTIVITY";

  private final String _pattern;
  private final String _subject;
  private final String _description;
  private final AssignTo _assignTo;
  private Boolean _addToParent;

  private AddActivityAppAction(Builder builder) {
    super(builder);
    _pattern = builder._pattern;
    _subject = builder._subject;
    _description = builder._description;
    _assignTo = builder._assignTo;
    _addToParent = builder._addToParent;
  }

  @JsonProperty(required = true)
  @Override
  public String getAction() {
    return ACTION;
  }

  @JsonProperty(required = true)
  public String getPattern() {
    return _pattern;
  }

  @JsonProperty(required = true)
  public String getSubject() {
    return _subject;
  }

  @JsonProperty(required = true)
  public String getDescription() {
    return _description;
  }

  @JsonProperty(required = true)
  public AssignTo getAssignTo() {
    return _assignTo;
  }

  @JsonProperty
  public Boolean isAddToParent() {
    return _addToParent;
  }

  public static Builder builder() {
    return new AddActivityAppAction.Builder();
  }

  @SuppressWarnings("unused")
  @JsonPOJOBuilder
  public static final class Builder extends AppAction.Builder<Builder> {
    private String _pattern;
    private String _subject;
    private String _description;
    private AssignTo _assignTo;
    private Boolean _addToParent;

    @Override
    public Builder withAction(String action) {
      if (!ACTION.equals(action)) {
        throw new IllegalArgumentException("Invalid action key for ACTION: " + action);
      }
      return this;
    }

    public Builder withPattern(String pattern) {
      _pattern = pattern;
      return this;
    }

    public Builder withSubject(String subject) {
      _subject = subject;
      return this;
    }

    public Builder withDescription(String description) {
      _description = description;
      return this;
    }

    public Builder withAssignTo(AssignTo assignTo) {
      _assignTo = assignTo;
      return this;
    }

    public Builder withAddToParent(Boolean value) {
      _addToParent = value;
      return this;
    }

    @Override
    public AddActivityAppAction build() {
      return new AddActivityAppAction(this);
    }
  }
}
