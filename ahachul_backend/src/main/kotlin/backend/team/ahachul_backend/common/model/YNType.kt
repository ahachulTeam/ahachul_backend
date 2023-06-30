package backend.team.ahachul_backend.common.model

enum class YNType {
    Y, N;

    companion object {
        fun convert(bool: Boolean): YNType {
            return when (bool) {
                true -> Y
                false -> N
            }
        }
    }
}