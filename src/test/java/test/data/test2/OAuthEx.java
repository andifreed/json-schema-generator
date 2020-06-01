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
 * Some of the credentials can be overridden by a credential provider.  For example the stored value
 * might be encrypted.
 * <p>
 * Credential names are:
 * "clientId"
 * "clientSecret"
 * "username"
 * "password"
 */
@JsonDeserialize(builder = OAuthEx.Builder.class)
@Data
@Builder(builderClassName = "Builder", toBuilder = true)
public class OAuthEx implements AuthMethodEx {

  public interface TokenStore {
    Object getToken();
    void setToken(Object token, Long expires);
  }

  public static final String METHOD = "OAuth";
  @JsonProperty(required = true)
  @JsonPropertyDescription("this is must be 'OAuth' to configure OAuth authentication")
  private final String method = METHOD;
  @JsonPropertyDescription("this is how to implement: values are 'accessCode', 'implicit', 'password', 'application'.")
  private final String flow;
  @JsonPropertyDescription("this is the url for authorization")
  private final String authorizationUrl;
  @JsonPropertyDescription("this is the url for token")
  private final String tokenUrl;
  private final String redirectUrl;
  @JsonPropertyDescription("this is the authorization scopes requested")
  private final String scopes;
  @JsonPropertyDescription("this is the Credentials, these values can also be provided by the CredentialProvider")
  private final CredentialsEx credentials;
  @JsonPropertyDescription("this is store for the transient token, normally it is just kept in the configuration,"
    + " but you can supply a class to externalize it. This class can also implement AccessTokenListener,"
    + " and the entire token will be received")
  private final Class<? extends TokenStore> tokenStoreClass;
  @JsonPropertyDescription("this is a class that implements Credential supplier. "
    + " The supplier can be initialized the with properties below, and will be asked for the value of the different properties, i.e.,"
    + " username, password, clientSecret, clientId.")
  private final Class<? extends CredentialSupplierSupplier> credentialSupplierSupplierClass;
  @JsonPropertyDescription("this is a map of name/values which will be passed to the CredentialSupplierSupplier to"
    + " to get a CredentialSupplier.")
  @lombok.Singular
  private final Map<String, String> properties;

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {
  }
}
