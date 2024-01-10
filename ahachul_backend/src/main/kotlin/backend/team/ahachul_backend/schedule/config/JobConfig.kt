package backend.team.ahachul_backend.schedule.config

import org.quartz.JobDetail
import org.quartz.Trigger

interface JobConfig {

    fun getJobTriggerPair(): Pair<JobDetail, Set<Trigger>> {
        return Pair(getJobDetail(), getTriggers())
    }

    fun getJobDetail(): JobDetail

    fun getTriggers(): Set<Trigger>
}
