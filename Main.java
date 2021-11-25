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

    for (int i=0;i<dados.numClientes;i++){
      new Cliente(i+1).start();  
    }

    for (int i=0;i<dados.numGarcons-1;i++){
      new Garcom(i+1).start();
    }

    //A thread principal também vai executar a função de garçom:
    new Garcom(dados.numGarcons).run();
    //O método run executa o método sobrescrito na classe sem criar uma nova thread

  }
}