We need to follow some conventions to keep the collections consistent.

There are two kinds of methods

- **Accessors** access some of the elements of a collection, but return a result which is unrelated to the collection.
  Example of accessors are: head, foldLeft, toList.

- **Transformers** access elements of a collection and produce a new collection of same type as a result.
  Example of transformers are: filter, map, groupBy, zip.

Given this, our _transformers_ should return the most specific type, e.g. `HashMap.map(...)` should return `HashMap`. E.g. `zipWithIndex` is also a transformer.

Our _accessors_ should return  an interface instead to keep our library extensible for future enhancements. E.g. `HashMap.groupBy` should return `Map` because we may decide in future that returning another Map implementation than HashMap is more efficient. Returning now HashMap would break API on future changes. The only disadvantage is that Map's methods have generic parameters with wildcards, which reduces usability slightly.

More specific, methods like `Seq.combinations()` or `Seq.crossProduct()` should return `Seq`. This applies also to subclasses of `Seq`. E.g. `CharSeq.combinations()` should also return `Seq`. Of course the concrete object returned remains `Vector` (for CharSeq).
