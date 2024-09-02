package tw.com.deathhit.core.travel_taipei_api

import tw.com.deathhit.core.travel_taipei_api.enum_type.Language

internal fun Language.toValue(): String = when(this) {
    Language.ENGLISH -> "en"
    Language.JAPANESE -> "ja"
    Language.KOREAN -> "ko"
    Language.SPANISH -> "es"
    Language.THAI -> "th"
    Language.VIETNAMESE -> "vi"
    Language.ZH_CN -> "zh-cn"
    Language.ZH_TW -> "zh-tw"
}