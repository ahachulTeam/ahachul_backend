package backend.team.ahachul_backend.schedule.job

import backend.team.ahachul_backend.api.lost.application.port.out.CategoryReader
import backend.team.ahachul_backend.api.lost.application.port.out.LostPostWriter
import backend.team.ahachul_backend.api.lost.application.service.LostPostFileService
import backend.team.ahachul_backend.api.lost.domain.entity.CategoryEntity
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.persistence.SubwayLineReader
import backend.team.ahachul_backend.common.utils.FileUtils
import backend.team.ahachul_backend.schedule.Lost112Data
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component


@Component
class UpdateLostDataJob(
    private val lostPostWriter: LostPostWriter,
    private val subwayLineReader: SubwayLineReader,
    private val categoryReader: CategoryReader,
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
        val subwayLineName = extractSubwayLine(receivedPlace)
        return runCatching {
            subwayLineReader.getByName(subwayLineName)
        }.getOrNull()
    }

    private fun getCategory(categoryName: String): CategoryEntity {
        val idx = categoryName.trim().indexOf(">")
        val primaryCategoryName = categoryName.substring(0, idx)
        return categoryReader.getCategoryByName(primaryCategoryName)
    }


    private fun extractSubwayLine(place: String): String {
        if (!place.endsWith(')')) return place

        val res = StringBuilder()
        for (i: Int in place.length - 2 downTo 0) {
            if (place[i] == '(') break
            res.append(place[i])
        }
        return res.reverse().toString()
    }
}
