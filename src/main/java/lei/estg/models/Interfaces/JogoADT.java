package lei.estg.models.Interfaces;

import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.*;

public interface JogoADT {

    public void iniciarJogo();
    public void terminarJogo();

    public Divisao selecionarEntrada(Player player, Edificio<Divisao> edificio);
    public void moverPlayer(Player player, Edificio<Divisao> edificio);
    public void moverInimigo(Inimigo inimigo, Edificio<Divisao> edificio) throws EmptyStackException;
    public void confronto(Player player, UnorderedArrayList<Inimigo> inimigos, Divisao divisao) throws EmptyStackException;

    public boolean verificarFimJogo(Player player, Alvo alvo, boolean playerSaiu);
    public void verificarVitoria();

}
