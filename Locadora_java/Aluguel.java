// Aluguel.java
import java.time.LocalDate;

public class Aluguel {

    private int id;
    private int idFilme;
    private int idCliente;
    private Filme filme;
    private Cliente cliente;
    private LocalDate dataAluguel;
    private LocalDate dataDevolucao;

    // Construtor
    public Aluguel(int idFilme, int idCliente) {
        this.idFilme = idFilme;
        this.idCliente = idCliente;
        this.dataAluguel = LocalDate.now(); // Pega a data atual
        this.dataDevolucao = null; // Inicia como nulo
    }

    public Aluguel(Filme filme, Cliente cliente) {
        this.cliente = cliente;
        this.filme = filme;
        this.idCliente = cliente.getId();
        this.idFilme = filme.getId();
        this.dataAluguel = LocalDate.now();
        this.dataDevolucao = null;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFilme() {
        return idFilme;
    }

    public void setIdFilme(int idFilme) {
        this.idFilme = idFilme;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public LocalDate getDataAluguel() {
        return dataAluguel;
    }

    public void setDataAluguel(LocalDate dataAluguel) {
        this.dataAluguel = dataAluguel;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public Filme getFilme() { return filme; }
    public void setFilme(Filme filme) { 
        this.filme = filme; 
        if (filme != null) this.idFilme = filme.getId();
    }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { 
        this.cliente = cliente; 
        if (cliente != null) this.idCliente = cliente.getId();
    }

}