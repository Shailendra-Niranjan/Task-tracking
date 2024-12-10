package smp.group.task.tracking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smp.group.task.tracking.Entity.Team;

import java.util.UUID;

public interface TeamRepo extends JpaRepository<Team , UUID> {
}
