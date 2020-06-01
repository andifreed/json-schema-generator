package test.data.test2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * An interceptor that adds the request header needed to use HTTP bearer authentication.
 * The "bearToken" or scheme value can be replace or modified (decrypted) by the credential provider.
 */
@Data
@JsonDeserialize(builder = HttpBearerAuthEx.Builder.class)
@Builder(builderClassName = "Builder", toBuilder = true)
public class HttpBearerAuthEx implements AuthMethodEx {
  public static final String METHOD = "HttpBearer";
  public static final String BEARER_TOKEN = "bearerToken";
  @JsonProperty(required = true)
  @JsonPropertyDescription("this is must be 'HttpBearer' to setup a HttpBearerAuth")
  private final String method = METHOD;

  @JsonPropertyDescription("This is the authorization scheme, by default 'Bearer'")
  private final String scheme;
  @JsonPropertyDescription("This is the token value.  It can also be supplied by the credential supplier.")
  private final String bearerToken;
  @JsonPropertyDescription("this is a class that implements Credential supplier. "
    + " The supplier can be initialized the with properties below, and will be asked for the value of the scheme, i.e.,"
    + " if scheme is defaulted it will be 'bearerToken', so the supplier will be asked for 'bearerToken' value.")
  private final Class<? extends CredentialSupplierSupplier> credentialSupplierSupplierClass;
  @JsonPropertyDescription("this is a map of name/values which will be passed to the CredentialSupplierSupplier to"
    + " to get a CredentialSupplier.")
  @lombok.Singular
  private final Map<String, String> properties;

  @JsonIgnore
  public CredentialSupplier credentialSupplier() {
    return ConfigEx.getInstanceFromSupplier(credentialSupplierSupplierClass, properties, (name, defaultValue) -> defaultValue);
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {
  }
}
