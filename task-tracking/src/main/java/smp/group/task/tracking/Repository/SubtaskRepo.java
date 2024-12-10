package smp.group.task.tracking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smp.group.task.tracking.Entity.SubTask;

import java.util.UUID;

@Repository
public interface SubtaskRepo extends JpaRepository<SubTask , UUID> {
}
