package util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

public class HibernateMain {
    public static void main(String[] args) {
        // 获取 Hibernate Session
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        System.out.println("Hibernate a déjà connecté avec la base de données et toutes les tables sont crées");

        session.close();
        sessionFactory.close();
    }
}