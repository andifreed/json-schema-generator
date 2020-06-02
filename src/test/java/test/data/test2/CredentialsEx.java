package test.data.test2;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(builder = CredentialsEx.Builder.class)
@Builder(builderClassName = "Builder", toBuilder = true)
public class CredentialsEx {

  @JsonPropertyDescription("this is the client id, this can also be supplied by the credential supplier configured with oath")
  private final String clientId;
  @JsonPropertyDescription("this is the client secret, this can also be supplied by the credential supplier configured with oath")
  private final String clientSecret;
  @JsonPropertyDescription("this is the username, this can also be supplied by the credential supplier configured with oath")
  private final String username;
  @JsonPropertyDescription("this is the password, this can also be supplied by the credential supplier configured with oath")
  private final String password;

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {
  }
}