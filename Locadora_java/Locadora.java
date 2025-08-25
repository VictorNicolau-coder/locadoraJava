import java.util.*;

public class Locadora {
    
    private static Scanner sc = new Scanner(System.in);
    private static ClienteDAO CD = new ClienteDAO();
    private static FilmeDAO FD = new FilmeDAO();
    private static AluguelDAO AD = new AluguelDAO();

    public static final String NOME_LOCADORA = "Locadora AV Filmes";

    private static Map<Integer, Runnable> acoes = new HashMap<>();
    static {
        acoes.put(1, () -> registrarCliente());
        acoes.put(2, () -> registrarFilme());
        acoes.put(3, () -> registrarEmprestimo());
        acoes.put(4, () -> editarCliente());
        acoes.put(5, () -> editarFilme());
        acoes.put(6, () -> registrarDevolucao());
        acoes.put(7, () -> editarEmprestimos());
        acoes.put(8, () -> excluirCliente());
        acoes.put(9, () -> excluirFilme());
        acoes.put(10, () -> cancelarEmprestimo());
    }

    public static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void exibirMensagemBoasVindas() {
        System.out.println("****************************************");
        System.out.println("Bem-vindo ao sistema da " + NOME_LOCADORA);
        System.out.print("****************************************");
    }

    public static int exibirEscolhas(){
        System.out.println("\nTecle enter para prosseguir...");
        sc.nextLine();

        limparTela();

        String[] opcoes = {
            "Registrar clientes",
            "Registrar filmes",
            "Registrar empréstimos",
            "Editar clientes",
            "Editar filmes",
            "Registrar devoluções",
            "Editar empréstimos",
            "Excluir cliente",
            "Excluir filme",
            "Cancelar empréstimo"
        };

        System.out.println("======= Dê sua ação, digitando o número correspondente =======");
        for (int i = 0; i < opcoes.length; i++) {
            System.out.printf("%d. %s%n", (i+1), opcoes[i]);
        }
        System.out.println("0. Encerrar programa");

        int op = sc.nextInt();

        if (acoes.containsKey(op)) {
            acoes.get(op).run();
        } else if (op == 0) {
            System.out.println("Programa encerrado!");
        } else {
            System.out.println("Opção inválida!");
        }

        return op;
    }

    private static void registrarCliente(){
        String[] dados = new String[3];

        sc.nextLine();

        System.out.print("Nome: ");
        dados[0] = sc.nextLine();
        System.out.print("Email: ");
        dados[1] = sc.nextLine();
        System.out.print("Telefone: ");
        dados[2] = sc.nextLine();

        CD.salvar(new Cliente(dados[0], dados[1], dados[2]));
    }

    private static void registrarFilme(){ 
        String[] dados = new String[2]; 
        
        sc.nextLine(); 
        
        System.out.print("Titulo: "); 
        dados[0] = sc.nextLine(); 
        System.out.print("Genero: "); 
        dados[1] = sc.nextLine(); 
        System.out.print("Ano lancamento: "); 
        int ano = sc.nextInt(); 
        
        FD.salvar(new Filme(dados[0], dados[1], ano, StatusFilme.DISPONIVEL)); 
        sc.nextLine();
    }

    private static void registrarEmprestimo() {
        sc.nextLine();
        
        System.out.println("=== Clientes disponíveis ===");
        for (Cliente c : CD.listar()) {
            System.out.println(c.getId() + " - " + c.getNome());
        }

        System.out.print("Digite o ID do cliente: ");
        int idCliente = sc.nextInt();
        Cliente cliente = CD.buscarPorId(idCliente); // método que retorna Cliente pelo ID

        if (cliente == null) {
            System.out.println("Cliente não encontrado!");
            return;
        }

        // 2. Listar filmes
        System.out.println("=== Filmes disponíveis ===");
        for (Filme f : FD.listar()) {   // assumindo que seu FilmeDAO tem método listar()
            if (f.getStatus() == StatusFilme.DISPONIVEL)
                System.out.println(f.getId() + " - " + f.getTitulo() + " - " + f.getStatus().toString());
        }

        System.out.print("Digite o ID do filme: ");
        int idFilme = sc.nextInt();
        Filme filme = FD.buscarPorId(idFilme); // método que retorna Filme pelo ID

        if (filme == null) {
            System.out.println("Filme não encontrado!");
            return;
        }

        // 3. Criar aluguel
        Aluguel aluguel = new Aluguel(idFilme, idCliente);
        AD.salvar(aluguel);

        sc.nextLine();
    }

