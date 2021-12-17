package com.ulan.timetable.appwidget.Dao

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
import android.database.Cursor
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
import java.util.HashMap

/**
 * From https://github.com/SubhamTyagi/TimeTable
 */
object AppWidgetDao : BaseDao() {
    private const val TABLE_NAME = "app_widget"
    fun saveAppWidgetConfig(
        appWidgetId: Int,
        backgroundColor: Int,
        timeStyle: Int,
        profile: Int,
        context: Context?
    ) {
        val db = DBManager.getDb(context)
        val values = ContentValues(5)
        values.put("backgroundColor", backgroundColor)
        values.put("timeStyle", timeStyle)
        values.put("profilePosition", profile)
        val whereClause = "appWidgetId = ?"
        val whereArgs = arrayOf(appWidgetId.toString())
        val number: Int = BaseDao.Companion.update(db!!, TABLE_NAME, values, whereClause, whereArgs)
        if (number == 0) {
            values.put("appWidgetId", appWidgetId)
            BaseDao.Companion.insert(db, TABLE_NAME, values)
        }
        DBManager.close(db)
    }

    fun getAppWidgetConfig(appWidgetId: Int, context: Context?): Map<String?, Int?>? {
        val db = DBManager.getDb(context)
        val selection = "appWidgetId = ?"
        val selectionArgs = arrayOf<String?>(appWidgetId.toString())
        val columns = arrayOf("backgroundColor", "timeStyle", "weekStyle")
        val cursor: Cursor = BaseDao.Companion.queryComplex(
            db!!,
            TABLE_NAME,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null,
            null
        )
        val count = cursor.count
        if (count == 0) {
            cursor.close()
            return null
        }
        var configMap: MutableMap<String?, Int?>? = null
        if (cursor.moveToNext()) { // id只存在一个，所以不用while
            configMap = HashMap()
            configMap["backgroundColor"] = cursor.getInt(cursor.getColumnIndex("backgroundColor"))
            configMap["timeStyle"] = cursor.getInt(cursor.getColumnIndex("timeStyle"))
            configMap["weekStyle"] = cursor.getInt(cursor.getColumnIndex("weekStyle"))
        }
        cursor.close()
        return configMap
    }

    fun getAppWidgetBackgroundColor(appWidgetId: Int, defaultColor: Int, context: Context?): Int {
        val db = DBManager.getDb(context)
        val selection = "appWidgetId = ?"
        val selectionArgs = arrayOf<String?>(appWidgetId.toString())
        val columns = arrayOf("backgroundColor")
        val cursor: Cursor = BaseDao.Companion.queryComplex(
            db!!,
            TABLE_NAME,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null,
            null
        )
        val count = cursor.count
        if (count == 0) {
            cursor.close()
            return defaultColor
        }
        val backgroundColorIndex = cursor.getColumnIndex("backgroundColor")
        val backgroundColor: Int
        backgroundColor = if (cursor.moveToNext()) { // id只存在一个，所以不用while
            cursor.getInt(backgroundColorIndex)
        } else {
            defaultColor
        }
        cursor.close()
        return backgroundColor
    }

    fun getAppWidgetTimeStyle(appWidgetId: Int, defaultTimeStyle: Int, context: Context?): Int {
        val db = DBManager.getDb(context)
        val selection = "appWidgetId = ?"
        val selectionArgs = arrayOf<String?>(appWidgetId.toString())
        val columns = arrayOf("timeStyle")
        val cursor: Cursor = BaseDao.Companion.queryComplex(
            db!!,
            TABLE_NAME,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null,
            null
        )
        val count = cursor.count
        if (count == 0) {
            cursor.close()
            return defaultTimeStyle
        }
        val timeStyleIndex = cursor.getColumnIndex("timeStyle")
        val timeStyle: Int
        timeStyle = if (cursor.moveToNext()) { // id只存在一个，所以不用while
            cursor.getInt(timeStyleIndex)
        } else {
            defaultTimeStyle
        }
        cursor.close()
        return timeStyle
    }

    fun saveAppWidgetCurrentTime(appWidgetId: Int, currentTime: Long, context: Context?) {
        val db = DBManager.getDb(context)
        val values = ContentValues(2)
        values.put("currentTime", currentTime)
        val whereClause = "appWidgetId = ?"
        val whereArgs = arrayOf(appWidgetId.toString())
        val number: Int = BaseDao.Companion.update(db!!, TABLE_NAME, values, whereClause, whereArgs)
        if (number == 0) {
            // 使用insertOrReplace会重置其他列的数据
            values.put("appWidgetId", appWidgetId)
            BaseDao.Companion.insert(db, TABLE_NAME, values)
        }
        DBManager.close(db)
    }

    fun getAppWidgetCurrentTime(appWidgetId: Int, defaultTime: Long, context: Context?): Long {
        val db = DBManager.getDb(context)
        val selection = "appWidgetId = ?"
        val selectionArgs = arrayOf<String?>(appWidgetId.toString())
        val columns = arrayOf("currentTime")
        val cursor: Cursor = BaseDao.Companion.queryComplex(
            db!!,
            TABLE_NAME,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null,
            null
        )
        val count = cursor.count
        if (count == 0) {
            cursor.close()
            return defaultTime
        }
        val currentTimeIndex = cursor.getColumnIndex("currentTime")
        var currentTime: Long = 0
        if (cursor.moveToNext()) { // id只存在一个，所以不用while
            currentTime = cursor.getLong(currentTimeIndex)
        }
        if (currentTime == 0L) {
            currentTime = defaultTime
        }
        cursor.close()
        return currentTime
    }

    fun getAppWidgetProfile(appWidgetId: Int, defaultProfile: Int, context: Context?): Int {
        val db = DBManager.getDb(context)
        val selection = "appWidgetId = ?"
        val selectionArgs = arrayOf<String?>(appWidgetId.toString())
        val columns = arrayOf("profilePosition")
        val cursor: Cursor = BaseDao.Companion.queryComplex(
            db!!,
            TABLE_NAME,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null,
            null
        )
        val count = cursor.count
        if (count == 0) {
            cursor.close()
            return defaultProfile
        }
        val currentProfileIndex = cursor.getColumnIndex("profilePosition")
        var currentProfile = 0
        if (cursor.moveToNext()) {
            currentProfile = cursor.getInt(currentProfileIndex)
        }
        if (currentProfile == 0) {
            currentProfile = defaultProfile
        }
        cursor.close()
        return currentProfile
    }

    fun deleteAppWidget(appWidgetId: Int, context: Context?) {
        val db = DBManager.getDb(context)
        BaseDao.Companion.delete(
            db!!,
            TABLE_NAME,
            "appWidgetId = ?",
            arrayOf(appWidgetId.toString())
        )
        DBManager.close(db)
    }

    fun clear(context: Context?) {
        val db = DBManager.getDb(context)
        BaseDao.Companion.delete(db!!, TABLE_NAME, null, null)
        DBManager.close(db)
    }
}