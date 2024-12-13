package lei.estg.models;

import lei.estg.dataStructures.Network;
import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.Interfaces.EdificioADT;
import lei.estg.dataStructures.interfaces.UnorderedListADT;

import java.util.Iterator;

/**
 * The type Edificio.
 *
 * @param <T> the type parameter
 */
public class Edificio<T> extends Network<T> implements EdificioADT<T> {

    /**
     * Instantiates a new Edificio.
     *
     * @param isBidirectional the is bidirectional
     */
    public Edificio(boolean isBidirectional) {
        super();
        this.isBidirectional = isBidirectional;
    }

    /**
     * Retrieves all vertices in the network.
     * This method collects all vertices in the network and returns them as an iterator.
     *
     * @return an iterator over all vertices in the network
     */
    @Override
    public Iterator<T> getVertex() {
        UnorderedListADT<T> vertex = new UnorderedArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            vertex.addToRear(vertices[i]);
        }
        return vertex.iterator();
    }

    /**
     * Retrieves the adjacent vertices of a given vertex.
     * This method finds all vertices that are directly connected to the specified vertex
     * and returns them as an iterator.
     *
     * @param divisao the vertex for which to find adjacent vertices
     * @return an iterator over the adjacent vertices of the specified vertex
     */
    @Override
    public Iterator<T> getAdjacentes(T divisao) {
        UnorderedListADT<T> adjacentes = new UnorderedArrayList<>();
        int index = getIndex(divisao);
        if (indexIsValid(index)) {
            for (int i = 0; i < numVertices; i++) {
                if (weightMatrix[index][i] != Double.POSITIVE_INFINITY && index != i) {
                    adjacentes.addToRear(vertices[i]);
                }
            }
        }
        return adjacentes.iterator();
    }

    /**
     * Finds a path between two vertices in the network.
     * This method uses depth-first search (DFS) to find a path from the start vertex to the end vertex.
     *
     * @param divisao1 the starting vertex
     * @param divisao2 the ending vertex
     * @return an iterator over the vertices in the path from divisao1 to divisao2, or null if no path is found
     * @throws EmptyStackException if the stack used in DFS is empty
     */
    @Override
    public Iterator<T> encontrarCaminho(T divisao1, T divisao2) throws EmptyStackException {
        UnorderedListADT<T> caminho = new UnorderedArrayList<>();
        boolean[] visitado = new boolean[numVertices];
        if (dfsCaminho(getIndex(divisao1), getIndex(divisao2), visitado, caminho)) {
            return caminho.iterator();
        }
        return null;
    }

    /**
     * Performs a depth-first search (DFS) to find a path between two vertices.
     * This method recursively explores the network to find a path from the start vertex to the end vertex.
     *
     * @param index1   the index of the starting vertex
     * @param index2   the index of the ending vertex
     * @param visitado an array indicating whether each vertex has been visited
     * @param caminho  a list to store the path of vertices
     * @return true if a path is found, false otherwise
     * @throws EmptyStackException if the stack used in DFS is empty
     */
    private boolean dfsCaminho(int index1, int index2, boolean[] visitado, UnorderedListADT<T> caminho) throws EmptyStackException {
        visitado[index1] = true;
        caminho.addToRear(vertices[index1]);
        if (index1 == index2) {
            return true;
        }
        for (int i = 0; i < numVertices; i++) {
            if (weightMatrix[index1][i] != Double.POSITIVE_INFINITY && !visitado[i]) {
                if (dfsCaminho(i, index2, visitado, caminho)) {
                    return true;
                }
            }
        }
        caminho.remove(vertices[index1]);
        return false;
    }

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
    @Override
    public void updateEdge(int index1, int index2, double weight) {

        if (indexIsValid(index1) && indexIsValid(index2)) {
            weightMatrix[index1][index2] = weight;

            if (isBidirectional) {
                weightMatrix[index2][index1] = weight;
            }
        } else {
            throw new IllegalArgumentException("Um ou ambos os vértices não foram encontrados na rede.");
        }
    }

    /**
     * Retrieves the number of vertices in the network.
     *
     * @return the number of vertices in the network
     */
    @Override
    public int getNumVertices() {
        return numVertices;
    }

    /**
     * Checks whether the network contains a specific vertex.
     * This method iterates through all vertices in the network to determine if the specified vertex is present.
     *
     * @param vertex the vertex to check for
     * @return true if the vertex is present in the network, false otherwise
     */
    private boolean containsVertex(T vertex) {
        for (int i = 0; i < numVertices; i++) {
            if (vertex.equals(vertices[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the vertex at a given index.
     *
     * @param index the index of the vertex
     * @return the vertex at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    private T getVertex(int index) {
        if (indexIsValid(index)) {
            return vertices[index];
        } else {
            throw new IndexOutOfBoundsException("Indice fora dos limites: " + index);
        }
    }

    /**
     * Retrieves the weight of the edge between two vertices.
     * This method returns the weight of the edge connecting the specified vertices.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     * @return the weight of the edge between the specified vertices
     * @throws IllegalArgumentException if either vertex is not found in the network
     */
    public double getWeight(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if (indexIsValid(index1) && indexIsValid(index2)) {
            return weightMatrix[index1][index2];
        } else {
            throw new IllegalArgumentException("Vértice não encontrado");
        }
    }

    /**
     * Finds the shortest path from a start vertex to an end vertex.
     * This method uses Dijkstra's algorithm to find the shortest path between two vertices in the network.
     *
     * @param startVertex the starting vertex
     * @param endVertex the ending vertex
     * @return an iterator over the vertices in the shortest path from startVertex to endVertex, or null if no path is found
     */
    public Iterator<T> findShortestPath(T startVertex, T endVertex){
        int startIndex = getIndex(startVertex);
        int targetIndex = getIndex(endVertex);
        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex)) {
            return null;
        }
        double[] distances = new double[numVertices];
        boolean[] visited = new boolean[numVertices];
        int[] previous = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            distances[i] = Double.POSITIVE_INFINITY;
            visited[i] = false;
            previous[i] = -1;
        }

        distances[startIndex] = 0;

        for (int i = 0; i < numVertices - 1; i++) {
            int u = minDistance(distances, visited);
            visited[u] = true;

            for (int v = 0; v < numVertices; v++) {
                if (!visited[v] && adjMatrix[u][v] && distances[u] != Double.POSITIVE_INFINITY
                        && distances[u] + weightMatrix[u][v] < distances[v]) {
                    distances[v] = distances[u] + weightMatrix[u][v];
                    previous[v] = u;
                }
            }
        }
        UnorderedArrayList<T> path = new UnorderedArrayList<>();
        for (int at = targetIndex; at != -1; at = previous[at]) {
            path.addToFront(getVertex(at));
        }

        if (path.first() != getVertex(startIndex)) {
            return null;
        }

        return path.iterator();
    }

}
