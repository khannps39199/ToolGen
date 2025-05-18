package khannps39199.khannps39199.Entity;
import jakarta.persistence.*;

import java.time.*;
import java.math.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Share")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long Id;
    @Column(name = "UserId")
    private String UserId;
    @Column(name = "VideoId")
    private String VideoId;
    @Column(name = "Emails")
    private String Emails;
    @Column(name = "ShareDate")
    private LocalDateTime ShareDate;
}
