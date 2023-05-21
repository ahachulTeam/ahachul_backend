package backend.team.ahachul_backend.common.schedule

import backend.team.ahachul_backend.common.logging.Logger
import jakarta.annotation.PostConstruct
import org.quartz.*
import org.springframework.context.annotation.Configuration


@Configuration
class ScheduleConfig(
    private val scheduler: Scheduler
){

    private val logger: Logger = Logger(javaClass)

    @PostConstruct
    fun init() {
        try {
            scheduler.scheduleJob(getJobDetail(), getTrigger())
        } catch (e: SchedulerException) {
            logger.info("===== Exception occur while scheduling: stop [${this::class.simpleName}]")
            throw JobExecutionException(true)
        }
    }

    private fun getJobDetail(): JobDetail {
        return JobBuilder.newJob()
            .ofType(UpdateLostDataJob::class.java)
            .withIdentity(JobKey("UPDATE_LOST_DATA_JOB", "LOST"))
            .build()
    }

    private fun getTrigger(): Trigger {
        return TriggerBuilder.newTrigger()
            .withIdentity(TriggerKey("UPDATE_LOST_DATA_TRIGGER ", "LOST"))
            .startNow()
            .withSchedule(
                CronScheduleBuilder.cronSchedule("0 05 23 ? * *")
            )
            .build()
    }
}