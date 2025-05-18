package khannps39199.khannps39199.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import khannps39199.khannps39199.Entity.dataBV;

@Repository
public interface dataBVRepository extends JpaRepository<dataBV, Integer> {
    // Add custom query methods here if needed
}
