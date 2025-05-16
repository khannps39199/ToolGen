package khannps39199.khannps39199.Entity;
import jakarta.persistence.*;

import java.time.*;
import java.math.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_Id")
    private String post_Id;
    @Column(name = "user_Id")
    private int user_Id;
    @Column(name = "title")
    private String title;
    @Column(name = "create_At")
    private LocalDateTime create_At;
    @Column(name = "pictures_Src")
    private String pictures_Src;
    @Column(name = "content")
    private String content;
    @Column(name = "view_Post")
    private int view_Post;
    @Column(name = "like_Count")
    private int like_Count;
    @Column(name = "share_Count")
    private int share_Count;
}
