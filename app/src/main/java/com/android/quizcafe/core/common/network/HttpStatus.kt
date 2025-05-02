package com.android.quizcafe.core.common.network

object HttpStatus {
    // Success : 2xx
    const val OK = 200
    const val CREATED = 201
    const val NO_CONTENT = 204

    // Client Error : 4xx
    const val BAD_REQUEST = 400
    const val UNAUTHORIZED = 401
    const val NOT_FOUND = 404
    const val REQUEST_TIMEOUT = 408
    const val TOO_MANY_REQUESTS = 429
    const val NETWORK_DISCONNECTED = 499

    // Server Error : 5xx
    const val INTERNAL_SERVER_ERROR = 500
    const val BAD_GATEWAY = 502

    // Unknown
    const val UNKNOWN = 999
}
