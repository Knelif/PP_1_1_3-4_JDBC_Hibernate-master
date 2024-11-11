package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UserDaoHibernateImpl implements UserDao {
    private static final String _createUserTableSQL = """
            CREATE TABLE IF NOT EXISTS user (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                name CHAR(20),
                lastname CHAR(20),
                age TINYINT
                )""";
    private static final String _dropUserTableSQL = "DROP TABLE IF EXISTS user";
    private static final String _truncateUserTableSQL = "TRUNCATE TABLE user";
    private static final String _selectAllUsersHQL = "FROM User";

    private final SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        sessionFactory = Util.getSessionFactory();
    }


    @Override
    public void createUsersTable() {
        executeSQL(session -> session.createSQLQuery(_createUserTableSQL).executeUpdate());
    }

    @Override
    public void dropUsersTable() {
        executeSQL(session -> session.createSQLQuery(_dropUserTableSQL).executeUpdate());
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        executeTransaction(session -> session.save(new User(name, lastName, age)));
    }

    @Override
    public void removeUserById(long id) {
        executeTransaction(session -> {
            User user = session.get(User.class, id);
            if (user != null) session.remove(user);
        });
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            userList = session.createQuery(_selectAllUsersHQL, User.class).getResultList();
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        executeSQL(session -> session.createSQLQuery(_truncateUserTableSQL).executeUpdate());
    }

    private void executeTransaction(Consumer<Session> sessionConsumer) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            sessionConsumer.accept(session);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
        }
    }

    private void executeSQL(Consumer<Session> sessionConsumer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            sessionConsumer.accept(session);
            session.getTransaction().commit();
        } catch (HibernateException hibernateException) {
            throw hibernateException;
        }
    }

}
