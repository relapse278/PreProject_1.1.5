package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final String SQL_DROP_USERS_TABLE = "DROP TABLE IF EXISTS users";
    private static final String SQL_CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS users (name varchar(64) NOT NULL, id BIGINT AUTO_INCREMENT, age SMALLINT NOT NULL, lastName varchar(64) NOT NULL, CONSTRAINT primary_key primary key (id))";
    private static final String SQL_CLEAN_USERS_TABLE = "DELETE FROM users";

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        sqlQuery(SQL_CREATE_USER_TABLE);
    }

    @Override
    public void dropUsersTable() {
        sqlQuery(SQL_DROP_USERS_TABLE);
    }

    private void sqlQuery(String sql) {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createSQLQuery(sql).addEntity(User.class);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        User user = null;

        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.delete(session.get(User.class, id));

            // ???
//            User user = new User();
//            user.setId(id);
//            session.delete(user);

            // second variant with [remove] - what's the difference?
            //session.remove(user);
            transaction.commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> listOfAllUsers = new ArrayList<>();

        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // is deprecated!!!
            //listOfAllUsers = session.createCriteria(User.class).list();

            // another variant with [criteria]
//            CriteriaQuery<User> criteriaQuery = session.getCriteriaBuilder().createQuery(User.class);
//            criteriaQuery.from(User.class);
//            listOfAllUsers = session.createQuery(criteriaQuery).getResultList();
            listOfAllUsers = session.createQuery("from User").list();

            transaction.commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return listOfAllUsers;
    }

    @Override
    public void cleanUsersTable() {
        sqlQuery(SQL_CLEAN_USERS_TABLE);
    }
}
