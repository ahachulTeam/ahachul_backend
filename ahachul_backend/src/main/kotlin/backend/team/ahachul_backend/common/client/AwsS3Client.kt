package backend.team.ahachul_backend.common.client

import backend.team.ahachul_backend.common.exception.CommonException
import backend.team.ahachul_backend.common.logging.Logger
import backend.team.ahachul_backend.common.properties.AwsS3Properties
import backend.team.ahachul_backend.common.response.ResponseCode
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

@Component
class AwsS3Client(
    private val s3Client: AmazonS3Client,
    private val s3Properties: AwsS3Properties
) {

    val logger: Logger = Logger(javaClass)

    fun upload(file: MultipartFile) {
        try {
            s3Client.putObject(
                PutObjectRequest(
                    s3Properties.bucketName,
                    generateRandomUUID(),
                    convertToFile(file)
                ).withCannedAcl(CannedAccessControlList.PublicRead)
            )
        } catch (e: CommonException) {
            logger.error(e.message, ResponseCode.BAD_REQUEST, e.stackTraceToString())
        }
    }

    fun upload(files: List<MultipartFile>) {
        files.forEach { upload(it) }
    }

    private fun generateRandomUUID(): String {
        return UUID.randomUUID().toString()
    }

    private fun convertToFile(multipartFile: MultipartFile): File {
        val tempFile = File.createTempFile(multipartFile.originalFilename!!, "")
        multipartFile.inputStream.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }
}