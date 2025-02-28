package DAO;

import model.Client;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import model.User;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UserDAO {
    public void saveUser(User user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Erreur lors de la sauvegarde de l'utilisateur", e);
        } finally {
            if (session != null) {
                session.close(); // Fermeture explicite de la session
            }
        }
    }

    public User getUserByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();

            if (user == null) {
                throw new RuntimeException("Aucun utilisateur trouvé avec cet email : " + email);
            }

            return user;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de l'utilisateur par email", e);
        }
    }


    public List<User> getAllUsers() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des utilisateurs", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void updateUser(User user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Erreur lors de la mise à jour de l'utilisateur", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void deleteUser(int userId) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            User user = session.get(User.class, userId);
            if (user != null) {
                session.delete(user);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Erreur lors de la suppression de l'utilisateur", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }







    /**
     * Obtenir une date antérieure à une date donnée
     */
    public List<Client> getRegularCustomers(int page, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Client", Client.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .list();
        }
    }

    /**
     * Obtenir des clients à haute valeur (Total des achats > 500€ ou nombre de commandes > 5)
     */
    public List<Client> getHighValueClients() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT DISTINCT c FROM Client c " +
                                    "LEFT JOIN Commande co ON co.client = c " +
                                    "GROUP BY c " +
                                    "HAVING SUM(co.prixTotal) > 500 OR COUNT(co) > 5", Client.class)
                    .list();
        }
    }

    /**
     * Obtenir de nouveaux clients (Inscrits récemment, mais n'ayant pas encore effectué d'achat)
     */
    public List<Client> getNewClients() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT c FROM Client c " +
                                    "LEFT JOIN Commande co ON co.client = c " +
                                    "WHERE co.idCommande IS NULL", Client.class)
                    .list();
        }
    }

    /**
     * Obtenir des clients inactifs (Aucune commande depuis 90 jours)
     */
    public List<Client> getInactiveClients() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT c FROM Client c " +
                                    "LEFT JOIN Commande co ON co.client = c " +
                                    "GROUP BY c " +
                                    "HAVING COALESCE(MAX(co.dateCommande), '2000-01-01') < :dateLimite", Client.class)
                    .setParameter("dateLimite", getDateBeforeDays(90))
                    .list();
        }
    }

    // **Les derniers jours**
    private Date getDateBeforeDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        return calendar.getTime();
    }

    // 🚀 **统计数据 - UI 需要的格式**
    public List<String> getRegularCustomerLabels() {
        return Collections.singletonList("Clients réguliers");
    }

    public List<Integer> getRegularCustomerData() {
        return Collections.singletonList(getRegularCustomers(1, 1000).size()); // 默认查询 1000 个
    }

    public List<String> getHighValueCustomerLabels() {
        return Collections.singletonList("Clients à haute valeur");
    }

    public List<Integer> getHighValueCustomerData() {
        return Collections.singletonList(getHighValueClients().size());
    }

    public List<String> getNewCustomerLabels() {
        return Collections.singletonList("Nouveaux clients");
    }

    public List<Integer> getNewCustomerData() {
        return Collections.singletonList(getNewClients().size());
    }

    public List<String> getInactiveCustomerLabels() {
        return Collections.singletonList("Clients inactifs");
    }

    public List<Integer> getInactiveCustomerData() {
        return Collections.singletonList(getInactiveClients().size());
    }
}


