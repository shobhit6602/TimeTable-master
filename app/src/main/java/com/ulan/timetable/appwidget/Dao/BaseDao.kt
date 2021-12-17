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
import android.view.MenuInflater
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
import android.database.Cursor
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

/**
 * From https://github.com/SubhamTyagi/TimeTable
 */
open class BaseDao {
    companion object {
        /**
         * 插入
         *
         * @param db            数据库
         * @param tableName     要操作的表名
         * @param contentValues 表中个字段的键值对（键的名字要和表中字段名一致，区分大小写）
         */
        fun insert(db: SQLiteDatabase, tableName: String?, contentValues: ContentValues?) {
            db.insert(tableName, null, contentValues)
        }

        /**
         * 插入或替换
         * 约束值不存在，进行插入
         * 约束值存在，新数据替换掉旧数据
         * 约束值：建表时UNIQUE规定的字段
         *
         * @param db            数据库
         * @param tableName     要操作的表名
         * @param contentValues 表中个字段的键值对（键的名字要和表中字段名一致，区分大小写）
         */
        fun insertOrReplace(db: SQLiteDatabase, tableName: String?, contentValues: ContentValues?) {
            db.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
        }

        /**
         * 插入或替换
         * 约束值不存在，进行插入
         * 约束值存在，则忽略
         * 约束值：建表时UNIQUE规定的字段
         *
         * @param db            数据库
         * @param tableName     要操作的表名
         * @param contentValues 表中个字段的键值对（键的名字要和表中字段名一致，区分大小写）
         */
        fun insertOrIgnore(db: SQLiteDatabase, tableName: String?, contentValues: ContentValues?) {
            db.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE)
        }

        /**
         * 删除
         *
         * @param db          数据库
         * @param tableName   要操作的表名
         * @param whereClause where条件
         * @param whereArgs   where条件语句中的字段值
         */
        fun delete(
            db: SQLiteDatabase,
            tableName: String?,
            whereClause: String?,
            whereArgs: Array<String>?
        ) {
            db.delete(tableName, whereClause, whereArgs)
        }

        fun update(
            db: SQLiteDatabase,
            tableName: String?,
            values: ContentValues?,
            whereClause: String?,
            whereArgs: Array<String>?
        ): Int {
            return db.update(tableName, values, whereClause, whereArgs)
        }

        /**
         * 查询
         *
         * @param db            数据库
         * @param tableName     要操作的表名
         * @param selection     条件语句，where部分
         * @param selectionArgs where语句中的字段值
         */
        fun query(
            db: SQLiteDatabase,
            tableName: String?,
            selection: String?,
            selectionArgs: Array<String?>?
        ): Cursor {
            return queryComplex(
                db,
                tableName,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null,
                null
            )
        }

        /**
         * 复杂查询
         *
         * @param db            数据库
         * @param tableName     要操作的表名
         * @param columns       要获取的字段数组
         * @param selection     条件语句，where部分
         * @param selectionArgs where语句中的字段值
         * @param groupBy       与SQL中的group by语句一样
         * @param having        group bu中的having语句
         * @param orderBy       SQL中的排序语句
         * @param limit         限制返回的数据与偏移量
         */
        fun queryComplex(
            db: SQLiteDatabase,
            tableName: String?,
            columns: Array<String>?,
            selection: String?,
            selectionArgs: Array<String?>?,
            groupBy: String?,
            having: String?,
            orderBy: String?,
            limit: String?
        ): Cursor {
            return db.query(
                tableName,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy,
                limit
            )
        }
    }
}