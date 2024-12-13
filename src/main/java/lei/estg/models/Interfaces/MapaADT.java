package lei.estg.models.Interfaces;

import lei.estg.models.Divisao;
import lei.estg.models.Edificio;


public interface MapaADT {
    /**
     * Displays the building map using GraphStream.
     * This method initializes a GraphStream graph, adds nodes for each division in the building,
     * and creates edges between adjacent divisions. It also applies a stylesheet for visual styling
     * and displays the graph.
     *
     * @param edificio the building containing the divisions and their connections
     */
    public void mostrarMapa(Edificio<Divisao> edificio);
}
