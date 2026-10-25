## 📘 Javadoc Demo

This project demonstrates how to use **Javadoc comments** in a simple multi-class Java application and generate HTML documentation from them.

---

### 🧱 Project Structure

```
SimpleJavadocDemo/
 ├── src/
 │    └── com/example/docdemo/
 │         ├── Main.java
 │         ├── Calculator.java
 │         └── GeometryUtils.java
 └── README.md
```

---

### 🚀 Running the Program

To compile and run the example:

```bash
cd SimpleJavadocDemo
javac -d out src/com/example/docdemo/*.java
java -cp out com.example.docdemo.Main
```

Expected output:

```
5 + 3 = 8.0
Area of circle with radius 2.5 = 19.634954084936208
```

---

### 🧭 Generating Documentation

To generate HTML documentation from the Javadoc comments, run:

```bash
javadoc -d docs -sourcepath src com.example.docdemo
```

This command means:

* `-d docs` → place generated docs in the `docs/` folder
* `-sourcepath src` → source files are under the `src` directory
* `com.example.docdemo` → generate documentation for this package

---

### 🌐 Viewing the Documentation

After generation, open the main page in your browser:

```bash
open docs/index.html     # macOS
xdg-open docs/index.html # Linux
start docs\index.html    # Windows
```

You’ll see linked documentation for:

* `Main` — program entry point
* `Calculator` — basic arithmetic
* `GeometryUtils` — geometry utilities

---

### 🏷️ Demonstrated Javadoc Features

| Tag                   | Description                 | Example                                                 |
| --------------------- | --------------------------- | ------------------------------------------------------- |
| `@param`              | Documents method parameters | `add(double a, double b)`                               |
| `@return`             | Describes what is returned  | `add` and `circleArea` methods                          |
| `@throws`             | Declares exceptions         | `divide` and `circleArea`                               |
| `@see`                | Links related classes       | Links between `Main`, `Calculator`, and `GeometryUtils` |
| `@version`, `@author` | Metadata                    | Top of each class                                       |
| HTML tags             | Formatting                  | `<p>`, `<em>`, `<code>`                                 |

---

### 🧩 Notes

* Any `.java` file with Javadoc comments can be included.
* The generated HTML includes:

  * Class summaries
  * Method descriptions
  * Parameter and return details
  * Links between related classes
