package io.vavr.collection.graph;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

public final class Edge  {

  private final Vertex vertex;
  private final Map<String,Object> properties;

  private Edge(Vertex vertex2, Map<String,Object> properties) {
    this.properties = properties;
    this.vertex = vertex2;
  }

  /**
   * Add a property to the relationship with a weight
   *
   * @param key obvious
   * @param value obvious
   * @return this
   */
  public Edge withProperty(String key, Object value) {
    return new Edge(this.vertex,this.properties.put(key,value));
  }

  public Edge withProperties(Map<String,Object> props) {
    return new Edge(this.vertex,this.properties.merge(props));
  }

  public static Edge to(Vertex other) {
    return new Edge(other,HashMap.empty());
  }


  public Vertex getVertex() {
    return vertex;
  }
}