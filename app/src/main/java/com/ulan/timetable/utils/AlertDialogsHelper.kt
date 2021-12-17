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
import com.ulan.timetable.utils.PreferenceUtil
import com.ulan.timetable.utils.WeekUtils
import androidx.appcompat.app.AppCompatActivity
import com.ulan.timetable.R
import android.annotation.SuppressLint
import com.ulan.timetable.adapters.WeekAdapter
import android.widget.AbsListView.MultiChoiceModeListener
import android.util.SparseBooleanArray
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
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
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
import androidx.cardview.widget.CardView
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
import android.widget.RemoteViewsService.RemoteViewsFactory
import com.ulan.timetable.appwidget.DayAppWidgetService.DayAppWidgetRemoteViewsFactory
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.*
import android.graphics.Color
import com.ulan.timetable.appwidget.DayAppWidgetService
import com.ulan.timetable.appwidget.DayAppWidgetProvider
import android.os.Bundle
import android.widget.SeekBar.OnSeekBarChangeListener
import com.ulan.timetable.appwidget.AppWidgetConstants
import com.ulan.timetable.utils.FragmentHelper
import androidx.preference.PreferenceFragmentCompat
import com.ulan.timetable.fragments.SettingsFragment
import com.ulan.timetable.activities.TimeSettingsActivity
import com.afollestad.materialdialogs.MaterialDialog.ListCallbackMultiChoice
import android.text.InputType
import android.text.format.DateFormat
import androidx.preference.SwitchPreferenceCompat
import com.ulan.timetable.receivers.DailyReceiver
import android.view.*
import android.widget.*
import com.google.android.material.navigation.NavigationView
import com.ulan.timetable.utils.ShortcutUtils
import androidx.drawerlayout.widget.DrawerLayout
import android.widget.AdapterView.OnItemSelectedListener
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
import androidx.appcompat.app.AlertDialog
import com.ulan.timetable.activities.NoteInfoActivity
import com.ulan.timetable.fragments.ProfileActivityFragment
import me.yaoandy107.ntut_timetable.CourseTableLayout
import me.yaoandy107.ntut_timetable.model.StudentCourse
import me.yaoandy107.ntut_timetable.model.CourseInfo
import com.ulan.timetable.activities.SummaryActivity.CustomCourseInfo
import com.github.tlaabs.timetableview.Schedule
import com.ulan.timetable.activities.SummaryActivity.CustomSchedule
import com.github.tlaabs.timetableview.TimetableView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.ulan.timetable.fragments.TimeSettingsFragment
import com.ulan.timetable.model.*
import java.lang.Exception
import java.util.*

/**
 * Created by Ulan on 22.10.2018.
 */
