O Programa executa as funções de garçons e clientes em um bar, onde cada garçom e cliente é representado por uma thread.
Os clientes entram na fila de espera, esperando pelo atendimento de um garçom. Após serem atendidos pelos garçons, os clientes entram na fila de espera dos pedidos (Fila armazenada por cada garçom). Após esperar os pedidos, os clientes consomem esse pedido, o que pode levar entre 2 à 10 minutos (segundos em tempo de execução do programa) e, por fim, os clientes têm 20% de chance de ficarem satisfeitos após consumirem os pedidos e irem embora.
Os garçons atendem os clientes uma quantidade R de rodadas, entregam os pedidos após esperar a preparação do pedido, o que pode levar entre 10 à 15 minutos (segundo em tempo de execução do programa) e, por fim, entrega o pedido aos clientes. Após finalizar todas as rodadas do bar, cada garçom despede um certo número de clientes que ficaram na fila de espera do atendimento.

---

Foi utilizada a forma de MultiThread padrão do Java, que é utilizando a classe Thread

---

Para executar o programa, basta compilar com o comando "javac \*.java" e executar com o comando "java Main n x c r", onde os parâmetros a,b,c e d são Número de clientes, Número de garçons, Capacidade de atendimento dos garçons e número de rodadas do bar, respectivamente.

---

Todos os tempos do programa leva em consideração que 1 segundo no programa representa 1 minuto da vida real.

---

Foi adicionada uma lógica para limitar a quantidade de clientes, que é calculada da seguinte forma: capacidade de atendimentos número de rodadas Essa lógica foi adicionada para evitar que clientes fiquem esperando para serem atendidos, sendo que já podemos saber através desse calculo se eles serão ou não atendidos pelo menos uma vez.

---

Foi adicionada uma lógica de que o cliente pode repetir pedidos, desde que ele não esteja satisfeito. A cada consumo de um pedido, o cliente tem 20% de chance de ficar satisfeito.

---

Alguns atributos foram declarados como "volatile" pois em alguns sistemas e versões, a JVM do Java guarda um cache dos atributos, não atualizando os atributos em tempo real modificados por outros objetos (um objeto Garçom alterando um atributo diretamente do objeto Dados, por exemplo), dessa forma, a palavra volatile evita com que o cache influencie nesses casos.

---

Em Java, todos os Objetos têm implicito um Lock Mutex, que é ativado com synchronized(objeto a ativar o lock mutex){}. Dessa forma, todos os trechos de código que precisam acessar ou alterar uma variável, vão esperar o bloco synchronized finalizar, para desativar o Lock Mutex.

---

A classe Thread em Java tem os métodos wait() e notify(), que fazem uma função de semáforo, onde o método wait() deixa a execução da thread parada até que o método notify() seja chamado.
