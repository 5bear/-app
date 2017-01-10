package com.springapp.dao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.transaction.internal.jdbc.JdbcTransaction;
import org.hibernate.engine.transaction.spi.LocalStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ZhanShaoxiong on 2016/3/5.
 */
@Repository
public class BaseDao {

    @Autowired
    private SessionFactory sessionFactory;
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    public <T> void save(T t) {
        Session session = getSession();
        JdbcTransaction tx = session.getTransaction().getLocalStatus()==LocalStatus.ACTIVE?(JdbcTransaction)session.getTransaction():(JdbcTransaction) session.beginTransaction();
        try {
            session.save(t);
            tx.commit();
        } catch (Exception err) {
            if (tx != null) {
                tx.rollback();
                err.printStackTrace();
            }
        }
    }

    public <T> void save(List<T> list) {
        Session session = getSession();
        JdbcTransaction tx = session.getTransaction().getLocalStatus()==LocalStatus.ACTIVE?(JdbcTransaction)session.getTransaction():(JdbcTransaction) session.beginTransaction();
        try {
            for (T t : list) {
                session.save(t);
            }
            tx.commit();
        } catch (Exception err) {
            if (tx != null) {
                tx.rollback();
                err.printStackTrace();
            }
        }
    }

    public <T> void delete(T t) {
        Session session = getSession();
        JdbcTransaction tx = session.getTransaction().getLocalStatus()==LocalStatus.ACTIVE?(JdbcTransaction)session.getTransaction():(JdbcTransaction) session.beginTransaction();
        try {
            session.delete(t);
            tx.commit();
        } catch (Exception err) {
            if (tx != null) {
                tx.rollback();
                err.printStackTrace();
            }
        }
    }

    public <T> void delete(Class<T> entityClass, Long id) {
        Session session = getSession();
        JdbcTransaction tx = session.getTransaction().getLocalStatus()==LocalStatus.ACTIVE?(JdbcTransaction)session.getTransaction():(JdbcTransaction) session.beginTransaction();
        try {
            session.delete(session.get(entityClass, id));
            tx.commit();
        } catch (Exception err) {
            if (tx != null) {
                tx.rollback();
                err.printStackTrace();
            }
        }
    }

    public <T> void update(T t) {
        Session session = getSession();
        JdbcTransaction tx = session.getTransaction().getLocalStatus()==LocalStatus.ACTIVE?(JdbcTransaction)session.getTransaction():(JdbcTransaction) session.beginTransaction();
        try {
            session.update(t);
            tx.commit();
        } catch (Exception err) {
            if (tx != null) {
                tx.rollback();
                err.printStackTrace();
            }
        }
    }

    public <T> void update(List<T> list) {
        Session session = getSession();
        JdbcTransaction tx = session.getTransaction().getLocalStatus()==LocalStatus.ACTIVE?(JdbcTransaction)session.getTransaction():(JdbcTransaction) session.beginTransaction();
        try {
            for (T t : list) {
                session.update(t);
            }
            tx.commit();
        } catch (Exception err) {
            if (tx != null) {
                tx.rollback();
                err.printStackTrace();
            }
        }
    }

    public <T> T get(Class<T> entityClass, Long id) {

        Session session = getSession();
        JdbcTransaction tx = session.getTransaction().getLocalStatus()==LocalStatus.ACTIVE?(JdbcTransaction)session.getTransaction():(JdbcTransaction) session.beginTransaction();
        T entity = null;
        try {
            entity = (T) session.get(entityClass, id);
            tx.commit();
        } catch (Exception err) {
            if (tx != null) {
                tx.rollback();
                err.printStackTrace();
            }
        }
        return entity;
    }

