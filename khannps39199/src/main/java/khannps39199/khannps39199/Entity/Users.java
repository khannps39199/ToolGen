package khannps39199.khannps39199.Entity;
import jakarta.persistence.*;

import java.time.*;
import java.math.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private String Id;
    @Column(name = "Password")
    private String Password;
    @Column(name = "Fullname")
    private String Fullname;
    @Column(name = "Email")
    private String Email;
    @Column(name = "Admin")
    private boolean Admin;
    @Column(name = "imgAvatar")
    private String imgAvatar;
}
