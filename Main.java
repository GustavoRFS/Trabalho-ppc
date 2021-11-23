//TODO: Corrigir exceptions
//TODO: Adicionar Cardápio
//TODO: Adicionar classe de pedidos do cliente
//TODO: Adicionar prints nos pontos necessário do codigo

public class Main{
  public static void main(String[] args) {
    Dados dados=Dados.getInstance();

    if (args.length<4){
      System.out.println("É necessário passar todos os argumentos!");
      return;
    }

    dados.numClientes=Integer.parseInt(args[0]);
    dados.numGarcons=Integer.parseInt(args[1]);
    dados.capAtendimento=Integer.parseInt(args[2]);
    dados.numRodadas=Integer.parseInt(args[3]);

    Thread threads[]=new Thread[dados.numClientes+dados.numGarcons-1];  

    for (int i=0;i<dados.numClientes;i++){
      Cliente cliente=new Cliente(i+1);
      cliente.start();
      threads[i]=cliente;
    }

    for (int i=0;i<dados.numGarcons-1;i++){
      Garcom garcom=new Garcom(i+1);
      garcom.start();
      threads[i+dados.numClientes]=garcom;
    }

    //A thread principal também vai executar a função de garçom:
    Garcom garcom=new Garcom(dados.numGarcons);
    garcom.run(); 
    //O método run executa o método sobrescrito na classe sem criar uma nova thread

  }
}