# java_examples
Some example java code. All of the technical bits are wrapped up in library modules and are demonstrated in some application module.

## Examples
### suggestion
a library allowing for the synchronous input and retrieval of character-based suggestions matching a prefix via a ```Suggester```

Usage:
```java
Suggester suggester = new Suggester();
suggester.addWordToSuggestions("a");
suggester.addWordToSuggestions("aardvark");
suggester.addWordToSuggestions("aaron");
List<String> suggestions = suggester.suggest("a");    // <-- returns ["a", "aardvark", "aaron"]
suggestions = suggester.suggest("aar");    // <-- returns ["aardvark", "aaron"]
suggestions = suggester.suggest("aaro");    // <-- returns ["aaron"]
suggestions = suggester.suggest("aaron");    // <-- returns ["aaron"]
suggestions = suggester.suggest("b");    // <-- returns []
```
Demonstrated by suggesterfx (a JavaFX desktop application)
### suggesterfx
a JavaFX desktop application demonstrating usage of suggestion seeded with 109582 English words

Usage (from the project's base directory):
```
$> ./gradlew :suggesterfx:run
```
Example:

![Screenshot](https://github.com/ryansgot/java_examples/blob/master/suggesterfx/readme_images/sample.png)
