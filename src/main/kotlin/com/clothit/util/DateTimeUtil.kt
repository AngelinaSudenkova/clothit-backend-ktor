package com.clothit.util

import java.time.Instant


class DateTimeUtil {


    companion object {


        private var currentTime: Instant = Instant.now()

        fun getCurrentTime(): Instant {
            if (currentTime == Instant.MIN) {
                return Instant.now()
            }
            return currentTime
        }

    }
}