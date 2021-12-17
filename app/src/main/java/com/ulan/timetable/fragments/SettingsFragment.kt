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
import com.ulan.timetable.fragments.SettingsFragment
import com.ulan.timetable.activities.TimeSettingsActivity
import com.afollestad.materialdialogs.MaterialDialog.ListCallbackMultiChoice
import android.text.InputType
import com.ulan.timetable.receivers.DailyReceiver
import android.content.BroadcastReceiver
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
import androidx.preference.*
import com.ulan.timetable.fragments.TimeSettingsFragment
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        tintIcons(preferenceScreen, PreferenceUtil.getTextColorPrimary(requireContext()))
        setTurnOff()
        var myPref = findPreference<Preference>("automatic_do_not_disturb")
        Objects.requireNonNull(myPref).onPreferenceClickListener =
            Preference.OnPreferenceClickListener { p: Preference? ->
                PreferenceUtil.setDoNotDisturb(requireActivity(), false)
                setTurnOff()
                true
            }
        myPref!!.isVisible = ProfileManagement.isPreferredProfile()
        val mp = findPreference<ListPreference>("theme")
        Objects.requireNonNull(mp).onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any ->
                mp!!.value = newValue.toString() + ""
                requireActivity().recreate()
                false
            }
        mp!!.summary = themeName
        myPref = findPreference("time_settings")
        Objects.requireNonNull(myPref).onPreferenceClickListener =
            Preference.OnPreferenceClickListener { p: Preference? ->
                startActivity(Intent(activity, TimeSettingsActivity::class.java))
                true
            }
        showPreselectionElements()
        myPref = findPreference("is_preselection")
        Objects.requireNonNull(myPref).onPreferenceClickListener =
            Preference.OnPreferenceClickListener { p: Preference? ->
                showPreselectionElements()
                true
            }
        myPref = findPreference("preselection_elements")
        Objects.requireNonNull(myPref).onPreferenceClickListener =
            Preference.OnPreferenceClickListener { p: Preference? ->
                val preselectedValues =
                    ArrayList(Arrays.asList(*requireContext().resources.getStringArray(R.array.preselected_subjects_values)))
                val preselected = PreferenceUtil.getPreselectionElements(requireContext())
                val preselectedIndices: MutableList<Int> = ArrayList()
                for (i in preselected!!.indices) {
                    preselectedIndices.add(preselectedValues.indexOf(preselected[i]))
                }
                MaterialDialog.Builder(requireContext())
                    .title(string.set_preselection_elements)
                    .items(R.array.preselected_subjects)
                    .itemsCallbackMultiChoice(preselectedIndices.toArray<Int>(arrayOf<Int>())) { dialog: MaterialDialog?, which: Array<Int?>, text: Array<CharSequence?>? ->
                        val selection: MutableList<String> = ArrayList()
                        for (i in which.indices) {
                            selection.add(preselectedValues[which[i]!!])
                        }
                        PreferenceUtil.setPreselectionElements(
                            requireContext(),
                            selection.toArray<String>(arrayOf<String>())
                        )
                        true
                    }
                    .positiveText(string.ok)
                    .onPositive { dialog: MaterialDialog, which: DialogAction? -> dialog.dismiss() }
                    .negativeText(string.cancel)
                    .onNegative { dialog: MaterialDialog, action: DialogAction? -> dialog.dismiss() }
                    .neutralText(string.de_select_all)
                    .onNeutral { dialog: MaterialDialog, action: DialogAction? ->
                        val selection = dialog.selectedIndices
                        if (Objects.requireNonNull(selection).length == 0) {
                            val select = arrayOfNulls<Int>(preselectedValues.size)
                            for (i in select.indices) {
                                select[i] = i
                            }
                            dialog.setSelectedIndices(select)
                        } else {
                            dialog.setSelectedIndices(arrayOf())
                        }
                    }
                    .autoDismiss(false)
                    .show()
                true
            }
    }

    private val themeName: String
        private get() {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val selectedTheme = sharedPreferences.getString("theme", "switch")
            val values = resources.getStringArray(R.array.theme_array_values)
            val names = resources.getStringArray(R.array.theme_array)
            for (i in values.indices) {
                if (values[i].equals(selectedTheme, ignoreCase = true)) {
                    return names[i]
                }
            }
            return ""
        }

    private fun setTurnOff() {
        val show =
            PreferenceUtil.isAutomaticDoNotDisturb(requireContext()) && ProfileManagement.isPreferredProfile()
        findPreference<Preference>("do_not_disturb_turn_off")!!.isVisible = show
    }

    private fun showPreselectionElements() {
        val show = PreferenceUtil.isPreselectionList(requireContext())
        findPreference<Preference>("preselection_elements")!!.isVisible = show
    }

    companion object {
        private fun tintIcons(preference: Preference, color: Int) {
            if (preference is PreferenceGroup) {
                val group = preference
                for (i in 0 until group.preferenceCount) {
                    tintIcons(group.getPreference(i), color)
                }
            } else {
                val icon = preference.icon
                if (icon != null) {
                    DrawableCompat.setTint(icon, color)
                }
            }
        }
    }
}