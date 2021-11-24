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

    int maximoClientes=dados.capAtendimento*dados.numRodadas;

    if (dados.numClientes>maximoClientes){
      System.out.printf("O número máximo de clientes (%d) foi atingido. %d não puderam entrar no bar\n",maximoClientes,dados.numClientes-maximoClientes);
      dados.numClientes=maximoClientes;
    }

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