import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * "yyyy-MM-dd'T'HH:mm:ss" 형식의 날짜 문자열을 "오늘", "n일 전", "yyyy-MM-dd" 형식으로 변환합니다.
 * Android API 레벨에 따라 자동으로 분기 처리됩니다.
 * @return 변환된 상대 날짜 문자열. 파싱 실패 시 원본 문자열을 반환합니다.
 */
fun String.toRelativeDate(): String {
    // API 레벨에 따라 적절한 private 확장 함수를 호출합니다.
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.toRelativeDateForApi26AndAbove()
    } else {
        this.toRelativeDateForApi24()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun String.toRelativeDateForApi26AndAbove(): String {
    return try {
        val createdAtDate = LocalDateTime.parse(this).toLocalDate()
        val today = LocalDate.now()
        val daysBetween = ChronoUnit.DAYS.between(createdAtDate, today)

        when {
            daysBetween == 0L -> "오늘"
            daysBetween == 1L -> "1일 전"
            daysBetween <= 7L -> "${daysBetween}일 전"
            else -> createdAtDate.toString()
        }
    } catch (e: DateTimeParseException) {
        // 날짜 형식이 잘못된 경우 원본 문자열 반환
        this
    }
}

private fun String.toRelativeDateForApi24(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    return try {
        val parsedDate: Date = inputFormat.parse(this) ?: return this

        val todayCalendar = Calendar.getInstance().apply { time = Date() }
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0)
        todayCalendar.set(Calendar.MINUTE, 0)
        todayCalendar.set(Calendar.SECOND, 0)
        todayCalendar.set(Calendar.MILLISECOND, 0)

        val createdAtCalendar = Calendar.getInstance()
        createdAtCalendar.time = parsedDate
        createdAtCalendar.set(Calendar.HOUR_OF_DAY, 0)
        createdAtCalendar.set(Calendar.MINUTE, 0)
        createdAtCalendar.set(Calendar.SECOND, 0)
        createdAtCalendar.set(Calendar.MILLISECOND, 0)

        val diffInMillis = todayCalendar.timeInMillis - createdAtCalendar.timeInMillis
        val daysBetween = diffInMillis / (24 * 60 * 60 * 1000)

        when {
            daysBetween == 0L -> "오늘"
            daysBetween == 1L -> "1일 전"
            daysBetween <= 7L -> "${daysBetween}일 전"
            else -> {
                val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                outputFormat.format(parsedDate)
            }
        }
    } catch (e: Exception) {
        // 파싱 중 오류 발생 시 원본 문자열 반환
        this
    }
}
