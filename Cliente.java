public class Cliente extends Thread {
  Dados dados;
  public int idCliente;
  private boolean satisfeito;

  public Cliente(int id){
    idCliente=id;
    dados=Dados.getInstance();
    satisfeito=false;
  }

  private void fazPedido(){
    dados.filaDeClientes.add(this);
  }

  private void esperaPedido() throws InterruptedException{
    //Aguarda o pedido. Quando o método notify() for chamado, essa espera será parada
    synchronized(this){
      wait();
    } 
  }

  private void recebePedido(){
    System.out.printf("Cliente %d: 'Garçom está entregando meu pedido :)'\n",idCliente);
  }

  private void consomePedido() throws InterruptedException{
    
    Double tempoDeConsumo=Math.random()*20.0+10; //Tempo de consumo pode variar de 10 à 30 minutos

    System.out.printf("Cliente %d: 'Comecei a consumir meu pedido :)'\n",idCliente);

    //Nesse programa, minutos na verdade serão segundos
    sleep(tempoDeConsumo.longValue()*1000);

    System.out.printf("Cliente %d: 'Terminei de consumir meu pedido :)'\n",idCliente);

    //20% de chance de já estar satisfeito
    if (Math.random()<0.20){
      satisfeito=true;
    }
  }

  @Override
  public void run(){
    while (dados.barEstaAberto() && !satisfeito){
      try{
        fazPedido();
        esperaPedido();
        recebePedido();
        consomePedido();
      }catch(InterruptedException e){}
    }
    System.out.printf("Cliente %d: 'Estou satisfeito, indo embora :)' \n",idCliente);
    --dados.numClientes;
  }
}
