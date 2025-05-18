package khannps39199.khannps39199.Entity;
import jakarta.persistence.*;

import java.time.*;
import java.math.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dataBV")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class dataBV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "masv")
    private int masv;
    @Column(name = "hoten")
    private String hoten;
    @Column(name = "diem")
    private String diem;
    @Column(name = "chuyennganh")
    private String chuyennganh;
}