object AlertDialogsHelper {
    //TODO: Rewrite Dialogs to and returning a dialog object, without activity
    fun getEditSubjectDialog(
        dbHelper: DbHelper?,
        activity: AppCompatActivity,
        alertLayout: View,
        runOnSafe: Runnable,
        week: Week
    ) {
        val editTextHashs = HashMap<Int, EditText>()
        val subject = alertLayout.findViewById<EditText>(R.id.subject_dialog)
        editTextHashs[string.subject] = subject
        val teacher = alertLayout.findViewById<EditText>(R.id.teacher_dialog)
        //        editTextHashs.put(R.string.teacher, teacher);
        val room = alertLayout.findViewById<EditText>(R.id.room_dialog)
        //        editTextHashs.put(R.string.room, room);
        val from_time = alertLayout.findViewById<TextView>(R.id.from_time)
        val to_time = alertLayout.findViewById<TextView>(R.id.to_time)
        val from_hour = alertLayout.findViewById<TextView>(R.id.from_hour)
        val to_hour = alertLayout.findViewById<TextView>(R.id.to_hour)
        val select_color = alertLayout.findViewById<Button>(R.id.select_color)
        select_color.setTextColor(
            ColorPalette.pickTextColorBasedOnBgColorSimple(
                week.color,
                Color.WHITE,
                Color.BLACK
            )
        )
        subject.setText(week.subject)
        teacher.setText(week.teacher)
        room.setText(week.room)
        from_time.text = WeekUtils.localizeTime(activity, week.fromTime)
        to_time.text = WeekUtils.localizeTime(activity, week.toTime)
        from_hour.text = "" + WeekUtils.getMatchingScheduleBegin(week.fromTime, activity)
        to_hour.text = "" + WeekUtils.getMatchingScheduleEnd(week.toTime, activity)
        select_color.setBackgroundColor(if (week.color != 0) week.color else Color.WHITE)
        from_time.setOnClickListener { v: View? ->
            val mHour = week.fromTime.substring(0, week.fromTime.indexOf(":")).toInt()
            val mMinute = week.fromTime.substring(week.fromTime.indexOf(":") + 1).toInt()
            val timePickerDialog = TimePickerDialog(activity,
                { view: TimePicker?, hourOfDay: Int, minute: Int ->
                    val newTime = String.format(
                        Locale.getDefault(), "%02d:%02d", hourOfDay, minute
                    )
                    from_time.text = WeekUtils.localizeTime(activity, newTime)
                    week.fromTime = newTime
                    from_hour.text = "" + WeekUtils.getMatchingScheduleBegin(newTime, activity)
                    try {
                        val value = WeekUtils.getMatchingScheduleBegin(
                            String.format(
                                Locale.getDefault(),
                                "%02d:%02d",
                                hourOfDay,
                                minute
                            ), activity
                        )
                        if (to_hour.text.toString()
                                .toInt() < value && PreferenceUtil.isIntelligentAutoFill(activity)
                        ) {
                            to_time.text = WeekUtils.localizeTime(
                                activity,
                                WeekUtils.getMatchingTimeEnd(value, activity)
                            )
                            week.toTime = WeekUtils.getMatchingTimeEnd(value, activity)
                            to_hour.text = "" + value
                        }
                    } catch (ignore: Exception) {
                    }
                }, mHour, mMinute, DateFormat.is24HourFormat(activity)
            )
            timePickerDialog.setTitle(string.choose_time)
            timePickerDialog.show()
        }
        to_time.setOnClickListener { v: View? ->
            val mHour = week.toTime.substring(0, week.toTime.indexOf(":")).toInt()
            val mMinute = week.toTime.substring(week.toTime.indexOf(":") + 1).toInt()
            val timePickerDialog = TimePickerDialog(activity,
                { view: TimePicker?, hourOfDay: Int, minute1: Int ->
                    val newTime = String.format(
                        Locale.getDefault(), "%02d:%02d", hourOfDay, minute1
                    )
                    to_time.text = WeekUtils.localizeTime(activity, newTime)
                    week.toTime = newTime
                    to_hour.text = "" + WeekUtils.getMatchingScheduleEnd(newTime, activity)
                    try {
                        val value = WeekUtils.getMatchingScheduleEnd(
                            String.format(
                                Locale.getDefault(),
                                "%02d:%02d",
                                hourOfDay,
                                minute1
                            ), activity
                        )
                        if (from_hour.text.toString()
                                .toInt() > value && PreferenceUtil.isIntelligentAutoFill(activity)
                        ) {
                            from_time.text = WeekUtils.localizeTime(
                                activity,
                                WeekUtils.getMatchingTimeBegin(value, activity)
                            )
                            week.fromTime = WeekUtils.getMatchingTimeBegin(value, activity)
                            from_hour.text = "" + value
                        }
                    } catch (ignore: Exception) {
                    }
                }, mHour, mMinute, DateFormat.is24HourFormat(activity)
            )
            timePickerDialog.setTitle(string.choose_time)
            timePickerDialog.show()
        }
        from_hour.setOnClickListener { v: View? ->
            val numberPicker = NumberPicker(activity)
            numberPicker.maxValue = 15
            numberPicker.minValue = 1
            numberPicker.value = from_hour.text.toString().toInt()
            MaterialDialog.Builder(activity)
                .customView(numberPicker, false)
                .positiveText(string.select)
                .onPositive { vi: MaterialDialog?, w: DialogAction? ->
                    val value = numberPicker.value
                    from_time.text = WeekUtils.localizeTime(
                        activity,
                        WeekUtils.getMatchingTimeBegin(value, activity)
                    )
                    week.fromTime = WeekUtils.getMatchingTimeBegin(value, activity)
                    from_hour.text = "" + value
                    try {
                        if (to_hour.text.toString()
                                .toInt() < value && PreferenceUtil.isIntelligentAutoFill(activity)
                        ) {
                            to_time.text = WeekUtils.localizeTime(
                                activity,
                                WeekUtils.getMatchingTimeEnd(value, activity)
                            )
                            week.toTime = WeekUtils.getMatchingTimeEnd(value, activity)
                            to_hour.text = "" + value
                        }
                    } catch (ignore: Exception) {
                    }
                }
                .show()
        }
        to_hour.setOnClickListener { v: View? ->
            val numberPicker = NumberPicker(activity)
            numberPicker.maxValue = 15
            numberPicker.minValue = 1
            numberPicker.value = to_hour.text.toString().toInt()
            MaterialDialog.Builder(activity)
                .customView(numberPicker, false)
                .positiveText(string.select)
                .onPositive { vi: MaterialDialog?, w: DialogAction? ->
                    val value = numberPicker.value
                    to_time.text = WeekUtils.localizeTime(
                        activity,
                        WeekUtils.getMatchingTimeEnd(value, activity)
                    )
                    week.toTime = WeekUtils.getMatchingTimeEnd(value, activity)
                    to_hour.text = "" + value
                    try {
                        if (from_hour.text.toString()
                                .toInt() > value && PreferenceUtil.isIntelligentAutoFill(activity)
                        ) {
                            from_time.text = WeekUtils.localizeTime(
                                activity,
                                WeekUtils.getMatchingTimeBegin(value, activity)
                            )
                            week.fromTime = WeekUtils.getMatchingTimeBegin(value, activity)
                            from_hour.text = "" + value
                        }
                    } catch (ignore: Exception) {
                    }
                }
                .show()
        }
        select_color.setOnClickListener { v: View? ->
            ColorPickerDialog()
                .withColor((select_color.background as ColorDrawable).color) // the default / initial color
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withTitle(activity.getString(string.choose_color))
                .withTheme(PreferenceUtil.getGeneralTheme(activity))
                .withCornerRadius(16f)
                .withAlphaEnabled(false)
                .withListener { dialog: ColorPickerDialog?, color: Int ->
                    // a color has been picked; use it
                    select_color.setBackgroundColor(color)
                    select_color.setTextColor(
                        ColorPalette.pickTextColorBasedOnBgColorSimple(
                            color,
                            Color.WHITE,
                            Color.BLACK
                        )
                    )
                }
                .clearPickers()
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withPicker(RGBPickerView::class.java)
                .show(activity.supportFragmentManager, "colorPicker")
        }
        subject.setOnEditorActionListener { v: TextView, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                if (event == null || !event.isShiftPressed) {
                    // the user is done typing.
                    //AutoFill other fields
                    for (w in WeekUtils.getAllWeeks(dbHelper!!)) {
                        if (w.subject.equals(v.text.toString(), ignoreCase = true)) {
                            if (teacher.text.toString().trim { it <= ' ' }
                                    .isEmpty()) teacher.setText(w.teacher)
                            if (room.text.toString().trim { it <= ' ' }
                                    .isEmpty()) room.setText(w.room)
                            select_color.setBackgroundColor(w.color)
                            select_color.setTextColor(
                                ColorPalette.pickTextColorBasedOnBgColorSimple(
                                    w.color,
                                    Color.WHITE,
                                    Color.BLACK
                                )
                            )
                        }
                    }
                    return@setOnEditorActionListener true
                }
            }
            false
        }
        subject.onFocusChangeListener = OnFocusChangeListener { v: View, hasFocus: Boolean ->
            if (!hasFocus) {
                for (w in WeekUtils.getAllWeeks(dbHelper!!)) {
                    if (w.subject.equals((v as EditText).text.toString(), ignoreCase = true)) {
                        if (teacher.text.toString().trim { it <= ' ' }
                                .isEmpty()) teacher.setText(w.teacher)
                        if (room.text.toString().trim { it <= ' ' }.isEmpty()) room.setText(w.room)
                        select_color.setBackgroundColor(w.color)
                        select_color.setTextColor(
                            ColorPalette.pickTextColorBasedOnBgColorSimple(
                                w.color,
                                Color.WHITE,
                                Color.BLACK
                            )
                        )
                    }
                }
            }
        }
        val alert = AlertDialog.Builder(activity)
        alert.setTitle(string.edit_subject)
        alert.setCancelable(false)
        val cancel = alertLayout.findViewById<Button>(R.id.cancel)
        val save = alertLayout.findViewById<Button>(R.id.save)
        alert.setView(alertLayout)
        val dialog = alert.create()
        dialog.show()
        cancel.setOnClickListener { v: View? ->
            subject.text.clear()
            teacher.text.clear()
            room.text.clear()
            from_time.setText(string.select_start_time)
            to_time.setText(string.select_end_time)
            from_hour.setText(string.lesson)
            to_hour.setText(string.lesson)
            select_color.setBackgroundColor(Color.WHITE)
            subject.requestFocus()
            from_hour.setText(string.lesson)
            to_hour.setText(string.lesson)
            dialog.dismiss()
        }
        save.setOnClickListener { v: View? ->
            if (TextUtils.isEmpty(subject.text) /*|| TextUtils.isEmpty(teacher.getText()) || TextUtils.isEmpty(room.getText())*/) {
                for ((key, value) in editTextHashs) {
                    if (TextUtils.isEmpty(value.text)) {
                        value.error =
                            activity.resources.getString(key) + " " + activity.resources.getString(
                                string.field_error
                            )
                        value.requestFocus()
                    }
                }
            } else if (!from_time.text.toString().matches(".*\\d+.*") || !to_time.text.toString()
                    .matches(".*\\d+.*")
            ) {
                Snackbar.make(alertLayout, string.time_error, Snackbar.LENGTH_LONG).show()
            } else {
                val buttonColor = select_color.background as ColorDrawable
                week.subject = subject.text.toString()
                week.teacher = teacher.text.toString()
                week.room = room.text.toString()
                week.color = buttonColor.color
                dbHelper!!.updateWeek(week)
                runOnSafe.run()
                databaseChanged(activity)
                dialog.dismiss()
            }
        }
    }

    fun getAddSubjectDialog(
        dbHelper: DbHelper,
        activity: AppCompatActivity,
        alertLayout: View,
        adapter: FragmentsTabAdapter,
        viewPager: ViewPager
    ) {
        val editTextHashs = HashMap<Int, EditText>()
        val subject = alertLayout.findViewById<EditText>(R.id.subject_dialog)
        subject.requestFocus()
        editTextHashs[string.subject] = subject
        val teacher = alertLayout.findViewById<EditText>(R.id.teacher_dialog)
        //        editTextHashs.put(R.string.teacher, teacher);
        val room = alertLayout.findViewById<EditText>(R.id.room_dialog)
        //        editTextHashs.put(R.string.room, room);
        val params = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            0.7f
        )
        val from_time = alertLayout.findViewById<TextView>(R.id.from_time)
        from_time.layoutParams = params
        val to_time = alertLayout.findViewById<TextView>(R.id.to_time)
        to_time.layoutParams = params
        val from_hour = alertLayout.findViewById<TextView>(R.id.from_hour)
        from_hour.layoutParams = params
        val to_hour = alertLayout.findViewById<TextView>(R.id.to_hour)
        to_hour.layoutParams = params
        if (PreferenceUtil.showTimes(activity)) {
            from_time.visibility = View.VISIBLE
            to_time.visibility = View.VISIBLE
            from_hour.visibility = View.GONE
            to_hour.visibility = View.GONE
        } else {
            from_time.visibility = View.GONE
            to_time.visibility = View.GONE
            from_hour.visibility = View.VISIBLE
            to_hour.visibility = View.VISIBLE
        }
        from_hour.setText(string.select_start_time)
        to_hour.setText(string.select_end_time)
        val select_color = alertLayout.findViewById<Button>(R.id.select_color)
        val week = Week()
        from_time.setOnClickListener { v: View? ->
            var mHour: Int
            var mMinute: Int
            try {
                val time = from_time.text.toString()
                mHour = time.substring(0, time.indexOf(":")).toInt()
                mMinute = time.substring(time.indexOf(":") + 1).toInt()
            } catch (ignore: Exception) {
                val c = Calendar.getInstance()
                mHour = c[Calendar.HOUR_OF_DAY]
                mMinute = c[Calendar.MINUTE]
            }
            val timePickerDialog = TimePickerDialog(activity,
                { view: TimePicker?, hourOfDay: Int, minute: Int ->
                    val newTime = String.format(
                        Locale.getDefault(), "%02d:%02d", hourOfDay, minute
                    )
                    from_time.text = WeekUtils.localizeTime(activity, newTime)
                    week.fromTime = newTime
                    from_hour.text = "" + WeekUtils.getMatchingScheduleBegin(newTime, activity)
                    try {
                        val value = WeekUtils.getMatchingScheduleBegin(
                            String.format(
                                Locale.getDefault(),
                                "%02d:%02d",
                                hourOfDay,
                                minute
                            ), activity
                        )
                        if (to_hour.text.toString()
                                .toInt() < value && PreferenceUtil.isIntelligentAutoFill(activity)
                        ) {
                            to_time.text = WeekUtils.localizeTime(
                                activity,
                                WeekUtils.getMatchingTimeEnd(value, activity)
                            )
                            week.toTime = WeekUtils.getMatchingTimeEnd(value, activity)
                            to_hour.text = "" + value
                        }
                    } catch (ignore: Exception) {
                    }
                }, mHour, mMinute, DateFormat.is24HourFormat(activity)
            )
            timePickerDialog.setTitle(string.choose_time)
            timePickerDialog.show()
        }
        to_time.setOnClickListener { v: View? ->
            var hour: Int
            var minute: Int
            try {
                val time = WeekUtils.getMatchingTimeEnd(
                    WeekUtils.getMatchingScheduleBegin(
                        from_time.text.toString(),
                        activity
                    ), activity
                )
                hour = time.substring(0, time.indexOf(":")).toInt()
                minute = time.substring(time.indexOf(":") + 1).toInt()
            } catch (ignore: Exception) {
                val c = Calendar.getInstance()
                hour = c[Calendar.HOUR_OF_DAY]
                minute = c[Calendar.MINUTE]
            }
            val timePickerDialog = TimePickerDialog(activity,
                { view: TimePicker?, hourOfDay: Int, minute1: Int ->
                    val newTime = String.format(
                        Locale.getDefault(), "%02d:%02d", hourOfDay, minute1
                    )
                    to_time.text = WeekUtils.localizeTime(activity, newTime)
                    week.toTime = newTime
                    to_hour.text = "" + WeekUtils.getMatchingScheduleEnd(newTime, activity)
                    try {
                        val value = WeekUtils.getMatchingScheduleEnd(
                            String.format(
                                Locale.getDefault(),
                                "%02d:%02d",
                                hourOfDay,
                                minute1
                            ), activity
                        )
                        if (from_hour.text.toString()
                                .toInt() > value && PreferenceUtil.isIntelligentAutoFill(activity)
                        ) {
                            from_time.text = WeekUtils.localizeTime(
                                activity,
                                WeekUtils.getMatchingTimeBegin(value, activity)
                            )
                            week.fromTime = WeekUtils.getMatchingTimeBegin(value, activity)
                            from_hour.text = "" + value
                        }
                    } catch (ignore: Exception) {
                    }
                }, hour, minute, DateFormat.is24HourFormat(activity)
            )
            timePickerDialog.setTitle(string.choose_time)
            timePickerDialog.show()
        }
        from_hour.setOnClickListener { v: View? ->
            val numberPicker = NumberPicker(activity)
            numberPicker.maxValue = 15
            numberPicker.minValue = 1
            try {
                numberPicker.value = from_hour.text.toString().toInt()
            } catch (ignore: Exception) {
            }
            MaterialDialog.Builder(activity)
                .customView(numberPicker, false)
                .positiveText(string.select)
                .onPositive { vi: MaterialDialog?, w: DialogAction? ->
                    val value = numberPicker.value
                    from_time.text = WeekUtils.localizeTime(
                        activity,
                        WeekUtils.getMatchingTimeBegin(value, activity)
                    )
                    week.fromTime = WeekUtils.getMatchingTimeBegin(value, activity)
                    from_hour.text = "" + value
                    try {
                        if (to_hour.text.toString()
                                .toInt() < value && PreferenceUtil.isIntelligentAutoFill(activity)
                        ) {
                            to_time.text = WeekUtils.localizeTime(
                                activity,
                                WeekUtils.getMatchingTimeEnd(value, activity)
                            )
                            week.toTime = WeekUtils.getMatchingTimeEnd(value, activity)
                            to_hour.text = "" + value
                        }
                    } catch (ignore: Exception) {
                    }
                }
                .show()
        }
        to_hour.setOnClickListener { v: View? ->
            val numberPicker = NumberPicker(activity)
            numberPicker.maxValue = 15
            numberPicker.minValue = 1
            try {
                numberPicker.value = from_hour.text.toString().toInt() + 1
            } catch (ignore: Exception) {
            }
            MaterialDialog.Builder(activity)
                .customView(numberPicker, false)
                .positiveText(string.select)
                .onPositive { vi: MaterialDialog?, w: DialogAction? ->
                    val value = numberPicker.value
                    to_time.text = WeekUtils.localizeTime(
                        activity,
                        WeekUtils.getMatchingTimeEnd(value, activity)
                    )
                    week.toTime = WeekUtils.getMatchingTimeEnd(value, activity)
                    to_hour.text = "" + value
                    try {
                        if (from_hour.text.toString()
                                .toInt() > value && PreferenceUtil.isIntelligentAutoFill(activity)
                        ) {
                            from_time.text = WeekUtils.localizeTime(
                                activity,
                                WeekUtils.getMatchingTimeBegin(value, activity)
                            )
                            week.fromTime = WeekUtils.getMatchingTimeBegin(value, activity)
                            from_hour.text = "" + value
                        }
                    } catch (ignore: Exception) {
                    }
                }
                .show()
        }
        select_color.setOnClickListener { v: View? ->
            ColorPickerDialog()
                .withColor((select_color.background as ColorDrawable).color) // the default / initial color
                .withTitle(activity.getString(string.choose_color))
                .withTheme(PreferenceUtil.getGeneralTheme(activity))
                .withCornerRadius(16f)
                .withAlphaEnabled(false)
                .withListener { dialog: ColorPickerDialog?, color: Int ->
                    // a color has been picked; use it
                    select_color.setBackgroundColor(color)
                    select_color.setTextColor(
                        ColorPalette.pickTextColorBasedOnBgColorSimple(
                            color,
                            Color.WHITE,
                            Color.BLACK
                        )
                    )
                }
                .clearPickers()
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withPicker(RGBPickerView::class.java)
                .show(activity.supportFragmentManager, "colorPicker")
        }
        subject.setOnEditorActionListener { v: TextView, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                if (event == null || !event.isShiftPressed) {
                    // the user is done typing.
                    //AutoFill other fields
                    for (w in WeekUtils.getAllWeeks(dbHelper)) {
                        if (w.subject.equals(v.text.toString(), ignoreCase = true)) {
                            if (teacher.text.toString().trim { it <= ' ' }
                                    .isEmpty()) teacher.setText(w.teacher)
                            if (room.text.toString().trim { it <= ' ' }
                                    .isEmpty()) room.setText(w.room)
                            select_color.setBackgroundColor(w.color)
                            select_color.setTextColor(
                                ColorPalette.pickTextColorBasedOnBgColorSimple(
                                    w.color,
                                    Color.WHITE,
                                    Color.BLACK
                                )
                            )
                        }
                    }
                    return@setOnEditorActionListener true
                }
            }
            false
        }
        subject.onFocusChangeListener = OnFocusChangeListener { v: View, hasFocus: Boolean ->
            if (!hasFocus) {
                for (w in WeekUtils.getAllWeeks(dbHelper)) {
                    if (w.subject.equals((v as EditText).text.toString(), ignoreCase = true)) {
                        if (teacher.text.toString().trim { it <= ' ' }
                                .isEmpty()) teacher.setText(w.teacher)
                        if (room.text.toString().trim { it <= ' ' }.isEmpty()) room.setText(w.room)
                        select_color.setBackgroundColor(w.color)
                        select_color.setTextColor(
                            ColorPalette.pickTextColorBasedOnBgColorSimple(
                                w.color,
                                Color.WHITE,
                                Color.BLACK
                            )
                        )
                    }
                }
            }
        }
        val alert = AlertDialog.Builder(activity)
        alert.setTitle(string.add_subject)
        alert.setCancelable(false)
        val cancel = alertLayout.findViewById<Button>(R.id.cancel)
        val submit = alertLayout.findViewById<Button>(R.id.save)
        alert.setView(alertLayout)
        val dialog = alert.create()

        //Preselection
        val fab = activity.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view: View? ->
            if (PreferenceUtil.isPreselectionList(activity)) {
                val customWeeks = WeekUtils.getPreselection(activity)
                val subjects = ArrayList<String?>()
                for (w in customWeeks) {
                    subjects.add(w.subject)
                }
                MaterialDialog.Builder(activity)
                    .title(string.pick_a_subject)
                    .items(subjects)
                    .itemsCallback { dialog1: MaterialDialog?, view1: View?, which: Int, text: CharSequence? ->
                        val w = customWeeks[which]
                        subject.setText(w.subject)
                        teacher.setText(w.teacher)
                        room.setText(w.room)
                        select_color.setBackgroundColor(w.color)
                        select_color.setTextColor(
                            ColorPalette.pickTextColorBasedOnBgColorSimple(
                                w.color,
                                Color.WHITE,
                                Color.BLACK
                            )
                        )
                        val key = (adapter.getItem(viewPager.currentItem) as WeekdayFragment).key
                        val weeks = dbHelper.getWeek(key)
                        var valueNew = 1
                        if (weeks!!.size > 0) {
                            valueNew = WeekUtils.getMatchingScheduleEnd(
                                weeks[weeks.size - 1].toTime,
                                activity
                            ) + 1
                        }
                        from_time.text = WeekUtils.localizeTime(
                            activity,
                            WeekUtils.getMatchingTimeBegin(valueNew, activity)
                        )
                        week.fromTime = WeekUtils.getMatchingTimeBegin(valueNew, activity)
                        from_hour.text = "" + valueNew
                        to_time.text = WeekUtils.localizeTime(
                            activity,
                            WeekUtils.getMatchingTimeEnd(valueNew, activity)
                        )
                        week.toTime = WeekUtils.getMatchingTimeEnd(valueNew, activity)
                        to_hour.text = "" + valueNew
                        dialog.show()
                    }
                    .positiveText(string.new_subject)
                    .onPositive { dialog1: MaterialDialog?, which: DialogAction? ->
                        val key = (adapter.getItem(viewPager.currentItem) as WeekdayFragment).key
                        val weeks = dbHelper.getWeek(key)
                        var valueNew = 1
                        if (weeks!!.size > 0) {
                            valueNew = WeekUtils.getMatchingScheduleEnd(
                                weeks[weeks.size - 1].toTime,
                                activity
                            ) + 1
                        }
                        from_time.text = WeekUtils.localizeTime(
                            activity,
                            WeekUtils.getMatchingTimeBegin(valueNew, activity)
                        )
                        week.fromTime = WeekUtils.getMatchingTimeBegin(valueNew, activity)
                        from_hour.text = "" + valueNew
                        to_time.text = WeekUtils.localizeTime(
                            activity,
                            WeekUtils.getMatchingTimeEnd(valueNew, activity)
                        )
                        week.toTime = WeekUtils.getMatchingTimeEnd(valueNew, activity)
                        to_hour.text = "" + valueNew
                        select_color.setBackgroundColor(Color.WHITE)
                        select_color.setTextColor(Color.BLACK)
                        dialog.show()
                    }
                    .show()
            } else {
                val key = (adapter.getItem(viewPager.currentItem) as WeekdayFragment).key
                val weeks = dbHelper.getWeek(key)
                var valueNew = 1
                if (weeks!!.size > 0) {
                    valueNew =
                        WeekUtils.getMatchingScheduleEnd(weeks[weeks.size - 1].toTime, activity) + 1
                }
                from_time.text = WeekUtils.localizeTime(
                    activity,
                    WeekUtils.getMatchingTimeBegin(valueNew, activity)
                )
                week.fromTime = WeekUtils.getMatchingTimeBegin(valueNew, activity)
                from_hour.text = "" + valueNew
                to_time.text = WeekUtils.localizeTime(
                    activity,
                    WeekUtils.getMatchingTimeEnd(valueNew, activity)
                )
                week.toTime = WeekUtils.getMatchingTimeEnd(valueNew, activity)
                to_hour.text = "" + valueNew
                dialog.show()
            }
        }
        cancel.setOnClickListener { v: View? ->
            subject.text.clear()
            teacher.text.clear()
            room.text.clear()
            from_time.setText(string.select_start_time)
            to_time.setText(string.select_end_time)
            from_hour.setText(string.select_start_time)
            to_hour.setText(string.select_end_time)
            select_color.setBackgroundColor(Color.WHITE)
            subject.requestFocus()
            dialog.dismiss()
        }
        submit.setOnClickListener { v: View? ->
            if (TextUtils.isEmpty(subject.text) /*|| TextUtils.isEmpty(teacher.getText()) || TextUtils.isEmpty(room.getText())*/) {
                for ((key, value) in editTextHashs) {
                    if (TextUtils.isEmpty(value.text)) {
                        value.error =
                            activity.resources.getString(key) + " " + activity.resources.getString(
                                string.field_error
                            )
                        value.requestFocus()
                    }
                }
            } else if (!from_time.text.toString().matches(".*\\d+.*") || !to_time.text.toString()
                    .matches(".*\\d+.*")
            ) {
                Snackbar.make(alertLayout, string.time_error, Snackbar.LENGTH_LONG).show()
            } else {
                val buttonColor = select_color.background as ColorDrawable
                week.subject = subject.text.toString()
                week.fragment = (adapter.getItem(viewPager.currentItem) as WeekdayFragment).key
                week.teacher = teacher.text.toString()
                week.room = room.text.toString()
                week.color = buttonColor.color
                dbHelper.insertWeek(week)
                adapter.notifyDataSetChanged()
                databaseChanged(activity)
                cancel.performClick()
            }
        }
    }

    fun getEditHomeworkDialog(
        dbHelper: DbHelper,
        activity: AppCompatActivity,
        alertLayout: View,
        adapter: ArrayList<Homework>,
        listView: ListView,
        listposition: Int
    ) {
        val editTextHashs = HashMap<Int, EditText>()
        val subject = alertLayout.findViewById<EditText>(R.id.subjecthomework)
        editTextHashs[string.subject] = subject
        val description = alertLayout.findViewById<EditText>(R.id.descriptionhomework)
        editTextHashs[string.description] = description
        val date = alertLayout.findViewById<TextView>(R.id.datehomework)
        val select_color = alertLayout.findViewById<Button>(R.id.select_color)
        val homework = adapter[listposition]
        subject.setText(homework.subject)
        description.setText(homework.description)
        date.text = WeekUtils.localizeDate(activity, homework.date)
        select_color.setBackgroundColor(if (homework.color != 0) homework.color else Color.WHITE)
        select_color.setTextColor(
            ColorPalette.pickTextColorBasedOnBgColorSimple(
                homework.color,
                Color.WHITE,
                Color.BLACK
            )
        )
        date.setOnClickListener { v: View? ->
            val calendar = Calendar.getInstance()
            val mYear = calendar[Calendar.YEAR]
            val mMonth = calendar[Calendar.MONTH]
            val mdayofMonth = calendar[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(
                activity,
                { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    val cal = Calendar.getInstance()
                    cal[Calendar.YEAR] = year
                    cal[Calendar.MONTH] = month
                    cal[Calendar.DAY_OF_MONTH] = dayOfMonth
                    date.text = WeekUtils.localizeDate(activity, Date(cal.timeInMillis))
                    homework.date = String.format(
                        Locale.getDefault(),
                        "%02d-%02d-%02d",
                        year,
                        month + 1,
                        dayOfMonth
                    )
                },
                mYear,
                mMonth,
                mdayofMonth
            )
            datePickerDialog.setTitle(string.choose_date)
            datePickerDialog.show()
        }
        select_color.setOnClickListener { v: View? ->
            ColorPickerDialog()
                .withColor((select_color.background as ColorDrawable).color) // the default / initial color
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withTitle(activity.getString(string.choose_color))
                .withTheme(PreferenceUtil.getGeneralTheme(activity))
                .withCornerRadius(16f)
                .withAlphaEnabled(false)
                .withListener { dialog: ColorPickerDialog?, color: Int ->
                    // a color has been picked; use it
                    select_color.setBackgroundColor(color)
                    select_color.setTextColor(
                        ColorPalette.pickTextColorBasedOnBgColorSimple(
                            color,
                            Color.WHITE,
                            Color.BLACK
                        )
                    )
                }
                .clearPickers()
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withPicker(RGBPickerView::class.java)
                .show(activity.supportFragmentManager, "colorPicker")
        }
        subject.setOnEditorActionListener { v: TextView, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                if (event == null || !event.isShiftPressed) {
                    // the user is done typing.
                    //AutoFill other fields
                    for (w in WeekUtils.getAllWeeks(dbHelper)) {
                        if (w.subject.equals(v.text.toString(), ignoreCase = true)) {
                            select_color.setBackgroundColor(w.color)
                            select_color.setTextColor(
                                ColorPalette.pickTextColorBasedOnBgColorSimple(
                                    w.color,
                                    Color.WHITE,
                                    Color.BLACK
                                )
                            )
                            //                                    date.setText(DBUtil.getNextOccurenceOfSubject(dbHelper, w.getSubject()));
//                                    homework.setDate(DBUtil.getNextOccurenceOfSubject(dbHelper, w.getSubject()));
                        }
                    }
                    return@setOnEditorActionListener true
                }
            }
            false
        }
        subject.onFocusChangeListener = OnFocusChangeListener { v: View, hasFocus: Boolean ->
            if (!hasFocus) {
                for (w in WeekUtils.getAllWeeks(dbHelper)) {
                    if (w.subject.equals((v as EditText).text.toString(), ignoreCase = true)) {
                        select_color.setBackgroundColor(w.color)
                        select_color.setTextColor(
                            ColorPalette.pickTextColorBasedOnBgColorSimple(
                                w.color,
                                Color.WHITE,
                                Color.BLACK
                            )
                        )
                        //                                    date.setText(DBUtil.getNextOccurenceOfSubject(dbHelper, w.getSubject()));
//                                    homework.setDate(DBUtil.getNextOccurenceOfSubject(dbHelper, w.getSubject()));
                    }
                }
            }
        }
        val alert = AlertDialog.Builder(activity)
        alert.setTitle(string.edit_homework)
        alert.setCancelable(false)
        val cancel = alertLayout.findViewById<Button>(R.id.cancel)
        val save = alertLayout.findViewById<Button>(R.id.save)
        alert.setView(alertLayout)
        val dialog = alert.create()
        dialog.show()
        cancel.setOnClickListener { v: View? ->
            subject.text.clear()
            description.text.clear()
            select_color.setBackgroundColor(Color.WHITE)
            subject.requestFocus()
            dialog.dismiss()
        }
        save.setOnClickListener { v: View? ->
            if (TextUtils.isEmpty(subject.text) || TextUtils.isEmpty(description.text)) {
                for ((key, value) in editTextHashs) {
                    if (TextUtils.isEmpty(value.text)) {
                        value.error =
                            activity.resources.getString(key) + " " + activity.resources.getString(
                                string.field_error
                            )
                        value.requestFocus()
                    }
                }
            } /*else if (!date.getText().toString().matches(".*\\d+.*")) {
                Snackbar.make(alertLayout, R.string.deadline_snackbar, Snackbar.LENGTH_LONG).show();
            }*/ else {
                val homeworkAdapter = listView.adapter as HomeworkAdapter
                val buttonColor = select_color.background as ColorDrawable
                homework.subject = subject.text.toString()
                homework.description = description.text.toString()
                homework.color = buttonColor.color
                dbHelper.updateHomework(homework)
                homeworkAdapter.notifyDataSetChanged()
                dialog.dismiss()
                MaterialDialog.Builder(activity)
                    .content(string.add_to_calendar)
                    .positiveText(string.yes)
                    .onPositive { s: MaterialDialog?, w: DialogAction? ->
                        val year = homework.date.substring(0, homework.date.indexOf("-"))
                        val month = homework.date.substring(
                            year.length + 1,
                            homework.date.indexOf("-") + year.length - 1
                        )
                        val day = homework.date.substring(year.length + month.length + 2)
                        val timeCalendar = Calendar.getInstance()
                        timeCalendar[year.toInt(), month.toInt(), day.toInt(), 0] = 0
                        val intent = Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(
                                CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                timeCalendar.timeInMillis
                            )
                            .putExtra(
                                CalendarContract.EXTRA_EVENT_END_TIME,
                                timeCalendar.timeInMillis
                            )
                            .putExtra(CalendarContract.Events.TITLE, homework.subject)
                            .putExtra(CalendarContract.Events.DESCRIPTION, homework.description)
                        try {
                            activity.startActivity(intent)
                        } catch (e2: ActivityNotFoundException) {
                            ChocoBar.builder().setActivity(activity)
                                .setText(activity.getString(string.no_calendar_app))
                                .setDuration(ChocoBar.LENGTH_LONG).red().show()
                        }
                    }
                    .negativeText(string.no)
                    .show()
            }
        }
    }

    fun getAddHomeworkDialog(
        dbHelper: DbHelper?,
        activity: AppCompatActivity,
        alertLayout: View,
        adapter: HomeworkAdapter
    ) {
        val editTextHashs = HashMap<Int, EditText>()
        val subject = alertLayout.findViewById<EditText>(R.id.subjecthomework)
        editTextHashs[string.subject] = subject
        subject.requestFocus()
        val description = alertLayout.findViewById<EditText>(R.id.descriptionhomework)
        editTextHashs[string.description] = description
        val date = alertLayout.findViewById<TextView>(R.id.datehomework)
        val select_color = alertLayout.findViewById<Button>(R.id.select_color)
        select_color.setTextColor(
            ColorPalette.pickTextColorBasedOnBgColorSimple(
                (select_color.background as ColorDrawable).color,
                Color.WHITE,
                Color.BLACK
            )
        )
        val homework = Homework()
        date.setOnClickListener { v: View? ->
            val calendar = Calendar.getInstance()
            val mYear = calendar[Calendar.YEAR]
            val mMonth = calendar[Calendar.MONTH]
            val mdayofMonth = calendar[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(
                activity,
                { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    val cal = Calendar.getInstance()
                    cal[Calendar.YEAR] = year
                    cal[Calendar.MONTH] = month
                    cal[Calendar.DAY_OF_MONTH] = dayOfMonth
                    date.text = WeekUtils.localizeDate(activity, Date(cal.timeInMillis))
                    homework.date = String.format(
                        Locale.getDefault(),
                        "%02d-%02d-%02d",
                        year,
                        month + 1,
                        dayOfMonth
                    )
                },
                mYear,
                mMonth,
                mdayofMonth
            )
            datePickerDialog.setTitle(string.choose_date)
            datePickerDialog.show()
        }
        select_color.setOnClickListener { v: View? ->
            ColorPickerDialog()
                .withColor((select_color.background as ColorDrawable).color) // the default / initial color
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withTitle(activity.getString(string.choose_color))
                .withTheme(PreferenceUtil.getGeneralTheme(activity))
                .withCornerRadius(16f)
                .withAlphaEnabled(false)
                .withListener { dialog: ColorPickerDialog?, color: Int ->
                    // a color has been picked; use it
                    select_color.setBackgroundColor(color)
                    select_color.setTextColor(
                        ColorPalette.pickTextColorBasedOnBgColorSimple(
                            color,
                            Color.WHITE,
                            Color.BLACK
                        )
                    )
                }
                .clearPickers()
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withPicker(RGBPickerView::class.java)
                .show(activity.supportFragmentManager, "colorPicker")
        }
        subject.setOnEditorActionListener { v: TextView, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                if (event == null || !event.isShiftPressed) {
                    // the user is done typing.
                    //AutoFill other fields
                    for (w in WeekUtils.getAllWeeks(dbHelper!!)) {
                        if (w.subject.equals(v.text.toString(), ignoreCase = true)) {
                            select_color.setBackgroundColor(w.color)
                            select_color.setTextColor(
                                ColorPalette.pickTextColorBasedOnBgColorSimple(
                                    w.color,
                                    Color.WHITE,
                                    Color.BLACK
                                )
                            )
                            //                                    date.setText(DBUtil.getNextOccurenceOfSubject(dbHelper, w.getSubject()));
//                                    homework.setDate(DBUtil.getNextOccurenceOfSubject(dbHelper, w.getSubject()));
                        }
                    }
                    return@setOnEditorActionListener true
                }
            }
            false
        }
        subject.onFocusChangeListener = OnFocusChangeListener { v: View, hasFocus: Boolean ->
            if (!hasFocus) {
                for (w in WeekUtils.getAllWeeks(dbHelper!!)) {
                    if (w.subject.equals((v as EditText).text.toString(), ignoreCase = true)) {
                        select_color.setBackgroundColor(w.color)
                        select_color.setTextColor(
                            ColorPalette.pickTextColorBasedOnBgColorSimple(
                                w.color,
                                Color.WHITE,
                                Color.BLACK
                            )
                        )
                        //                                    date.setText(DBUtil.getNextOccurenceOfSubject(dbHelper, w.getSubject()));
//                                    homework.setDate(DBUtil.getNextOccurenceOfSubject(dbHelper, w.getSubject()));
                    }
                }
            }
        }
        val alert = AlertDialog.Builder(activity)
        alert.setTitle(string.add_homework)
        val cancel = alertLayout.findViewById<Button>(R.id.cancel)
        val save = alertLayout.findViewById<Button>(R.id.save)
        alert.setView(alertLayout)
        alert.setCancelable(false)
        val dialog = alert.create()
        val fab = activity.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view: View? -> dialog.show() }
        cancel.setOnClickListener { v: View? ->
            subject.text.clear()
            description.text.clear()
            select_color.setBackgroundColor(Color.WHITE)
            subject.requestFocus()
            dialog.dismiss()
        }
        save.setOnClickListener { v: View? ->
            if (TextUtils.isEmpty(subject.text) || TextUtils.isEmpty(description.text)) {
                for ((key, value) in editTextHashs) {
                    if (TextUtils.isEmpty(value.text)) {
                        value.error =
                            activity.resources.getString(key) + " " + activity.resources.getString(
                                string.field_error
                            )
                        value.requestFocus()
                    }
                }
            } /* else if (!date.getText().toString().matches(".*\\d+.*")) {
                Snackbar.make(alertLayout, R.string.deadline_snackbar, Snackbar.LENGTH_LONG).show();
            }*/ else {
                val buttonColor = select_color.background as ColorDrawable
                homework.subject = subject.text.toString()
                homework.description = description.text.toString()
                homework.color = buttonColor.color
                dbHelper!!.insertHomework(homework)
                adapter.clear()
                adapter.addAll(dbHelper.homework)
                adapter.notifyDataSetChanged()
                subject.text.clear()
                description.text.clear()
                date.setText(string.choose_date)
                select_color.setBackgroundColor(Color.WHITE)
                subject.requestFocus()
                dialog.dismiss()
                if (homework.date != null && !homework.date.trim { it <= ' ' }.isEmpty()) {
                    MaterialDialog.Builder(activity)
                        .content(string.add_to_calendar)
                        .positiveText(string.yes)
                        .onPositive { s: MaterialDialog?, w: DialogAction? ->
                            val year = homework.date.substring(0, homework.date.indexOf("-"))
                            val month = homework.date.substring(
                                year.length + 1,
                                homework.date.indexOf("-") + year.length - 1
                            )
                            val day = homework.date.substring(year.length + month.length + 2)
                            val timeCalendar = Calendar.getInstance()
                            timeCalendar[year.toInt(), month.toInt(), day.toInt(), 0] = 0
                            val intent = Intent(Intent.ACTION_INSERT)
                                .setData(CalendarContract.Events.CONTENT_URI)
                                .putExtra(
                                    CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                    timeCalendar.timeInMillis
                                )
                                .putExtra(
                                    CalendarContract.EXTRA_EVENT_END_TIME,
                                    timeCalendar.timeInMillis
                                )
                                .putExtra(CalendarContract.Events.TITLE, homework.subject)
                                .putExtra(CalendarContract.Events.DESCRIPTION, homework.description)
                            try {
                                activity.startActivity(intent)
                            } catch (e2: ActivityNotFoundException) {
                                ChocoBar.builder().setActivity(activity)
                                    .setText(activity.getString(string.no_calendar_app))
                                    .setDuration(ChocoBar.LENGTH_LONG).red().show()
                            }
                        }
                        .negativeText(string.no)
                        .show()
                }
            }
        }
    }

    fun getEditTeacherDialog(
        dbHelper: DbHelper,
        activity: AppCompatActivity,
        alertLayout: View,
        adapter: ArrayList<Teacher>,
        listView: ListView,
        listposition: Int
    ) {
        val editTextHashs = HashMap<Int, EditText>()
        val name = alertLayout.findViewById<EditText>(R.id.name_dialog)
        editTextHashs[string.name] = name
        val post = alertLayout.findViewById<EditText>(R.id.post_dialog)
        //        editTextHashs.put(R.string.post, post);
        val phone_number = alertLayout.findViewById<EditText>(R.id.phonenumber_dialog)
        //        editTextHashs.put(R.string.phone_number, phone_number);
        val email = alertLayout.findViewById<EditText>(R.id.email_dialog)
        editTextHashs[string.email] = email
        val select_color = alertLayout.findViewById<Button>(R.id.select_color)
        val teacher = adapter[listposition]
        name.setText(teacher.name)
        post.setText(teacher.post)
        phone_number.setText(teacher.phonenumber)
        email.setText(teacher.email)
        select_color.setBackgroundColor(if (teacher.color != 0) teacher.color else Color.WHITE)
        select_color.setOnClickListener { v: View? ->
            ColorPickerDialog()
                .withColor((select_color.background as ColorDrawable).color) // the default / initial color
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withTitle(activity.getString(string.choose_color))
                .withTheme(PreferenceUtil.getGeneralTheme(activity))
                .withCornerRadius(16f)
                .withAlphaEnabled(false)
                .withListener { dialog: ColorPickerDialog?, color: Int ->
                    // a color has been picked; use it
                    select_color.setBackgroundColor(color)
                    select_color.setTextColor(
                        ColorPalette.pickTextColorBasedOnBgColorSimple(
                            color,
                            Color.WHITE,
                            Color.BLACK
                        )
                    )
                }
                .clearPickers()
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withPicker(RGBPickerView::class.java)
                .show(activity.supportFragmentManager, "colorPicker")
        }
        val alert = AlertDialog.Builder(activity)
        alert.setTitle(string.edit_teacher)
        alert.setCancelable(false)
        val cancel = alertLayout.findViewById<Button>(R.id.cancel)
        val save = alertLayout.findViewById<Button>(R.id.save)
        alert.setView(alertLayout)
        val dialog = alert.create()
        dialog.show()
        cancel.setOnClickListener { v: View? -> dialog.dismiss() }
        save.setOnClickListener { v: View? ->
            if (TextUtils.isEmpty(name.text) /*|| TextUtils.isEmpty(post.getText()) || TextUtils.isEmpty(phone_number.getText())*/ || TextUtils.isEmpty(
                    email.text
                )
            ) {
                for ((key, value) in editTextHashs) {
                    if (TextUtils.isEmpty(value.text)) {
                        value.error =
                            activity.resources.getString(key) + " " + activity.resources.getString(
                                string.field_error
                            )
                        value.requestFocus()
                    }
                }
            } else {
                val teachersAdapter = listView.adapter as TeachersAdapter
                val buttonColor = select_color.background as ColorDrawable
                teacher.name = name.text.toString()
                teacher.post = post.text.toString()
                teacher.phonenumber = phone_number.text.toString()
                teacher.email = email.text.toString()
                teacher.color = buttonColor.color
                dbHelper.updateTeacher(teacher)
                teachersAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }
    }

    fun getAddTeacherDialog(
        dbHelper: DbHelper?,
        activity: AppCompatActivity,
        alertLayout: View,
        adapter: TeachersAdapter
    ) {
        val editTextHashs = HashMap<Int, EditText>()
        val name = alertLayout.findViewById<EditText>(R.id.name_dialog)
        editTextHashs[string.name] = name
        val post = alertLayout.findViewById<EditText>(R.id.post_dialog)
        //        editTextHashs.put(R.string.post, post);
        val phone_number = alertLayout.findViewById<EditText>(R.id.phonenumber_dialog)
        //        editTextHashs.put(R.string.phone_number, phone_number);
        val email = alertLayout.findViewById<EditText>(R.id.email_dialog)
        editTextHashs[string.email] = email
        val select_color = alertLayout.findViewById<Button>(R.id.select_color)
        select_color.setTextColor(
            ColorPalette.pickTextColorBasedOnBgColorSimple(
                (select_color.background as ColorDrawable).color,
                Color.WHITE,
                Color.BLACK
            )
        )
        val teacher = Teacher()
        select_color.setOnClickListener { v: View? ->
            ColorPickerDialog()
                .withColor((select_color.background as ColorDrawable).color) // the default / initial color
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withTitle(activity.getString(string.choose_color))
                .withTheme(PreferenceUtil.getGeneralTheme(activity))
                .withCornerRadius(16f)
                .withAlphaEnabled(false)
                .withListener { dialog: ColorPickerDialog?, color: Int ->
                    // a color has been picked; use it
                    select_color.setBackgroundColor(color)
                    select_color.setTextColor(
                        ColorPalette.pickTextColorBasedOnBgColorSimple(
                            color,
                            Color.WHITE,
                            Color.BLACK
                        )
                    )
                }
                .clearPickers()
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withPicker(RGBPickerView::class.java)
                .show(activity.supportFragmentManager, "colorPicker")
        }
        val alert = AlertDialog.Builder(activity)
        alert.setTitle(activity.resources.getString(string.add_teacher))
        alert.setCancelable(false)
        val cancel = alertLayout.findViewById<Button>(R.id.cancel)
        val save = alertLayout.findViewById<Button>(R.id.save)
        alert.setView(alertLayout)
        val dialog = alert.create()
        val fab = activity.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view: View? -> dialog.show() }
        cancel.setOnClickListener { v: View? -> dialog.dismiss() }
        save.setOnClickListener { v: View? ->
            if (TextUtils.isEmpty(name.text) /*|| TextUtils.isEmpty(post.getText()) || TextUtils.isEmpty(phone_number.getText())*/ || TextUtils.isEmpty(
                    email.text
                )
            ) {
                for ((key, value) in editTextHashs) {
                    if (TextUtils.isEmpty(value.text)) {
                        value.error =
                            activity.resources.getString(key) + " " + activity.resources.getString(
                                string.field_error
                            )
                        value.requestFocus()
                    }
                }
            } else {
                val buttonColor = select_color.background as ColorDrawable
                teacher.name = name.text.toString()
                teacher.post = post.text.toString()
                teacher.phonenumber = phone_number.text.toString()
                teacher.email = email.text.toString()
                teacher.color = buttonColor.color
                dbHelper!!.insertTeacher(teacher)
                adapter.clear()
                adapter.addAll(dbHelper.teacher)
                adapter.notifyDataSetChanged()
                name.text.clear()
                post.text.clear()
                phone_number.text.clear()
                email.text.clear()
                select_color.setBackgroundColor(Color.WHITE)
                name.requestFocus()
                dialog.dismiss()
            }
        }
    }

    fun getEditNoteDialog(
        dbHelper: DbHelper?,
        activity: AppCompatActivity,
        alertLayout: View,
        adapter: ArrayList<Note>,
        listView: ListView,
        listposition: Int
    ) {
        val title = alertLayout.findViewById<EditText>(R.id.titlenote)
        val select_color = alertLayout.findViewById<Button>(R.id.select_color)
        val note = adapter[listposition]
        title.setText(note.title)
        select_color.setBackgroundColor(if (note.color != 0) note.color else Color.WHITE)
        select_color.setTextColor(
            ColorPalette.pickTextColorBasedOnBgColorSimple(
                note.color,
                Color.WHITE,
                Color.BLACK
            )
        )
        select_color.setOnClickListener { v: View? ->
            ColorPickerDialog()
                .withColor((select_color.background as ColorDrawable).color) // the default / initial color
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withTitle(activity.getString(string.choose_color))
                .withTheme(PreferenceUtil.getGeneralTheme(activity))
                .withCornerRadius(16f)
                .withAlphaEnabled(false)
                .withListener { dialog: ColorPickerDialog?, color: Int ->
                    // a color has been picked; use it
                    select_color.setBackgroundColor(color)
                    select_color.setTextColor(
                        ColorPalette.pickTextColorBasedOnBgColorSimple(
                            color,
                            Color.WHITE,
                            Color.BLACK
                        )
                    )
                }
                .clearPickers()
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withPicker(RGBPickerView::class.java)
                .show(activity.supportFragmentManager, "colorPicker")
        }
        val alert = AlertDialog.Builder(activity)
        alert.setTitle(string.edit_note)
        val cancel = alertLayout.findViewById<Button>(R.id.cancel)
        val save = alertLayout.findViewById<Button>(R.id.save)
        alert.setView(alertLayout)
        alert.setCancelable(false)
        val dialog = alert.create()
        dialog.show()
        cancel.setOnClickListener { v: View? ->
            title.text.clear()
            select_color.setBackgroundColor(Color.WHITE)
            dialog.dismiss()
        }
        save.setOnClickListener { v: View? ->
            if (TextUtils.isEmpty(title.text)) {
                title.error = activity.resources.getString(string.title_error)
                title.requestFocus()
            } else {
                val buttonColor = select_color.background as ColorDrawable
                note.title = title.text.toString()
                note.color = buttonColor.color
                dbHelper!!.updateNote(note)
                val notesAdapter = listView.adapter as NotesAdapter
                notesAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }
    }

    fun getAddNoteDialog(
        dbHelper: DbHelper?,
        activity: AppCompatActivity,
        alertLayout: View,
        adapter: NotesAdapter
    ) {
        val title = alertLayout.findViewById<EditText>(R.id.titlenote)
        title.requestFocus()
        val select_color = alertLayout.findViewById<Button>(R.id.select_color)
        select_color.setTextColor(
            ColorPalette.pickTextColorBasedOnBgColorSimple(
                (select_color.background as ColorDrawable).color,
                Color.WHITE,
                Color.BLACK
            )
        )
        val note = Note()
        select_color.setOnClickListener { v: View? ->
            ColorPickerDialog()
                .withColor((select_color.background as ColorDrawable).color) // the default / initial color
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withTitle(activity.getString(string.choose_color))
                .withTheme(PreferenceUtil.getGeneralTheme(activity))
                .withCornerRadius(16f)
                .withAlphaEnabled(false)
                .withListener { dialog: ColorPickerDialog?, color: Int ->
                    // a color has been picked; use it
                    select_color.setBackgroundColor(color)
                    select_color.setTextColor(
                        ColorPalette.pickTextColorBasedOnBgColorSimple(
                            color,
                            Color.WHITE,
                            Color.BLACK
                        )
                    )
                }
                .clearPickers()
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withPicker(RGBPickerView::class.java)
                .show(activity.supportFragmentManager, "colorPicker")
        }
        val alert = AlertDialog.Builder(activity)
        alert.setTitle(string.add_note)
        val cancel = alertLayout.findViewById<Button>(R.id.cancel)
        val save = alertLayout.findViewById<Button>(R.id.save)
        alert.setView(alertLayout)
        alert.setCancelable(false)
        val dialog = alert.create()
        val fab = activity.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view: View? -> dialog.show() }
        cancel.setOnClickListener { v: View? ->
            title.text.clear()
            select_color.setBackgroundColor(Color.WHITE)
            dialog.dismiss()
        }
        save.setOnClickListener { v: View? ->
            if (TextUtils.isEmpty(title.text)) {
                title.error = activity.resources.getString(string.title_error)
                title.requestFocus()
            } else {
                val buttonColor = select_color.background as ColorDrawable
                note.title = title.text.toString()
                note.color = buttonColor.color
                dbHelper!!.insertNote(note)
                adapter.clear()
                adapter.addAll(dbHelper.note)
                adapter.notifyDataSetChanged()
                title.text.clear()
                select_color.setBackgroundColor(Color.WHITE)
                dialog.dismiss()
            }
        }
    }

    fun getEditExamDialog(
        dbHelper: DbHelper,
        activity: AppCompatActivity,
        alertLayout: View,
        adapter: ArrayList<Exam>,
        listView: ListView,
        listposition: Int
    ) {
        val editTextHashs = HashMap<Int, EditText>()
        val subject = alertLayout.findViewById<EditText>(R.id.subjectexam_dialog)
        editTextHashs[string.subject] = subject
        val teacher = alertLayout.findViewById<EditText>(R.id.teacherexam_dialog)
        //        editTextHashs.put(R.string.teacher, teacher);
        val room = alertLayout.findViewById<EditText>(R.id.roomexam_dialog)
        //        editTextHashs.put(R.string.room, room);
        val date = alertLayout.findViewById<TextView>(R.id.dateexam_dialog)
        val time = alertLayout.findViewById<TextView>(R.id.timeexam_dialog)
        val hour = alertLayout.findViewById<TextView>(R.id.hourexam_dialog)
        val select_color = alertLayout.findViewById<Button>(R.id.select_color)
        val exam = adapter[listposition]
        subject.setText(exam.subject)
        teacher.setText(exam.teacher)
        room.setText(exam.room)
        date.text = WeekUtils.localizeDate(activity, exam.date)
        if (exam.time != null && !exam.time.trim { it <= ' ' }.isEmpty()) {
            hour.text = "" + WeekUtils.getMatchingScheduleBegin(exam.time, activity)
            time.text = WeekUtils.localizeTime(activity, exam.time)
        } else {
            hour.text = "0"
            time.text = "0:0"
        }
        select_color.setBackgroundColor(exam.color)
        select_color.setTextColor(
            ColorPalette.pickTextColorBasedOnBgColorSimple(
                exam.color,
                Color.WHITE,
                Color.BLACK
            )
        )
        date.setOnClickListener { v: View? ->
            val calendar = Calendar.getInstance()
            val mYear = calendar[Calendar.YEAR]
            val mMonth = calendar[Calendar.MONTH]
            val mdayofMonth = calendar[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(
                activity,
                { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    val cal = Calendar.getInstance()
                    cal[Calendar.YEAR] = year
                    cal[Calendar.MONTH] = month
                    cal[Calendar.DAY_OF_MONTH] = dayOfMonth
                    date.text = WeekUtils.localizeDate(activity, Date(cal.timeInMillis))
                    exam.date = String.format(
                        Locale.getDefault(),
                        "%02d-%02d-%02d",
                        year,
                        month + 1,
                        dayOfMonth
                    )
                },
                mYear,
                mMonth,
                mdayofMonth
            )
            datePickerDialog.setTitle(string.choose_date)
            datePickerDialog.show()
        }
        time.setOnClickListener { v: View? ->
            var mHour: Int
            var mMinute: Int
            try {
                mHour = exam.time.substring(0, exam.time.indexOf(":")).toInt()
                mMinute = exam.time.substring(exam.time.indexOf(":") + 1).toInt()
            } catch (ignore: Exception) {
                mHour = 0
                mMinute = 0
            }
            val timePickerDialog = TimePickerDialog(activity,
                { view: TimePicker?, hourOfDay: Int, minute: Int ->
                    val newTime = String.format(
                        Locale.getDefault(), "%02d:%02d", hourOfDay, minute
                    )
                    time.text = WeekUtils.localizeTime(activity, newTime)
                    exam.time = newTime
                    hour.text = "" + WeekUtils.getMatchingScheduleBegin(newTime, activity)
                }, mHour, mMinute, DateFormat.is24HourFormat(activity)
            )
            timePickerDialog.setTitle(string.choose_time)
            timePickerDialog.show()
        }
        hour.setOnClickListener { v: View? ->
            val numberPicker = NumberPicker(activity)
            numberPicker.maxValue = 15
            numberPicker.minValue = 1
            try {
                numberPicker.value = hour.text.toString().toInt()
            } catch (e: Exception) {
                numberPicker.value = 1
            }
            MaterialDialog.Builder(activity)
                .customView(numberPicker, false)
                .positiveText(string.select)
                .onPositive { vi: MaterialDialog?, w: DialogAction? ->
                    val value = numberPicker.value
                    time.text = WeekUtils.localizeTime(
                        activity,
                        WeekUtils.getMatchingTimeBegin(value, activity)
                    )
                    exam.time = WeekUtils.getMatchingTimeBegin(value, activity)
                    hour.text = "" + value
                }
                .show()
        }
        select_color.setOnClickListener { v: View? ->
            ColorPickerDialog()
                .withColor((select_color.background as ColorDrawable).color) // the default / initial color
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withTitle(activity.getString(string.choose_color))
                .withTheme(PreferenceUtil.getGeneralTheme(activity))
                .withCornerRadius(16f)
                .withAlphaEnabled(false)
                .withListener { dialog: ColorPickerDialog?, color: Int ->
                    // a color has been picked; use it
                    select_color.setBackgroundColor(color)
                    select_color.setTextColor(
                        ColorPalette.pickTextColorBasedOnBgColorSimple(
                            color,
                            Color.WHITE,
                            Color.BLACK
                        )
                    )
                }
                .clearPickers()
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withPicker(RGBPickerView::class.java)
                .show(activity.supportFragmentManager, "colorPicker")
        }
        subject.setOnEditorActionListener { v: TextView, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                if (event == null || !event.isShiftPressed) {
                    // the user is done typing.
                    //AutoFill other fields
                    for (w in WeekUtils.getAllWeeks(dbHelper)) {
                        if (w.subject.equals(v.text.toString(), ignoreCase = true)) {
                            if (teacher.text.toString().trim { it <= ' ' }
                                    .isEmpty()) teacher.setText(w.teacher)
                            if (room.text.toString().trim { it <= ' ' }
                                    .isEmpty()) room.setText(w.room)
                            select_color.setBackgroundColor(w.color)
                            select_color.setTextColor(
                                ColorPalette.pickTextColorBasedOnBgColorSimple(
                                    w.color,
                                    Color.WHITE,
                                    Color.BLACK
                                )
                            )
                        }
                    }
                    return@setOnEditorActionListener true
                }
            }
            false
        }
        subject.onFocusChangeListener = OnFocusChangeListener { v: View, hasFocus: Boolean ->
            if (!hasFocus) {
                for (w in WeekUtils.getAllWeeks(dbHelper)) {
                    if (w.subject.equals((v as EditText).text.toString(), ignoreCase = true)) {
                        if (teacher.text.toString().trim { it <= ' ' }
                                .isEmpty()) teacher.setText(w.teacher)
                        if (room.text.toString().trim { it <= ' ' }.isEmpty()) room.setText(w.room)
                        select_color.setBackgroundColor(w.color)
                        select_color.setTextColor(
                            ColorPalette.pickTextColorBasedOnBgColorSimple(
                                w.color,
                                Color.WHITE,
                                Color.BLACK
                            )
                        )
                    }
                }
            }
        }
        val alert = AlertDialog.Builder(activity)
        alert.setTitle(activity.resources.getString(string.add_exam))
        alert.setCancelable(false)
        val cancel = alertLayout.findViewById<Button>(R.id.cancel)
        val save = alertLayout.findViewById<Button>(R.id.save)
        alert.setView(alertLayout)
        val dialog = alert.create()
        dialog.show()
        cancel.setOnClickListener { v: View? ->
            subject.text.clear()
            teacher.text.clear()
            room.text.clear()
            select_color.setBackgroundColor(Color.WHITE)
            subject.requestFocus()
            dialog.dismiss()
        }
        save.setOnClickListener { v: View? ->
            if (TextUtils.isEmpty(subject.text) /* || TextUtils.isEmpty(teacher.getText()) || TextUtils.isEmpty(room.getText())*/) {
                for ((key, value) in editTextHashs) {
                    if (TextUtils.isEmpty(value.text)) {
                        value.error =
                            activity.resources.getString(key) + " " + activity.resources.getString(
                                string.field_error
                            )
                        value.requestFocus()
                    }
                }
            } else if (!date.text.toString().matches(".*\\d+.*")) {
                Snackbar.make(alertLayout, string.date_error, Snackbar.LENGTH_LONG).show()
            } /*else if (!time.getText().toString().matches(".*\\d+.*")) {
                Snackbar.make(alertLayout, R.string.time_error, Snackbar.LENGTH_LONG).show();
            }*/ else {
                val buttonColor = select_color.background as ColorDrawable
                exam.subject = subject.text.toString()
                exam.teacher = teacher.text.toString()
                exam.room = room.text.toString()
                exam.color = buttonColor.color
                dbHelper.updateExam(exam)
                val examsAdapter = listView.adapter as ExamsAdapter
                examsAdapter.notifyDataSetChanged()
                dialog.dismiss()
                MaterialDialog.Builder(activity)
                    .content(string.add_to_calendar)
                    .positiveText(string.yes)
                    .onPositive { s: MaterialDialog?, w: DialogAction? ->
                        val year = exam.date.substring(0, exam.date.indexOf("-"))
                        val month = exam.date.substring(
                            year.length + 1,
                            exam.date.indexOf("-") + year.length - 1
                        )
                        val day = exam.date.substring(year.length + month.length + 2)
                        val hour2: String
                        val minute: String
                        if (exam.time != null && !exam.time.trim { it <= ' ' }.isEmpty()) {
                            hour2 = exam.time.substring(0, exam.time.indexOf(":"))
                            minute = exam.time.substring(hour2.length + 1)
                        } else {
                            hour2 = "0"
                            minute = "0"
                        }
                        val timeCalendar = Calendar.getInstance()
                        timeCalendar[year.toInt(), month.toInt(), day.toInt(), hour2.toInt()] =
                            minute.toInt()
                        val intent = Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(
                                CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                timeCalendar.timeInMillis
                            )
                            .putExtra(
                                CalendarContract.EXTRA_EVENT_END_TIME,
                                timeCalendar.timeInMillis
                            )
                            .putExtra(CalendarContract.Events.TITLE, exam.subject)
                            .putExtra(CalendarContract.Events.DESCRIPTION, exam.teacher)
                            .putExtra(CalendarContract.Events.EVENT_LOCATION, exam.room)
                        try {
                            activity.startActivity(intent)
                        } catch (e2: ActivityNotFoundException) {
                            ChocoBar.builder().setActivity(activity)
                                .setText(activity.getString(string.no_calendar_app))
                                .setDuration(ChocoBar.LENGTH_LONG).red().show()
                        }
                    }
                    .negativeText(string.no)
                    .show()
            }
        }
    }

    fun getAddExamDialog(
        dbHelper: DbHelper?,
        activity: AppCompatActivity,
        alertLayout: View,
        adapter: ExamsAdapter
    ) {
        val editTextHashs = HashMap<Int, EditText>()
        val subject = alertLayout.findViewById<EditText>(R.id.subjectexam_dialog)
        editTextHashs[string.subject] = subject
        subject.requestFocus()
        val teacher = alertLayout.findViewById<EditText>(R.id.teacherexam_dialog)
        //        editTextHashs.put(R.string.teacher, teacher);
        val room = alertLayout.findViewById<EditText>(R.id.roomexam_dialog)
        //        editTextHashs.put(R.string.room, room);
        val date = alertLayout.findViewById<TextView>(R.id.dateexam_dialog)
        val time = alertLayout.findViewById<TextView>(R.id.timeexam_dialog)
        val hour = alertLayout.findViewById<TextView>(R.id.hourexam_dialog)
        val select_color = alertLayout.findViewById<Button>(R.id.select_color)
        select_color.setTextColor(
            ColorPalette.pickTextColorBasedOnBgColorSimple(
                (select_color.background as ColorDrawable).color,
                Color.WHITE,
                Color.BLACK
            )
        )
        hour.setText(string.select_time)
        if (PreferenceUtil.showTimes(activity)) {
            hour.visibility = View.GONE
            time.visibility = View.VISIBLE
            time.layoutParams =
                LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, .7f)
        } else {
            hour.visibility = View.VISIBLE
            time.visibility = View.GONE
            hour.layoutParams =
                LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, .7f)
        }
        val exam = Exam()
        date.setOnClickListener { v: View? ->
            val calendar = Calendar.getInstance()
            val mYear = calendar[Calendar.YEAR]
            val mMonth = calendar[Calendar.MONTH]
            val mdayofMonth = calendar[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(
                activity,
                { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    val cal = Calendar.getInstance()
                    cal[Calendar.YEAR] = year
                    cal[Calendar.MONTH] = month
                    cal[Calendar.DAY_OF_MONTH] = dayOfMonth
                    date.text = WeekUtils.localizeDate(activity, Date(cal.timeInMillis))
                    exam.date = String.format(
                        Locale.getDefault(),
                        "%02d-%02d-%02d",
                        year,
                        month + 1,
                        dayOfMonth
                    )
                },
                mYear,
                mMonth,
                mdayofMonth
            )
            datePickerDialog.setTitle(string.choose_date)
            datePickerDialog.show()
        }
        time.setOnClickListener { v: View? ->
            val c = Calendar.getInstance()
            val mHour = c[Calendar.HOUR_OF_DAY]
            val mMinute = c[Calendar.MINUTE]
            val timePickerDialog = TimePickerDialog(activity,
                { view: TimePicker?, hourOfDay: Int, minute: Int ->
                    val newTime = String.format(
                        Locale.getDefault(), "%02d:%02d", hourOfDay, minute
                    )
                    time.text = WeekUtils.localizeTime(activity, newTime)
                    exam.time = newTime
                    hour.text = "" + WeekUtils.getMatchingScheduleBegin(newTime, activity)
                }, mHour, mMinute, DateFormat.is24HourFormat(activity)
            )
            timePickerDialog.setTitle(string.choose_time)
            timePickerDialog.show()
        }
        hour.setOnClickListener { v: View? ->
            val numberPicker = NumberPicker(activity)
            numberPicker.maxValue = 15
            numberPicker.minValue = 1
            MaterialDialog.Builder(activity)
                .customView(numberPicker, false)
                .positiveText(string.select)
                .onPositive { vi: MaterialDialog?, w: DialogAction? ->
                    val value = numberPicker.value
                    time.text = WeekUtils.localizeTime(
                        activity,
                        WeekUtils.getMatchingTimeBegin(value, activity)
                    )
                    exam.time = WeekUtils.getMatchingTimeBegin(value, activity)
                    hour.text = "" + value
                }
                .show()
        }
        select_color.setOnClickListener { v: View? ->
            ColorPickerDialog()
                .withColor((select_color.background as ColorDrawable).color) // the default / initial color
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withTitle(activity.getString(string.choose_color))
                .withTheme(PreferenceUtil.getGeneralTheme(activity))
                .withCornerRadius(16f)
                .withAlphaEnabled(false)
                .withListener { dialog: ColorPickerDialog?, color: Int ->
                    // a color has been picked; use it
                    select_color.setBackgroundColor(color)
                    select_color.setTextColor(
                        ColorPalette.pickTextColorBasedOnBgColorSimple(
                            color,
                            Color.WHITE,
                            Color.BLACK
                        )
                    )
                }
                .clearPickers()
                .withPresets(*ColorPalette.PRIMARY_COLORS)
                .withPicker(RGBPickerView::class.java)
                .show(activity.supportFragmentManager, "colorPicker")
        }
        subject.setOnEditorActionListener { v: TextView, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                if (event == null || !event.isShiftPressed) {
                    // the user is done typing.
                    //AutoFill other fields
                    for (w in WeekUtils.getAllWeeks(dbHelper!!)) {
                        if (w.subject.equals(v.text.toString(), ignoreCase = true)) {
                            if (teacher.text.toString().trim { it <= ' ' }
                                    .isEmpty()) teacher.setText(w.teacher)
                            if (room.text.toString().trim { it <= ' ' }
                                    .isEmpty()) room.setText(w.room)
                            select_color.setBackgroundColor(w.color)
                            select_color.setTextColor(
                                ColorPalette.pickTextColorBasedOnBgColorSimple(
                                    w.color,
                                    Color.WHITE,
                                    Color.BLACK
                                )
                            )
                        }
                    }
                    return@setOnEditorActionListener true
                }
            }
            false
        }
        subject.onFocusChangeListener = OnFocusChangeListener { v: View, hasFocus: Boolean ->
            if (!hasFocus) {
                for (w in WeekUtils.getAllWeeks(dbHelper!!)) {
                    if (w.subject.equals((v as EditText).text.toString(), ignoreCase = true)) {
                        if (teacher.text.toString().trim { it <= ' ' }
                                .isEmpty()) teacher.setText(w.teacher)
                        if (room.text.toString().trim { it <= ' ' }.isEmpty()) room.setText(w.room)
                        select_color.setBackgroundColor(w.color)
                        select_color.setTextColor(
                            ColorPalette.pickTextColorBasedOnBgColorSimple(
                                w.color,
                                Color.WHITE,
                                Color.BLACK
                            )
                        )
                    }
                }
            }
        }
        val alert = AlertDialog.Builder(activity)
        alert.setTitle(activity.resources.getString(string.add_exam))
        alert.setCancelable(false)
        val cancel = alertLayout.findViewById<Button>(R.id.cancel)
        val save = alertLayout.findViewById<Button>(R.id.save)
        alert.setView(alertLayout)
        val dialog = alert.create()
        val fab = activity.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view: View? -> dialog.show() }
        cancel.setOnClickListener { v: View? ->
            subject.text.clear()
            teacher.text.clear()
            room.text.clear()
            select_color.setBackgroundColor(Color.WHITE)
            subject.requestFocus()
            dialog.dismiss()
        }
        save.setOnClickListener { v: View? ->
            if (TextUtils.isEmpty(subject.text) /*|| TextUtils.isEmpty(teacher.getText()) || TextUtils.isEmpty(room.getText())*/) {
                for ((key, value) in editTextHashs) {
                    if (TextUtils.isEmpty(value.text)) {
                        value.error =
                            activity.resources.getString(key) + " " + activity.resources.getString(
                                string.field_error
                            )
                        value.requestFocus()
                    }
                }
            } else if (!date.text.toString().matches(".*\\d+.*")) {
                Snackbar.make(alertLayout, string.date_error, Snackbar.LENGTH_LONG).show()
            } /*else if (!time.getText().toString().matches(".*\\d+.*")) {
                Snackbar.make(alertLayout, R.string.time_error, Snackbar.LENGTH_LONG).show();
            }*/ else {
                val buttonColor = select_color.background as ColorDrawable
                exam.subject = subject.text.toString()
                exam.teacher = teacher.text.toString()
                exam.room = room.text.toString()
                exam.color = buttonColor.color
                dbHelper!!.insertExam(exam)
                adapter.clear()
                adapter.addAll(dbHelper.exam)
                adapter.notifyDataSetChanged()
                subject.text.clear()
                teacher.text.clear()
                room.text.clear()
                date.setText(string.choose_date)
                time.setText(string.select_time)
                hour.setText(string.select_time)
                select_color.setBackgroundColor(Color.WHITE)
                subject.requestFocus()
                dialog.dismiss()
                MaterialDialog.Builder(activity)
                    .content(string.add_to_calendar)
                    .positiveText(string.yes)
                    .onPositive { s: MaterialDialog?, w: DialogAction? ->
                        val year = exam.date.substring(0, exam.date.indexOf("-"))
                        val month = exam.date.substring(
                            year.length + 1,
                            exam.date.indexOf("-") + year.length - 1
                        )
                        val day = exam.date.substring(year.length + month.length + 2)
                        val hour2: String
                        val minute: String
                        if (exam.time != null && !exam.time.trim { it <= ' ' }.isEmpty()) {
                            hour2 = exam.time.substring(0, exam.time.indexOf(":"))
                            minute = exam.time.substring(hour2.length + 1)
                        } else {
                            hour2 = "0"
                            minute = "0"
                        }
                        val timeCalendar = Calendar.getInstance()
                        timeCalendar[year.toInt(), month.toInt(), day.toInt(), hour2.toInt()] =
                            minute.toInt()
                        val intent = Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(
                                CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                timeCalendar.timeInMillis
                            )
                            .putExtra(
                                CalendarContract.EXTRA_EVENT_END_TIME,
                                timeCalendar.timeInMillis
                            )
                            .putExtra(CalendarContract.Events.TITLE, exam.subject)
                            .putExtra(CalendarContract.Events.DESCRIPTION, exam.teacher)
                            .putExtra(CalendarContract.Events.EVENT_LOCATION, exam.room)
                        try {
                            activity.startActivity(intent)
                        } catch (e2: ActivityNotFoundException) {
                            ChocoBar.builder().setActivity(activity)
                                .setText(activity.getString(string.no_calendar_app))
                                .setDuration(ChocoBar.LENGTH_LONG).red().show()
                        }
                    }
                    .negativeText(string.no)
                    .show()
            }
        }
    }

    fun getDeleteDialog(context: Context, runnable: Runnable, deleteSubject: String?) {
        MaterialDialog.Builder(context)
            .title(context.getString(string.are_you_sure))
            .content(context.getString(string.delete_content, deleteSubject))
            .positiveText(context.getString(string.yes))
            .onPositive { dialog: MaterialDialog, which: DialogAction? ->
                runnable.run()
                dialog.dismiss()
            }
            .onNegative { dialog: MaterialDialog, which: DialogAction? -> dialog.dismiss() }
            .negativeText(context.getString(string.no))
            .show()
    }

    private fun databaseChanged(context: Context) {
        NotificationUtil.sendNotificationCurrentLesson(context, false)
        setDoNotDisturbReceivers(context, false)
    }
}