    public <T> T find(String hql, Class<T> entityClass, Object[] params) {

        Session session = getSession();

        JdbcTransaction tx = session.getTransaction().getLocalStatus()==LocalStatus.ACTIVE?(JdbcTransaction)session.getTransaction():(JdbcTransaction) session.beginTransaction();
        Query query = session.createQuery(hql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
        T result = (T) query.uniqueResult();
        try {
            tx.commit();
        } catch (Exception err) {
            if (tx != null) {
                tx.rollback();
                err.printStackTrace();
            }
        }
        return result;
    }

    public <T> T find(String hql, Class<T> entityClass) {
        return find(hql, entityClass, null);
    }

    public <T> List<T> findAll(String hql, Class<T> entityClass) {
        return findAll(hql, entityClass, null);
    }

    public <T> List<T> findAll(String hql, Class<T> entityClass, Object...params) {
        Session session = getSession();
        JdbcTransaction tx = session.getTransaction().getLocalStatus()==LocalStatus.ACTIVE?(JdbcTransaction)session.getTransaction():(JdbcTransaction) session.beginTransaction();
        System.out.println(hql);
        Query query = session.createQuery(hql);
        List<T> result = null;
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
        try {
            result = (List<T>) query.setCacheable(true).list();
            tx.commit();
        } catch (Exception err) {
            if (tx != null) {
                tx.rollback();
                err.printStackTrace();
            }
        }
        return result;
    }

    public List findAll(String hql, Object[] params, int firstResult, int maxResults) {
        Session session = getSession();
        JdbcTransaction tx = session.getTransaction().getLocalStatus()==LocalStatus.ACTIVE?(JdbcTransaction)session.getTransaction():(JdbcTransaction) session.beginTransaction();

        Query query = session.createQuery(hql);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i, params[i]);
        }
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        List result = null;
        try {
            result = query.setCacheable(true).list();
            tx.commit();
        } catch (Exception err) {
            if (tx != null) {
                tx.rollback();
                err.printStackTrace();
            }
        }
        return result;
    }

    public List findAll(String hql) {
        return findAll(hql, new Object[]{});
    }

    public List findAll(String hql, Object[] params) {
        Session session = getSession();
        JdbcTransaction tx = session.getTransaction().getLocalStatus()==LocalStatus.ACTIVE?(JdbcTransaction)session.getTransaction():(JdbcTransaction) session.beginTransaction();

        Query query = session.createQuery(hql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
        List result = null;
        try {
            result = query.setCacheable(true).list();
            tx.commit();
        } catch (Exception err) {
            if (tx != null) {
                tx.rollback();
                err.printStackTrace();
            }
        }
        return result;
    }

    public <T> List<T> findByPage(final String hql, Class<T> entityClass,
                                  final int firstResult, final int maxResult) {
        return findByPage(hql, entityClass, null, firstResult,
                maxResult);
    }


    public <T> List<T> findByPage(final String hql, Class<T> entityClass,
                                  final Object[] params, final int firstResult, final int maxResult) {

        Session session = getSession();
        JdbcTransaction tx = session.getTransaction().getLocalStatus()==LocalStatus.ACTIVE?(JdbcTransaction)session.getTransaction():(JdbcTransaction) session.beginTransaction();

        Query query = session.createQuery(hql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);
        List<T> result = null;
        try {
            result = (List<T>) query.setCacheable(true).list();
            tx.commit();
        } catch (Exception err) {
            if (tx != null) {
                tx.rollback();
                err.printStackTrace();
            }
        }
        return result;
    }

    public long getCount(final String hql) {
        Session session = getSession();
        JdbcTransaction tx = session.getTransaction().getLocalStatus()==LocalStatus.ACTIVE?(JdbcTransaction)session.getTransaction():(JdbcTransaction) session.beginTransaction();

        Query query = session.createQuery(hql);
        Number count = (Number) query.uniqueResult();
        try {
            tx.commit();
        } catch (Exception err) {
            if (tx != null) {
                tx.rollback();
                err.printStackTrace();
            }
        }
        return count.longValue();
    }

    public long getCount(final String hql, final Object[] params) {
        Session session = getSession();
        JdbcTransaction tx = session.getTransaction().getLocalStatus()==LocalStatus.ACTIVE?(JdbcTransaction)session.getTransaction():(JdbcTransaction) session.beginTransaction();

        Query query = session.createQuery(hql);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i, params[i]);
        }
        Number count = (Number) query.uniqueResult();
        try {
            tx.commit();
        } catch (Exception err) {
            if (tx != null) {
                tx.rollback();
                err.printStackTrace();
            }
        }
        return count.longValue();
    }
    public <T> List<T> findAllBySql(String hql, Class<T> entityClass, Object...params) {
        Session session = getSession();
        JdbcTransaction tx = (JdbcTransaction)session.getTransaction();
        if(tx.getLocalStatus().equals(LocalStatus.ACTIVE))
            System.out.print("已存在活动的事务");
        else
            tx=(JdbcTransaction) session.beginTransaction();
        System.out.println(hql);
        Query query = session.createSQLQuery(hql).addEntity(entityClass);
        List<T> result = null;
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
        try {
            result = (List<T>) query.list();
            tx.commit();
        } catch (Exception err) {
            if (tx != null) {
                tx.rollback();
                err.printStackTrace();
            }
        }
        return result;
    }
    public <T> List<T> findSQLByPage(final String hql, Class<T> entityClass,
                                  final Object[] params, final int firstResult, final int maxResult) {

        Session session = getSession();
        JdbcTransaction tx = session.getTransaction().getLocalStatus()==LocalStatus.ACTIVE?(JdbcTransaction)session.getTransaction():(JdbcTransaction) session.beginTransaction();

        Query query = session.createSQLQuery(hql).addEntity(entityClass);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);
        List<T> result = null;
        try {
            result = (List<T>) query.list();
            tx.commit();
        } catch (Exception err) {
            if (tx != null) {
                tx.rollback();
                err.printStackTrace();
            }
        }
        return result;
    }
    public <T> List<T> findSQLByPage(final String hql, Class<T> entityClass,
                                  final int firstResult, final int maxResult) {
        return findSQLByPage(hql, entityClass, null, firstResult,
                maxResult);
    }

    public void excuteSql(String sql){
        Session session = getSession();
        JdbcTransaction tx = session.getTransaction().getLocalStatus()==LocalStatus.ACTIVE?(JdbcTransaction)session.getTransaction():(JdbcTransaction) session.beginTransaction();

        Query query = session.createSQLQuery(sql);
        query.executeUpdate();
        try {
            tx.commit();
        } catch (Exception err) {
            if (tx != null) {
                tx.rollback();
                err.printStackTrace();
            }
        }
    }
}

