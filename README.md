# Moshi IR and Moshi KSP Comparison

This project can be configured with either moshi IR or moshi KSP codegen to compare how they treat @JsonQualifiers with
different use-site targets. Uncomment the right configuration in build.gradle and run main to see the output for each.

Take this class:

```kotlin
@JsonClass(generateAdapter = true)
data class SerializableDto(
    @param:TestJsonQualifier
    val param: String,
    @field:TestJsonQualifier
    val field: String,
    @get:TestJsonQualifier
    val get: String,
    @TestJsonQualifier
    val default: String,
    @set:TestJsonQualifier
    var set: String,
)
```

And a custom adapter like this:

```kotlin
@ToJson
fun toJson(@TestJsonQualifier string: String): String {
    return "to qualified"
}
```

Using Moshi KSP, the class is output as:

```json
{
  "param": "to qualified",
  "field": "to qualified",
  "get": "get",
  "default": "to qualified",
  "set": "set"
}
```

Using Moshi IR, the output is:

```json
{
  "param": "to qualified",
  "field": "field",
  "get": "get",
  "default": "to qualified",
  "set": "set"
}
```

Moshi KSP will use the custom adapter when `@field:` is used, but Moshi IR uses the default serializer. It's unclear 
whether that use-site target should work or not.   