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

  private void recebeERegistraPedidos() throws InterruptedException{
    for (int i=0;i<dados.capAtendimento;i++){
      if (dados.filaDeClientes.size()>0){
        Cliente clienteAtual;
        synchronized (pedidosClientes){
          clienteAtual=dados.filaDeClientes.poll();
          pedidosClientes.add(clienteAtual);
        }
        
        System.out.printf("Garçom %d: 'Atendendo o cliente %d'\n",idGarcom,clienteAtual.idCliente);

        sleep(1000); //Leva 1 minuto (1 segundo no programa) para registrar um pedido
      
        System.out.printf("Garçom %d: 'Acabei de atender cliente %d'\n", idGarcom,clienteAtual.idCliente);
      }
    }
  }

  private void entregaPedidos() throws InterruptedException{
    Double tempoPreparo=Math.random()*5.0+10;

    //Leva entre 10 à 15 minutos (10 à 15 segundos no programa)
    //para preparar os pedidos de uma rodada de pedidos
    sleep(tempoPreparo.longValue()*1000); 
  
    for (int i=0;i<pedidosClientes.size();i++){
      Cliente clienteAtual;
      synchronized(pedidosClientes){
        clienteAtual=pedidosClientes.poll();
      }

      sleep(500); //Leva 500 milissegundos para entregar o pedido para cada cliente

      synchronized(clienteAtual){
        clienteAtual.notify();
      }
    
      System.out.printf("Garçom %d: 'Entreguei o pedido para o cliente %d' \n",idGarcom,clienteAtual.idCliente);
    }
  }

  @Override
  public void run(){
    while (dados.existemClientesNoBar()){ 
      if (dados.filaDeClientes.size()>0){   
        try{
          recebeERegistraPedidos();

          entregaPedidos(); 

          synchronized(dados){
            --dados.numRodadas;
          }
        }catch(InterruptedException e){}  
      } 
    } 
  }
}
