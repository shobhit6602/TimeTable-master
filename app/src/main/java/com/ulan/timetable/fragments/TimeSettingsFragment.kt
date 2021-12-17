package com.ulan.timetable.fragments

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
import android.content.ActivityNotFoundException
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
import android.content.BroadcastReceiver
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
import androidx.preference.Preference
import com.ulan.timetable.fragments.TimeSettingsFragment
import java.util.*

class TimeSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
        setPreferencesFromResource(R.xml.settings_time, rootKey)
        var myPref = findPreference<Preference>("start_time")
        Objects.requireNonNull(myPref).onPreferenceClickListener =
            Preference.OnPreferenceClickListener { p: Preference ->
                val oldTimes = PreferenceUtil.getStartTime(
                    context
                )
                val timePickerDialog = TimePickerDialog(activity,
                    { view: TimePicker?, hourOfDay: Int, minute: Int ->
                        PreferenceUtil.setStartTime(
                            context, hourOfDay, minute, 0
                        )
                        p.summary = "$hourOfDay:$minute"
                    }, oldTimes[0], oldTimes[1], true
                )
                timePickerDialog.setTitle(string.start_of_school)
                timePickerDialog.show()
                true
            }
        val oldTimes = PreferenceUtil.getStartTime(context)
        myPref!!.summary = oldTimes[0].toString() + ":" + oldTimes[1]
        myPref = findPreference("set_period_length")
        Objects.requireNonNull(myPref).onPreferenceClickListener =
            Preference.OnPreferenceClickListener { p: Preference ->
                val numberPicker = NumberPicker(context)
                numberPicker.maxValue = 180
                numberPicker.minValue = 1
                numberPicker.value = PreferenceUtil.getPeriodLength(context)
                MaterialDialog.Builder(requireContext())
                    .customView(numberPicker, false)
                    .positiveText(string.select)
                    .onPositive { d: MaterialDialog?, w: DialogAction? ->
                        val value = numberPicker.value
                        PreferenceUtil.setPeriodLength(context, value)
                        p.summary = value.toString() + " " + getString(string.minutes)
                    }
                    .show()
                true
            }
        myPref!!.summary = PreferenceUtil.getPeriodLength(context).toString() + " " + getString(
            string.minutes
        )
        myPref = findPreference("two_weeks")
        Objects.requireNonNull(myPref).onPreferenceClickListener =
            Preference.OnPreferenceClickListener { p: Preference? ->
                setTermStartVisibility()
                true
            }
        setTermStartVisibility()
        myPref = findPreference("term_start")
        val calendar = PreferenceUtil.getTermStart(requireContext())
        Objects.requireNonNull(myPref).title =
            getString(string.start_of_term) + " (" + WeekUtils.localizeDate(
                requireContext(),
                Date(calendar.timeInMillis)
            ) + ")"
        myPref!!.onPreferenceClickListener = Preference.OnPreferenceClickListener { p: Preference ->
            val calendar2 = PreferenceUtil.getTermStart(requireContext())
            val mYear2 = calendar2[Calendar.YEAR]
            val mMonth2 = calendar2[Calendar.MONTH]
            val mDayofMonth2 = calendar2[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(
                requireActivity(),
                { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    PreferenceUtil.setTermStart(requireContext(), year, month, dayOfMonth)
                    val cal = Calendar.getInstance()
                    cal[Calendar.YEAR] = year
                    cal[Calendar.MONTH] = month
                    cal[Calendar.DAY_OF_MONTH] = dayOfMonth
                    p.title = getString(string.start_of_term) + " (" + WeekUtils.localizeDate(
                        requireContext(),
                        Date(cal.timeInMillis)
                    ) + ")"
                },
                mYear2,
                mMonth2,
                mDayofMonth2
            )
            datePickerDialog.setTitle(string.choose_date)
            datePickerDialog.show()
            true
        }
    }

    private fun setTermStartVisibility() {
        findPreference<Preference>("term_start")!!.isVisible =
            PreferenceUtil.isTwoWeeksEnabled(requireContext())
    }
}