package database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;
import processing.object.Dump;
import processing.object.Warning;

import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.Observer;
import java.util.Properties;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class DataBase extends Thread{
    private static String DB_CONNECTION;
    private static String DB_USER;
    private static String DB_PASSWORD;
    private static String DB_STATUS;
    private static String DB_COMMIT;

    private SessionFactory sf;
    private Transaction transaction;
    private Session session;

    private LinkedBlockingQueue<Dump> queueInfo = new LinkedBlockingQueue<Dump>();
    private LinkedBlockingQueue<Warning> queueWarning = new LinkedBlockingQueue<Warning>();
    private UpdVal updValBufferInfo, updValBufferWarning;

    private int timeDelay;

    public DataBase(int timeDelay){
        this.timeDelay = timeDelay;

        Properties property = new Properties();
        try {
            FileInputStream fis = new FileInputStream("hibernate.properties");
            property.load(fis);
            DB_CONNECTION = property.getProperty("hibernate.connection.url");
            DB_USER = property.getProperty("hibernate.connection.username");
            DB_PASSWORD = property.getProperty("hibernate.connection.password");
            DB_STATUS = property.getProperty("hibernate.hbm2ddl.auto");
            DB_COMMIT = property.getProperty("hibernate.connection.autocommit");

            System.out.println("HOST: " + DB_CONNECTION + ", LOGIN: " + DB_USER + ", PASSWORD: " + DB_PASSWORD + ", AUTOCOMMIT: " + DB_COMMIT + ", CREATE/UPDATE: " + DB_STATUS);
        } catch (IOException e) {
            e.printStackTrace();
    }

        Configuration configuration= new Configuration()
                .setProperty("hibernate.connection.url", DB_CONNECTION)
                .setProperty("hibernate.connection.username", DB_USER)
                .setProperty("hibernate.connection.password", DB_PASSWORD)
                .setProperty("hibernate.hbm2ddl.auto", DB_STATUS)
                .setProperty("hibernate.connection.autocommit", DB_COMMIT);
        configuration.configure();

        ServiceRegistryBuilder serviceRegistry = new ServiceRegistryBuilder()
                .applySettings(configuration.getProperties());

        sf = configuration.buildSessionFactory(serviceRegistry.buildServiceRegistry());
        session = sf.openSession();

        this.start();
        this.setPriority(MIN_PRIORITY);
        this.setName("DataBase");

    }

    @Override
    public void run(){
        while(!isInterrupted()) {
            try {
                Thread.currentThread().sleep(timeDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!queueInfo.isEmpty()) {
                try {
                    transaction = session.beginTransaction();
                    try {
                        while (!queueInfo.isEmpty()) {
                            Dump dump = queueInfo.take();
                            session.save(dump);
                            Thread.currentThread().sleep(200);
                            updValBufferInfo.update(queueInfo.size());
                        }
                        transaction.commit();
                    } catch (HibernateException ex) {
                        transaction.rollback();
                        //MUST WRITE TO FILE
                        ex.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    //session.close();
                }
            }

            if (!queueWarning.isEmpty()) {
                try {
                    transaction = session.beginTransaction();
                    try {
                        while (!queueWarning.isEmpty()) {
                            Warning warning = queueWarning.take();
                            session.save(warning);
                            Thread.currentThread().sleep(200);
                            updValBufferWarning.update(queueWarning.size());
                        }
                        transaction.commit();
                    } catch (HibernateException ex) {
                        transaction.rollback();
                        //MUST WRITE TO FILE
                        ex.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    //session.close();
                }
            }

            System.out.println("Size INFO: " + queueInfo.size());
            System.out.println("Size WARNING: " + queueWarning.size());
        }
    }



    public void setUpdList(UpdVal updVal1, UpdVal updVal2){
        this.updValBufferInfo = updVal1;
        this.updValBufferWarning = updVal2;
    }

    public void writeToDB(Dump dump){
        try {
            queueInfo.put(dump);
        } catch (InterruptedException e) {
            //MUST REPFORM COMMAND CLOSE SESSION
            e.printStackTrace();
        }
        updValBufferInfo.update(queueInfo.size());
    }

    public void writeToDB(String str){
        Warning warning = new Warning();
        warning.setIdWarning(str);
        warning.setRecordTime(new Timestamp(System.currentTimeMillis()));
        try {
            queueWarning.put(warning);
        } catch (InterruptedException e) {
            //MUST REPFORM COMMAND CLOSE SESSION
            e.printStackTrace();
        }
        updValBufferWarning.update(queueWarning.size());
    }

    public void close(){
        sf.close();
        session.close();
    }

    public int getSizeQueueInfo() {
        return queueInfo.size();
    }

    public int getSizeQueueWarning() {
        return queueWarning.size();
    }
}