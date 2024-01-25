package tw.com.deathhit.core.app_database.enum_type

enum class Language(internal val id: Int) {
    ENGLISH(Id.ENGLISH),
    JAPANESE(Id.JAPANESE),
    KOREAN(Id.KOREAN),
    ZH_CN(Id.ZH_CN),
    ZH_TW(Id.ZH_TW);

    internal object Id{
        const val ENGLISH = 1000
        const val JAPANESE = 2000
        const val KOREAN = 3000
        const val ZH_CN = 4000
        const val ZH_TW = 5000
    }
}