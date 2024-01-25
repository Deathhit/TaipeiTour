package tw.com.deathhit.feature.set_language

import tw.com.deathhit.domain.enum_type.Language

internal fun Language.toDisplayText() = when(this) {
    Language.ENGLISH -> "English"
    Language.JAPANESE -> "日本語"
    Language.KOREAN -> "한국어"
    Language.ZH_CN -> "简体中文"
    Language.ZH_TW -> "繁體中文"
}