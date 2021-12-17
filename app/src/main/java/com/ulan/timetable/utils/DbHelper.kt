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
import com.ulan.timetable.model.*
import java.util.*

/**
 * Created by Ulan on 07.09.2018.
 */
//TODO: Rewrite to Kotlin and RoomDB
class DbHelper : SQLiteOpenHelper {
    private var context: Context?

    constructor(context: Context?) : super(
        context,
        getDBName(ProfileManagement.getSelectedProfilePosition()),
        null,
        DB_VERSION
    ) {
        this.context = context
    }

    constructor(context: Context?, selectedProfile: Int) : super(
        context,
        getDBName(selectedProfile),
        null,
        DB_VERSION
    ) {
        this.context = context
    }

    private constructor(context: Context?, odd: Boolean) : super(
        context,
        DB_NAME + "_odd",
        null,
        6
    ) {
        this.context = context
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TIMETABLE = ("CREATE TABLE IF NOT EXISTS " + TIMETABLE + "("
                + WEEK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + WEEK_SUBJECT + " TEXT,"
                + WEEK_FRAGMENT + " TEXT,"
                + WEEK_TEACHER + " TEXT,"
                + WEEK_ROOM + " TEXT,"
                + WEEK_FROM_TIME + " TEXT,"
                + WEEK_TO_TIME + " TEXT,"
                + WEEK_COLOR + " INTEGER" + ")")
        val CREATE_TIMETABLE_ODD = ("CREATE TABLE IF NOT EXISTS " + TIMETABLE_ODD + "("
                + WEEK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + WEEK_SUBJECT + " TEXT,"
                + WEEK_FRAGMENT + " TEXT,"
                + WEEK_TEACHER + " TEXT,"
                + WEEK_ROOM + " TEXT,"
                + WEEK_FROM_TIME + " TEXT,"
                + WEEK_TO_TIME + " TEXT,"
                + WEEK_COLOR + " INTEGER" + ")")
        val CREATE_HOMEWORKS = ("CREATE TABLE IF NOT EXISTS " + HOMEWORKS + "("
                + HOMEWORKS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + HOMEWORKS_SUBJECT + " TEXT,"
                + HOMEWORKS_DESCRIPTION + " TEXT,"
                + HOMEWORKS_DATE + " TEXT,"
                + HOMEWORKS_COLOR + " INTEGER" + ")")
        val CREATE_NOTES = ("CREATE TABLE IF NOT EXISTS " + NOTES + "("
                + NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NOTES_TITLE + " TEXT,"
                + NOTES_TEXT + " TEXT,"
                + NOTES_COLOR + " INTEGER" + ")")
        val CREATE_TEACHERS = ("CREATE TABLE IF NOT EXISTS " + TEACHERS + "("
                + TEACHERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TEACHERS_NAME + " TEXT,"
                + TEACHERS_POST + " TEXT,"
                + TEACHERS_PHONE_NUMBER + " TEXT,"
                + TEACHERS_EMAIL + " TEXT,"
                + TEACHERS_COLOR + " INTEGER" + ")")
        val CREATE_EXAMS = ("CREATE TABLE IF NOT EXISTS " + EXAMS + "("
                + EXAMS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EXAMS_SUBJECT + " TEXT,"
                + EXAMS_TEACHER + " TEXT,"
                + EXAMS_ROOM + " TEXT,"
                + EXAMS_DATE + " TEXT,"
                + EXAMS_TIME + " TEXT,"
                + EXAMS_COLOR + " INTEGER" + ")")
        db.execSQL(CREATE_TIMETABLE)
        db.execSQL(CREATE_TIMETABLE_ODD)
        db.execSQL(CREATE_HOMEWORKS)
        db.execSQL(CREATE_NOTES)
        db.execSQL(CREATE_TEACHERS)
        db.execSQL(CREATE_EXAMS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onCreate(db)
        when (oldVersion) {
            6 -> migrateEvenOddWeeks(db)
            else -> migrateEvenOddWeeks(db)
        }
    }

    private fun migrateEvenOddWeeks(db: SQLiteDatabase) {
        val keys = arrayOf<String>(
            WeekdayFragment.Companion.KEY_MONDAY_FRAGMENT,
            WeekdayFragment.Companion.KEY_TUESDAY_FRAGMENT,
            WeekdayFragment.Companion.KEY_WEDNESDAY_FRAGMENT,
            WeekdayFragment.Companion.KEY_THURSDAY_FRAGMENT,
            WeekdayFragment.Companion.KEY_FRIDAY_FRAGMENT,
            WeekdayFragment.Companion.KEY_SATURDAY_FRAGMENT,
            WeekdayFragment.Companion.KEY_SUNDAY_FRAGMENT
        )
        val oldOddWeeks = ArrayList<Week>()
        val oldDbHelper = DbHelper(context, true)
        for (key in keys) {
            oldOddWeeks.addAll(oldDbHelper.getWeek(key, TIMETABLE))
        }
        for (week in oldOddWeeks) {
            insertWeek(week, TIMETABLE_ODD, db)
        }
    }

    /**
     * Methods for Week fragments
     */
    private val timetableTable: String
        private get() = getTimetableTable(Calendar.getInstance())

    private fun getTimetableTable(now: Calendar): String {
        return if (PreferenceUtil.isEvenWeek(
                context,
                now
            )
        ) TIMETABLE else TIMETABLE_ODD
    }

    fun insertWeek(week: Week) {
        insertWeek(week, timetableTable, this.writableDatabase)
    }

    private fun insertWeek(week: Week, tableName: String, db: SQLiteDatabase) {
        val contentValues = ContentValues()
        contentValues.put(WEEK_SUBJECT, week.subject)
        contentValues.put(WEEK_FRAGMENT, week.fragment)
        contentValues.put(WEEK_TEACHER, week.teacher)
        contentValues.put(WEEK_ROOM, week.room)
        contentValues.put(WEEK_FROM_TIME, week.fromTime)
        contentValues.put(WEEK_TO_TIME, week.toTime)
        contentValues.put(WEEK_COLOR, week.color)
        db.insert(tableName, null, contentValues)
        db.update(tableName, contentValues, WEEK_FRAGMENT, null)
        //        db.close();
    }

    fun deleteWeekById(week: Week) {
        val db = this.writableDatabase
        db.delete(timetableTable, WEEK_ID + " = ? ", arrayOf(week.id.toString()))
        db.close()
    }

    fun updateWeek(week: Week) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(WEEK_SUBJECT, week.subject)
        contentValues.put(WEEK_TEACHER, week.teacher)
        contentValues.put(WEEK_ROOM, week.room)
        contentValues.put(WEEK_FROM_TIME, week.fromTime)
        contentValues.put(WEEK_TO_TIME, week.toTime)
        contentValues.put(WEEK_COLOR, week.color)
        db.update(timetableTable, contentValues, WEEK_ID + " = " + week.id, null)
        db.close()
    }

