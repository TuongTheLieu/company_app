package backend.flutter.repository;

import backend.flutter.entity.Company;
import backend.flutter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    void deleteById(String userId);


    boolean existsByEmail(String email);

    User findByEmail(String email);

    User findByRefreshTokenAndEmail(String refreshToken, String email);

    List<User> findByCompany(Company company);
}
