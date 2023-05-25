package backend.team.ahachul_backend.schedule.listener

import backend.team.ahachul_backend.common.logging.Logger
import backend.team.ahachul_backend.schedule.job.UpdateLostDataJob
import org.quartz.*
import org.springframework.stereotype.Component


@Component
class JobFailureHandlingListener: TriggerListener {

    companion object {
        const val MAX_RETRY_COUNT: Int = 3
    }

    private val logger: Logger = Logger(javaClass)

    override fun getName(): String? {
        return this::class.simpleName
    }


    override fun triggerFired(trigger: Trigger?, context: JobExecutionContext?) {
        logger.info("Job triggered: ${ context!!.jobDetail.key }")
    }

    override fun vetoJobExecution(trigger: Trigger?, context: JobExecutionContext?): Boolean {
        if (context == null) return false

        val jobDataMap = context.jobDetail.jobDataMap
        val executeCount = jobDataMap.getInt(UpdateLostDataJob.RETRY_KEY)
        logger.info("${ context.jobDetail.key } Job triggered.. checking retry count: $executeCount")

        return when {
            executeCount < MAX_RETRY_COUNT -> false
            else -> {
                logger.info("Stop trigger Job ${context.jobDetail.key} : $executeCount exceed max count $MAX_RETRY_COUNT")
                true
            }
        }
    }

    override fun triggerMisfired(trigger: Trigger?) {
        logger.info("Job trigger misfired: ${ trigger!!.key }")
    }

    override fun triggerComplete(
        trigger: Trigger?,
        context: JobExecutionContext?,
        triggerInstructionCode: Trigger.CompletedExecutionInstruction?
    ) {
        logger.info("Job trigger Completed: ${ context!!.jobDetail.key }")
    }
}