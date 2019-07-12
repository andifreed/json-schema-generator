package rmistry.schema.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = AddNoteAppAction.Builder.class)
public final class AddNoteAppAction extends AppAction {

  @SuppressWarnings("WeakerAccess")
  public static final String ACTION = "ADD_NOTE";

  private final String _topic;
  private final String _subject;
  private final String _securityType;
  private final Boolean _addToParent;
  private final Boolean _markConfidential;

  private AddNoteAppAction(Builder builder) {
    super(builder);
    _topic = builder._topic;
    _subject = builder._subject;
    _securityType = builder._securityType;
    _markConfidential = builder._markConfidential;
    _addToParent = builder._addToParent;
  }

  @JsonProperty(required = true)
  @Override
  public String getAction() {
    return ACTION;
  }

   @JsonProperty(required = true)
  public String getTopic() {
    return _topic;
  }

  @JsonProperty(required = true)
  public String getSubject() {
    return _subject;
  }

  @JsonProperty(required = true)
  public String getSecurityType() {
    return _securityType;
  }

  @JsonProperty(required = true)
  public Boolean isMarkConfidential() {
    return _markConfidential;
  }

  @JsonProperty
  public Boolean isAddToParent() {
    return _addToParent;
  }

  public static Builder builder() {
    return new AddNoteAppAction.Builder();
  }

  @SuppressWarnings("unused")
  @JsonPOJOBuilder
  public static final class Builder extends AppAction.Builder<Builder> {
    private Boolean _addToParent;
    private String _topic;
    private String _subject;
    private String _securityType;
    private Boolean _markConfidential;

    @Override
    public Builder withAction(String action) {
      if (!ACTION.equals(action)) {
        throw new IllegalArgumentException("Invalid action key for ACTION: " + action);
      }
      return this;
    }

    public Builder withTopic(String topic) {
      _topic = topic;
      return this;
    }

    public Builder withSubject(String subject) {
      _subject = subject;
      return this;
    }

    public Builder withSecurityType(String securityType) {
      _securityType = securityType;
      return this;
    }

    public Builder withMarkConfidential(Boolean markConfidential) {
      _markConfidential = markConfidential;
      return this;
    }

    public Builder withAddToParent(Boolean value) {
      _addToParent = value;
      return this;
    }

    public AddNoteAppAction build() {
      return new AddNoteAppAction(this);
    }
  }
}
