import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// (Requisito 11) Acesso 'default' para a classe
class FilmeDAO {

    private ConexaoMySql conexao = new ConexaoMySql();

    // (Requisito 17) Método para "Create" (Salvar)
    public void salvar(Filme filme) {
        // A query usa os getters do objeto Filme
        String query = "INSERT INTO filme (titulo, genero, ano_lancamento, status) VALUES ('" +
                filme.getTitulo() + "', '" +
                filme.getGenero() + "', " +
                filme.getAnoLancamento() + ", '" +
                filme.getStatus().toString() + "')";

        System.out.println("Executando query: " + query);
        Connection con = conexao.getConnection();

        try {
            if (con != null) {
                Statement st = con.createStatement();
                st.executeUpdate(query);
                System.out.println("Filme salvo com sucesso!");
                con.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar filme!");
            e.printStackTrace();
        }
    }

    // Listar todos os filmes
    public List<Filme> listar() {
        List<Filme> filmes = new ArrayList<>();
        String query = "SELECT * FROM filme";

        try (Connection con = conexao.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Filme f = new Filme(
                    rs.getString("titulo"),
                    rs.getString("genero"),
                    rs.getInt("ano_lancamento"),
                    StatusFilme.valueOf(rs.getString("status"))
                );
                f.setId(rs.getInt("id"));
                filmes.add(f);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar filmes!");
            e.printStackTrace();
        }

        return filmes;
    }

    // Buscar filme por ID
    public Filme buscarPorId(int id) {
        String query = "SELECT * FROM filme WHERE id = ?";

        try (Connection con = conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Filme f = new Filme(
                    rs.getString("titulo"),
                    rs.getString("genero"),
                    rs.getInt("ano_lancamento"),
                    StatusFilme.valueOf(rs.getString("status"))
                );
                f.setId(rs.getInt("id"));

                return f;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar filme!");
            e.printStackTrace();
        }

        return null;
    }
    
    // (Requisito 17) Método para "Delete" (Excluir)
    public void excluir(String titulo) {
        String query = "DELETE FROM filme WHERE titulo = '" + titulo + "'";
        System.out.println("Executando query: " + query);
        Connection con = conexao.getConnection();
        
        try {
            if (con != null) {
                Statement st = con.createStatement();
                int rowsAffected = st.executeUpdate(query);
                if (rowsAffected > 0) {
                    System.out.println("Filme '" + titulo + "' excluído com sucesso!");
                } else {
                    System.out.println("Nenhum filme com o título '" + titulo + "' foi encontrado.");
                }
                con.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao excluir filme!");
            e.printStackTrace();
        }
    }

    // (Requisito 17) Método para "Update" (Editar Status)
    public void editarStatus(int id, StatusFilme novoStatus) {
        String query = "UPDATE filme SET status = '" + novoStatus.toString() + "' WHERE id = " + id;
        System.out.println("Executando query: " + query);
        Connection con = conexao.getConnection();
        
        try {
            if (con != null) {
                Statement st = con.createStatement();
                int rowsAffected = st.executeUpdate(query);
                 if (rowsAffected > 0) {
                    System.out.println("Status do filme atualizado com sucesso!");
                } else {
                    System.out.println("Nenhum filme com esse id foi encontrado para atualização.");
                }
                con.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao editar status do filme!");
            e.printStackTrace();
        }
    }

    public void editarFilme(int id, Filme filme) {
        String query = "UPDATE filme SET titulo = ?, genero = ?, ano_lancamento = ? WHERE id = ?";

        try (Connection con = conexao.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, filme.getTitulo());
            ps.setString(2, filme.getGenero());
            ps.setInt(3, filme.getAnoLancamento());
            ps.setInt(4, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Dados do filme atualizado com sucesso!");
            } else {
                System.out.println("Nenhum filme com esse id foi encontrado para atualização.");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao editar filme!");
            e.printStackTrace();
        }
    }
}