    fun getWeek(fragment: String?): ArrayList<Week> {
        return getWeek(fragment, Calendar.getInstance())
    }

    fun getWeek(fragment: String?, now: Calendar): ArrayList<Week> {
        return getWeek(fragment, getTimetableTable(now))
    }

    private fun getWeek(fragment: String?, dbName: String): ArrayList<Week> {
        val db = this.writableDatabase
        val weeklist = ArrayList<Week>()
        var week: Week
        val cursor = db.rawQuery(
            "SELECT * FROM ( SELECT * FROM " + dbName + " ORDER BY " + WEEK_FROM_TIME + " ) WHERE " + WEEK_FRAGMENT + " LIKE '" + fragment + "%'",
            null
        )
        while (cursor.moveToNext()) {
            week = Week()
            week.id = cursor.getInt(cursor.getColumnIndex(WEEK_ID))
            week.fragment = cursor.getString(cursor.getColumnIndex(WEEK_FRAGMENT))
            week.subject = cursor.getString(cursor.getColumnIndex(WEEK_SUBJECT))
            week.teacher = cursor.getString(cursor.getColumnIndex(WEEK_TEACHER))
            week.room = cursor.getString(cursor.getColumnIndex(WEEK_ROOM))
            week.fromTime = cursor.getString(cursor.getColumnIndex(WEEK_FROM_TIME))
            week.toTime = cursor.getString(cursor.getColumnIndex(WEEK_TO_TIME))
            week.color = cursor.getInt(cursor.getColumnIndex(WEEK_COLOR))
            weeklist.add(week)
        }
        return weeklist
    }

