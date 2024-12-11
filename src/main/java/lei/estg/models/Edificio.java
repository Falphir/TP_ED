package lei.estg.models;

import lei.estg.dataStructures.Network;
import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.Interfaces.EdificioADT;
import lei.estg.dataStructures.interfaces.UnorderedListADT;

import java.util.Iterator;

public class Edificio<T> extends Network<T> implements EdificioADT<T> {

    public Edificio(boolean isBidirectional) {
        super();
        this.isBidirectional = isBidirectional;
    }

    public Edificio(boolean isBidirectional, int initialCapacity) {
        super();
        this.isBidirectional = isBidirectional;
        this.capacity = initialCapacity;
    }

    @Override
    public Iterator<T> getVertex() {
        UnorderedListADT<T> vertex = new UnorderedArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            vertex.addToRear(vertices[i]);
        }
        return vertex.iterator();
    }

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

    @Override
    public Iterator<T> encontrarCaminho(T divisao1, T divisao2) throws EmptyStackException {
        UnorderedListADT<T> caminho = new UnorderedArrayList<>();
        boolean[] visitado = new boolean[numVertices];
        if (dfsCaminho(getIndex(divisao1), getIndex(divisao2), visitado, caminho)) {
            return caminho.iterator();
        }
        return null;
    }

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

    @Override
    public int getNumVertices() {
        return numVertices;
    }

    /**
     * Checks whether the network contains a specific vertex.
     *
     * @param vertex the vertex to check for
     * @return true if the vertex is present in the network, false otherwise
     */
    public boolean containsVertex(T vertex) {
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
    public T getVertex(int index) {
        if (indexIsValid(index)) {
            return vertices[index];
        } else {
            throw new IndexOutOfBoundsException("Indice fora dos limites: " + index);
        }
    }

    /**
     * Retrieves the weight of the edge between two vertices.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     * @return the weight of the edge
     * @throws IllegalArgumentException if either vertex is not found
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
     * Finds the shortest path from a start vertex to an end vertex, avoiding certain locations.
     *
     * @param startVertex the index of the start vertex
     * @param endVertex   the index of the end vertex
     * @return an iterator over the indices of the vertices in the shortest path
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

        // Algoritmo de Dijkstra
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
