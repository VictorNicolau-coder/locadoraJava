// AluguelDAO.java
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class AluguelDAO {

    private ConexaoMySql conexao = new ConexaoMySql();
    private ClienteDAO CD = new ClienteDAO();
    private FilmeDAO FD = new FilmeDAO();

    // Método para registrar um novo aluguel
    public void salvar(Aluguel aluguel) {
        Cliente c = CD.buscarPorId(aluguel.getIdCliente());
        Filme f = FD.buscarPorId(aluguel.getIdFilme());

        if (c.getNome().equals("Cassandra") && f.getTitulo().equals("Star Wars: Episode IV")){
            System.out.println("Usuario está bloqueado para fazer empréstimos");
        } else {
            String queryAluguel = "INSERT INTO aluguel (id_filme, id_cliente, data_aluguel) VALUES (?, ?, ?)";
            String queryUpdateFilme = "UPDATE filme SET status = 'ALUGADO' WHERE id = ?";

            Connection con = conexao.getConnection();

            try {
                if (con != null) {
                    con.setAutoCommit(false);

                    // Inserir aluguel
                    PreparedStatement psAluguel = con.prepareStatement(queryAluguel);
                    psAluguel.setInt(1, aluguel.getIdFilme());
                    psAluguel.setInt(2, aluguel.getIdCliente());
                    psAluguel.setDate(3, Date.valueOf(aluguel.getDataAluguel()));
                    psAluguel.executeUpdate();

                    // Atualizar status do filme
                    PreparedStatement psUpdateFilme = con.prepareStatement(queryUpdateFilme);
                    psUpdateFilme.setInt(1, aluguel.getIdFilme());
                    psUpdateFilme.executeUpdate();

                    con.commit();
                    System.out.println("Aluguel registrado e status do filme atualizado com sucesso!");
                }
            } catch (SQLException e) {
                System.err.println("Erro ao registrar aluguel!");
                e.printStackTrace();
                try {
                    if (con != null) con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } finally {
                try {
                    if (con != null) {
                        con.setAutoCommit(true);
                        con.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public boolean excluir(int id) {
        String query = "DELETE FROM emprestimo WHERE id = ?";
        
        try (Connection con = conexao.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0; // true se excluiu, false se não encontrou
            
        } catch (SQLException e) {
            System.err.println("Erro ao excluir empréstimo!");
            e.printStackTrace();
            return false;
        }
    }

    public List<Aluguel> listar() {
        List<Aluguel> alugueis = new ArrayList<>();
        String query = "SELECT * FROM aluguel";

        try (Connection con = conexao.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                // Buscar cliente e filme pelo ID
                int idCliente = rs.getInt("id_cliente");
                int idFilme = rs.getInt("id_filme");

                Cliente c = CD.buscarPorId(idCliente);
                Filme f = FD.buscarPorId(idFilme);

                if (c != null && f != null) {
                    Aluguel a = new Aluguel(f.getId(), c.getId());
                    a.setId(rs.getInt("id")); // se tiver ID do aluguel no banco
                    alugueis.add(a);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar empréstimos!");
            e.printStackTrace();
        }

        return alugueis;
    }

    public void devolver(int id_filme) {
        String query = "UPDATE aluguel SET data_devolucao = '" + LocalDate.now() + "' WHERE id_filme = " + id_filme + " ";

        try (Connection con = conexao.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data de devolução atualizada com sucesso!");
            } else {
                System.out.println("Nenhum filme com esse id foi encontrado para atualização.");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro em momento de devolução");
            e.printStackTrace();
        }
    }

    public void editar(Aluguel aluguel) {
        String query = "UPDATE aluguel SET id_cliente = ?, id_filme = ? WHERE id = ?";

        try (Connection con = conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, aluguel.getIdCliente());
            ps.setInt(2, aluguel.getIdFilme());
            ps.setInt(3, aluguel.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Aluguel atualizado com sucesso!");
            } else {
                System.out.println("Nenhum aluguel com esse id foi encontrado.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar aluguel!");
            e.printStackTrace();
        }
    }

}


