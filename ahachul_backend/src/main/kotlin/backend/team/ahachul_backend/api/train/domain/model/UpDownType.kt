package backend.team.ahachul_backend.api.train.domain.model

enum class UpDownType {
    UP, DOWN;

    companion object {
        fun from(code: String): UpDownType {
            return when (code) {
                "상행", "내선" -> UP
                "하행", "외선" -> DOWN
                else -> throw IllegalArgumentException()
            }
        }
    }
}