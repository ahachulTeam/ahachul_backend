package backend.team.ahachul_backend.schedule.job

import backend.team.ahachul_backend.api.lost.application.port.out.CategoryReader
import backend.team.ahachul_backend.api.lost.application.port.out.LostPostWriter
import backend.team.ahachul_backend.api.lost.application.service.LostPostFileService
import backend.team.ahachul_backend.api.lost.domain.entity.CategoryEntity
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.storage.CategoryStorage
import backend.team.ahachul_backend.common.storage.SubwayLineStorage
import backend.team.ahachul_backend.common.utils.FileUtils
import backend.team.ahachul_backend.schedule.Lost112Data
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component


@Component
class UpdateLostDataJob(
    private val lostPostWriter: LostPostWriter,
    private val subwayLineStorage: SubwayLineStorage,
    private val categoryStorage: CategoryStorage,
    private val lostPostFileService: LostPostFileService
): QuartzJobBean() {

    override fun executeInternal(context: JobExecutionContext) {
        val jobDataMap = context.jobDetail.jobDataMap
        val fileReadPath = jobDataMap.getString("FILE_READ_PATH")
        val response = FileUtils.readFileData<List<Map<String, Lost112Data>>>(fileReadPath)
        saveLostPosts(response)
    }

    private fun saveLostPosts(response: List<Map<String, Lost112Data>>) {
        response.forEach { map ->
            map.values.forEach {
                val subwayLine = getSubwayLineEntity(it.receiptPlace)
                val category = getCategory(it.categoryName)
                val lostPost = LostPostEntity.ofLost112(it, subwayLine, category)
                val entity = lostPostWriter.save(lostPost)
                lostPostFileService.saveLostPostFileUrl(entity, it.imageUrl)
            }
        }
    }

    private fun getSubwayLineEntity(receivedPlace: String): SubwayLineEntity? {
        val subwayLineName = subwayLineStorage.extractSubWayLine(receivedPlace)
        return subwayLineStorage.getSubwayLineEntityByName(subwayLineName)
    }

    private fun getCategory(categoryName: String): CategoryEntity? {
        val idx = categoryName.trim().indexOf(">")
        val primaryCategoryName = categoryName.substring(0, idx)
        return categoryStorage.getCategoryByName(primaryCategoryName)
    }
}
