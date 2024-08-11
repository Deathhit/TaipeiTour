package tw.com.deathhit.core.app_database.enum_type

enum class Language(internal val id: Int) {
    ENGLISH(Id.ENGLISH),
    JAPANESE(Id.JAPANESE),
    KOREAN(Id.KOREAN),
    SPANISH(Id.SPANISH),
    THAI(Id.THAI),
    VIETNAMESE(Id.VIETNAMESE),
    ZH_CN(Id.ZH_CN),
    ZH_TW(Id.ZH_TW);

    internal object Id{
        const val ENGLISH = 1000
        const val JAPANESE = 3000
        const val KOREAN = 4000
        const val SPANISH = 5000
        const val THAI = 6000
        const val VIETNAMESE = 7000
        const val ZH_CN = 8000
        const val ZH_TW = 9000
    }
}