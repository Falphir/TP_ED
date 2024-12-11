package lei.estg.models.Interfaces;

import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.*;

public interface JogoADT {
    public void terminarJogo(Player player, Edificio<Divisao> edificio);
    public Divisao selecionarEntrada(Player player, Edificio<Divisao> edificio);
    public Divisao encontrarPlayer(Player player, Edificio<Divisao> edificio);
    public void moverPlayer(Player player, Edificio<Divisao> edificio);
    public void moverInimigo(Inimigo inimigo, Edificio<Divisao> edificio) throws EmptyStackException;
    public void confronto(Player player, Edificio<Divisao> edificio ,UnorderedArrayList<Inimigo> inimigos, Divisao divisao) throws EmptyStackException;
    public boolean verificarFimJogo(Player player, Alvo alvo, boolean playerSaiu);

}
