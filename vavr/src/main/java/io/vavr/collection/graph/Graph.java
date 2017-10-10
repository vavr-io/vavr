package io.vavr.collection.graph;


import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

interface Graph   {

  Graph addVertex(Map<String, Object> properties);

  Graph addVertex(Vertex n);

  /**
   * Maybe represent a subgraph?
   *
   * @param query The query to apply. This might be too highlevel
   * @return a Graph representing all nodes found.
   */
  Stream<Vertex> findVertices(Predicate<Vertex> query);


  Stream<Vertex> traverse(UnaryOperator<Vertex> algo);





  Set<Vertex> getVertices();

}
