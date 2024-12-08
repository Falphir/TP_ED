package lei.estg.models.Interfaces;

import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.Divisao;
import lei.estg.models.Edificio;
import lei.estg.models.Inimigo;
import lei.estg.models.Player;

public interface JogoADT {

    public void iniciarJogo();
    public void terminarJogo();

    public void selecionarEntrada(Player player, Edificio<Divisao> edificio);
    public void moverPlayer(Player player, Edificio<Divisao> edificio);
    public void moverInimigo(Inimigo inimigo, Edificio<Divisao> edificio) throws EmptyStackException;

    public boolean verificarFimJogo();
    public void verificarVitoria();

}
