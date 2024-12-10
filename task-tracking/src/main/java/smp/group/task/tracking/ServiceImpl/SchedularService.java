package smp.group.task.tracking.ServiceImpl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class SchedularService {

    @Scheduled(fixedRate = 300000)
    void checkTaskProgress(){

    }

    @Scheduled(cron = "0 0 0 * * ?")
    void notifyPendingTask(){

    }

}
