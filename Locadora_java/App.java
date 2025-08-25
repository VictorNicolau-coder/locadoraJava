
public class App {

    public static void main(String[] args){
        Locadora.exibirMensagemBoasVindas();

        int op;
        do{
            op = Locadora.exibirEscolhas();
        }while(op != 0);
    }

}
