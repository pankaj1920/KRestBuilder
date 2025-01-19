package com.app.krestbuilder.utils

infix fun <T> Boolean.then(trueBranch: T) = if (this) trueBranch else null

infix fun <T> T?.otherwise(falseBranch: T): T = this ?: falseBranch