package io.vavr.collection.graph;

import static org.junit.Assert.*;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import org.junit.Test;

/**
 * @author cbongiorno on 7/28/17.
 */
public class GraphTest {

  @Test
  public void testAddNode() throws Exception {
    Graph g = HashGraph.of(Vertex.weighted(10f).withProperty("name","christian"));
  }

  @Test
  public void testVertex() throws Exception {
    Vertex me = Vertex.v(HashMap.of("name", "christian"));
    Vertex vertex = Vertex.v().relateTo(me);

    assertTrue(vertex.relatesTo(me));

  }

  @Test
  public void testDfs() throws Exception {
    Vertex v1 = Vertex.v("id", 1).relateTo(
        Vertex.v("id", 2),
        Vertex.v("id", 3).relateTo(
            Vertex.v("id", 4),
            Vertex.v("id", 5).relateTo(
                Vertex.v("id", 6)
            )
        )
    );

    Graph g = HashGraph.of(v1);

    g.traverse()

  }
}