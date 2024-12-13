package lei.estg.models.Interfaces;

import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.dataStructures.interfaces.NetworkADT;

import java.util.Iterator;

/**
 * The interface Edificio adt.
 *
 * @param <T> the type parameter
 */
public interface EdificioADT<T> extends NetworkADT<T> {

    /**
     * Retrieves all vertices in the network.
     * This method collects all vertices in the network and returns them as an iterator.
     *
     * @return an iterator over all vertices in the network
     */
    public Iterator<T> getVertex();

    /**
     * Retrieves the adjacent vertices of a given vertex.
     * This method finds all vertices that are directly connected to the specified vertex
     * and returns them as an iterator.
     *
     * @param divisao the vertex for which to find adjacent vertices
     * @return an iterator over the adjacent vertices of the specified vertex
     */
    public Iterator<T> getAdjacentes(T divisao);

    /**
     * Finds a path between two vertices in the network.
     * This method uses depth-first search (DFS) to find a path from the start vertex to the end vertex.
     *
     * @param divisao1 the starting vertex
     * @param divisao2 the ending vertex
     * @return an iterator over the vertices in the path from divisao1 to divisao2, or null if no path is found
     * @throws EmptyStackException if the stack used in DFS is empty
     */
    public Iterator<T> encontrarCaminho(T divisao1, T divisao2) throws EmptyStackException;

    /**
     * Updates the weight of the edge between two vertices.
     * This method sets the weight of the edge between the specified vertices.
     * If the network is bidirectional, it also updates the weight of the edge in the opposite direction.
     *
     * @param index1 the index of the first vertex
     * @param index2 the index of the second vertex
     * @param weight the new weight of the edge
     * @throws IllegalArgumentException if one or both vertices are not found in the network
     */
    public void updateEdge(int index1, int index2, double weight);

    /**
     * Retrieves the number of vertices in the network.
     *
     * @return the number of vertices in the network
     */
    public int getNumVertices();

    /**
     * Retrieves the weight of the edge between two vertices.
     * This method returns the weight of the edge connecting the specified vertices.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     * @return the weight of the edge between the specified vertices
     * @throws IllegalArgumentException if either vertex is not found in the network
     */
    public double getWeight(T vertex1, T vertex2);

    /**
     * Finds the shortest path from a start vertex to an end vertex.
     * This method uses Dijkstra's algorithm to find the shortest path between two vertices in the network.
     *
     * @param startVertex the starting vertex
     * @param endVertex the ending vertex
     * @return an iterator over the vertices in the shortest path from startVertex to endVertex, or null if no path is found
     */
    public Iterator<T> findShortestPath(T startVertex, T endVertex);
}
