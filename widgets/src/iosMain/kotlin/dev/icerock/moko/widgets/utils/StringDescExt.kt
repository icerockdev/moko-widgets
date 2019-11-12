/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.plural.pluralizedString
import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

fun StringDesc.localized(): String {
    return toLocalizedString(object : StringDesc.Formatter {
        override fun formatPlural(resource: PluralsResource, number: Int, args: Array<out Any>): String {
            val pluralized = pluralizedString(
                bundle = resource.bundle,
                resourceId = resource.resourceId,
                number = number
            )!!
            return stringWithFormat(pluralized, args)
        }

        override fun formatString(string: String, args: Array<out Any>): String {
            return stringWithFormat(string, args)
        }

        override fun plural(resource: PluralsResource, number: Int): String {
            return pluralizedString(
                bundle = resource.bundle,
                resourceId = resource.resourceId,
                number = number
            )!!
        }

        private fun stringWithFormat(format: String, args: Array<out Any>): String {
            // bad but objc interop limited :(
            // When calling variadic C functions spread operator is supported only for *arrayOf(...)
            return when (args.size) {
                0 -> NSString.stringWithFormat(format)
                1 -> NSString.stringWithFormat(format, args[0])
                2 -> NSString.stringWithFormat(format, args[0], args[1])
                3 -> NSString.stringWithFormat(format, args[0], args[1], args[2])
                4 -> NSString.stringWithFormat(format, args[0], args[1], args[2], args[3])
                5 -> NSString.stringWithFormat(format, args[0], args[1], args[2], args[3], args[4])
                6 -> NSString.stringWithFormat(format, args[0], args[1], args[2], args[3], args[4], args[5])
                7 -> NSString.stringWithFormat(format, args[0], args[1], args[2], args[3], args[4], args[5], args[6])
                8 -> NSString.stringWithFormat(
                    format,
                    args[0],
                    args[1],
                    args[2],
                    args[3],
                    args[4],
                    args[5],
                    args[6],
                    args[7]
                )
                9 -> NSString.stringWithFormat(
                    format,
                    args[0],
                    args[1],
                    args[2],
                    args[3],
                    args[4],
                    args[5],
                    args[6],
                    args[7],
                    args[8]
                )
                else -> throw IllegalArgumentException("can't handle more then 9 arguments now")
            }
        }
    })
}