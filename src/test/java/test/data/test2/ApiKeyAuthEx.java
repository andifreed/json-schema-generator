package test.data.test2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * This will apply the ApiKey to the authorization, it will use the schema to pull the value from the credential supplier
 * the paramName value is used as the key to credentials.
 */
@Data()
@JsonDeserialize(builder = ApiKeyAuthEx.Builder.class)
@Builder(builderClassName = "Builder", toBuilder = true)
public class ApiKeyAuthEx implements AuthMethodEx {

  public enum Location {
    query, // add to request query params
    header, // add to request header
    cookie // add to request header as cookie
  }

  public static final String METHOD = "ApiKey";
  @JsonPropertyDescription("this is required to be 'ApiKey', for ApiKeyAuthEx")
  private final String method = METHOD;
  @JsonPropertyDescription("this is where this key belongs: header, parameter, cookie.  Defaults to header")
  private final Location location;
  @JsonPropertyDescription("this is where the name of the header, parameter or cookie. Default is apikey")
  private final String paramName;
  @JsonPropertyDescription("this is value of the parameter.  Note that if credential supplier defined, this value will be requests from it.")
  private final String apiKey;
  @JsonPropertyDescription("this is a class that implements Credential supplier. "
    + " The supplier can be initialized the with properties below, and will be asked for the value of the paramName, i.e.,"
    + " if paramName is defaulted it will be 'apikey', so the supplier will be asked for 'apikey' value.")
  private final Class<? extends CredentialSupplierSupplier> credentialSupplierSupplierClass;
  @JsonPropertyDescription("this is a map of name/values which will be passed to the CredentialSupplierSupplier to"
    + " to get a CredentialSupplier.")
  @lombok.Singular
  private final Map<String, String> properties;

  @JsonIgnore
  public CredentialSupplier credentialSupplier() {
    return ConfigEx.getInstanceFromSupplier(credentialSupplierSupplierClass, properties, (name, defaultValue) -> defaultValue);
  }


  @SuppressWarnings({"unused", "FieldCanBeLocal"})
  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {
  }
}
