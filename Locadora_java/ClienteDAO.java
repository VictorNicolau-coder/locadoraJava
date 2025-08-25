// ClienteDAO.java
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class ClienteDAO {

    private ConexaoMySql conexao = new ConexaoMySql();

    // Método para "Create" (Salvar um novo cliente)
    public void salvar(Cliente cliente) {
        String query = "INSERT INTO cliente (nome, email, telefone) VALUES (?, ?, ?)";
        Connection con = conexao.getConnection();

        try {
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, cliente.getNome());
                ps.setString(2, cliente.getEmail());
                ps.setString(3, cliente.getTelefone());
                ps.executeUpdate();
                System.out.println("Cliente salvo com sucesso!");
                con.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar cliente!");
            e.printStackTrace();
        }
    }

    public boolean excluir(int id) {
        String query = "DELETE FROM cliente WHERE id = ?";
        
        try (Connection con = conexao.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0; // true se excluiu, false se não encontrou
            
        } catch (SQLException e) {
            System.err.println("Erro ao excluir cliente!");
            e.printStackTrace();
            return false;
        }
    }

    // Método para "Read" (Exibir todos os clientes)
    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();
        String query = "SELECT * FROM cliente";

        try (Connection con = conexao.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Cliente c = new Cliente(
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("telefone")
                );
                c.setId(rs.getInt("id"));
                clientes.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar clientes!");
            e.printStackTrace();
        }

        return clientes;
    }

    // Buscar cliente por ID
    public Cliente buscarPorId(int id) {
        String query = "SELECT * FROM cliente WHERE id = ?";
        try (Connection con = conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Cliente c = new Cliente(
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("telefone")
                );
                c.setId(rs.getInt("id"));
                return c;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar cliente!");
            e.printStackTrace();
        }
        return null;
    }

    public void editarCliente(int id, Cliente cliente) {
        String query = "UPDATE cliente SET nome = ?, email = ?, telefone = ? WHERE id = ?";

        try (Connection con = conexao.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getEmail());
            ps.setString(3, cliente.getTelefone());
            ps.setInt(4, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Dados do cliente atualizado com sucesso!");
            } else {
                System.out.println("Nenhum cliente com esse id foi encontrado para atualização.");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao editar cliente!");
            e.printStackTrace();
        }
    }
}