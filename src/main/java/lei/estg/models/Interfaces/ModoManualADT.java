package lei.estg.models.Interfaces;

import lei.estg.models.Divisao;
import lei.estg.models.Edificio;
import lei.estg.models.Player;

public interface ModoManualADT extends JogoADT{

    public void mostrarInimigos(Edificio<Divisao> edificio);
    public void mostrarItens(Edificio<Divisao> edificio);
    public void mostrarAlvo(Player player, Edificio<Divisao> edificio);

    public void mostrarTodosCaminhosMaisProximos(Player player, Edificio<Divisao> edificio);

}
