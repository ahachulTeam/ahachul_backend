package backend.team.ahachul_backend.common.client

import backend.team.ahachul_backend.common.properties.AwsS3Properties
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.PutObjectRequest
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

    fun upload(file: MultipartFile) {
        s3Client.putObject(
            PutObjectRequest(
                s3Properties.bucketName,
                generateRandomUUID(),
                convertToFile(file)
            )
        )
    }

    private fun generateRandomUUID(): String {
        return UUID.randomUUID().toString()
    }

    private fun convertToFile(multipartFile: MultipartFile): File {
        val file = File(multipartFile.originalFilename!!)
        file.createNewFile()
        val fos = FileOutputStream(file)
        fos.write(multipartFile.bytes)
        fos.close()
        return file
    }
}