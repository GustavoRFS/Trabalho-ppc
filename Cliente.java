public class Cliente extends Thread {
  Dados dados;
  public int idCliente;
  private boolean satisfeito;
  private int numPedidos;

  public Cliente(int id){
    idCliente=id;
    dados=Dados.getInstance();
    satisfeito=false;
    numPedidos=0;
  }

  @Override
  public String toString(){
    return String.format("Cliente %d", idCliente);
  }

  private void esperaGarcomEFazPedido() throws InterruptedException{

    synchronized(dados.filaDeClientes){
      dados.filaDeClientes.add(this);
    }

    numPedidos++;

    System.out.printf("Cliente %d: 'Aguardado garçom para fazer meu %dº pedido :)'\n",idCliente,numPedidos);
    synchronized(this){
      wait();
    }
    if (!dados.barEstaAberto()){
      System.out.printf("Cliente %d: 'Estou indo embora sem fazer meu %dº pedido, o bar está fechando :('\n",idCliente,numPedidos);
      synchronized (dados.numClientes){
        dados.numClientes--;
      }
      return;
    }
    System.out.printf("Cliente %d: 'Acabei de fazer meu %dº pedido :)'\n",idCliente,numPedidos);
  }

  private void esperaPedido() throws InterruptedException{
    System.out.printf("Cliente %d: 'Aguardando meu %dº pedido :)'\n",idCliente,numPedidos);
    //Aguarda o pedido. Quando o método notify() for chamado, essa espera será parada
    synchronized(this){
      wait();
    } 
  }

  private void recebePedido(){
    System.out.printf("Cliente %d: 'Garçom está entregando meu %dº pedido :)'\n",idCliente,numPedidos);
  }

  private void consomePedido() throws InterruptedException{

    System.out.printf("Cliente %d: 'Comecei a consumir meu %dº pedido :)'\n",idCliente,numPedidos);

    //Tempo de consumo pode variar de 2 à 10 minutos
    Double tempoDeConsumo=Math.random()*8.0+2; 

    //Nesse programa, minutos na verdade serão segundos
    sleep(tempoDeConsumo.longValue()*1000);

    System.out.printf("Cliente %d: 'Terminei de consumir meu %dº pedido :)'\n",idCliente,numPedidos);

    //20% de chance de já estar satisfeito
    if (Math.random()<0.20){
      satisfeito=true;
    }
  }

  @Override
  public void run(){
    while (dados.barEstaAberto() && !satisfeito){
      try{
        esperaGarcomEFazPedido();
        if (!dados.barEstaAberto()){
          return;
        }
        esperaPedido();
        recebePedido();
        consomePedido();
      }catch(InterruptedException e){
        e.printStackTrace();
      }
    }
    if (satisfeito){
      System.out.printf("Cliente %d: 'Estou satisfeito, indo embora :)' \n",idCliente);
    }
    else{
      System.out.printf("Cliente %d: 'Bar já está fechando :('\n",idCliente);
    }
    --dados.numClientes;
  }
}