    private static void editarCliente() {
        sc.nextLine(); // limpa buffer

        System.out.println("=== Clientes cadastrados ===");
        for (Cliente c : CD.listar()) {
            System.out.println(c.getId() + " - " + c.getNome() + " | " + c.getEmail() + " | " + c.getTelefone());
        }

        System.out.print("Digite o ID do cliente que deseja editar: ");
        int id = sc.nextInt();
        sc.nextLine(); // limpar buffer

        Cliente clienteExistente = CD.buscarPorId(id);
        if (clienteExistente == null) {
            System.out.println("Cliente não encontrado!");
            return;
        }

        System.out.print("Novo nome (" + clienteExistente.getNome() + "): ");
        String nome = sc.nextLine();
        System.out.print("Novo email (" + clienteExistente.getEmail() + "): ");
        String email = sc.nextLine();
        System.out.print("Novo telefone (" + clienteExistente.getTelefone() + "): ");
        String telefone = sc.nextLine();

        // Se o usuário não digitou nada, mantém o valor antigo
        if (nome.isEmpty()) nome = clienteExistente.getNome();
        if (email.isEmpty()) email = clienteExistente.getEmail();
        if (telefone.isEmpty()) telefone = clienteExistente.getTelefone();

        CD.editarCliente(id, new Cliente(nome, email, telefone));
    }

    private static void editarFilme() {
        sc.nextLine(); // limpa buffer

        System.out.println("=== Filmes cadastrados ===");
        for (Filme f : FD.listar()) {
            System.out.println(f.getId() + " - " + f.getTitulo() + " | " + f.getGenero() + " | " + f.getAnoLancamento());
        }

        System.out.print("Digite o ID do filme que deseja editar: ");
        int id = sc.nextInt();
        sc.nextLine(); // limpar buffer

        Filme filmeExistente = FD.buscarPorId(id);
        if (filmeExistente == null) {
            System.out.println("Filme não encontrado!");
            return;
        }

        System.out.print("Novo título (" + filmeExistente.getTitulo() + "): ");
        String titulo = sc.nextLine();
        System.out.print("Novo gênero (" + filmeExistente.getGenero() + "): ");
        String genero = sc.nextLine();
        System.out.print("Novo ano (" + filmeExistente.getAnoLancamento() + "): ");
        String anoStr = sc.nextLine();

        // Se o usuário não digitou nada, mantém o valor antigo
        if (titulo.isEmpty()) titulo = filmeExistente.getTitulo();
        if (genero.isEmpty()) genero = filmeExistente.getGenero();
        int ano = anoStr.isEmpty() ? filmeExistente.getAnoLancamento() : Integer.parseInt(anoStr);
        StatusFilme st = filmeExistente.getStatus();

        FD.editarFilme(id, new Filme(titulo, genero, ano, st));
    }

    private static void registrarDevolucao(){
        sc.nextLine();
        
        // 2. Listar filmes
        System.out.println("=== Filmes alugados ===");
        for (Filme f : FD.listar()) {   // assumindo que seu FilmeDAO tem método listar()
            if (f.getStatus() == StatusFilme.ALUGADO)
                System.out.println(f.getId() + " - " + f.getTitulo() + " - " + f.getStatus().toString());
        }

        System.out.print("Digite o ID do filme: ");
        int idFilme = sc.nextInt();
        FD.editarStatus(idFilme, StatusFilme.DISPONIVEL);
        Filme filme = FD.buscarPorId(idFilme); // método que retorna Filme pelo ID

        if (filme == null) {
            System.out.println("Filme não encontrado!");
            return;
        }

        // 3. Criar aluguel
        AD.devolver(filme.getId());
        System.out.println("Devolução registrada com sucesso!");
    }

