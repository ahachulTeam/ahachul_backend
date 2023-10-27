package backend.team.ahachul_backend.schedule.config

import backend.team.ahachul_backend.schedule.job.UpdateLostDataJob
import backend.team.ahachul_backend.schedule.listener.JobFailureHandlingListener
import jakarta.annotation.PostConstruct
import org.quartz.*
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.util.concurrent.TimeUnit


@Configuration
@Profile("dev")
class ScheduleConfig(
    private val scheduler: Scheduler
){

    @PostConstruct
    fun init() {
        try {
            setTriggerListener()
            scheduler.scheduleJob(getJobDetail(), getTrigger())
        } catch (e: SchedulerException) {
            TimeUnit.MINUTES.sleep(1)
            throw JobExecutionException(true)
        }
    }

    private fun setTriggerListener() {
        val listenerManager = scheduler.listenerManager
        listenerManager.addTriggerListener(JobFailureHandlingListener())
    }

    private fun getJobDetail(): JobDetail {
        return JobBuilder.newJob()
            .ofType(UpdateLostDataJob::class.java)
            .usingJobData(getJobDataMap())
            .withIdentity(JobKey("UPDATE_LOST_DATA_JOB", "LOST"))
            .build()
    }

    private fun getJobDataMap(): JobDataMap {
        val jobDataMap = JobDataMap()
        jobDataMap.put("EXECUTION_COUNT", 0)
        jobDataMap.put("MAX_RETRY_COUNT", 3)
        jobDataMap.put("FILE_READ_PATH", "/home/")
        return jobDataMap
    }

    private fun getTrigger(): Trigger {
        return TriggerBuilder.newTrigger()
            .withIdentity(TriggerKey("UPDATE_LOST_DATA_TRIGGER ", "LOST"))
            .startNow()
            .withSchedule(
                CronScheduleBuilder.cronSchedule("0 10 * ? * *")
                    .withMisfireHandlingInstructionFireAndProceed()
            )
            .build()
    }
}
