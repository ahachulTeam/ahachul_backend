package backend.team.ahachul_backend

import org.junit.jupiter.api.Test
import java.util.regex.Pattern

//@SpringBootTest
class AhachulBackendApplicationTests {

    @Test
    fun contextLoads() {
    }

    @Test
    fun regex() {
//        val log = "[timestamp][thread][logger][userId][name]"
//        val temp = "나는 바보다"
//        val matcher = Pattern.compile("나는 (?<timestamp>.*)다").matcher(temp)
//        if (matcher.matches()) {
//            print(matcher.group("timestamp"))
//        }
        val log = "24-01-15 22:55:43 [http-nio-8080-exec-3] [HASHTAG_LOGGER:13] - userId = 1 hashtag = 이름3"
//        val log = "24-01-15 23:05:35 [http-nio-8080-exec-2][HASHTAG_LOGGER:13][1][이름3]"
        val matcher = Pattern.compile("(?<timestamp>.*) \\[(?<thread>.*)\\] \\[(?<logger>.*)\\] - userId = (?<userId>.*) hashtag = (?<name>.*)").matcher(log)
        if (matcher.matches()) {
            println(matcher.group("timestamp"))  // yy-MM-dd HH:mm:ss
            println(matcher.group("userId"))
            println(matcher.group("name"))
        }
    }

    @Test
    fun test() {
        val strings = listOf("나는 바보입니다", "나는 개입니다")
        for (string in strings) {
            val m = Pattern.compile("나는 (?<timestamp>.*)입니다").matcher(string)
            if (m.matches()) {
                println(m.group("timestamp"))
            }
        }
    }
}
