package khannps39199.khannps39199.Entity;
import jakarta.persistence.*;

import java.time.*;
import java.math.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shares")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class shares {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shareId")
    private String shareId;
    @Column(name = "userId")
    private int userId;
    @Column(name = "postId")
    private int postId;
    @Column(name = "likeAt")
    private LocalDateTime likeAt;
}
