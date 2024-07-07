package com.example.model

import com.neovisionaries.i18n.CountryCode
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
/**
 * https://en.wikipedia.org/wiki/ISO_3166-1_alpha-3
 */
value class Country(val code: CountryCode)
