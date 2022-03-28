/*
 * Copyright (c) 2020 Hai Zhang <dreaming.in.code.zh@gmail.com>
 * All Rights Reserved.
 */

package com.example.filemanager.utils

fun <T : CharSequence> T.takeIfNotBlank(): T? = if (isNotBlank()) this else null

fun <T : CharSequence> T.takeIfNotEmpty(): T? = if (isNotEmpty()) this else null
