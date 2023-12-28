package com.grossmax.androidtodolist.utils

import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.mp.KoinPlatformTools


/**
 * Get instance instance from Koin
 * @param qualifier
 * @param parameters
 */
inline fun <reified T : Any> koinGet(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T {
    return getKoin().get(qualifier, parameters)
}

/**
 * Lazy inject instance from Koin
 * @param qualifier
 * @param mode - LazyThreadSafetyMode
 * @param parameters
 */
inline fun <reified T : Any> koinInject(
    qualifier: Qualifier? = null,
    mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode(),
    noinline parameters: ParametersDefinition? = null,
): Lazy<T> =
    lazy(mode) { koinGet<T>(qualifier, parameters) }
