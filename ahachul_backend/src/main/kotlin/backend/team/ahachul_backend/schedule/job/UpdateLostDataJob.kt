package backend.team.ahachul_backend.schedule.job

import backend.team.ahachul_backend.api.lost.application.port.out.LostPostWriter
import backend.team.ahachul_backend.api.lost.domain.entity.CategoryEntity
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.storage.CategoryStorage
import backend.team.ahachul_backend.common.storage.SubwayLineStorage
import backend.team.ahachul_backend.common.utils.FileUtils
import backend.team.ahachul_backend.schedule.domain.Lost112Data
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component


@Component
class UpdateLostDataJob(
    private val lostPostWriter: LostPostWriter,
    private val subwayLineStorage: SubwayLineStorage,
    private val categoryStorage: CategoryStorage
): QuartzJobBean() {

    override fun executeInternal(context: JobExecutionContext) {
        val jobDataMap = context.jobDetail.jobDataMap
        val fileReadPath = jobDataMap.getString("FILE_READ_PATH")

        for (i: Int in 1 .. 10) {
            val fileFullReadPath = "$fileReadPath$i.json"
            val response = FileUtils.readFileData<List<Map<String, Lost112Data>>>(fileFullReadPath)
            response?.let { saveLostPosts(it) }
        }
    }

    private fun saveLostPosts(response: List<Map<String, Lost112Data>>) {
        val lostPosts = mutableListOf<LostPostEntity>()
        response.forEach { lostMap ->
            lostMap.values.map {
                val subwayLine = getSubwayLineEntity(it.receiptPlace)
                val category = getCategory(it.categoryName)
                val lostPost = LostPostEntity.ofLost112(it, subwayLine, category, it.imageUrl)
                lostPosts.add(lostPost)
            }
        }
        lostPostWriter.saveAll(lostPosts)
    }

    private fun getSubwayLineEntity(receivedPlace: String): SubwayLineEntity? {
        val subwayLineName = subwayLineStorage.extractSubWayLine(receivedPlace)
        return subwayLineStorage.getSubwayLineEntityByName(subwayLineName)
    }

    private fun getCategory(categoryName: String): CategoryEntity? {
        val primaryCategoryName = categoryStorage.extractPrimaryCategory(categoryName)
        return categoryStorage.getCategoryByName(primaryCategoryName)
    }
}
