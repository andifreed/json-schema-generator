/*
copied from https://gist.github.com/rrmistry/2246c959d1c9cc45894ecf55305c61fd
license unchanged
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import org.apache.commons.io.IOUtils;
import rmistry.schema.CustomSchemaFactoryWrapper.CustomAnySchema;
import rmistry.schema.CustomSchemaFactoryWrapper.CustomObjectSchema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;

import static com.fasterxml.jackson.databind.SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS;
import static rmistry.schema.CustomSchemaFactoryWrapper.addClassDescription;

/**
 * @author rmistry
 */
public class GenerateSchemas {

  private static final ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory())
      .setSerializationInclusion(JsonInclude.Include.NON_NULL);
  private static final ObjectMapper JSON_MAPPER = new ObjectMapper()
      .setSerializationInclusion(JsonInclude.Include.NON_NULL);

  private static JsonSchema generateSchemaFromJavaClass(SchemaFactoryWrapper visitor, ObjectMapper mapper,
                                                        Class<?> classToGenerate) throws JsonMappingException {
    mapper.acceptJsonFormatVisitor(mapper.constructType(classToGenerate), visitor);
    return visitor.finalSchema();
  }

  static JsonSchema generateSchemaFromJavaClass(SchemaFactoryWrapper visitor, Class<?> classToGenerate)
    throws JsonMappingException {
    ObjectMapper mapper = new ObjectMapper()
      .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
      .enable(ORDER_MAP_ENTRIES_BY_KEYS);

    JsonSchema schema = generateSchemaFromJavaClass(visitor, mapper, classToGenerate);
    addClassDescription(schema, classToGenerate);
    return schema;
  }

  public static JsonSchema generateSchemaFromJavaClass(Class<?> classToGenerate)
      throws JsonProcessingException {
    CustomSchemaFactoryWrapper visitor = new CustomSchemaFactoryWrapper();
    JsonSchema schema = generateSchemaFromJavaClass(visitor, classToGenerate);
    // Need to clone global list of dependencies for this schema
    if (schema instanceof CustomAnySchema) {
      ((CustomAnySchema) schema).definitions = visitor.globalDefinitionClasses;
    } else if (schema instanceof CustomObjectSchema) {
      ((CustomObjectSchema) schema).definitions = visitor.globalDefinitionClasses;
    }
    return schema;
  }

  /**
   * This does not deal well with subclass, the superclass will appear as the only valid value
   * in the
   *
   * @param configClass the classes to dump
   * @return the resulting string
   * @throws JsonProcessingException on jackson errors
   */
  public static String serializeSchemaForDoc(Class<?> configClass) throws JsonProcessingException {
    JsonNode transformed = createSchemaForDoc(configClass, true);
    return JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(transformed);
  }

  public static JsonNode createSchemaForDoc(Class<?> configClass, boolean urnToClassName) throws JsonProcessingException {
    JsonSchema jsonSchema = GenerateSchemas.generateSchemaFromJavaClass(configClass);
    JsonNode wrk = JSON_MAPPER.valueToTree(jsonSchema);
    if (urnToClassName) {
      wrk = replaceURNWithClass(wrk);
    }
    wrk = replaceTypeAnyWithString("", wrk);
    return wrk;
  }

  private static JsonNode replaceTypeAnyWithString(String name, JsonNode content) {
    if (content.isObject()) {
      ObjectNode rtn = JSON_MAPPER.createObjectNode();
      for (Iterator<Map.Entry<String, JsonNode>> it = content.fields(); it.hasNext(); ) {
        Map.Entry<String, JsonNode> entry = it.next();
        String key = entry.getKey();
        JsonNode value = entry.getValue();
        if ("type".equals(key) && "any".equals(value.textValue()) && !"additionalProperties".equals(name)) {
          rtn.set(key, new TextNode("string"));
        } else {
          rtn.set(key, replaceTypeAnyWithString(key, value));
        }
      }
      return rtn;
    } else if (content.isContainerNode()) {
      ArrayNode rtn = JSON_MAPPER.createArrayNode();
      for (Iterator<JsonNode> it = content.elements(); it.hasNext(); ) {
        rtn.add(replaceTypeAnyWithString("", it.next()));
      }
      return rtn;
    }
    return content;

  }

  private static JsonNode replaceURNWithClass(JsonNode content) {
    if (content.isObject()) {
      ObjectNode rtn = JSON_MAPPER.createObjectNode();
      for (Iterator<Map.Entry<String, JsonNode>> it = content.fields(); it.hasNext(); ) {
        Map.Entry<String, JsonNode> entry = it.next();
        String key = entry.getKey();
        JsonNode value = entry.getValue();
        if (key.startsWith("urn:jsonschema:")) {
          key = replaceURN(key);
        }
        if (("id".equals(key) || "$ref".equals(key)) && value.isTextual() && value.textValue().startsWith("urn:jsonschema:")) {
          String replace = replaceURN(value.textValue());
          rtn.put(key, replace);
        } else {
          rtn.set(key, replaceURNWithClass(value));
        }
      }
      return rtn;
    } else if (content.isContainerNode()) {
      ArrayNode rtn = JSON_MAPPER.createArrayNode();
      for (Iterator<JsonNode> it = content.elements(); it.hasNext(); ) {
        rtn.add(replaceURNWithClass(it.next()));
      }
      return rtn;
    }
    return content;
  }

  private static String replaceURN(String str) {
    return str.replace("urn:jsonschema:", "")
        .replace(':', '.');
  }

  public static String genJsonForAClass(Class<?> classToGenerate, boolean urnToClassName, ClassLoader resClassLoader,
                                        boolean writeYamlToBuild, Runnable handleFailure) throws IOException {
    JsonNode schemaNodes = createSchemaForDoc(classToGenerate, urnToClassName);
    if (writeYamlToBuild) {
      writeYaml("build/test/reports", classToGenerate, schemaNodes);
    }
    String content = JSON_MAPPER.writerWithDefaultPrettyPrinter()
        .writeValueAsString(schemaNodes)
        .replace("\" :","\":")
        .replace("\r","");
    if (resClassLoader != null) {
      String resName = classToGenerate.getSimpleName() + ".schema.json";
      InputStream resource = resClassLoader.getResourceAsStream(resName);
      String expected = resource == null ? "" : IOUtils.toString(resource, "utf-8");
      if (!content.equals(expected)) {
        writeJson("src/test/resources", resName, content);
        if (handleFailure != null) {
          handleFailure.run();
        }
      }
    }
    return content;
  }

  private static void writeYaml(String dirName, Class<?> classToGenerate, JsonNode schema) throws IOException {
    File yamlFile = new File(dirName + "/" + classToGenerate.getSimpleName() + ".schema.yaml");
    if (yamlFile.getParentFile().mkdirs()) {
      System.out.println("Could not create " + yamlFile.getParent());
    }
    Files.write(yamlFile.toPath(), YAML_MAPPER.writeValueAsString(schema).getBytes(StandardCharsets.UTF_8));
  }

  private static void writeJson(String dirName, String resName, String content) throws IOException {
    File outFile = new File(dirName + "/" + resName);
    if (outFile.getParentFile().mkdirs()) {
      System.out.println("Could not create " + outFile.getParent());
    }
    Files.write(outFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
  }
}