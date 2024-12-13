package lei.estg.dataStructures;

import lei.estg.dataStructures.interfaces.NetworkADT;

import java.util.Iterator;

/**
 * The Network class represents a network data structure, extending the functionality of a graph.
 * It manages a set of vertices connected by edges, where each edge has an associated weight.
 * This class also supports bidirectional edges and operations to find the shortest path and
 * shortest path weight in the network.
 *
 * @param <T> the type of elements held in this network
 */
public class Network<T> extends Graph<T> implements NetworkADT<T> {

    /**
     * Matrix to store the weights of the edges.
     */
    protected double[][] weightMatrix;

    /**
     * Indicates whether the network is bidirectional.
     */
    protected boolean isBidirectional;

    /**
     * Default constructor that initializes the network with a default capacity.
     */
    public Network() {
        super();
        weightMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        initializeWeightMatrix();
    }

    /**
     * Constructor that initializes the network as either bidirectional or unidirectional.
     *
     * @param isBidirectional true if the network is bidirectional, false otherwise
     */
    public Network(boolean isBidirectional) {
        super();
        this.isBidirectional = isBidirectional;
        weightMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        initializeWeightMatrix();
    }

    /**
     * Initializes the weight matrix with default values.
     * The weight for an edge from a vertex to itself is set to 0, and to Double.POSITIVE_INFINITY for all other edges.
     */
    private void initializeWeightMatrix() {
        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            for (int j = 0; j < DEFAULT_CAPACITY; j++) {
                weightMatrix[i][j] = Double.POSITIVE_INFINITY;
            }
        }
    }

    /**
     * Expands the capacity of the network when necessary.
     */
    @Override
    protected void expandCapacity() {
        super.expandCapacity();

        double[][] largerWeightMatrix = new double[vertices.length][vertices.length];

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                largerWeightMatrix[i][j] = weightMatrix[i][j];
            }
        }

        for (int i = 0; i < vertices.length; i++) {
            for (int j = 0; j < vertices.length; j++) {
                if (i >= numVertices || j >= numVertices) {
                    largerWeightMatrix[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }

        weightMatrix = largerWeightMatrix;
    }

    /**
     * Adds an edge between two vertices with a specified weight.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     * @param weight  the weight of the edge
     */
    @Override
    public void addEdge(T vertex1, T vertex2, double weight) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = true;
            weightMatrix[index1][index2] = weight;

            if (isBidirectional) {
                adjMatrix[index2][index1] = true;
                weightMatrix[index2][index1] = weight;
            }
        }
    }

    /**
     * Calculates the weight of the shortest path between two vertices.
     *
     * @param startVertex the starting vertex
     * @param targetVertex the target vertex
     * @return the weight of the shortest path
     */
    @Override
    public double shortestPathWeight(T startVertex, T targetVertex) {
        int startIndex = getIndex(startVertex);
        int targetIndex = getIndex(targetVertex);
        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex)) {
            return Double.POSITIVE_INFINITY;
        }
        double[] distances = new double[numVertices];
        boolean[] visited = new boolean[numVertices];

        for (int i = 0; i < numVertices; i++) {
            distances[i] = Double.POSITIVE_INFINITY;
            visited[i] = false;
        }

        distances[startIndex] = 0;

        for (int i = 0; i < numVertices - 1; i++) {
            int u = minDistance(distances, visited);
            visited[u] = true;

            for (int v = 0; v < numVertices; v++) {

                if (!visited[v] && adjMatrix[u][v] && distances[u] != Double.POSITIVE_INFINITY
                        && distances[u] + weightMatrix[u][v] < distances[v]) {
                    distances[v] = distances[u] + weightMatrix[u][v];
                }
            }
        }
        return distances[targetIndex];
    }

    /**
     * Finds the vertex with the minimum distance that has not been visited.
     *
     * @param distances an array of distances to each vertex
     * @param visited   an array indicating whether each vertex has been visited
     * @return the index of the vertex with the minimum distance
     */
    protected int minDistance(double[] distances, boolean[] visited) {
        double min = Double.POSITIVE_INFINITY;
        int minIndex = -1;

        for (int v = 0; v < distances.length; v++) {
            if (!visited[v] && distances[v] < min) {
                min = distances[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    @Override
    public String toString() {
        String result = "Network:\n";
        result += "Bidirectional: " + isBidirectional + "\n";
        result += "Vertices: " + numVertices + "\n";

        result += "Vertices List:\n";
        for (int i = 0; i < numVertices; i++) {
            result += "[" + i + "] " + vertices[i] + "\n";
        }

        result += "\nEdges and Weights:\n";
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (adjMatrix[i][j]) {
                    result += "Edge: " + vertices[i] + " -> " + vertices[j];
                    result += ", Weight: " + weightMatrix[i][j] + "\n";
                }
            }
        }
        return result;
    }
}