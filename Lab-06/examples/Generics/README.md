# Generics Demo

A small demonstration of Java generics (a topic beyond the Core material —
see the course's enrichment deep-dives):

- `AnyBox<T>` — an unbounded generic container that holds any type.
- `SubtypeBox<T extends Animal>` — an upper-bounded type parameter: only
  `Animal` and its subclasses (`Dog`, `Cat`) are allowed.
- `AncestorWriter<T>` — lower-bounded wildcards (`? super T`) on method
  parameters, following the PECS rule (Producer Extends, Consumer Super).
- `Demo` — a main program exercising all three.

## Build and Run

```bash
javac -d out *.java
java -cp out Demo
```
