package khannps39199.khannps39199.Entity;
import jakarta.persistence.*;

import java.time.*;
import java.math.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_Id")
    private String like_Id;
    @Column(name = "user_Id")
    private int user_Id;
    @Column(name = "post_Id")
    private int post_Id;
    @Column(name = "like_At")
    private LocalDateTime like_At;
}
