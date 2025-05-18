package khannps39199.khannps39199.Entity;
import jakarta.persistence.*;

import java.time.*;
import java.math.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Favorite")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private String Id;
    @Column(name = "UserId")
    private String UserId;
    @Column(name = "VideoId")
    private String VideoId;
    @Column(name = "LikeDate")
    private LocalDateTime LikeDate;
}
