/*
copied from https://gist.github.com/rrmistry/2246c959d1c9cc45894ecf55305c61fd
license unchanged
made changes for java 7 compatibility
 */

/*
MIT License

Copyright (c) 2019 Rohit Mistry

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package rmistry.schema;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.FormatVisitorFactory;
import com.fasterxml.jackson.module.jsonSchema.factories.JsonSchemaFactory;
import com.fasterxml.jackson.module.jsonSchema.factories.ObjectVisitor;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.fasterxml.jackson.module.jsonSchema.factories.VisitorContext;
import com.fasterxml.jackson.module.jsonSchema.factories.WrapperFactory;
import com.fasterxml.jackson.module.jsonSchema.types.AnySchema;
import com.fasterxml.jackson.module.jsonSchema.types.ArraySchema;
import com.fasterxml.jackson.module.jsonSchema.types.NullSchema;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import com.fasterxml.jackson.module.jsonSchema.types.ReferenceSchema;
import com.fasterxml.jackson.module.jsonSchema.types.StringSchema;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author rmistry
 */
class CustomSchemaFactoryWrapper extends SchemaFactoryWrapper {

  private static final Comparator<Object> COMPARATOR = Comparator.comparing(o -> ((ReferenceSchema) o).get$ref());

  CustomSchemaFactoryWrapper() {
    this(null);
  }

  private CustomSchemaFactoryWrapper(SerializerProvider p) {
    super(p);
    this.schemaProvider = new CustomJsonSchemaFactory();
    this.visitorFactory = new CustomFormatVisitorFactory(this);
    this.globalDefinitionClasses = new LinkedHashMap<>();
  }

  Map<String, JsonSchema> globalDefinitionClasses;

  static class CustomObjectSchema extends ObjectSchema {
    @JsonProperty
    Map<String, JsonSchema> definitions;
  }

  static class CustomAnySchema extends AnySchema {
    @JsonProperty
    Map<String, JsonSchema> definitions;
  }

  static public class CustomJsonSchemaFactory extends JsonSchemaFactory {
    @Override
    public AnySchema anySchema() {
      return new CustomAnySchema();
    }

    @Override
    public ObjectSchema objectSchema() {
      return new CustomObjectSchema();
    }
  }

  private static CustomSchemaFactoryWrapper getNewWrapper(CustomSchemaFactoryWrapper visitor) {
    CustomSchemaFactoryWrapper wrapper = new CustomSchemaFactoryWrapper();
    wrapper.globalDefinitionClasses = visitor.globalDefinitionClasses;
    return wrapper;
  }

  public static class CustomWrapperFactory extends WrapperFactory {
    private final CustomSchemaFactoryWrapper schemaFactory;

    CustomWrapperFactory(CustomSchemaFactoryWrapper sf) {
      schemaFactory = sf;
    }

    @Override
    public SchemaFactoryWrapper getWrapper(SerializerProvider provider) {
      CustomSchemaFactoryWrapper wrapper = getNewWrapper(schemaFactory);
      wrapper.setProvider(provider);
      return wrapper;
    }

    @Override
    public SchemaFactoryWrapper getWrapper(SerializerProvider provider, VisitorContext rvc) {
      CustomSchemaFactoryWrapper wrapper = getNewWrapper(schemaFactory);
      wrapper.setProvider(provider);
      wrapper.setVisitorContext(rvc);
      return wrapper;
    }
  }

  private static Collection<Class<?>> getAllJsonSubTypeClasses(Class<?> classToTest) {
    Annotation[] annotations = classToTest.getAnnotations();
    Map<String, Class<?>> output = new TreeMap<>();
    for (Annotation a : annotations) {
      if (JsonSubTypes.class.isAssignableFrom(a.annotationType())) {
        for (JsonSubTypes.Type subType : ((JsonSubTypes) a).value()) {
          output.put(subType.value().getSimpleName(), subType.value());
        }
      } else if (JsonTypeInfo.class.isAssignableFrom(a.annotationType())) {
        Class<?> defaultImpl = ((JsonTypeInfo) a).defaultImpl();
        if (!JsonTypeInfo.class.isAssignableFrom(defaultImpl)) {
          output.put(defaultImpl.getSimpleName(), defaultImpl);
        }
      }
    }
    return output.values();
  }

  private static String javaTypeToUrn(Class<?> classObj) {
    String name = classObj.getCanonicalName();
    return "urn:jsonschema:" + name.replace('.', ':').replace('$', ':');
  }

  private class CustomObjectVisitor extends ObjectVisitor {

    private final CustomSchemaFactoryWrapper schemaFactory;

    CustomObjectVisitor(SerializerProvider provider,
                        ObjectSchema schema,
                        WrapperFactory wrapperFactory,
                        CustomSchemaFactoryWrapper sf) {
      super(provider, schema, wrapperFactory);
      schemaFactory = sf;
    }

    @Override
    public void property(BeanProperty prop) throws JsonMappingException {
      schema.putProperty(prop, createOrGetSchema(prop));
    }

