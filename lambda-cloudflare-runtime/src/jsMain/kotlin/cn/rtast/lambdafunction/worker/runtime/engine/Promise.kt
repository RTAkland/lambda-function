/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-27
 */


package cn.rtast.lambdafunction.worker.runtime.engine

import js.promise.Promise

internal external fun <T> waitUntil(promise: Promise<T>)