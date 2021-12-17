/*
 * Copyright (c) 2020 Felix Hollederer
 *     This file is part of GymWenApp.
 *
 *     GymWenApp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     GymWenApp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with GymWenApp.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.ulan.timetable.utils

import com.ulan.timetable.receivers.setDoNotDisturbReceivers
import androidx.core.graphics.drawable.toBitmap
import com.ulan.timetable.utils.ShortcutUtils.Companion.createShortcuts
import com.mikepenz.aboutlibraries.LibsBuilder.withActivityTitle
import com.mikepenz.aboutlibraries.LibsBuilder.withAboutIconShown
import com.mikepenz.aboutlibraries.LibsBuilder.withFields
import com.mikepenz.aboutlibraries.LibsBuilder.withLicenseShown
import com.mikepenz.aboutlibraries.LibsBuilder.withAboutDescription
import com.mikepenz.aboutlibraries.LibsBuilder.withAboutAppName
import com.mikepenz.aboutlibraries.LibsBuilder.start
import saschpe.android.customtabs.CustomTabsHelper.Companion.addKeepAliveExtra
import saschpe.android.customtabs.CustomTabsHelper.Companion.openCustomTab
import android.database.sqlite.SQLiteOpenHelper
import com.ulan.timetable.utils.DbHelper
import com.ulan.timetable.profiles.ProfileManagement
import android.database.sqlite.SQLiteDatabase
import com.ulan.timetable.fragments.WeekdayFragment
import com.ulan.timetable.model.Week
import com.ulan.timetable.utils.PreferenceUtil
import com.ulan.timetable.model.Homework
import com.ulan.timetable.model.Teacher
import com.ulan.timetable.model.Exam
import com.ulan.timetable.utils.WeekUtils
import androidx.appcompat.app.AppCompatActivity
import com.ulan.timetable.R
import android.annotation.SuppressLint
import android.widget.EditText
import com.ulan.timetable.adapters.WeekAdapter
import android.widget.AbsListView.MultiChoiceModeListener
import android.util.SparseBooleanArray
import android.view.MenuInflater
import android.content.SharedPreferences.Editor
import android.app.Activity
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback
import com.afollestad.materialdialogs.DialogAction
import android.content.pm.PackageManager
import android.app.PendingIntent
import android.app.AlarmManager
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDelegate
import android.util.TypedValue
import com.ulan.timetable.activities.SettingsActivity
import com.ulan.timetable.utils.NotificationUtil
import com.github.stephenvinouze.shapetextdrawable.ShapeTextDrawable
import com.github.stephenvinouze.shapetextdrawable.ShapeForm
import android.graphics.Typeface
import androidx.core.graphics.drawable.IconCompat
import com.ulan.timetable.activities.MainActivity
import android.media.RingtoneManager
import com.ulan.timetable.receivers.NotificationDismissButtonReceiver
import android.app.NotificationChannel
import android.widget.TextView
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.widget.TimePicker
import android.widget.NumberPicker
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog
import android.graphics.drawable.ColorDrawable
import me.jfenn.colorpickerdialog.interfaces.OnColorPickedListener
import me.jfenn.colorpickerdialog.views.picker.RGBPickerView
import android.widget.TextView.OnEditorActionListener
import android.view.inputmethod.EditorInfo
import android.view.View.OnFocusChangeListener
import android.text.TextUtils
import com.google.android.material.snackbar.Snackbar
import com.ulan.timetable.utils.AlertDialogsHelper
import com.ulan.timetable.adapters.FragmentsTabAdapter
import androidx.viewpager.widget.ViewPager
import android.widget.LinearLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.afollestad.materialdialogs.MaterialDialog.ListCallback
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import com.ulan.timetable.adapters.HomeworkAdapter
import android.provider.CalendarContract
import com.pd.chocobar.ChocoBar
import com.ulan.timetable.adapters.TeachersAdapter
import com.ulan.timetable.adapters.NotesAdapter
import com.ulan.timetable.adapters.ExamsAdapter
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.cardview.widget.CardView
import android.view.LayoutInflater
import androidx.core.widget.ImageViewCompat
import android.content.res.ColorStateList
import com.ulan.timetable.activities.TeachersActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.ulan.timetable.appwidget.Dao.BaseDao
import kotlin.jvm.Synchronized
import com.ulan.timetable.appwidget.Dao.DBManager
import com.ulan.timetable.appwidget.Dao.DataBaseHelper
import com.ulan.timetable.appwidget.Dao.AppWidgetDao
import android.widget.RemoteViewsService
import android.widget.RemoteViewsService.RemoteViewsFactory
import com.ulan.timetable.appwidget.DayAppWidgetService.DayAppWidgetRemoteViewsFactory
import android.widget.RemoteViews
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.*
import com.ulan.timetable.appwidget.DayAppWidgetService
import com.ulan.timetable.appwidget.DayAppWidgetProvider
import android.os.Bundle
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.RadioGroup
import android.widget.SeekBar
import com.ulan.timetable.appwidget.AppWidgetConstants
import android.widget.ImageButton
import com.ulan.timetable.utils.FragmentHelper
import androidx.preference.PreferenceFragmentCompat
import com.ulan.timetable.fragments.SettingsFragment
import com.ulan.timetable.activities.TimeSettingsActivity
import com.afollestad.materialdialogs.MaterialDialog.ListCallbackMultiChoice
import android.text.InputType
import android.text.format.DateFormat
import androidx.preference.SwitchPreferenceCompat
import com.ulan.timetable.receivers.DailyReceiver
import com.google.android.material.navigation.NavigationView
import com.ulan.timetable.utils.ShortcutUtils
import androidx.drawerlayout.widget.DrawerLayout
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.AdapterView
import com.ulan.timetable.activities.ProfileActivity
import com.google.android.material.tabs.TabLayout
import androidx.core.view.GravityCompat
import com.mikepenz.aboutlibraries.LibsBuilder
import com.ulan.timetable.R.string
import com.ulan.timetable.activities.ExamsActivity
import com.ulan.timetable.activities.HomeworkActivity
import com.ulan.timetable.activities.NotesActivity
import com.ulan.timetable.activities.SummaryActivity
import info.isuru.sheriff.enums.SheriffPermission
import com.ajts.androidmads.library.SQLiteToExcel
import com.ajts.androidmads.library.SQLiteToExcel.ExportListener
import com.ajts.androidmads.library.ExcelToSQLite
import com.ajts.androidmads.library.ExcelToSQLite.ImportListener
import androidx.browser.customtabs.CustomTabsIntent
import saschpe.android.customtabs.WebViewFallback
import info.isuru.sheriff.helper.Sheriff
import info.isuru.sheriff.interfaces.PermissionListener
import com.ulan.timetable.activities.MainActivity.MyPermissionListener
import android.widget.AdapterView.OnItemClickListener
import com.ulan.timetable.activities.NoteInfoActivity
import com.ulan.timetable.fragments.ProfileActivityFragment
import me.yaoandy107.ntut_timetable.CourseTableLayout
import me.yaoandy107.ntut_timetable.model.StudentCourse
import me.yaoandy107.ntut_timetable.model.CourseInfo
import com.ulan.timetable.activities.SummaryActivity.CustomCourseInfo
import com.github.tlaabs.timetableview.Schedule
import com.ulan.timetable.activities.SummaryActivity.CustomSchedule
import com.github.tlaabs.timetableview.TimetableView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.ulan.timetable.fragments.TimeSettingsFragment
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object WeekUtils {
    fun getNextWeek(weeks: ArrayList<Week?>): Week? {
        val calendar = Calendar.getInstance()
        calendar[Calendar.MINUTE] = calendar[Calendar.MINUTE] + 1
        var hour = "" + calendar[Calendar.HOUR_OF_DAY]
        if (hour.length < 2) hour = "0$hour"
        var minutes = "" + calendar[Calendar.MINUTE]
        if (minutes.length < 2) minutes = "0$minutes"
        val now = "$hour:$minutes"
        for (i in weeks.indices) {
            val week = weeks[i]
            if (now.compareTo(
                    week.getFromTime(),
                    ignoreCase = true
                ) >= 0 && now.compareTo(week.getToTime(), ignoreCase = true) <= 0 || now.compareTo(
                    week.getToTime(),
                    ignoreCase = true
                ) <= 0
            ) {
                return week
            }
        }
        return null
    }

    fun getAllWeeks(dbHelper: DbHelper): ArrayList<Week> {
        val keys = arrayOf<String>(
            WeekdayFragment.Companion.KEY_MONDAY_FRAGMENT,
            WeekdayFragment.Companion.KEY_TUESDAY_FRAGMENT,
            WeekdayFragment.Companion.KEY_WEDNESDAY_FRAGMENT,
            WeekdayFragment.Companion.KEY_THURSDAY_FRAGMENT,
            WeekdayFragment.Companion.KEY_FRIDAY_FRAGMENT,
            WeekdayFragment.Companion.KEY_SATURDAY_FRAGMENT,
            WeekdayFragment.Companion.KEY_SUNDAY_FRAGMENT
        )
        val calendar = Calendar.getInstance()
        val customWeeks = getWeeks(dbHelper, keys, calendar)
        calendar[Calendar.WEEK_OF_YEAR] = calendar[Calendar.WEEK_OF_YEAR] - 1
        customWeeks.addAll(getWeeks(dbHelper, keys, calendar))
        return removeDuplicates(customWeeks)
    }

    private fun getWeeks(
        dbHelper: DbHelper,
        keys: Array<String>,
        calendar: Calendar
    ): ArrayList<Week> {
        val weeks = ArrayList<Week>()
        for (key in keys) {
            weeks.addAll(dbHelper.getWeek(key, calendar))
        }
        return weeks
    }

    fun getPreselection(activity: AppCompatActivity): ArrayList<Week> {
        val dbHelper = DbHelper(activity)
        val customWeeks = getAllWeeks(dbHelper)
        val subjects = ArrayList<String>()
        for (w in customWeeks) {
            subjects.add(w.subject.toUpperCase())
        }
        val preselectedValues =
            ArrayList(Arrays.asList(*activity.resources.getStringArray(R.array.preselected_subjects_values)))
        val preselected = PreferenceUtil.getPreselectionElements(activity)
        val preselectedColors = activity.resources.getIntArray(R.array.preselected_subjects_colors)
        val preselectedLanguage = activity.resources.getStringArray(R.array.preselected_subjects)
        for (i in preselected!!.indices) {
            if (preselectedValues.contains(preselected[i])) {
                val langValue = preselectedLanguage[preselectedValues.indexOf(
                    preselected[i]
                )]
                if (!subjects.contains(langValue.toUpperCase())) customWeeks.add(
                    0,
                    Week(langValue, "", "", "", "", preselectedColors[i])
                )
            }
        }
        Collections.sort(customWeeks) { week1: Week, week2: Week ->
            week1.subject.compareTo(
                week2.subject,
                ignoreCase = true
            )
        }
        return customWeeks
    }

    private fun removeDuplicates(weeks: ArrayList<Week>): ArrayList<Week> {
        val returnValue = ArrayList<Week>()
        val returnValueSubjects = ArrayList<String>()
        for (w in weeks) {
            if (!returnValueSubjects.contains(w.subject.toUpperCase())) {
                returnValue.add(w)
                returnValueSubjects.add(w.subject.toUpperCase())
            }
        }
        return returnValue
    }

    fun getMatchingScheduleBegin(time: String?, context: Context?): Int {
        return getMatchingScheduleBegin(
            time,
            PreferenceUtil.getStartTime(context),
            PreferenceUtil.getPeriodLength(context)
        )
    }

    fun getMatchingScheduleBegin(time: String?, startTime: IntArray, lessonDuration: Int): Int {
        val schedule = getDurationOfWeek(
            Week(
                "",
                "",
                "",
                startTime[0].toString() + ":" + (startTime[1] - 1),
                time,
                0
            ), false, lessonDuration
        )
        return if (schedule == 0) 1 else schedule
    }

    fun getMatchingScheduleEnd(time: String?, context: Context?): Int {
        return getMatchingScheduleEnd(
            time,
            PreferenceUtil.getStartTime(context),
            PreferenceUtil.getPeriodLength(context)
        )
    }

    fun getMatchingScheduleEnd(time: String?, startTime: IntArray, lessonDuration: Int): Int {
        val schedule = getDurationOfWeek(
            Week(
                "",
                "",
                "",
                startTime[0].toString() + ":" + startTime[1],
                time,
                0
            ), false, lessonDuration
        )
        return if (schedule == 0) 1 else schedule
    }

    fun getMatchingTimeBegin(hour: Int, context: Context?): String {
        return getMatchingTimeBegin(
            hour,
            PreferenceUtil.getStartTime(context),
            PreferenceUtil.getPeriodLength(context)
        )
    }

    fun getMatchingTimeBegin(hour: Int, startTime: IntArray, lessonDuration: Int): String {
        val startOfSchool = Calendar.getInstance()
        startOfSchool[Calendar.HOUR_OF_DAY] = startTime[0]
        startOfSchool[Calendar.MINUTE] = startTime[1]
        startOfSchool.timeInMillis =
            startOfSchool.timeInMillis + (hour - 1) * lessonDuration * 60 * 1000
        return String.format(
            Locale.getDefault(),
            "%02d:%02d",
            startOfSchool[Calendar.HOUR_OF_DAY],
            startOfSchool[Calendar.MINUTE]
        )
    }

    fun getMatchingTimeEnd(hour: Int, context: Context?): String {
        return getMatchingTimeEnd(
            hour,
            PreferenceUtil.getStartTime(context),
            PreferenceUtil.getPeriodLength(context)
        )
    }

    fun getMatchingTimeEnd(hour: Int, startTime: IntArray, lessonDuration: Int): String {
        val startOfSchool = Calendar.getInstance()
        startOfSchool[Calendar.HOUR_OF_DAY] = startTime[0]
        startOfSchool[Calendar.MINUTE] = startTime[1]
        startOfSchool.timeInMillis = startOfSchool.timeInMillis + hour * lessonDuration * 60 * 1000
        return String.format(
            Locale.getDefault(),
            "%02d:%02d",
            startOfSchool[Calendar.HOUR_OF_DAY],
            startOfSchool[Calendar.MINUTE]
        )
    }

    fun getDurationOfWeek(w: Week, countOnlyIfFitsLessonsTime: Boolean, lessonDuration: Int): Int {
        val weekCalendarStart = Calendar.getInstance()
        val startHour = w.fromTime.substring(0, w.fromTime.indexOf(":")).toInt()
        weekCalendarStart[Calendar.HOUR_OF_DAY] = startHour
        val startMinute = w.fromTime.substring(w.fromTime.indexOf(":") + 1).toInt()
        weekCalendarStart[Calendar.MINUTE] = startMinute
        val weekCalendarEnd = Calendar.getInstance()
        val endHour = w.toTime.substring(0, w.toTime.indexOf(":")).toInt()
        weekCalendarEnd[Calendar.HOUR_OF_DAY] = endHour
        val endMinute = w.toTime.substring(w.toTime.indexOf(":") + 1).toInt()
        weekCalendarEnd[Calendar.MINUTE] = endMinute
        val differencesInMillis = weekCalendarEnd.timeInMillis - weekCalendarStart.timeInMillis
        val inMinutes = (differencesInMillis / 1000 / 60).toInt()
        if (inMinutes < lessonDuration && countOnlyIfFitsLessonsTime) return 0
        val multiplier: Int
        multiplier = if (inMinutes % lessonDuration > 0 && !countOnlyIfFitsLessonsTime) {
            inMinutes / lessonDuration + 1
        } else inMinutes / lessonDuration
        return multiplier
    }

    fun isEvenWeek(termStart1: Calendar, now1: Calendar, startOnSunday: Boolean): Boolean {
        var isEven = true
        val termStart = Calendar.getInstance(Locale.GERMAN)
        termStart.timeInMillis = termStart1.timeInMillis
        val now = Calendar.getInstance(Locale.GERMAN)
        now.timeInMillis = now1.timeInMillis
        var weekDifference = now[Calendar.WEEK_OF_YEAR] - termStart[Calendar.WEEK_OF_YEAR]
        if (weekDifference < 0) {
            weekDifference = -weekDifference
        }
        for (i in 0 until weekDifference) {
            isEven = !isEven
        }
        if (startOnSunday) {
            if (now[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY) {
                isEven = !isEven
            }
        }
        return isEven
    }

    fun localizeDate(context: Context?, date: String?): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return try {
            localizeDate(context, dateFormat.parse(date))
        } catch (e: Exception) {
            date
        }
    }

    fun localizeDate(context: Context?, date: Date?): String {
        val dateFormat = DateFormat.getDateFormat(context)
        return dateFormat.format(date)
    }

    fun localizeTime(context: Context, time: String?): String? {
        val dateFormat = SimpleDateFormat("HH:mm")
        return try {
            localizeTime(context, dateFormat.parse(time))
        } catch (e: Exception) {
            time
        }
    }

    private fun localizeTime(context: Context, date: Date): String {
        val dateFormat = DateFormat.getTimeFormat(context)
        return dateFormat.format(date)
    }
}