    @Override
    public void optionalProperty(BeanProperty prop) throws JsonMappingException {
      schema.putOptionalProperty(prop.getName(), createOrGetSchema(prop));
    }

    private JsonSchema createOrGetSchema(BeanProperty prop) throws JsonMappingException {
      JsonSchema schema = getOrCreateSchema(prop.getType(), prop);
      addPropDescription(schema, prop);
      return schema;
    }

    private JsonSchema getOrCreateSchema(JavaType type, BeanProperty prop) throws JsonMappingException {
      Class<?> rawClass = type.getRawClass();
      String refId = javaTypeToUrn(rawClass);
      if (Class.class.equals(rawClass)) {
        StringSchema clsSchema = new StringSchema();
        clsSchema.set$ref(refId);
        return clsSchema; // if schema for class already generated exit
      } else if (type.isCollectionLikeType()) {
        ArraySchema arraySchema = new ArraySchema();
        arraySchema.setItemsSchema(getOrCreateSchema(type.containedType(0), prop));
        return arraySchema;
      } else if (isModel(type)) {
        if (schemaFactory.globalDefinitionClasses.containsKey(refId)) {
          return new ReferenceSchema(refId);
        }
        ObjectSchema propSchema = new ObjectSchema();
        propSchema.setId(refId);
        // we are generating the class so flag it as built for recursion
        schemaFactory.globalDefinitionClasses.put(refId, propSchema);
        if (!handleSubClasses(rawClass, propSchema)) {
          return createNewClass(rawClass, refId);
        }
        return propSchema;
      }
      return propertySchema(prop);
    }

    private boolean handleSubClasses(Class<?> rawClass, ObjectSchema propSchema) throws JsonMappingException {
      Set<Object> inheritingClasses = new TreeSet<>(COMPARATOR);
      for (Class<?> subClass : getAllJsonSubTypeClasses(rawClass)) {
        String subClassRef = javaTypeToUrn(subClass);
        inheritingClasses.add(new ReferenceSchema(subClassRef));
        if (!schemaFactory.globalDefinitionClasses.containsKey(subClassRef)) {
          createNewClass(subClass, subClassRef);
        }
      }
      if (inheritingClasses.size() > 0) {
        propSchema.setOneOf(inheritingClasses);
      } else {
        return false;
      }
      return true;
    }

    private JsonSchema createNewClass(Class<?> rawClass, String refId) throws JsonMappingException {
      SchemaFactoryWrapper visitor = getNewWrapper(schemaFactory);
      try {
        schemaFactory.globalDefinitionClasses.put(refId, new NullSchema());
        JsonSchema subClassSchema = GenerateSchemas.generateSchemaFromJavaClass(visitor, rawClass);
        schemaFactory.globalDefinitionClasses.put(refId, subClassSchema);
        if (subClassSchema instanceof ObjectSchema) {
          ((ObjectSchema) subClassSchema).rejectAdditionalProperties();
        }
        schemaFactory.globalDefinitionClasses.put(refId, subClassSchema);
        return subClassSchema;
      } catch (Exception ex) {
        throw JsonMappingException.from(visitor.getProvider(), "Exception occurred", ex);
      }
    }
  }

  private void addPropDescription(JsonSchema schema, BeanProperty property) {
    JsonPropertyDescription propDescAnn = property.getAnnotation(JsonPropertyDescription.class);
    if (propDescAnn != null) {
      schema.setDescription(propDescAnn.value());
    }
    JsonProperty propAnn = property.getAnnotation(JsonProperty.class);
    if (propAnn != null && propAnn.required()) {
      schema.setRequired(true);
    }
  }

  public static void addClassDescription(JsonSchema schema, Class<?> cls) {
    JsonClassDescription annotation = cls.getAnnotation(JsonClassDescription.class);
    if (annotation != null) {
      schema.setDescription(annotation.value());
    }
  }

  private static boolean isModel(JavaType type) {
    return type.getRawClass() != String.class
        && !isBoxedPrimitive(type.getRawClass())
        && !type.isPrimitive()
        && !type.isMapLikeType()
        && !type.isCollectionLikeType()
        && !type.isEnumType();
  }

  private static boolean isBoxedPrimitive(Class<?> type) {
    return type == Boolean.class
        || type == Byte.class
        || type == Long.class
        || type == Integer.class
        || type == Short.class
        || type == Float.class
        || type == Double.class;
  }

  public class CustomFormatVisitorFactory extends FormatVisitorFactory {
    private final CustomSchemaFactoryWrapper schemaFactory;

    CustomFormatVisitorFactory(CustomSchemaFactoryWrapper schemaFactoryWrapper) {
      super(new CustomWrapperFactory(schemaFactoryWrapper));
      schemaFactory = schemaFactoryWrapper;
    }

    @Override
    public JsonObjectFormatVisitor objectFormatVisitor(SerializerProvider provider, ObjectSchema objectSchema, VisitorContext rvc) {
      CustomObjectVisitor v = new CustomObjectVisitor(provider,
          objectSchema,
          new CustomWrapperFactory(schemaFactory),
          schemaFactory);
      v.setVisitorContext(rvc);
      return v;
    }
  }
}