[![](https://jitpack.io/v/BrokenEarthDev/FusionYAML.svg)](https://jitpack.io/#BrokenEarthDev/FusionYAML)


<h1>FusionYAML</h1>

<h4>What is FusionYAML?</h4>
FusionYAML, formally known as SimpleYAML, is a library that eases retrieving, creating, removing, and modifying data. FusionYAML 
contains GSON-style classes and method for retrieving, creating, removing, and modifying data. FusionYAML was decided to be recreated
from scratch to support flexibility.

<h4>Why use FusionYAML</h4>
Our priority is simplicity and convenience.

<h2>Core Features</h2>
<ul>
<li>Converting YAML to JSON and vice versa</li>
<li>Contains GSON-style methods and classes for updating and retrieving data</li>
<li>Configurations</li>
</ul>

<h2>Examples</h2>
<ul>
<li>YamlElements</li>
</ul>

```java
YamlObject object = new YamlObject();
object.set("key", true);
YamlNode node = new YamlNode();
node.addChild("age", 63);
node.addChild("height", "1.77m");
object.set("Bill Gates", node);
System.out.println("YAML STRING");
System.out.println(object.toString());
System.out.println("JSON STRING");
System.out.println(object.toJsonString());
```

The above will print:
```yaml
YAML STRING
key: true
Bill Gates:
  age: 63
  height: 1.77m

JSON STRING
{"key":true,"Bill Gates":{"age":63,"height":"1.77m"}}
```

<ul>
  <li>Configurations</li>
</ul>

**FileConfiguration**

```java
File file = new File("path to file");
FileConfiguration configuration = new FileConfiguration(file);
configuration.set("dx", 321);
configuration.save(file);
```
<h5>File Data</h5>

```yaml
dx: 321
```


**WebConfiguration**

```java
WebConfiguration configuration = new WebConfiguration("url here");
configuration.set("x", 312);
configuration.save(new File("path to file"));
```

<h5>File Data</h5>

```yaml
x: 312
```
<ul>
  <li>Serialization and Deserialization</li>  
</ul>

```java
Test test = new Test(2, 4, 6);
ObjectSerializer serializer = new ObjectSerializer();
Object serialized = serializer.serialize(test);

ObjectDeserializer deserializer = new ObjectDeserializer();
Test deserializedTest = deserializer.deserialize((Map) serialized, Test.class);
```


<h2>Creating a Pull Request</h2>
There are a few requirements that you'll have to follow to get your request considered.
<ul>
  <li>Using standard java conventions</li>
  <li>Adhering to java's good practices</li>
  <li>Well documented code</li>
</ul>
In your pull request, make sure to explain why should the code be deemed useful in full detail.

<h2>Become a Collaborator</h2>
Contact me via discord if you meet one of these requirements:
<ul>
  <li>Atleast one pull request accepted from the user</li>
  <li>demonstrate appropriate java skills</li>
  <li>if you're reflxction</li>
</ul>

<h2>Planned Features</h2>
<ul>
  <li>Support serialization and deserialization</li>
  <li>Adding events</li>
  <li>Asynchronous web configuration</li>
  <li>Changing data directly on the webpage using FTP</li>
  <li>String Literals in YAML</li>
  <li>Supporting basic math operations in YAML</li>
</ul>
