package lei.estg.models.Interfaces;

import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.*;

public interface JogoADT {

    public void iniciarJogo();
    public void terminarJogo(Player player, Edificio<Divisao> edificio, Missao missao);

    public Divisao selecionarEntrada(Player player, Edificio<Divisao> edificio);
    public Divisao encontrarPlayer(Player player, Edificio<Divisao> edificio);
    public void moverPlayer(Player player, Edificio<Divisao> edificio);
    public void moverInimigo(Inimigo inimigo, Edificio<Divisao> edificio) throws EmptyStackException;
    public void confronto(Player player, UnorderedArrayList<Inimigo> inimigos, Divisao divisao) throws EmptyStackException;
    public void mostrarInimigos(Edificio<Divisao> edificio);
    public void mostrarItens(Edificio<Divisao> edificio);
    public void mostrarAlvo(Edificio<Divisao> edificio);

    public void mostrarTodosCaminhosMaisProximos(Player player, Edificio<Divisao> edificio);

    public boolean verificarFimJogo(Player player, Alvo alvo, boolean playerSaiu, Missao missao);
    public void verificarVitoria();

}
