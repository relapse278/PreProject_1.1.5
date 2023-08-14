package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.Service;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public final class Util {
    // реализуйте настройку соеденения с БД
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    //    // testing the program with another DB
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/kata_preproject_1";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    private static Util instance;

    // Hibernate
    private static final String HIBERNATE_DIALECT = "org.hibernate.dialect.MySQL5Dialect";
    private static final String HIBERNATE_SHOW_SQL = "true";
    private static final String HIBERNATE_CURRENT_SESSION_CONTEXT_CLASS = "thread";
    private static final String HIBERNATE_HBM2DDL_AUTO = ""; // теперь таблица не будет удаляться и создаваться автоматически // "create-drop"; // "none" ???
    private static SessionFactory sessionFactory = null;
    private static ThreadLocal<Session> threadLocal = null;
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties hibernateProperties = new Properties();
                hibernateProperties.put(Environment.DRIVER, DB_DRIVER);
                hibernateProperties.put(Environment.URL, DB_URL);
                hibernateProperties.put(Environment.USER, DB_USER);
                hibernateProperties.put(Environment.PASS, DB_PASSWORD);
                hibernateProperties.put(Environment.DIALECT, HIBERNATE_DIALECT);
                hibernateProperties.put(Environment.SHOW_SQL, HIBERNATE_SHOW_SQL);
                hibernateProperties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS,
                        HIBERNATE_CURRENT_SESSION_CONTEXT_CLASS);
                hibernateProperties.put(Environment.HBM2DDL_AUTO, HIBERNATE_HBM2DDL_AUTO);

                configuration.setProperties(hibernateProperties);
                configuration.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            try {
                sessionFactory.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private Util() {

    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(DB_DRIVER).getDeclaredConstructor().newInstance();
            System.out.println("Connection OK!");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch(Exception ex) {
            System.out.println("Connection ERROR!");
            ex.printStackTrace();
        }

        return connection;
    }

    public static Util getInstance() {
        Util localInstance = instance;
        if (null == localInstance) {
            synchronized (Util.class) {
                localInstance = instance;
                if (null == localInstance) {
                    instance = localInstance = new Util();
                }
            }
        }
        return localInstance;
    }
}