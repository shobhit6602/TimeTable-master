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
import android.app.*
import android.widget.EditText
import com.ulan.timetable.adapters.WeekAdapter
import android.widget.AbsListView.MultiChoiceModeListener
import android.util.SparseBooleanArray
import android.view.MenuInflater
import android.content.SharedPreferences.Editor
import android.os.Build
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback
import com.afollestad.materialdialogs.DialogAction
import android.content.pm.PackageManager
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
import android.widget.TextView
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
import android.graphics.Color
import android.graphics.drawable.Drawable
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
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.app.TaskStackBuilder
import com.ulan.timetable.fragments.TimeSettingsFragment
import java.lang.StringBuilder
import java.util.*

object NotificationUtil {
    private const val NOTIFICATION_SUMMARY_ID = 9090
    private const val NOTIFICATION_NEXT_WEEK_ID = 3030
    private const val CHANNEL_ID = "notification"
    private const val CHANNEL_ID_LOUD = "notificationLoud"
    fun sendNotificationSummary(context: Context, alert: Boolean) {
        ProfileManagement.initProfiles(context)
        for (i in 0 until ProfileManagement.getSize()) {
            sendNotificationSummary(context, alert, i, NOTIFICATION_SUMMARY_ID + i)
        }
    }

    private fun sendNotificationSummary(
        context: Context,
        alert: Boolean,
        profilePosition: Int,
        notificationID: Int
    ) {
        Thread(label@ Runnable {
            val db = DbHelper(context, profilePosition)
            val lessons = getLessons(
                db.getWeek(getCurrentDay(Calendar.getInstance()[Calendar.DAY_OF_WEEK])),
                context
            )
                ?: return@label
            val builder = NotificationCompat.Builder(context, "id")
                .setSmallIcon(R.drawable.ic_assignment_black_24dp)
                .setContentTitle(
                    context.getString(string.notification_summary_title) + if (ProfileManagement.isMoreThanOneProfile()) " (" + ProfileManagement.getProfile(
                        profilePosition
                    ).name + ")" else ""
                )
                .setStyle(NotificationCompat.BigTextStyle().bigText(lessons))
            sendNotification(context, alert, builder, notificationID)
        }).start()
    }

    fun removeNotificationCurrentLesson(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager?.cancel(NOTIFICATION_NEXT_WEEK_ID)
    }

    fun sendNotificationCurrentLesson(context: Context, alert: Boolean) {
        ProfileManagement.initProfiles(context)
        if (!ProfileManagement.isPreferredProfile()) return
        Thread(label@ Runnable {
            val db = DbHelper(context, ProfileManagement.getPreferredProfilePosition())
            var weeks = db.getWeek(getCurrentDay(Calendar.getInstance()[Calendar.DAY_OF_WEEK]))
            val nextWeek = WeekUtils.getNextWeek(weeks!!) ?: return@label
            weeks = ArrayList()
            weeks.add(nextWeek)
            val lesson = StringBuilder()
            if (PreferenceUtil.showTimes(context)) lesson.append(
                context.getString(string.time_from).substring(0, 1).toUpperCase()
            )
                .append(context.getString(string.time_from).substring(1))
                .append(" ")
                .append(nextWeek.fromTime)
                .append(" - ")
                .append(nextWeek.toTime) else {
                val start = WeekUtils.getMatchingScheduleBegin(nextWeek.fromTime, context)
                val end = WeekUtils.getMatchingScheduleEnd(nextWeek.toTime, context)
                if (start == end) {
                    lesson.append(start)
                        .append(". ")
                        .append(context.getString(string.lesson))
                } else {
                    lesson.append(start)
                        .append(".-")
                        .append(end)
                        .append(". ")
                        .append(context.getString(string.lesson))
                }
            }
            if (!nextWeek.room.trim { it <= ' ' }.isEmpty()) {
                lesson.append(" ")
                    .append(context.getString(string.share_msg_in_room))
                    .append(" ")
                    .append(nextWeek.room)
            }
            val name = StringBuilder()
                .append(nextWeek.subject)
            if (!nextWeek.teacher.trim { it <= ' ' }.isEmpty()) {
                name.append(" ")
                    .append(context.getString(string.with_teacher))
                    .append(" ")
                    .append(nextWeek.teacher)
            }
            val style = NotificationCompat.MessagingStyle(Person.Builder().setName("me").build())
            style.conversationTitle = context.getString(string.notification_next_week_title)
            val color = nextWeek.color
            val textColor = ColorPalette.pickTextColorBasedOnBgColorSimple(
                nextWeek.color,
                Color.WHITE,
                Color.BLACK
            )
            var textSize =
                context.resources.getInteger(R.integer.notification_max_text_size) - context.resources.getInteger(
                    R.integer.notification_text_size_factor
                ) * nextWeek.room.length
            if (textSize < context.resources.getInteger(R.integer.notification_min_text_size)) textSize =
                context.resources.getInteger(R.integer.notification_min_text_size)
            val drawable: Drawable = ShapeTextDrawable(
                ShapeForm.ROUND,
                color,
                10f,
                nextWeek.room,
                textColor,
                true,
                Typeface.create("sans-serif-light", Typeface.NORMAL),
                textSize,
                Color.TRANSPARENT,
                0
            )
            val person = Person.Builder().setName(name).setIcon(
                IconCompat.createWithBitmap(
                    drawable.toBitmap(
                        context.resources.getInteger(R.integer.notification_bitmap_size),
                        context.resources.getInteger(R.integer.notification_bitmap_size),
                        null
                    )
                )
            ).build()
            style.addMessage(NotificationCompat.MessagingStyle.Message(lesson, 0, person))
            val builder = NotificationCompat.Builder(context, "id")
                .setStyle(style)
                .setSmallIcon(R.drawable.ic_assignment_next_black_24dp)
                .setColor(color)
            sendNotification(context, alert, builder, NOTIFICATION_NEXT_WEEK_ID)
        }).start()
    }

