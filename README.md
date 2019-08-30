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

```
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
