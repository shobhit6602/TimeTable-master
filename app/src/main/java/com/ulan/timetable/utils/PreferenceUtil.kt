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
import android.content.res.Configuration
import android.graphics.Color
import com.ulan.timetable.appwidget.DayAppWidgetService
import com.ulan.timetable.appwidget.DayAppWidgetProvider
import android.os.Bundle
import android.provider.Settings
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
import androidx.core.graphics.drawable.DrawableCompat
import androidx.preference.PreferenceManager
import com.ulan.timetable.fragments.TimeSettingsFragment
import java.lang.Exception
import java.util.*

object PreferenceUtil {
    private fun getBooleanSettings(context: Context?, key: String, defaultValue: Boolean): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue)
    }

    fun isNotification(context: Context?): Boolean {
        return getBooleanSettings(context, "timetableNotif", true)
    }

    fun setAlarmTime(context: Context, vararg times: Int) {
        if (times.size != 3) {
            if (times.size > 0 && times[0] == 0) {
                setAlarm(context, false)
            } else {
                println("wrong parameters")
            }
            return
        }
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPref.edit()
        setAlarm(context, true)
        editor.putInt("Alarm_hour", times[0])
        editor.putInt("Alarm_minute", times[1])
        editor.putInt("Alarm_second", times[2])
        editor.commit()
    }

    fun getAlarmTime(context: Context?): IntArray {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        return intArrayOf(
            sharedPref.getInt("Alarm_hour", 7),
            sharedPref.getInt("Alarm_minute", 55),
            sharedPref.getInt("Alarm_second", 0)
        )
    }

    private fun setAlarm(context: Context, value: Boolean) {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putBoolean("alarm", value)
        editor.commit()
    }

    fun isAlarmOn(context: Context): Boolean {
        return getBooleanSettings(context, "alarm", false)
    }

    fun doNotDisturbDontAskAgain(context: Context?): Boolean {
        return getBooleanSettings(context, "do_not_disturb_dont_ask", false)
    }

    fun setDoNotDisturbDontAskAgain(context: Context, value: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putBoolean("do_not_disturb_dont_ask", value).apply()
    }

    fun isAutomaticDoNotDisturb(context: Context?): Boolean {
        return getBooleanSettings(context, "automatic_do_not_disturb", true)
    }

    fun setDoNotDisturb(activity: Activity, dontAskAgain: Boolean) {
        val notificationManager =
            activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if the notification policy access has been granted for the app.
            if (!Objects.requireNonNull(notificationManager).isNotificationPolicyAccessGranted && !dontAskAgain) {
                val drawable =
                    ContextCompat.getDrawable(activity, R.drawable.ic_do_not_disturb_on_black_24dp)
                try {
                    val wrappedDrawable = DrawableCompat.wrap(
                        Objects.requireNonNull(drawable)
                    )
                    DrawableCompat.setTint(wrappedDrawable, getTextColorPrimary(activity))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                MaterialDialog.Builder(activity)
                    .title(string.permission_required)
                    .content(string.do_not_disturb_permission_desc)
                    .onPositive { dialog: MaterialDialog?, which: DialogAction? ->
                        val intent = Intent(
                            Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
                        )
                        activity.startActivity(intent)
                    }
                    .positiveText(string.permission_ok_button)
                    .negativeText(string.permission_cancel_button)
                    .onNegative { dialog: MaterialDialog, which: DialogAction? -> dialog.dismiss() }
                    .icon(Objects.requireNonNull(drawable))
                    .onNeutral { dialog: MaterialDialog?, which: DialogAction? ->
                        setDoNotDisturbDontAskAgain(
                            activity,
                            true
                        )
                    }
                    .neutralText(string.dont_show_again)
                    .show()
            }
        }
        setDoNotDisturbReceivers(activity, false)
    }

    fun isDoNotDisturbTurnOff(context: Context?): Boolean {
        return getBooleanSettings(context, "do_not_disturb_turn_off", false)
    }

    fun isAlwaysNotification(context: Context?): Boolean {
        return getBooleanSettings(context, "alwaysNotification", false)
    }

    fun setOneTimeAlarm(
        context: Context,
        cls: Class<*>,
        hour: Int,
        min: Int,
        second: Int,
        id: Int
    ) {
        // cancel already scheduled reminders
        cancelAlarm(context, cls, id)
        val currentCalendar = Calendar.getInstance()
        val customCalendar = Calendar.getInstance()
        customCalendar.timeInMillis = System.currentTimeMillis()
        customCalendar[Calendar.HOUR_OF_DAY] = hour
        customCalendar[Calendar.MINUTE] = min
        customCalendar[Calendar.SECOND] = second
        if (customCalendar.before(currentCalendar)) customCalendar.add(Calendar.DATE, 1)

        // Enable a receiver
        val receiver = ComponentName(context, cls)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        val intent = Intent(context, cls)
        val pendingIntent = PendingIntent.getBroadcast(context.applicationContext, id, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            ?: return
        val startTime = customCalendar.timeInMillis
        if (Build.VERSION.SDK_INT < 23) {
            if (Build.VERSION.SDK_INT >= 19) {
                if (System.currentTimeMillis() < startTime) alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    startTime,
                    pendingIntent
                )
            } else {
                if (System.currentTimeMillis() < startTime) alarmManager[AlarmManager.RTC_WAKEUP, startTime] =
                    pendingIntent
            }
        } else {
            if (System.currentTimeMillis() < startTime) alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                startTime,
                pendingIntent
            )
        }
    }

    fun setRepeatingAlarm(
        context: Context,
        cls: Class<*>,
        hour: Int,
        min: Int,
        second: Int,
        id: Int,
        interval: Long
    ) {
        // cancel already scheduled reminders
        cancelAlarm(context, cls, id)
        val currentCalendar = Calendar.getInstance()
        val customCalendar = Calendar.getInstance()
        customCalendar.timeInMillis = System.currentTimeMillis()
        customCalendar[Calendar.HOUR_OF_DAY] = hour
        customCalendar[Calendar.MINUTE] = min
        customCalendar[Calendar.SECOND] = second
        if (customCalendar.before(currentCalendar)) customCalendar.add(Calendar.DATE, 1)

        // Enable a receiver
        val receiver = ComponentName(context, cls)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        val intent = Intent(context, cls)
        val pendingIntent = PendingIntent.getBroadcast(context.applicationContext, id, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            ?: return
        val startTime = customCalendar.timeInMillis
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, interval, pendingIntent)
    }

    fun cancelAlarm(context: Context, cls: Class<*>, id: Int) {
        val intent = Intent(context, cls)
        val pendingIntent =
            PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Objects.requireNonNull(am).cancel(pendingIntent)
        pendingIntent.cancel()
    }

    @StyleRes
    fun getGeneralTheme(context: Context): Int {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        return getThemeResFromPrefValue(sharedPref.getString("theme", "switch")!!, context)
    }

    @StyleRes
    private fun getThemeResFromPrefValue(themePrefValue: String, context: Context): Int {
        return when (themePrefValue) {
            "dark" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                R.style.AppTheme_Dark
            }
            "black" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                R.style.AppTheme_Black
            }
            "switch" -> {
                val nightModeFlags =
                    context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                when (nightModeFlags) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        R.style.AppTheme_Dark
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        R.style.AppTheme_Light
                    }
                    else -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        R.style.AppTheme_Light
                    }
                }
            }
            "light" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                R.style.AppTheme_Light
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                R.style.AppTheme_Light
            }
        }
    }

    @StyleRes
    fun getGeneralThemeNoActionBar(context: Context): Int {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        return getThemeResFromPrefValueNoActionBar(
            sharedPref.getString("theme", "switch")!!,
            context
        )
    }

    @StyleRes
    private fun getThemeResFromPrefValueNoActionBar(themePrefValue: String, context: Context): Int {
        return when (themePrefValue) {
            "dark" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                R.style.AppTheme_Dark_NoActionBar
            }
            "black" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                R.style.AppTheme_Black_NoActionBar
            }
            "switch" -> {
                val nightModeFlags =
                    context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                when (nightModeFlags) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        R.style.AppTheme_Dark_NoActionBar
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        R.style.AppTheme_Light_NoActionBar
                    }
                    else -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        R.style.AppTheme_Light_NoActionBar
                    }
                }
            }
            "light" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                R.style.AppTheme_Light_NoActionBar
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                R.style.AppTheme_Light_NoActionBar
            }
        }
    }

    fun isDark(context: Context): Boolean {
        val theme = getGeneralTheme(context)
        return when (theme) {
            R.style.AppTheme_Dark, R.style.AppTheme_Black -> true
            R.style.AppTheme_Light -> false
            else -> false
        }
    }

    fun getTextColorPrimary(context: Context): Int {
        return getThemeColor(android.R.attr.textColorPrimary, context)
    }

    fun getTextColorSecondary(context: Context): Int {
        return getThemeColor(android.R.attr.textColorSecondary, context)
    }

    fun getPrimaryColor(context: Context): Int {
        return getThemeColor(R.attr.colorPrimary, context)
    }

    private fun getThemeColor(themeAttributeId: Int, context: Context): Int {
        return try {
            val outValue = TypedValue()
            val theme = context.theme
            val wasResolved = theme.resolveAttribute(themeAttributeId, outValue, true)
            if (wasResolved) {
                ContextCompat.getColor(context, outValue.resourceId)
            } else {
                // fallback colour handling
                Color.BLACK
            }
        } catch (e: Exception) {
            Color.BLACK
        }
    }

    fun isSevenDays(context: Context?): Boolean {
        return getBooleanSettings(context, SettingsActivity.Companion.KEY_SEVEN_DAYS_SETTING, false)
    }

    fun isWeekStartOnSunday(context: Context?): Boolean {
        return getBooleanSettings(
            context,
            SettingsActivity.Companion.KEY_START_WEEK_ON_SUNDAY,
            false
        )
    }

    fun isSummaryLibrary1(context: Context?): Boolean {
        return getBooleanSettings(context, "summary_lib", !showTimes(context))
    }

    fun setSummaryLibrary(context: Context?, value: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putBoolean("summary_lib", value).commit()
    }

    fun setStartTime(context: Context?, vararg times: Int) {
        if (times.size != 3) {
            return
        }
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPref.edit()
        editor.putInt("start_hour", times[0])
        editor.putInt("start_minute", times[1])
        editor.putInt("start_second", times[2])
        editor.commit()
    }

    fun getStartTime(context: Context?): IntArray {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        return intArrayOf(
            sharedPref.getInt("start_hour", 8),
            sharedPref.getInt("start_minute", 0),
            sharedPref.getInt("start_second", 0)
        )
    }

    fun setPeriodLength(context: Context?, length: Int) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putInt("period_length", length).apply()
    }

    fun getPeriodLength(context: Context?): Int {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("period_length", 60)
    }

    fun hasStartActivityBeenShown(context: Context?): Boolean {
        return getBooleanSettings(context, "start_activity", false)
    }

    fun setStartActivityShown(context: Context?, value: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putBoolean("start_activity", value).commit()
    }

    fun showTimes(context: Context?): Boolean {
        return getBooleanSettings(context, "show_times", false)
    }

    //Even, odd weeks
    fun isTwoWeeksEnabled(context: Context?): Boolean {
        return getBooleanSettings(context, "two_weeks", false)
    }

    fun setTermStart(context: Context?, year: Int, month: Int, day: Int) {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putInt("term_year", year)
        editor.putInt("term_month", month)
        editor.putInt("term_day", day)
        editor.commit()
    }

    fun getTermStart(context: Context?): Calendar {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val calendar = Calendar.getInstance()
        val year = sharedPref.getInt("term_year", -999999999)

        //If start has not been set
        if (year == -999999999) {
            setTermStart(
                context,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            )
            return getTermStart(context)
        }
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = sharedPref.getInt("term_month", 0)
        calendar[Calendar.DAY_OF_MONTH] = sharedPref.getInt("term_day", 0)
        return calendar
    }

    fun isEvenWeek(context: Context?, now: Calendar): Boolean {
        return if (isTwoWeeksEnabled(context)) {
            WeekUtils.isEvenWeek(
                getTermStart(context),
                now,
                isWeekStartOnSunday(context)
            )
        } else true
    }

    fun isIntelligentAutoFill(context: Context?): Boolean {
        return getBooleanSettings(context, "auto_fill", true)
    }

    fun setReminderTime(context: Context?, length: Int) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putInt("reminder_time", length).apply()
    }

    fun getReminderTime(context: Context?): Int {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("reminder_time", 15)
    }

    fun isReminder(context: Context?): Boolean {
        return getBooleanSettings(context, "reminder", false)
    }

    fun isNotificationAtEnd(context: Context?): Boolean {
        return getBooleanSettings(context, "notification_end", true)
    }

    fun isPreselectionList(context: Context?): Boolean {
        return getBooleanSettings(context, "is_preselection", true)
    }

    fun setPreselectionElements(context: Context?, value: Array<String?>) {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val stringSet: Set<String> = HashSet()
        Collections.addAll<String>(stringSet, *value)
        sharedPrefs.edit().putStringSet("preselection_elements", stringSet).apply()
    }

    fun getPreselectionElements(context: Context): Array<String> {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val preselection = sharedPrefs.getStringSet("preselection_elements", null)
        return if (preselection == null) context.resources.getStringArray(R.array.preselected_subjects_values) else preselection.toArray<String>(
            arrayOf<String>()
        )
    }
}