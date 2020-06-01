package test.data.test2;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.function.Function;
import java.util.function.Predicate;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = RetrySetupEx.Builder.class)
public class RetrySetupEx implements EventHandlerSetupEx {

  public static final String TYPE = "retry";
  private final String type = TYPE;

  private final String name;
  private final Integer maxAttempts;
  private final BackoffEx backoff;
  private final Class<? extends Predicate<Throwable>> retryOnExceptionPredicateClass;
  private final Class<? extends Predicate<Object>> retryOnResultPredicateClass;
  private final Class<? extends Function<Long, Integer>> intervalFunctionClass;

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {
  }
}
