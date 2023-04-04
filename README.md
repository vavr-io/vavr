# Experimental fork of 'vavr' with CHAMP collections

## Status

This is an experimental fork of [github.com/vavr-io/vavr](https://github.com/vavr-io/vavr).

## CHAMP collections

This fork contains additional collections, that use
CHAMP (Compressed Hash-Array Mapped Prefix-tree) as their underlying data structures.
The collections are derived from [github.com/usethesource/capsule](https://github.com/usethesource/capsule),
and [github.com/wrandelshofer/jhotdraw8](https://github.com/wrandelshofer/jhotdraw8).

The collections are:

* ChampSet
* ChampMap
* SequencedChampSet
* SequencedChampMap

Each collection has a mutable partner:

* MutableChampSet
* MutableChampMap
* MutableSequencedChampSet
* MutableSequencedChampMap

## Performance characteristics

### ChampSet, ChampMap, MutableChampSet, MutableChampMap:

* Maximal supported size: 2<sup>30</sup> elements.
* Get/Insert/Remove: O(1)
* Head/Tail: O(1)
* Iterator creation: O(1)
* Iterator.next(): O(1)
* toImmutable/toMutable: O(1) + a cost distributed across subsequent updates of the mutable copy

The costs are only constant in the limit. In practice, they are more like
O(log<sub>32</sub> N).

If a collection is converted from/to immutable/mutable, the mutual copy
of the collection loses ownership of all its trie nodes. Updates are slightly
more expensive for the mutable copy, until it gains exclusive ownership of all trie
nodes again.

### SequencedChampSet, SequencedChampMap, MutableSequencedChampSet, MutableSequencedChampMap:

* Maximal supported size: 2<sup>30</sup> elements.
* Get/Insert/Remove: O(1) amortized
* Head/Tail: O(1)
* Iterator creation: O(1)
* Iterator.next(): O(1)
* toImmutable/toMutable: O(1) + a cost distributed across subsequent updates of the mutable copy

The collections store a sequence number with each data element, and they maintain
a second CHAMP trie, that is indexed by the sequence number.
The sequence numbers must be renumbered from time to time, to prevent
large gaps and overflows/underflows.

To support iteration over the elements, we maintain a second CHAMP trie, which
uses the sequence number as the key to the elements.


## Benchmarks

The following chart shows a comparison of the CHAMP maps with vavr collections
and with Scala collections. Scala org.scala-lang:scala-library:2.13.10 was used.

The collections have 1 million entries.

The y-axis is labeled in nanoseconds. The bars are cut off at 1'500 ns (!).
This cuts off the elapsed times of functions that run in linear times.

![](BenchmarkChart.png)

* **scala.HashMap**<br>
  Uses a CHAMP trie as its underlying data structure.<br>
  Performs all operations in constant time.<br>
  Iterates in an unspecified sequence.<br>
  This is the fastest implementation except for the contains() operation.
* **scala.VectorMap** <br>
  Uses a CHAMP trie and a radix-balanced finger tree (Vector) as its
  underlying data structures.<br>
  Performs all operations in amortised constant time.<br>
  Iterates in the sequence in which the entries were inserted in the map.<br>
  May hog memory, because deleted elements are replaced by tomb stone objects.<br>
  This is one of the fastest implementation except for iteration.
* **scala.TreeSeqMap** <br>
  Uses a CHAMP trie and a red-black tree as its underlying data structures.<br>
  Performs all operations in amortised constant time.<br>
  Iterates in the sequence in which the entries were inserted in the map.<br>
  This is one of the slowest implementations.
* **vavr.HashMap**<br>
  Uses a HAMP trie as its underlying data structure.<br>
  Performs all operations in constant time.<br>
  This is one of the fastest implementations.
* **vavr.LinkedHashMap** <br>
  Uses a HAMP trie and a Banker's queue as its underlying data structure.<br>
  Performs some operations in constant time and some in linear time.<br>
  Iterates in the sequence in which the entries were inserted in the map.<br>
  This implementation is the fastest for read operations, but the
  slowest for update operations.
* **vavr.ChampMap** <br>
  Uses a CHAMP trie as its underlying data structure.<br>
  Performs all operations in constant time.<br>
  Iterates in an unspecified sequence.<br>
* **vavr.SequencedChampMap**<br>
  Uses two CHAMP tries as its underlying data structures.<br>
  Performs all operations in amortised constant time.<br>
  Iterates in the sequence in which the entries were inserted in the map.<br>
  This class has the same design trade-offs like scala.TreeSeqMap.<br>
  This is one of the slower implementations - it beats scala.TreeSeqMap
  except for head() and tail() operations.
 

