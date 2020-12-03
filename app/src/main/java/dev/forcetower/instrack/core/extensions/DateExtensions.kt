package dev.forcetower.instrack.core.extensions

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields

val LOCAL_DATE_TIME_EPOCH: LocalDateTime = LocalDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC)

val LocalDateTime.daysSinceEpoch: Long
    get() = LOCAL_DATE_TIME_EPOCH.until(this, ChronoUnit.DAYS)

val LocalDateTime.weekOfYear: Int
    get() = this.get(WeekFields.SUNDAY_START.weekOfYear())

inline fun <R> ClosedRange<LocalDateTime>.map(transform: (LocalDateTime) -> R): List<R> {
    val iterable = this.iterable()
    return iterable.mapTo(ArrayList(3), transform)
}

fun ClosedRange<LocalDateTime>.iterable(): Iterable<LocalDateTime> {
    return object : Iterable<LocalDateTime> {
        override fun iterator(): Iterator<LocalDateTime> {
            return object : Iterator<LocalDateTime> {
                private var next = this@iterable.start
                private val finalElement = this@iterable.endInclusive
                private var hasNext = !next.isAfter(this@iterable.endInclusive)

                override fun hasNext(): Boolean = hasNext

                override fun next(): LocalDateTime {
                    val value = next
                    if (value == finalElement) {
                        hasNext = false
                    } else {
                        next = next.plusDays(1)
                    }
                    return value
                }
            }
        }
    }
}

fun Long.asLocalDateTimeMs(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.of("UTC"))
}

fun Long.asLocalDateTimeSec(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.of("UTC"))
}
