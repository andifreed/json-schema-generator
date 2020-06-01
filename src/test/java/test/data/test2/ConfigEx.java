package test.data.test2;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@Data
@JsonDeserialize(builder = ConfigEx.Builder.class)
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonClassDescription("This is an external representation of the config object.\n"
  + "  This accepts suppliers for what the objects that the configuration needs.\n"
  + "  The config object can be initialized via ConfigEx.parse()")
public class ConfigEx {

  public interface ConfigurableSupplier<T> extends Function<Map<String, String>, T> {
  }

  @JsonPropertyDescription("This is the base path to the endpoint.")
  private final String basePath;
  @Singular
  @JsonPropertyDescription("This is the event handlers that should be applied to each request, RetrySetupEx, FallbackSetupEx, CircuitBreakerEx.")
  private final List<EventHandlerSetupEx> eventHandlers;

  @JsonPropertyDescription("This is how to authenticate: 'HttpBasic', 'HttpBearer', 'ApiKey', or 'OAuth' are supported,"
    + " each has specific attributes based on their 'method' property.")
  private final AuthMethodEx auth;
  @JsonPropertyDescription("This to override the logger for this end point, i.e., you may want to log different end points to different loggers.")
  private final String loggerName;
  @JsonPropertyDescription("This is whether to deal with trace headers, but default the standards headers are forwarded.")
  private final Class<?> traceActionsClass;
  @JsonPropertyDescription("This is an ObjectMapper, but default the standard mapper is used.")
  private final Class<? extends Supplier<ObjectMapper>> mapperSupplierClass;
  @JsonPropertyDescription("This is timeout information.")
  private final TimeoutSetupEx timeout;
  @JsonPropertyDescription("This is setup information for ssl or tls.")
  private final SSLSetupEx ssl;
  @Singular
  @JsonPropertyDescription("This is a collection of additional interceptors, authentication, tracing will be added as interceptors.")
  private final List<InterceptorSetupEx> additionalInterceptors;

  public static <T> T getInstance(Class<? extends T> reqClass) {
    try {
      return reqClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Cannot instantiate", e);
    }
  }

  public static <T> T getInstanceFromSupplier(Class<? extends Function<Map<String, String>, T>> supplierClass,
                                              Map<String, String> map, T defaultSupplier) {
    return Optional.ofNullable(supplierClass)
        .map(ConfigEx::getInstance)
        .map((supplier) -> supplier.apply(map == null ? Collections.emptyMap() : map))
        .orElse(defaultSupplier);
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {
  }
}
