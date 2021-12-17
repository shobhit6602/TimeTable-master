package com.ulan.timetable.appwidget

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
import android.view.ViewGroup
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
import androidx.preference.SwitchPreferenceCompat
import com.ulan.timetable.receivers.DailyReceiver
import android.view.View
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

/**
 * From https://github.com/SubhamTyagi/TimeTable
 */
class AppWidgetConfigureActivity : Activity(), View.OnClickListener, OnSeekBarChangeListener,
    RadioGroup.OnCheckedChangeListener {
    private var mAppWidgetId = 0
    private var mRgBgColor: RadioGroup? = null
    private var mRgTimeStyle: RadioGroup? = null
    private var mSbIntensity: SeekBar? = null
    private var mTvIntensity: TextView? = null
    private var mTvTimeStyle: TextView? = null
    private var selectedProfile = 0
    public override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(PreferenceUtil.getGeneralTheme(this))
        super.onCreate(savedInstanceState)
        setResult(RESULT_CANCELED)
        val extras = intent.extras
        if (extras == null) {
            finish()
            return
        }
        mAppWidgetId = extras.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }
        setContentView(R.layout.activity_appwidget_configure)
        initView()
        setListener()
        val configMap = AppWidgetDao.getAppWidgetConfig(mAppWidgetId, applicationContext)
        configMap?.let { setConfig(it) }
    }

    private fun initView() {
        mRgBgColor = findViewById(R.id.rg_bg_color)
        mTvIntensity = findViewById(R.id.tv_intensity)
        mSbIntensity = findViewById(R.id.sb_intensity)
        mRgTimeStyle = findViewById(R.id.rg_time_style)
        mTvTimeStyle = findViewById(R.id.tv_time_style)
        ProfileManagement.initProfiles(this)
        val listView = findViewById<ListView>(R.id.widget_creation_profile_list)
        listView.adapter = ProfileListAdapter(this)
    }

    private fun setListener() {
        findViewById<View>(R.id.btn_confirm).setOnClickListener(this)
        findViewById<View>(R.id.btn_cancel).setOnClickListener(this)
        mSbIntensity!!.setOnSeekBarChangeListener(this)
        mRgTimeStyle!!.setOnCheckedChangeListener(this)
    }

    private fun setConfig(configMap: Map<String?, Int?>) {
        val backgroundColor = configMap["backgroundColor"]
        if (backgroundColor != null && backgroundColor != -1) {
            val r = ((backgroundColor shr 16 and 0xff) / 255.0f * 100).toInt()
            if (r == 0) {
                mRgBgColor!!.check(R.id.rb_black)
            } else {
                mRgBgColor!!.check(R.id.rb_white)
            }
            val a = Math.round((backgroundColor shr 24 and 0xff) / 255.0f * 100)
            mSbIntensity!!.progress = a
        }
        val timeStyle = configMap["timeStyle"]
        if (timeStyle != null && timeStyle != -1) {
            when (timeStyle) {
                AppWidgetConstants.TIME_STYLE_SECOND -> mRgTimeStyle!!.check(R.id.rb_time_style_2)
                AppWidgetConstants.TIME_STYLE_THIRD -> mRgTimeStyle!!.check(R.id.rb_time_style_3)
                AppWidgetConstants.TIME_STYLE_FIRST -> mRgTimeStyle!!.check(R.id.rb_time_style_1)
                else -> {
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_cancel -> finish()
            R.id.btn_confirm -> {
                val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
                DayAppWidgetProvider.Companion.updateAppWidgetConfig(
                    appWidgetManager,
                    mAppWidgetId,
                    settingColor,
                    timeStyle,
                    profile,
                    this
                )
                val resultValue = Intent()
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId)
                setResult(RESULT_OK, resultValue)
                finish()
            }
        }
    }

    val settingColor: Int
        get() {
            val progress = mSbIntensity!!.progress
            val alpha = progress * 255 / 100
            return if (mRgBgColor!!.checkedRadioButtonId == R.id.rb_black) {
                Color.argb(alpha, 0, 0, 0)
            } else {
                Color.argb(alpha, 255, 255, 255)
            }
        }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (seekBar.id == R.id.sb_intensity) {
            mTvIntensity!!.text = getString(string.app_widget_configure_intensity, progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        when (checkedId) {
            R.id.rb_time_style_1 -> mTvTimeStyle!!.text =
                getString(string.app_widget_configure_time_style, "Morning section")
            R.id.rb_time_style_2 -> mTvTimeStyle!!.text =
                getString(string.app_widget_configure_time_style, "Previous")
            R.id.rb_time_style_3 -> mTvTimeStyle!!.text =
                getString(string.app_widget_configure_time_style, "Upper 1")
        }
    }

    val timeStyle: Int
        get() = when (mRgTimeStyle!!.checkedRadioButtonId) {
            R.id.rb_time_style_2 -> AppWidgetConstants.TIME_STYLE_SECOND
            R.id.rb_time_style_3 -> AppWidgetConstants.TIME_STYLE_THIRD
            R.id.rb_time_style_1 -> AppWidgetConstants.TIME_STYLE_FIRST
            else -> AppWidgetConstants.TIME_STYLE_FIRST
        }
    val profile: Int
        get() = if (selectedProfile < ProfileManagement.getSize() && selectedProfile > 0) selectedProfile else 0

    override fun onBackPressed() {
        finish()
    }

    internal inner class ProfileListAdapter(con: Context) : ArrayAdapter<Array<String?>?>(con, 0) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_profiles_entry, null)
            }
            return generateView(convertView!!, position)
        }

        override fun getCount(): Int {
            return ProfileManagement.getSize()
        }

        private fun generateView(base: View, position: Int): View {
            val p = ProfileManagement.getProfile(position)
            val name = base.findViewById<TextView>(R.id.profilelist_name)
            name.text = p.name
            val edit = base.findViewById<ImageButton>(R.id.profilelist_edit)
            edit.visibility = View.GONE
            val delete = base.findViewById<ImageButton>(R.id.profilelist_delete)
            delete.visibility = View.GONE
            val star = base.findViewById<ImageButton>(R.id.profilelist_preferred)
            if (position == selectedProfile) {
                star.setImageResource(R.drawable.ic_star_black_24dp)
            } else {
                star.setImageResource(R.drawable.ic_star_border_black_24dp)
            }
            star.setOnClickListener { v: View? ->
                if (selectedProfile == position) {
                    selectedProfile = -1
                    star.setImageResource(R.drawable.ic_star_border_black_24dp)
                } else {
                    selectedProfile = position
                    star.setImageResource(R.drawable.ic_star_black_24dp)
                }
                notifyDataSetChanged()
            }
            return base
        }
    }
}