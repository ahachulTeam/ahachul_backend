package backend.team.ahachul_backend.api.rank.event

import backend.team.ahachul_backend.api.rank.application.service.HashTagRankService
import backend.team.ahachul_backend.common.logging.Logger
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component


@Component
class HashTagSearchEventListener(
    private val hashTagRankService: HashTagRankService
){

    private val logger: Logger = Logger(javaClass)

    @Async
    @EventListener(HashTagSearchEvent::class)
    fun handle(event: HashTagSearchEvent) {
        logger.info("new hashtag search event handled: ${event.hashTagName}")
        try {
            hashTagRankService.saveLog(event.hashTagName, event.userId)
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }
}
