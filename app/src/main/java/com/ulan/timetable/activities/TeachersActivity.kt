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
import com.ulan.timetable.model.Homework
import com.ulan.timetable.model.Teacher
import com.ulan.timetable.model.Exam
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
import android.view.*
import android.widget.*
import androidx.preference.SwitchPreferenceCompat
import com.ulan.timetable.receivers.DailyReceiver
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
import java.util.*

class TeachersActivity : AppCompatActivity() {
    private val context: Context = this
    private var listView: ListView? = null
    private var db: DbHelper? = null
    private var adapter: TeachersAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(PreferenceUtil.getGeneralTheme(this))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teachers)
        initAll()
    }

    private fun initAll() {
        setupAdapter()
        setupListViewMultiSelect()
        setupCustomDialog()
    }

    private fun setupAdapter() {
        db = DbHelper(context)
        listView = findViewById(R.id.teacherlist)
        adapter = TeachersAdapter(
            db!!,
            this@TeachersActivity,
            listView,
            R.layout.listview_teachers_adapter,
            db.getTeacher()
        )
        listView.setAdapter(adapter)
    }

    private fun setupListViewMultiSelect() {
        val coordinatorLayout = findViewById<CoordinatorLayout>(R.id.coordinatorTeachers)
        listView!!.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
        listView!!.setMultiChoiceModeListener(object : MultiChoiceModeListener {
            override fun onItemCheckedStateChanged(
                mode: ActionMode,
                position: Int,
                id: Long,
                checked: Boolean
            ) {
                val checkedCount = listView!!.checkedItemCount
                mode.title = checkedCount.toString() + " " + resources.getString(string.selected)
                if (checkedCount == 0) mode.finish()
            }

            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                val menuInflater = mode.menuInflater
                menuInflater.inflate(R.menu.toolbar_action_mode, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.action_delete -> {
                        val removelist = ArrayList<Teacher>()
                        val checkedItems = listView!!.checkedItemPositions
                        var i = 0
                        while (i < checkedItems.size()) {
                            val key = checkedItems.keyAt(i)
                            if (checkedItems[key]) {
                                db!!.deleteTeacherById(Objects.requireNonNull(adapter!!.getItem(key)))
                                removelist.add(adapter.getTeacherList()[key])
                            }
                            i++
                        }
                        adapter.getTeacherList().removeAll(removelist)
                        db!!.updateTeacher(adapter.getTeacher())
                        adapter!!.notifyDataSetChanged()
                        mode.finish()
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode) {}
        })
    }

    private fun setupCustomDialog() {
        val alertLayout = layoutInflater.inflate(R.layout.dialog_add_teacher, null)
        AlertDialogsHelper.getAddTeacherDialog(db, this@TeachersActivity, alertLayout, adapter!!)
    }
}