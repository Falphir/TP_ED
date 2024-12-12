package lei.estg.utils;

import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.models.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Iterator;

public class Mapa {
    private Graph graphStream;

    public void mostrarMapa(Edificio<Divisao> edificio) {
        graphStream = new SingleGraph("Building Map");

        String styleSheet = "node { size: 20px; fill-color: red; }"
                + "edge { size: 2px; fill-color: black; }";

        Iterator vertices = edificio.getVertex();
        while (vertices.hasNext()) {
            Divisao divisao = (Divisao) vertices.next();
            if (divisao != null) {
                String nodeId = divisao.getNome().replaceAll("\\s+", "_");
                graphStream.addNode(nodeId);
                graphStream.getNode(nodeId).addAttribute("ui.label", divisao.getNome());
            }
        }

        Iterator vertices2 = edificio.getVertex();
        while (vertices2.hasNext()) {
            Divisao divisao = (Divisao) vertices2.next();
            if (divisao != null) {
                Iterator<Divisao> vizinhos = edificio.getAdjacentes(divisao);
                while (vizinhos.hasNext()) {
                    Divisao vizinho = (Divisao) vizinhos.next();
                    String nodeId1 = divisao.getNome().replaceAll("\\s+", "_");
                    String nodeId2 = vizinho.getNome().replaceAll("\\s+", "_");
                    String edgeId = nodeId1 + "_" + nodeId2;

                    if (graphStream.getEdge(edgeId) == null && graphStream.getEdge(nodeId2 + "_" + nodeId1) == null) {
                        graphStream.addEdge(edgeId, nodeId1, nodeId2);
                    }
                }
            }
        }

        graphStream.addAttribute("ui.stylesheet", styleSheet);
        graphStream.addAttribute("ui.quality");
        graphStream.addAttribute("ui.antialias");
        graphStream.display();
    }
}
