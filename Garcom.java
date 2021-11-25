import java.util.LinkedList;

public class Garcom extends Thread {
  private Dados dados;
  public int idGarcom;
  public LinkedList<Cliente> pedidosClientes;

  public Garcom(int id){
    dados=Dados.getInstance();
    idGarcom=id;
    pedidosClientes=new LinkedList<Cliente>();
  }

  @Override
  public String toString(){
    return String.format("Garcom %d", idGarcom);
  }

  private void despedirCliente(Cliente cliente){
    System.out.printf("Garçom %d: 'Despedindo cliente %d :('\n",idGarcom,cliente.idCliente);
    synchronized (cliente){
      cliente.notify();
    }
  }

  private void fecharBar(){
    int quantidadeClientes=dados.filaDeClientes.size();

    if (quantidadeClientes<dados.numGarcons){
      if (idGarcom==1){
        for (int i=0;i<quantidadeClientes;i++){
          despedirCliente(dados.filaDeClientes.get(i));
        }
      }
    }
    else{
      //Cada garçom vai despedir uma certa quantidade de clientes
      int quantidadeADespedir=quantidadeClientes/dados.numGarcons;
      
      //Se a quantidade de clientes for impar, o ultimo garçom despede um cliente
      // a mais que os outros garçons 
      if (quantidadeClientes%2==1 && idGarcom==dados.numGarcons){
        for (int i=(idGarcom-1)*quantidadeADespedir;i<idGarcom*quantidadeADespedir+1;i++){
          System.out.println(i);
          despedirCliente(dados.filaDeClientes.get(i));
        }
      }
      else{
        for (int i=(idGarcom-1)*quantidadeADespedir;i<idGarcom*quantidadeADespedir;i++){
          System.out.println(i);
          despedirCliente(dados.filaDeClientes.get(i));
        }
      }
    }
  }

  private void recebeERegistraPedidos() throws InterruptedException{
    int clientesASeremAtendidos;
    synchronized(dados.capAtendimento){
      synchronized(dados.filaDeClientes){
        clientesASeremAtendidos=Math.min(dados.capAtendimento, dados.filaDeClientes.size());
      }
    }
    
    for (int i=0;i<clientesASeremAtendidos;i++){

      sleep(1000); //Leva um minuto para registrar os pedidos dos clientes
    
      synchronized (dados.filaDeClientes){
        if (dados.filaDeClientes.size()>0){
          Cliente clienteAtual=dados.filaDeClientes.poll();
          pedidosClientes.add(clienteAtual);
          
          synchronized (clienteAtual){
            clienteAtual.notify();
          }
          
          System.out.printf("Garçom %d: 'Atendendo o cliente %d'\n",idGarcom,clienteAtual.idCliente);

          sleep(1000); //Leva 1 minuto (1 segundo no programa) para registrar um pedido
          
          System.out.printf("Garçom %d: 'Acabei de atender cliente %d'\n", idGarcom,clienteAtual.idCliente);      
        }
      }
    }
  }

  private void entregaPedidos() throws InterruptedException{
    Double tempoPreparo=Math.random()*5.0+10;

    //Leva entre 10 à 15 minutos (10 à 15 segundos no programa)
    //para preparar os pedidos de uma rodada de pedidos
    sleep(tempoPreparo.longValue()*1000); 
  
    int quantidadeClientes=pedidosClientes.size();
    
    for (int i=0;i<quantidadeClientes;i++){
      Cliente clienteAtual=pedidosClientes.poll();

      sleep(500); //Leva 0.5 minutos para entregar o pedido para cada cliente

      synchronized(clienteAtual){
        System.out.printf("Garçom %d: 'Entregando o pedido para o cliente %d' \n",idGarcom,clienteAtual.idCliente);
        clienteAtual.notify();
      }
    
      System.out.printf("Garçom %d: 'Entreguei o pedido para o cliente %d' \n",idGarcom,clienteAtual.idCliente);
    }
  }

  @Override
  public void run(){
    while (dados.existemClientesNoBar()){ 
      int quantidadeClientes;
      synchronized (dados.filaDeClientes){
        quantidadeClientes=dados.filaDeClientes.size();
      }
      if (!dados.barEstaAberto()){
        fecharBar();
        return;
      }
      else if (quantidadeClientes>0){   
        try{
          int rodadaAtual;
          synchronized(dados.contadorRodadas){
            dados.contadorRodadas++; 
            rodadaAtual=dados.contadorRodadas;
            System.out.printf("Garçom %d: Início da rodada %d\n",idGarcom,rodadaAtual);
          }
          recebeERegistraPedidos();

          entregaPedidos(); 

          System.out.printf("Garçom %d: Fim da rodada %d\n",idGarcom,rodadaAtual);

          if (!dados.barEstaAberto()){
            return;
          }
          //Aguarda entre 15 e 35 minutos para próxima rodada
          Double tempoParaProximaRodada=Math.random()*20.0+15;
          sleep((tempoParaProximaRodada.longValue())*1000); 
        }catch(InterruptedException e){
          e.printStackTrace();
        }  
      }
    }
  }
}
