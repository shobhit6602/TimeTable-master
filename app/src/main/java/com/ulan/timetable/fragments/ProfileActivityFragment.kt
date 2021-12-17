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
import androidx.fragment.app.Fragment
import com.ulan.timetable.fragments.TimeSettingsFragment
import com.ulan.timetable.profiles.Profile

class ProfileActivityFragment : Fragment() {
    private var adapter: ProfileListAdapter? = null
    private var preferredProfilePos = ProfileManagement.getPreferredProfilePosition()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        adapter = ProfileListAdapter(requireContext(), 0)
        (root.findViewById<View>(R.id.profile_list) as ListView).adapter = adapter
        requireActivity().findViewById<View>(R.id.fab)
            .setOnClickListener { v: View? -> openAddDialog() }
        return root
    }

    private inner class ProfileListAdapter internal constructor(con: Context, resource: Int) :
        ArrayAdapter<Array<String?>?>(con, resource) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_profiles_entry, null)
            }
            return if (position < ProfileManagement.getSize()) generateProfileView(
                convertView!!,
                position
            ) else generateInfoView(
                convertView!!
            )
        }

        override fun getCount(): Int {
            return ProfileManagement.getSize() + 1
        }

        private fun generateProfileView(base: View, position: Int): View {
            val p = ProfileManagement.getProfile(position)
            val name = base.findViewById<TextView>(R.id.profilelist_name)
            name.textSize = 20f
            name.text = p.name
            val edit = base.findViewById<ImageButton>(R.id.profilelist_edit)
            edit.visibility = View.VISIBLE
            edit.setOnClickListener { v: View? -> openEditDialog(position) }
            val delete = base.findViewById<ImageButton>(R.id.profilelist_delete)
            delete.visibility = View.VISIBLE
            delete.setOnClickListener { v: View? -> openDeleteDialog(position) }
            val star = base.findViewById<ImageButton>(R.id.profilelist_preferred)
            star.visibility = View.VISIBLE
            if (position == preferredProfilePos) {
                star.setImageResource(R.drawable.ic_star_black_24dp)
            } else {
                star.setImageResource(R.drawable.ic_star_border_black_24dp)
            }
            star.setOnClickListener { v: View? -> setPreferredProfile(position) }
            return base
        }

        private fun generateInfoView(base: View): View {
            base.findViewById<View>(R.id.profilelist_edit).visibility = View.GONE
            base.findViewById<View>(R.id.profilelist_delete).visibility = View.GONE
            base.findViewById<View>(R.id.profilelist_preferred).visibility =
                View.GONE
            val name = base.findViewById<TextView>(R.id.profilelist_name)
            name.text = getString(string.preferred_profile_explanation)
            name.textSize = 12f
            return base
        }
    }

    private fun openAddDialog() {
        val builder = MaterialDialog.Builder(requireActivity())
        builder.title(getString(string.profiles_add))

        // Set up the input
        val input = EditText(requireContext())
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.hint = getString(string.name)
        builder.customView(input, true)

        // Set up the buttons
        builder.onPositive { dialog: MaterialDialog?, which: DialogAction? ->
            //Add Profile
            val inputText = input.text.toString()
            if (!inputText.trim { it <= ' ' }.isEmpty()) ProfileManagement.addProfile(
                Profile(
                    inputText
                )
            )
            adapter!!.notifyDataSetChanged()
        }
        builder.onNegative { dialog: MaterialDialog, which: DialogAction? -> dialog.dismiss() }
        builder.positiveText(string.add)
        builder.negativeText(string.cancel)
        builder.show()
    }

    private fun openEditDialog(position: Int) {
        val builder = MaterialDialog.Builder(requireContext())
        builder.title(getString(string.profiles_edit))

        // Set up the input
        val base = LinearLayout(requireContext())
        base.orientation = LinearLayout.VERTICAL
        val name = EditText(requireContext())
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        name.inputType = InputType.TYPE_CLASS_TEXT
        name.setText(ProfileManagement.getProfile(position).name)
        name.setHint(string.name)
        base.addView(name)
        builder.customView(base, true)

        // Set up the buttons
        builder.positiveText(getString(string.ok))
        builder.negativeText(getString(string.cancel))
        builder.onPositive { dialog: MaterialDialog?, which: DialogAction? ->
            val profile = ProfileManagement.getProfile(position)
            val nameText = name.text.toString()
            //Do not enter empty text
            ProfileManagement.editProfile(position, Profile(if (nameText.trim { it <= ' ' }
                    .isEmpty()) profile.name else nameText))
            adapter!!.notifyDataSetChanged()
        }
        builder.onNegative { dialog: MaterialDialog, which: DialogAction? -> dialog.dismiss() }
        builder.show()
    }

    private fun openDeleteDialog(position: Int) {
        val p = ProfileManagement.getProfile(position)
        MaterialDialog.Builder(requireContext())
            .title(getString(string.profiles_delete_submit_heading))
            .content(getString(string.profiles_delete_message, p.name))
            .positiveText(getString(string.yes))
            .onPositive { dialog: MaterialDialog?, which: DialogAction? ->
                ProfileManagement.removeProfile(position)
                val dbHelper = DbHelper(context)
                dbHelper.deleteAll()
                adapter!!.notifyDataSetChanged()
            }
            .onNegative { dialog: MaterialDialog, which: DialogAction? -> dialog.dismiss() }
            .negativeText(getString(string.no))
            .show()
    }

    private fun setPreferredProfile(position: Int) {
        ProfileManagement.setPreferredProfilePosition(position)
        preferredProfilePos = ProfileManagement.getPreferredProfilePosition()
        adapter!!.notifyDataSetChanged()
    }
}