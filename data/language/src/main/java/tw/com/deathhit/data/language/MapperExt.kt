package tw.com.deathhit.data.language

import tw.com.deathhit.domain.enum_type.Language

internal fun Int.toLanguage() = when(this) {
    Language.ENGLISH.toValue() -> Language.ENGLISH
    Language.JAPANESE.toValue() -> Language.JAPANESE
    Language.KOREAN.toValue() -> Language.KOREAN
    Language.ZH_CN.toValue() -> Language.ZH_CN
    Language.ZH_TW.toValue() -> Language.ZH_TW
    else -> throw RuntimeException("Unexpected value of $this!")
}

internal fun Language.toValue() = when(this) {
    Language.ENGLISH -> 1000
    Language.JAPANESE -> 2000
    Language.KOREAN -> 3000
    Language.ZH_CN -> 4000
    Language.ZH_TW -> 5000
}