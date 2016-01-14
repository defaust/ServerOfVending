package processing.object;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name="logging")
public class Dump implements Serializable{
    private long id;
    private Timestamp receivedTime;
    private char command;
    private String imei;
    private String ip;
    private float csq;
    private float balSim;
    private Timestamp eventTime;
    private int banknotes;
    private float coins;
    private int levelTank;
    private int meter1;
    private int meter2;
    private int meter3;
    private int events;

    @Id
    @GeneratedValue()
    @Column (name="id", nullable = false, unique = true, columnDefinition = "BIGINT")
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "receivedTime", columnDefinition = "TIMESTAMP")
    public Timestamp getReceivedTime() {
        return receivedTime;
    }
    public void setReceivedTime(Timestamp currentTime) {
        this.receivedTime = currentTime;
    }

    @Column(name = "command", columnDefinition = "CHAR")
    public char getCommand() {
        return command;
    }
    public void setCommand(char command) {
        this.command = command;
    }

    @Column(name = "imei", columnDefinition = "VARCHAR(15)")
    public String getImei() {
        return imei;
    }
    public void setImei(String imei) {
        this.imei = imei;
    }

    @Column(name = "ip", columnDefinition = "VARCHAR(15)")
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name = "csq", columnDefinition = "FLOAT")
    public float getCsq() {
        return csq;
    }
    public void setCsq(float csq) {
        this.csq = csq;
    }

    @Column(name = "ballanceOfSim", columnDefinition = "FLOAT")
    public float getBalSim() {
        return balSim;
    }
    public void setBalSim(float balSim) {
        this.balSim = balSim;
    }

    @Column(name = "eventTime", columnDefinition = "TIMESTAMP")
    public Timestamp getEventTime() {
        return eventTime;
    }
    public void setEventTime(Timestamp epochTime) {
        this.eventTime = epochTime;
    }

    @Column(name = "banknotes", columnDefinition = "INT")
    public int getBanknotes() {
        return banknotes;
    }
    public void setBanknotes(int banknotes) {
        this.banknotes = banknotes;
    }

    @Column(name = "coins", columnDefinition = "FLOAT")
    public float getCoins() {
        return coins;
    }
    public void setCoins(float coins) {
        this.coins = coins;
    }

    @Column(name = "levelTank", columnDefinition = "FLOAT")
    public int getLevelTank() {
        return levelTank;
    }
    public void setLevelTank(int levelTank) {
        this.levelTank = levelTank;
    }

    @Column(name = "meter1", columnDefinition = "DOUBLE")
    public int getMeter1() {
        return meter1;
    }
    public void setMeter1(int meter1) {
        this.meter1 = meter1;
    }

    @Column(name = "meter2", columnDefinition = "DOUBLE")
    public int getMeter2() {
        return meter2;
    }
    public void setMeter2(int meter2) {
        this.meter2 = meter2;
    }

    @Column(name = "meter3", columnDefinition = "DOUBLE")
    public int getMeter3() {
        return meter3;
    }
    public void setMeter3(int meter3) {
        this.meter3 = meter3;
    }

    @Column(name = "event", columnDefinition = "CHAR")
    public int getEvents() {
        return events;
    }
    public void setEvents(int events) {
        this.events = events;
    }
}