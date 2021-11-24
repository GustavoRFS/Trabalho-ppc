import java.util.LinkedList;

import javax.swing.plaf.synth.SynthCheckBoxMenuItemUI;

public class Dados {
  private static Dados instancia;

  public volatile Integer numClientes;
  public volatile Integer numGarcons;
  public volatile Integer capAtendimento; 
  public volatile Integer numRodadas;
  public volatile Integer contadorRodadas;
  
  public volatile LinkedList<Cliente> filaDeClientes;

  public boolean existemClientesNoBar(){
    synchronized(numClientes){
      return numClientes>0;
    }
  }

  public boolean barEstaAberto(){
    synchronized(numRodadas){
      return contadorRodadas<numRodadas;
    }
  }

  //Padrão Singleton, onde essa classe só pode ter uma instância do programa
  //Bom para centralizar os dados que podem ser acessados por qualquer outra classe
  private Dados(){ 
    contadorRodadas=0;
    filaDeClientes=new LinkedList<Cliente>();
  }

  public static Dados getInstance(){
    if (instancia==null){
      instancia=new Dados();
    }
    return instancia;
  }
}
