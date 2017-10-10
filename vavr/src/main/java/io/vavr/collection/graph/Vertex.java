package io.vavr.collection.graph;

import static io.vavr.collection.HashMap.empty;

import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import lombok.Getter;

interface Vertex {


  Vertex withProperty(String name, Object value);


  Vertex relateTo(Vertex other);

  Vertex relateTo(Vertex ... others);

  Seq<Edge> getEdges();

  Map<String, Object> getProperties();

  static Vertex v() {
    return new PrimitiveVertex();
  }

  Vertex withProperties(Map<String, Object> properties);

  static Vertex v(Map<String, Object> properties) {
    return new PrimitiveVertex(properties);
  }

  boolean relatesTo(Vertex other);

  static Vertex v(String key, Object value) {
    return new PrimitiveVertex(HashMap.of(key,value));
  }


  class PrimitiveVertex implements Vertex {


    @Getter
    private final Map<String, Object> properties;

    private final Map<Vertex,Edge> edges;

    private PrimitiveVertex() {
      this(empty(), empty());
    }

    private PrimitiveVertex(Map<Vertex,Edge> edges, Map<String, Object> properties) {
      this.properties = properties;
      this.edges = edges;
    }

    public PrimitiveVertex(Map<String, Object> properties) {
      this(empty(),properties);
    }

    @Override
    public boolean relatesTo(Vertex other) {
      return edges.containsKey(other);
    }

    @Override
    public Vertex withProperty(String name, Object value) {
      return new PrimitiveVertex(this.edges, this.properties.put(name, value));
    }

    @Override
    public Vertex withProperties(Map<String, Object> props) {
      return new PrimitiveVertex(this.edges, this.properties.merge(props));
    }

    @Override
    public Vertex relateTo(Vertex other) {
      return new PrimitiveVertex(this.edges.put(other, Edge.to(other)), this.properties);
    }

    @Override
    public Vertex relateTo(Vertex... others) {
      return new PrimitiveVertex(HashSet.of(others).map(o -> edges.put(o,Edge.to(o))).fold(empty(), Map::merge), this.properties);
    }

    @Override
    public Seq<Edge> getEdges() {
      return this.edges.values();
    }
  }

  static Vertex weighted(Float weight) {
    return new WeightedVertex(weight);
  }

  class WeightedVertex extends PrimitiveVertex implements Comparable<WeightedVertex> {

    private WeightedVertex(Float weight) {
      super(empty(), HashMap.of("weight", weight));
    }

    @Override
    public int compareTo(WeightedVertex o) {
      return this.getWeight().compareTo(o.getWeight());
    }

    public Float getWeight() {
      return (Float) getProperties().getOrElse("weight", null);
    }
  }

}