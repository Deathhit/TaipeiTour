package tw.com.deathhit.data.language

import tw.com.deathhit.domain.enum_type.Language

internal fun Int.toLanguage() = when(this) {
    Language.ENGLISH.toValue() -> Language.ENGLISH
    Language.JAPANESE.toValue() -> Language.JAPANESE
    Language.KOREAN.toValue() -> Language.KOREAN
    Language.SPANISH.toValue() -> Language.SPANISH
    Language.THAI.toValue() -> Language.THAI
    Language.VIETNAMESE.toValue() -> Language.VIETNAMESE
    Language.ZH_CN.toValue() -> Language.ZH_CN
    Language.ZH_TW.toValue() -> Language.ZH_TW
    else -> throw RuntimeException("Unexpected value of $this!")
}

internal fun Language.toValue() = when(this) {
    Language.ENGLISH -> 1000
    Language.JAPANESE -> 3000
    Language.KOREAN -> 4000
    Language.SPANISH -> 5000
    Language.THAI -> 6000
    Language.VIETNAMESE -> 7000
    Language.ZH_CN -> 8000
    Language.ZH_TW -> 9000
}