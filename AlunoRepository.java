import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import com.j256.ormlite.table.TableUtils;
import java.util.List;
import java.util.ArrayList;

public class AlunoRepository {
    private static Database database;
    private static Dao<Aluno, Integer> dao;
    private List<Aluno> loadedAlunos;
    private Aluno loadedAluno; 

    public AlunoRepository(Database database) {
        AlunoRepository.setDatabase(database);
        loadedAlunos = new ArrayList<Aluno>();
    }

    public static void setDatabase(Database database) {
        AlunoRepository.database = database;
        try {
            dao = DaoManager.createDao(database.getConnection(), Aluno.class);
            TableUtils.createTableIfNotExists(database.getConnection(), Aluno.class);
        } catch (SQLException e) {
            System.out.println(e);
        }            
    }

    public Aluno create(Aluno aluno) {
        int nrows = 0;
        try {
            nrows = dao.create(aluno);
            if (nrows == 0)
                throw new SQLException("Error: object not saved");
            this.loadedAluno = aluno;
            loadedAlunos.add(aluno);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return aluno;
    }    

    public void update(Aluno aluno) {
        try {
            int nrows = dao.update(aluno);
            if (nrows == 0)
                throw new SQLException("Error: object not updated");
            this.loadedAluno = aluno;
            int index = loadedAlunos.indexOf(aluno);
            if (index >= 0) {
                loadedAlunos.set(index, aluno);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void delete(Aluno aluno) {
        try {
            int nrows = dao.delete(aluno);
            if (nrows == 0)
                throw new SQLException("Error: object not deleted");
            loadedAlunos.remove(aluno);
            if (this.loadedAluno.equals(aluno)) {
                this.loadedAluno = loadedAlunos.size() > 0 ? loadedAlunos.get(0) : null;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public Aluno loadFromId(int id) {
        try {
            this.loadedAluno = dao.queryForId(id);
            if (this.loadedAluno != null)
                this.loadedAlunos.add(this.loadedAluno);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return this.loadedAluno;
    }    
    
    public List<Aluno> loadAll() {
        try {
            this.loadedAlunos = dao.queryForAll();
            if (this.loadedAlunos.size() != 0)
                this.loadedAluno = this.loadedAlunos.get(0);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return this.loadedAlunos;
    }

}