    private static void editarEmprestimos() {
        sc.nextLine(); // limpar buffer

        List<Aluguel> alugueis = AD.listar();
        if (alugueis.isEmpty()) {
            System.out.println("Nenhum empréstimo registrado!");
            return;
        }

        System.out.println("=== Empréstimos cadastrados ===");
        for (int i = 0; i < alugueis.size(); i++) {
            Aluguel a = alugueis.get(i);
            a.setCliente(CD.buscarPorId(a.getIdCliente()));
            a.setFilme(FD.buscarPorId(a.getIdFilme()));
            System.out.println(i+1 + " - Cliente: " + a.getCliente().getNome() +
                            " | Filme: " + a.getFilme().getTitulo());
        }

        System.out.print("Digite o índice do empréstimo que deseja editar: ");
        int idx = sc.nextInt();
        sc.nextLine(); // limpar buffer

        if (idx < 0 || idx >= alugueis.size()) {
            System.out.println("Índice inválido!");
            return;
        }

        Aluguel aluguelSelecionado = alugueis.get(idx);

        // Escolher novo cliente
        System.out.println("=== Clientes disponíveis ===");
        for (Cliente c : CD.listar()) {
            System.out.println(c.getId() + " - " + c.getNome());
        }
        System.out.print("Digite o ID do novo cliente (ou 0 para manter atual): ");
        int idCliente = sc.nextInt();
        sc.nextLine();

        if (idCliente != 0) {
            Cliente novoCliente = CD.buscarPorId(idCliente);
            if (novoCliente != null) {
                aluguelSelecionado.setCliente(novoCliente);
            } else {
                System.out.println("Cliente não encontrado. Mantendo o anterior.");
            }
        }

        // Escolher novo filme
        System.out.println("=== Filmes disponíveis ===");
        for (Filme f : FD.listar()) {
            System.out.println(f.getId() + " - " + f.getTitulo());
        }
        System.out.print("Digite o ID do novo filme (ou 0 para manter atual): ");
        int idFilme = sc.nextInt();
        sc.nextLine();

        if (idFilme != 0) {
            Filme novoFilme = FD.buscarPorId(idFilme);
            if (novoFilme != null) {
                aluguelSelecionado.setFilme(novoFilme);
            } else {
                System.out.println("Filme não encontrado. Mantendo o anterior.");
            }
        }

        // Atualizar no banco
        AD.editar(aluguelSelecionado);

        System.out.println("Empréstimo atualizado com sucesso!");
    }

    private static void excluirCliente(){
        System.out.println("=== Clientes cadastrados ===");
        for (Cliente c : CD.listar()){
            System.out.println(c.getId() + " - " + c.getNome());
        }

        
        System.out.print("Digite o id do cliente a ser excluído: ");
        int id = sc.nextInt();

        if (CD.buscarPorId(id) == null) {
            System.out.println("Cliente não encontrado!");
            return;
        }

        CD.excluir(id);
        sc.nextLine();
    }
    
    private static void excluirFilme(){
        sc.nextLine();

        System.out.println("=== Filmes cadastrados ===");
        for (Filme f : FD.listar()){
            if (f.getStatus() == StatusFilme.DISPONIVEL)
                System.out.println(f.getId() + " - " + f.getTitulo());
        }

        System.out.print("Digite o id do filme a ser excluído: ");
        int id = sc.nextInt();

        if (FD.buscarPorId(id) == null) {
            System.out.println("Filme não encontrado!");
            return;
        }

        FD.excluir(id);
    }

    private static void cancelarEmprestimo(){

    }
}

