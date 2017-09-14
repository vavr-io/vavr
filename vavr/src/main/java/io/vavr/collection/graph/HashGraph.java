package io.vavr.collection.graph;

import static java.util.function.Function.identity;

import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import io.vavr.collection.Traversable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import lombok.Getter;

/**
 * @author cbongiorno on 7/29/17.
 */
public class HashGraph implements Graph {

  @Getter
  private HashSet<Vertex> vertices;

  private HashGraph() {
    this(HashSet.empty());
  }

  private HashGraph(HashSet<Vertex> verticies) {
    this.vertices = verticies;
  }

  private HashGraph(Vertex ... vertices) {
    this(HashSet.of(vertices));
  }


  public Graph addVertex(Map<String, Object> properties) {
    return new HashGraph(HashSet.of(Vertex.v(properties)));
  }

  @Override
  public Graph addVertex(Vertex n) {
    return null;
  }

  @Override
  public Stream<Vertex> findVertices(Predicate<Vertex> query) {
    return vertices.toStream().filter(query);
  }

  @Override
  public Stream<Vertex> traverse(UnaryOperator<Vertex> algo) {
    return null;
  }


  public static HashGraph of(Vertex ... vertices) {
    return new HashGraph(vertices);
  }

  public static HashGraph empty() {
    return  new HashGraph(HashSet.empty());
  }

  public Map<Vertex,Stream<Vertex>> dfs() {
    return vertices.toMap(identity(), this::dive);
  }

  private Stream<Vertex> dive(Vertex current) {
    return current.getEdges().toStream().map(Edge::getVertex).map(this::dive).flatMap(identity()).append(current);
  }
}
