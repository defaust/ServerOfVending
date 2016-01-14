package processing.object;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name="warning")
public class Warning implements Serializable{
    private long id;
    private Timestamp recordTime;
    private String idWarning;

    @Id
    @GeneratedValue()
    @Column (name="id", unique = true, columnDefinition = "BIGINT")
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column (name="recordTime", columnDefinition = "TIMESTAMP")
    public Timestamp getRecordTime() {
        return recordTime;
    }
    public void setRecordTime(Timestamp currentTime) {
        this.recordTime = currentTime;
    }

    @Column (name="description")
    public String getIdWarning() {
        return idWarning;
    }
    public void setIdWarning(String idWarning) {
        this.idWarning = idWarning;
    }
}
