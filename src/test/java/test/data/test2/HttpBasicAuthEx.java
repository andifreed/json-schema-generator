package test.data.test2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * An interceptor that adds the request header needed to use HTTP basic authentication.
 * <p>
 * The credentials maybe extract or updated (decrypted) from/by a credential supplier
 * credentials are "username" and "password"
 */
@EqualsAndHashCode(callSuper = false)
@Data
@JsonDeserialize(builder = HttpBasicAuthEx.Builder.class)
@Builder(builderClassName = "Builder", toBuilder = true)
public class HttpBasicAuthEx implements AuthMethodEx {

  public static final String METHOD = "HttpBasic";
  @JsonProperty(required = true)
  @JsonPropertyDescription("this is must be 'HttpBasic' to setup a HttpAuthentication")
  private final String method = METHOD;
  @JsonPropertyDescription("This is the username, it can also be supplied by the credential supplier")
  private final String username;
  @JsonPropertyDescription("This is the password, it can also be supplied by the credential supplier")
  private final String password;
  @JsonPropertyDescription("this is a class that implements Credential supplier. "
    + " The supplier can be initialized the with properties below, and will be asked for the value of the username and password")
  private final Class<? extends CredentialSupplierSupplier> credentialSupplierSupplierClass;
  @JsonPropertyDescription("this is a map of name/values which will be passed to the CredentialSupplierSupplier to"
    + " to get a CredentialSupplier.")
  @lombok.Singular
  private final Map<String, String> properties;

  @JsonIgnore
  public CredentialSupplier credentialSupplier() {
    return ConfigEx.getInstanceFromSupplier(credentialSupplierSupplierClass, properties, (name, defaultValue) -> defaultValue);
  }

  @SuppressWarnings("unused")
  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {
  }
}
