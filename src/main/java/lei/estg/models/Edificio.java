package lei.estg.models;

import lei.estg.dataStructures.Network;
import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.dataStructures.interfaces.EdificioADT;
import lei.estg.dataStructures.interfaces.UnorderedListADT;

import java.util.Iterator;

public class Edificio<T> extends Network<T> implements EdificioADT<T> {

    public Edificio() {
        super();
    }


    @Override
    public void addDivisao(T divisao) {
        if (numVertices == weightMatrix.length) {
            expandCapacity();
        }
        vertices[numVertices] = divisao;
        numVertices++;
    }

    @Override
    public void removeDivisao(T divisao) {
        if(indexIsValid(getIndex(divisao))) {
            for (int i = getIndex(divisao); i < numVertices - 1; i++) {
                vertices[i] = vertices[i + 1];
                for (int j = 0; j < numVertices; j++) {
                    weightMatrix[i][j] = weightMatrix[i + 1][j];
                }
            }
            numVertices--;
        }
    }

    @Override
    public boolean containsDivisao(T divisao) {
        return getIndex(divisao) != -1;
    }

    @Override
    public Iterator<T> getDivisoes() {
        UnorderedListADT<T> divisoes = new UnorderedArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            divisoes.addToRear(vertices[i]);
        }
        return divisoes.iterator();
    }

    @Override
    public void addLigacao(T divisao1, T divisao2, int peso) {
        if(indexIsValid(getIndex(divisao1)) && indexIsValid(getIndex(divisao2))) {
            weightMatrix[getIndex(divisao1)][getIndex(divisao2)] = peso;
            weightMatrix[getIndex(divisao2)][getIndex(divisao1)] = peso;
        }
    }

    @Override
    public void removeLigacao(T divisao1, T divisao2) {
        if(indexIsValid(getIndex(divisao1)) && indexIsValid(getIndex(divisao2))) {
            weightMatrix[getIndex(divisao1)][getIndex(divisao2)] = Integer.MAX_VALUE;
            weightMatrix[getIndex(divisao2)][getIndex(divisao1)] = Integer.MAX_VALUE;
        }
    }

    @Override
    public boolean existeLigacao(T divisao1, T divisao2) {
        return weightMatrix[getIndex(divisao1)][getIndex(divisao2)] != Integer.MAX_VALUE;
    }

    @Override
    public int getPesoLigacao(T divisao1, T divisao2) {
        return (int) getWeight(divisao1, divisao2);
    }

    @Override
    public Iterator<T> getAdjacentes(T divisao) {
        UnorderedListADT<T> adjacentes = new UnorderedArrayList<>();
        int index = getIndex(divisao);
        if (indexIsValid(index)) {
            for (int i = 0; i < numVertices; i++) {
                if (weightMatrix[index][i] != Integer.MAX_VALUE) {
                    adjacentes.addToRear(vertices[i]);
                }
            }
        }
        return adjacentes.iterator();
    }

    @Override
    public boolean existeCaminho(T divisao1, T divisao2) {
        boolean[] visitado = new boolean[numVertices];
        return dfs(getIndex(divisao1), getIndex(divisao2), visitado);
    }

    private boolean dfs(int index1, int index2, boolean[] visitado) {
        if (index1 == index2) {
            return true;
        }
        visitado[index1] = true;
        for (int i = 0; i < numVertices; i++) {
            if (weightMatrix[index1][i] != Integer.MAX_VALUE && !visitado[i]) {
                if (dfs(i, index2, visitado)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Iterator<T> encontrarCaminho(T divisao1, T divisao2) throws EmptyStackException {
        UnorderedListADT<T> caminho = new UnorderedArrayList<>();
        boolean[] visitado = new boolean[numVertices];
        if(dfsCaminho(getIndex(divisao1), getIndex(divisao2), visitado, caminho)) {
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
            if (weightMatrix[index1][i] != Integer.MAX_VALUE && !visitado[i]) {
                if (dfsCaminho(i, index2, visitado, caminho)) {
                    return true;
                }
            }
        }
        caminho.remove(vertices[index1]);
        return false;
    }

    @Override
    public void updateEdge(int index1, int index2, int weight) {

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
    public String toString() {
        String result = "Edificio:\n";

        // Listar divisões e suas características
        for (int i = 0; i < numVertices; i++) {
            T divisao = vertices[i];
            result += "Divisão: " + divisao.toString() + "\n";

            // Listar conexões da divisão atual
            result += "  Conexões: ";
            boolean temConexoes = false;
            for (int j = 0; j < numVertices; j++) {
                if (weightMatrix[i][j] != Integer.MAX_VALUE) {
                    temConexoes = true;
                    result += vertices[j].toString() + " (Peso: " + weightMatrix[i][j] + "), ";
                }
            }
            if (!temConexoes) {
                result += "Nenhuma";
            }
            result += "\n";

            // Verificar se a divisão é uma instância de `Divisao` para acessar inimigos e itens
            if (divisao instanceof lei.estg.models.Divisao) {
                lei.estg.models.Divisao d = (lei.estg.models.Divisao) divisao;

                // Listar inimigos
                result += "  Inimigos: ";
                Iterator<lei.estg.models.Inimigo> inimigosIterator = d.getInimigos().iterator();
                if (inimigosIterator.hasNext()) {
                    while (inimigosIterator.hasNext()) {
                        result += inimigosIterator.next().toString() + ", ";
                    }
                } else {
                    result += "Nenhum";
                }
                result += "\n";

                // Listar itens
                result += "  Itens: ";
                Iterator<lei.estg.models.Item> itensIterator = d.getItems().iterator();
                if (itensIterator.hasNext()) {
                    while (itensIterator.hasNext()) {
                        result += itensIterator.next().toString() + ", ";
                    }
                } else {
                    result += "Nenhum";
                }
                result += "\n";

                // Mostrar alvo, se existir
                if (d.getAlvo() != null) {
                    result += "  Alvo: " + d.getAlvo().toString() + "\n";
                } else {
                    result += "  Alvo: Nenhum\n";
                }
            }
        }

        return result;
    }


}