    /**
     * Methods for Homeworks activity
     */
    fun insertHomework(homework: Homework) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(HOMEWORKS_SUBJECT, homework.subject)
        contentValues.put(HOMEWORKS_DESCRIPTION, homework.description)
        contentValues.put(HOMEWORKS_DATE, homework.date)
        contentValues.put(HOMEWORKS_COLOR, homework.color)
        db.insert(HOMEWORKS, null, contentValues)
        db.close()
    }

    fun updateHomework(homework: Homework) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(HOMEWORKS_SUBJECT, homework.subject)
        contentValues.put(HOMEWORKS_DESCRIPTION, homework.description)
        contentValues.put(HOMEWORKS_DATE, homework.date)
        contentValues.put(HOMEWORKS_COLOR, homework.color)
        db.update(HOMEWORKS, contentValues, HOMEWORKS_ID + " = " + homework.id, null)
        db.close()
    }

    fun deleteHomeworkById(homework: Homework) {
        val db = this.writableDatabase
        db.delete(HOMEWORKS, HOMEWORKS_ID + " = ? ", arrayOf(homework.id.toString()))
        db.close()
    }

    val homework: ArrayList<Homework>
        get() {
            val db = this.writableDatabase
            val homelist = ArrayList<Homework>()
            var homework: Homework
            val cursor = db.rawQuery(
                "SELECT * FROM " + HOMEWORKS + " ORDER BY datetime(" + HOMEWORKS_DATE + ") ASC",
                null
            )
            while (cursor.moveToNext()) {
                homework = Homework()
                homework.id = cursor.getInt(cursor.getColumnIndex(HOMEWORKS_ID))
                homework.subject = cursor.getString(cursor.getColumnIndex(HOMEWORKS_SUBJECT))
                homework.description =
                    cursor.getString(cursor.getColumnIndex(HOMEWORKS_DESCRIPTION))
                homework.date = cursor.getString(cursor.getColumnIndex(HOMEWORKS_DATE))
                homework.color = cursor.getInt(cursor.getColumnIndex(HOMEWORKS_COLOR))
                homelist.add(homework)
            }
            cursor.close()
            db.close()
            return homelist
        }

    /**
     * Methods for Notes activity
     */
    fun insertNote(note: Note) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NOTES_TITLE, note.title)
        contentValues.put(NOTES_TEXT, note.text)
        contentValues.put(NOTES_COLOR, note.color)
        db.insert(NOTES, null, contentValues)
        db.close()
    }

    fun updateNote(note: Note) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NOTES_TITLE, note.title)
        contentValues.put(NOTES_TEXT, note.text)
        contentValues.put(NOTES_COLOR, note.color)
        db.update(NOTES, contentValues, NOTES_ID + " = " + note.id, null)
        db.close()
    }

    fun deleteNoteById(note: Note) {
        val db = this.writableDatabase
        db.delete(NOTES, NOTES_ID + " =? ", arrayOf(note.id.toString()))
        db.close()
    }

    val note: ArrayList<Note>
        get() {
            val db = this.writableDatabase
            val notelist = ArrayList<Note>()
            var note: Note
            val cursor = db.rawQuery("SELECT * FROM " + NOTES, null)
            while (cursor.moveToNext()) {
                note = Note()
                note.id = cursor.getInt(cursor.getColumnIndex(NOTES_ID))
                note.title = cursor.getString(cursor.getColumnIndex(NOTES_TITLE))
                note.text = cursor.getString(cursor.getColumnIndex(NOTES_TEXT))
                note.color = cursor.getInt(cursor.getColumnIndex(NOTES_COLOR))
                notelist.add(note)
            }
            cursor.close()
            db.close()
            return notelist
        }

    /**
     * Methods for Teachers activity
     */
    fun insertTeacher(teacher: Teacher) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TEACHERS_NAME, teacher.name)
        contentValues.put(TEACHERS_POST, teacher.post)
        contentValues.put(TEACHERS_PHONE_NUMBER, teacher.phonenumber)
        contentValues.put(TEACHERS_EMAIL, teacher.email)
        contentValues.put(TEACHERS_COLOR, teacher.color)
        db.insert(TEACHERS, null, contentValues)
        db.close()
    }

    fun updateTeacher(teacher: Teacher) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TEACHERS_NAME, teacher.name)
        contentValues.put(TEACHERS_POST, teacher.post)
        contentValues.put(TEACHERS_PHONE_NUMBER, teacher.phonenumber)
        contentValues.put(TEACHERS_EMAIL, teacher.email)
        contentValues.put(TEACHERS_COLOR, teacher.color)
        db.update(TEACHERS, contentValues, TEACHERS_ID + " = " + teacher.id, null)
        db.close()
    }

    fun deleteTeacherById(teacher: Teacher) {
        val db = this.writableDatabase
        db.delete(TEACHERS, TEACHERS_ID + " =? ", arrayOf(teacher.id.toString()))
        db.close()
    }

    val teacher: ArrayList<Teacher>
        get() {
            val db = this.writableDatabase
            val teacherlist = ArrayList<Teacher>()
            var teacher: Teacher
            val cursor = db.rawQuery("SELECT * FROM " + TEACHERS, null)
            while (cursor.moveToNext()) {
                teacher = Teacher()
                teacher.id = cursor.getInt(cursor.getColumnIndex(TEACHERS_ID))
                teacher.name = cursor.getString(cursor.getColumnIndex(TEACHERS_NAME))
                teacher.post = cursor.getString(cursor.getColumnIndex(TEACHERS_POST))
                teacher.phonenumber =
                    cursor.getString(cursor.getColumnIndex(TEACHERS_PHONE_NUMBER))
                teacher.email = cursor.getString(cursor.getColumnIndex(TEACHERS_EMAIL))
                teacher.color = cursor.getInt(cursor.getColumnIndex(TEACHERS_COLOR))
                teacherlist.add(teacher)
            }
            cursor.close()
            db.close()
            return teacherlist
        }

    /**
     * Methods for Exams activity
     */
    fun insertExam(exam: Exam) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(EXAMS_SUBJECT, exam.subject)
        contentValues.put(EXAMS_TEACHER, exam.teacher)
        contentValues.put(EXAMS_ROOM, exam.room)
        contentValues.put(EXAMS_DATE, exam.date)
        contentValues.put(EXAMS_TIME, exam.time)
        contentValues.put(EXAMS_COLOR, exam.color)
        db.insert(EXAMS, null, contentValues)
        db.close()
    }

    fun updateExam(exam: Exam) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(EXAMS_SUBJECT, exam.subject)
        contentValues.put(EXAMS_TEACHER, exam.teacher)
        contentValues.put(EXAMS_ROOM, exam.room)
        contentValues.put(EXAMS_DATE, exam.date)
        contentValues.put(EXAMS_TIME, exam.time)
        contentValues.put(EXAMS_COLOR, exam.color)
        db.update(EXAMS, contentValues, EXAMS_ID + " = " + exam.id, null)
        db.close()
    }

    fun deleteExamById(exam: Exam) {
        val db = this.writableDatabase
        db.delete(EXAMS, EXAMS_ID + " =? ", arrayOf(exam.id.toString()))
        db.close()
    }

    val exam: ArrayList<Exam>
        get() {
            val db = this.writableDatabase
            val examslist = ArrayList<Exam>()
            var exam: Exam
            val cursor = db.rawQuery("SELECT * FROM " + EXAMS, null)
            while (cursor.moveToNext()) {
                exam = Exam()
                exam.id = cursor.getInt(cursor.getColumnIndex(EXAMS_ID))
                exam.subject = cursor.getString(cursor.getColumnIndex(EXAMS_SUBJECT))
                exam.teacher = cursor.getString(cursor.getColumnIndex(EXAMS_TEACHER))
                exam.room = cursor.getString(cursor.getColumnIndex(EXAMS_ROOM))
                exam.date = cursor.getString(cursor.getColumnIndex(EXAMS_DATE))
                exam.time = cursor.getString(cursor.getColumnIndex(EXAMS_TIME))
                exam.color = cursor.getInt(cursor.getColumnIndex(EXAMS_COLOR))
                examslist.add(exam)
            }
            cursor.close()
            db.close()
            return examslist
        }

    fun deleteAll() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS " + TIMETABLE)
        db.execSQL("DROP TABLE IF EXISTS " + TIMETABLE_ODD)
        db.execSQL("DROP TABLE IF EXISTS " + HOMEWORKS)
        db.execSQL("DROP TABLE IF EXISTS " + NOTES)
        db.execSQL("DROP TABLE IF EXISTS " + TEACHERS)
        db.execSQL("DROP TABLE IF EXISTS " + EXAMS)
        db.close()
        onCreate(this.writableDatabase)
    }

    companion object {
        private const val DB_VERSION = 7
        private const val DB_NAME = "timetabledb"
        private const val TIMETABLE = "timetable"
        private const val TIMETABLE_ODD = "timetable_odd"
        private const val WEEK_ID = "id"
        private const val WEEK_SUBJECT = "subject"
        private const val WEEK_FRAGMENT = "fragment"
        private const val WEEK_TEACHER = "teacher"
        private const val WEEK_ROOM = "room"
        private const val WEEK_FROM_TIME = "fromtime"
        private const val WEEK_TO_TIME = "totime"
        private const val WEEK_COLOR = "color"
        private const val HOMEWORKS = "homeworks"
        private const val HOMEWORKS_ID = "id"
        private const val HOMEWORKS_SUBJECT = "subject"
        private const val HOMEWORKS_DESCRIPTION = "description"
        private const val HOMEWORKS_DATE = "date"
        private const val HOMEWORKS_COLOR = "color"
        private const val NOTES = "notes"
        private const val NOTES_ID = "id"
        private const val NOTES_TITLE = "title"
        private const val NOTES_TEXT = "text"
        private const val NOTES_COLOR = "color"
        private const val TEACHERS = "teachers"
        private const val TEACHERS_ID = "id"
        private const val TEACHERS_NAME = "name"
        private const val TEACHERS_POST = "post"
        private const val TEACHERS_PHONE_NUMBER = "phonenumber"
        private const val TEACHERS_EMAIL = "email"
        private const val TEACHERS_COLOR = "color"
        private const val EXAMS = "exams"
        private const val EXAMS_ID = "id"
        private const val EXAMS_SUBJECT = "subject"
        private const val EXAMS_TEACHER = "teacher"
        private const val EXAMS_ROOM = "room"
        private const val EXAMS_DATE = "date"
        private const val EXAMS_TIME = "time"
        private const val EXAMS_COLOR = "color"
        fun getDBName(selectedProfile: Int): String {
            val dbName: String
            dbName =
                if (selectedProfile == 0) DB_NAME //If the app was installed before the profiles were added
                else DB_NAME + "_" + selectedProfile
            return dbName
        }
    }
}