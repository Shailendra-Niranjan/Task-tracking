package smp.group.task.tracking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import smp.group.task.tracking.Entity.Users;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    Users findUsersByEmail(String email);
    @Query("select u from Users u where u.email in :emails")
    List<Users> findUsersByEmails(@Param("emails") List<String> emails);

}
