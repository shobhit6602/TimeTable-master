package com.ulan.timetable.activities

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
import android.content.ContentValues
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
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.app.Activity
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback
import com.afollestad.materialdialogs.DialogAction
import android.content.Intent
import android.content.ComponentName
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
import android.content.ActivityNotFoundException
import com.pd.chocobar.ChocoBar
import com.ulan.timetable.adapters.TeachersAdapter
import com.ulan.timetable.adapters.NotesAdapter
import com.ulan.timetable.adapters.ExamsAdapter
import android.widget.ArrayAdapter
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
import android.widget.RemoteViewsService
import android.widget.RemoteViewsService.RemoteViewsFactory
import com.ulan.timetable.appwidget.DayAppWidgetService.DayAppWidgetRemoteViewsFactory
import android.widget.RemoteViews
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
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
import android.content.BroadcastReceiver
import android.view.*
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
import com.github.tlaabs.timetableview.Time
import com.ulan.timetable.fragments.TimeSettingsFragment
import java.lang.StringBuilder
import java.util.*

class SummaryActivity : AppCompatActivity() {
    private var schoolStart: String? = null
    private var weeks = ArrayList<ArrayList<Week>?>()
    private var dbHelper: DbHelper? = null
    private var header: Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(PreferenceUtil.getGeneralTheme(this))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        val oldTimes = PreferenceUtil.getStartTime(this)
        schoolStart = oldTimes[0].toString() + ":" + oldTimes[1]
        findViewById<View>(R.id.courseTable).visibility = View.GONE
        dbHelper = if (ACTION_SHOW.equals(intent.action, ignoreCase = true)) {
            DbHelper(this, ProfileManagement.loadPreferredProfilePosition())
        } else {
            DbHelper(this)
        }
        weeks = ArrayList()
        val startOnSunday = PreferenceUtil.isWeekStartOnSunday(this)
        if (!startOnSunday) {
            weeks.add(dbHelper!!.getWeek(WeekdayFragment.Companion.KEY_MONDAY_FRAGMENT))
            weeks.add(dbHelper!!.getWeek(WeekdayFragment.Companion.KEY_TUESDAY_FRAGMENT))
            weeks.add(dbHelper!!.getWeek(WeekdayFragment.Companion.KEY_WEDNESDAY_FRAGMENT))
            weeks.add(dbHelper!!.getWeek(WeekdayFragment.Companion.KEY_THURSDAY_FRAGMENT))
            weeks.add(dbHelper!!.getWeek(WeekdayFragment.Companion.KEY_FRIDAY_FRAGMENT))
            weeks.add(dbHelper!!.getWeek(WeekdayFragment.Companion.KEY_SATURDAY_FRAGMENT))
            weeks.add(dbHelper!!.getWeek(WeekdayFragment.Companion.KEY_SUNDAY_FRAGMENT))
        } else {
            weeks.add(dbHelper!!.getWeek(WeekdayFragment.Companion.KEY_SUNDAY_FRAGMENT))
            weeks.add(dbHelper!!.getWeek(WeekdayFragment.Companion.KEY_MONDAY_FRAGMENT))
            weeks.add(dbHelper!!.getWeek(WeekdayFragment.Companion.KEY_TUESDAY_FRAGMENT))
            weeks.add(dbHelper!!.getWeek(WeekdayFragment.Companion.KEY_WEDNESDAY_FRAGMENT))
            weeks.add(dbHelper!!.getWeek(WeekdayFragment.Companion.KEY_THURSDAY_FRAGMENT))
            weeks.add(dbHelper!!.getWeek(WeekdayFragment.Companion.KEY_FRIDAY_FRAGMENT))
            weeks.add(dbHelper!!.getWeek(WeekdayFragment.Companion.KEY_SATURDAY_FRAGMENT))
        }
        header = resources.getStringArray(R.array.timetable_header)
        if (startOnSunday) {
            val headerList = Arrays.asList(*header)
            val sunday = headerList[headerList.size - 1]
            val newHeader: MutableList<String> = ArrayList()
            newHeader.add(sunday)
            for (i in 0 until headerList.size - 1) {
                newHeader.add(headerList[i])
            }
            header = newHeader.toArray<String>(arrayOf<String>())
        }
        if (PreferenceUtil.isSummaryLibrary1(this)) setupCourseTableLibrary1() else setupTimetableLibrary2()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.summary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_changeSummary) {
            PreferenceUtil.setSummaryLibrary(this, !PreferenceUtil.isSummaryLibrary1(this))
            recreate()
        } else if (item.itemId == R.id.action_settings) {
            startActivity(Intent(this, TimeSettingsActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Setup the course Table with Library: https://github.com/asdoi/TimetableUI
     */
    private fun setupCourseTableLibrary1() {
        val courseTable = findViewById<CourseTableLayout>(R.id.courseTable)
        courseTable.visibility = View.VISIBLE
        courseTable.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        courseTable.setAnimation(false)
        val studentCourse = StudentCourse()
        val courseInfoList = ArrayList<CourseInfo>()
        val durationStrings: MutableList<List<String>> = ArrayList()
        for (i in 0..6) {
            durationStrings.add(ArrayList())
        }
        for (j in weeks.indices) {
            for (i in weeks[j]!!.indices) {
                val w = weeks[j]!![i]
                val start = WeekUtils.getMatchingScheduleBegin(w.fromTime, this)
                val end = WeekUtils.getMatchingScheduleEnd(w.toTime, this)
                durationStrings[j].add(i, generateLessonsString(end - start + 1, start - 1))
            }
        }
        for (j in weeks.indices) {
            for (i in weeks[j]!!.indices) {
                val w = weeks[j]!![i]
                val courseTimes = arrayOfNulls<String>(7)
                Arrays.fill(courseTimes, "")
                courseTimes[j] = "" + durationStrings[j][i]
                val courseInfo = CustomCourseInfo(w)
                courseInfo.setCourseTime(courseTimes)
                courseInfoList.add(courseInfo)
            }
        }

        // Set timetable
        studentCourse.courseList = courseInfoList
        courseTable.setHeader(*header)
        courseTable.setTextSize(14)
        courseTable.setStudentCourse(studentCourse)
        courseTable.setOnCourseClickListener { view: View ->
            val item = view.tag as CustomCourseInfo
            val alertLayout = layoutInflater.inflate(R.layout.dialog_add_subject, null)
            AlertDialogsHelper.getEditSubjectDialog(
                dbHelper,
                this,
                alertLayout,
                { recreate() },
                item.week
            )
        }
    }

    private class CustomCourseInfo(val week: Week) : CourseInfo() {

        init {
            val name = StringBuilder(week.subject)
            if (week.teacher != null && !week.teacher.trim { it <= ' ' }
                    .isEmpty()) name.append("\n").append(
                week.teacher
            )
            if (week.room != null && !week.teacher.trim { it <= ' ' }.isEmpty()) name.append("\n")
                .append(
                    week.room
                )
            setName(name.toString())
            color = week.color
        }
    }

    /**
     * Setup the course Table with Library: https://github.com/asdoi/TimetableView
     */
    private fun setupTimetableLibrary2() {
        val done: MutableList<String?> = ArrayList()
        val colors = ArrayList<String>()
        val timetableContent: MutableList<ArrayList<Schedule>> = ArrayList()
        var rows = 0
        for (j in weeks.indices) {
            for (i in weeks[j]!!.indices) {
                val schedules = ArrayList<Schedule>()
                val w = weeks[j]!![i]
                val subject = w.subject
                if (done.contains(subject)) continue
                var i1 = i + 1
                for (j1 in j until weeks.size) {
                    while (i1 < weeks[j1]!!.size) {
                        if (weeks[j1]!![i1].subject.equals(subject, ignoreCase = true)) {
                            val schedule = CustomSchedule(weeks[j1]!![i1], j1)
                            schedules.add(schedule)
                            if (schedule.startTime.hour > rows) rows = schedule.startTime.hour
                        }
                        i1++
                    }
                    i1 = 0
                }
                val schedule = CustomSchedule(w, j)
                schedules.add(schedule)
                if (w.color != -1) colors.add(
                    String.format(
                        "#%06X",
                        0xFFFFFF and w.color
                    )
                ) else colors.add(
                    String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(this, R.color.grey))
                )
                if (schedule.startTime.hour > rows) rows = schedule.startTime.hour
                done.add(subject)
                timetableContent.add(schedules)
            }
        }
        val startHour = schoolStart!!.substring(0, schoolStart!!.indexOf(":")).toInt()
        val newHeader = arrayOfNulls<String>(8)
        System.arraycopy(header, 0, newHeader, 1, newHeader.size - 1)
        val timetable = TimetableView.Builder(this)
            .setColumnCount(6 + if (PreferenceUtil.isSevenDays(this)) 2 else 0)
            .setRowCount(10)
            .setStartTime(startHour)
            .setHeaderTitle(newHeader)
            .setStickerColors(colors.toArray(arrayOf()))
            .build()
        timetable.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        for (schedules in timetableContent) {
            timetable.add(schedules)
        }
        (findViewById<View>(R.id.summary_linear) as LinearLayout).addView(timetable)

/*        timetable.setOnStickerSelectEventListener((idx, schedules1) -> {
            CustomSchedule schedule = (CustomSchedule) schedules1.get(idx);
            Week week = schedule.getWeek();
            final View alertLayout = this.getLayoutInflater().inflate(R.layout.dialog_add_subject, null);
            AlertDialogsHelper.getEditSubjectDialog(this, alertLayout, () -> setupTimetableLibrary2(), week);
        });*/
    }

    private class CustomSchedule internal constructor(val week: Week, day: Int) : Schedule() {

        init {
            val startHour = week.fromTime.substring(0, week.fromTime.indexOf(":")).toInt()
            val startMinute = week.fromTime.substring(week.fromTime.indexOf(":") + 1).toInt()
            val endHour = week.toTime.substring(0, week.toTime.indexOf(":")).toInt()
            val endMinute = week.toTime.substring(week.toTime.indexOf(":") + 1).toInt()
            classTitle = week.subject // sets subject
            classPlace = """
                ${week.room}
                ${week.teacher}
                """.trimIndent() // sets place
            professorName = "" // sets professor
            startTime =
                Time(startHour, startMinute) // sets the beginning of class time (hour,minute)
            endTime = Time(endHour, endMinute) // sets the end of class time (hour,minute)
            setDay(day)
        }
    }

    companion object {
        const val ACTION_SHOW = "showSummary"
        private fun generateLessonsString(duration: Int, hoursBefore: Int): String {
            val durationString = StringBuilder()
            for (i in 1..duration) {
                durationString.append(i + hoursBefore).append(" ")
            }
            return durationString.toString()
        }
    }
}