    private fun sendNotification(
        context: Context,
        alert: Boolean,
        notificationBuilder: NotificationCompat.Builder?,
        id: Int
    ) {
        if (notificationBuilder == null || !PreferenceUtil.isNotification(context) || Build.VERSION.SDK_INT < 21) return
        val `when` = System.currentTimeMillis()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationIntent = Intent(context, MainActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntentWithParentStack(notificationIntent)
        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        if (alert) createNotificationChannelLoud(context) else createNotificationChannel(context)
        val mNotifyBuilder = notificationBuilder
            .setChannelId(if (alert) CHANNEL_ID_LOUD else CHANNEL_ID)
            .setAutoCancel(true)
            .setWhen(`when`)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(if (alert) RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) else null)
            .setOnlyAlertOnce(!alert)
            .setContentIntent(pendingIntent)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O && !alert) {
            mNotifyBuilder.priority = NotificationCompat.PRIORITY_LOW
        }
        if (PreferenceUtil.isAlwaysNotification(context)) {
            //Dismiss button intent
            val buttonIntent = Intent(context, NotificationDismissButtonReceiver::class.java)
            buttonIntent.putExtra(
                NotificationDismissButtonReceiver.Companion.EXTRA_NOTIFICATION_ID,
                id
            )
            val btPendingIntent = PendingIntent.getBroadcast(
                context,
                UUID.randomUUID().hashCode(),
                buttonIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            mNotifyBuilder.setOngoing(true)
            mNotifyBuilder.addAction(
                R.drawable.ic_close_black_24dp,
                context.getString(string.notification_dismiss),
                btPendingIntent
            )
        }
        notificationManager?.notify(id, mNotifyBuilder.build())
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(string.channel),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = context.getString(string.channel_desc)
            channel.enableLights(false)
            channel.setSound(null, null)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = context.getSystemService(
                NotificationManager::class.java
            )
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel)
        }
    }

    private fun createNotificationChannelLoud(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID_LOUD,
                context.getString(string.channel_important),
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = context.getString(string.channel_desc)
            channel.enableLights(true)
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = context.getSystemService(
                NotificationManager::class.java
            )
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel)
        }
    }

    fun getLessons(weeks: ArrayList<Week?>, context: Context): String? {
        val lessons = StringBuilder()
        for (week in weeks) {
            if (week != null) {
                lessons.append(week.subject)
                    .append(" ")
                    .append(context.getString(string.time_from))
                    .append(" ")
                if (PreferenceUtil.showTimes(context)) lessons.append(week.fromTime)
                    .append(" - ")
                    .append(week.toTime) else {
                    val start = WeekUtils.getMatchingScheduleBegin(week.fromTime, context)
                    val end = WeekUtils.getMatchingScheduleEnd(week.toTime, context)
                    if (start == end) {
                        lessons.append(start)
                            .append(". ")
                            .append(context.getString(string.lesson))
                    } else {
                        lessons.append(start)
                            .append(".-")
                            .append(end)
                            .append(". ")
                            .append(context.getString(string.lesson))
                    }
                }
                if (!week.teacher.trim { it <= ' ' }.isEmpty()) {
                    lessons.append(" ")
                        .append(context.getString(string.with_teacher))
                        .append(" ")
                        .append(week.teacher)
                }
                if (!week.room.trim { it <= ' ' }.isEmpty()) {
                    lessons.append(" ")
                        .append(context.getString(string.share_msg_in_room))
                        .append(" ")
                        .append(week.room)
                }
                lessons.append("\n")
            }
        }
        return if (lessons.toString() != "") lessons.toString()
            .substring(0, lessons.toString().length - 1) else null
    }

    fun getCurrentDay(day: Int): String? {
        var currentDay: String? = null
        when (day) {
            1 -> currentDay = "Sunday"
            2 -> currentDay = "Monday"
            3 -> currentDay = "Tuesday"
            4 -> currentDay = "Wednesday"
            5 -> currentDay = "Thursday"
            6 -> currentDay = "Friday"
            7 -> currentDay = "Saturday"
        }
        return currentDay
    }
}