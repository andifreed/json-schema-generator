package test.data.test1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, visible = true, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "target", defaultImpl = AssignToDefault.class)
@JsonSubTypes({
  @JsonSubTypes.Type(value = AssignToGroup.class, name = AssignToGroup.TARGET)
})
public interface AssignTo {

  @JsonProperty(required = true)
  String getTarget();
}
