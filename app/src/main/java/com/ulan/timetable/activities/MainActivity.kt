package com.ulan.timetable.activities

import android.Manifest
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
import android.net.Uri
import com.ulan.timetable.appwidget.DayAppWidgetService
import com.ulan.timetable.appwidget.DayAppWidgetProvider
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
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
import androidx.appcompat.app.ActionBarDrawerToggle
import com.ulan.timetable.activities.NoteInfoActivity
import com.ulan.timetable.fragments.ProfileActivityFragment
import me.yaoandy107.ntut_timetable.CourseTableLayout
import me.yaoandy107.ntut_timetable.model.StudentCourse
import me.yaoandy107.ntut_timetable.model.CourseInfo
import com.ulan.timetable.activities.SummaryActivity.CustomCourseInfo
import com.github.tlaabs.timetableview.Schedule
import com.ulan.timetable.activities.SummaryActivity.CustomSchedule
import com.github.tlaabs.timetableview.TimetableView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.preference.PreferenceManager
import com.ulan.timetable.fragments.TimeSettingsFragment
import java.io.File
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var adapter: FragmentsTabAdapter? = null
    private var viewPager: ViewPager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(PreferenceUtil.getGeneralThemeNoActionBar(this))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ProfileManagement.initProfiles(this)
        if (Build.VERSION.SDK_INT >= 25) {
            createShortcuts(this)
        }
        if (!PreferenceUtil.hasStartActivityBeenShown(this)) {
            MaterialDialog.Builder(this)
                .content(string.first_start_setup)
                .positiveText(string.ok)
                .onPositive { v: MaterialDialog?, w: DialogAction? ->
                    startActivity(
                        Intent(
                            this,
                            TimeSettingsActivity::class.java
                        )
                    )
                }
                .show()
        }
        initAll()
    }

    public override fun onStart() {
        super.onStart()
        setDoNotDisturbReceivers(this, false)
    }

    private fun initAll() {
        NotificationUtil.sendNotificationCurrentLesson(this, false)
        PreferenceUtil.setDoNotDisturb(this, PreferenceUtil.doNotDisturbDontAskAgain(this))
        initSpinner()
        setupWeeksTV()
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val headerview = navigationView.getHeaderView(0)
        headerview.findViewById<View>(R.id.nav_header_main_settings)
            .setOnClickListener { v: View? ->
                startActivity(
                    Intent(
                        this,
                        SettingsActivity::class.java
                    )
                )
            }
        val title = headerview.findViewById<TextView>(R.id.nav_header_main_title)
        title.setText(string.app_name)
        val desc = headerview.findViewById<TextView>(R.id.nav_header_main_desc)
        desc.setText(string.nav_drawer_description)
        PreferenceManager.setDefaultValues(this, R.xml.settings, false)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, string.navigation_drawer_open, string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        setupFragments()
        setupCustomDialog()
    }

    private var dontfire = true
    private fun initSpinner() {
        //Set Profiles
        val parentSpinner = findViewById<Spinner>(R.id.profile_spinner)
        if (ProfileManagement.isMoreThanOneProfile()) {
            parentSpinner.visibility = View.VISIBLE
            dontfire = true
            val list: MutableList<String?> = ProfileManagement.getProfileListNames()
            list.add(getString(string.profiles_edit))
            val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            parentSpinner.adapter = dataAdapter
            parentSpinner.setSelection(ProfileManagement.getSelectedProfilePosition())
            parentSpinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (dontfire) {
                        dontfire = false
                        return
                    }
                    val item = parent.getItemAtPosition(position).toString()
                    if (item == getString(string.profiles_edit)) {
                        val intent = Intent(baseContext, ProfileActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        //Change profile position
                        ProfileManagement.setSelectedProfile(position)
                        startActivity(Intent(baseContext, MainActivity::class.java))
                        finish()
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
        } else {
            parentSpinner.visibility = View.GONE
        }
    }

    private fun setupWeeksTV() {
        val weekView = findViewById<TextView>(R.id.main_week_tV)
        if (PreferenceUtil.isTwoWeeksEnabled(this)) {
            weekView.visibility = View.VISIBLE
            if (PreferenceUtil.isEvenWeek(
                    this,
                    Calendar.getInstance()
                )
            ) weekView.setText(string.even_week) else weekView.setText(
                string.odd_week
            )
        } else weekView.visibility = View.GONE
    }

    private fun setupFragments() {
        adapter = FragmentsTabAdapter(supportFragmentManager)
        viewPager = findViewById(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val mondayFragment = WeekdayFragment(WeekdayFragment.Companion.KEY_MONDAY_FRAGMENT)
        val tuesdayFragment = WeekdayFragment(WeekdayFragment.Companion.KEY_TUESDAY_FRAGMENT)
        val wednesdayFragment = WeekdayFragment(WeekdayFragment.Companion.KEY_WEDNESDAY_FRAGMENT)
        val thursdayFragment = WeekdayFragment(WeekdayFragment.Companion.KEY_THURSDAY_FRAGMENT)
        val fridayFragment = WeekdayFragment(WeekdayFragment.Companion.KEY_FRIDAY_FRAGMENT)
        val saturdayFragment = WeekdayFragment(WeekdayFragment.Companion.KEY_SATURDAY_FRAGMENT)
        val sundayFragment = WeekdayFragment(WeekdayFragment.Companion.KEY_SUNDAY_FRAGMENT)
        val startOnSunday = PreferenceUtil.isWeekStartOnSunday(this)
        val showWeekend = PreferenceUtil.isSevenDays(this)
        if (!startOnSunday) {
            adapter!!.addFragment(mondayFragment, resources.getString(string.monday))
            adapter!!.addFragment(tuesdayFragment, resources.getString(string.tuesday))
            adapter!!.addFragment(wednesdayFragment, resources.getString(string.wednesday))
            adapter!!.addFragment(thursdayFragment, resources.getString(string.thursday))
            adapter!!.addFragment(fridayFragment, resources.getString(string.friday))
            if (showWeekend) {
                adapter!!.addFragment(saturdayFragment, resources.getString(string.saturday))
                adapter!!.addFragment(sundayFragment, resources.getString(string.sunday))
            }
        } else {
            adapter!!.addFragment(sundayFragment, resources.getString(string.sunday))
            adapter!!.addFragment(mondayFragment, resources.getString(string.monday))
            adapter!!.addFragment(tuesdayFragment, resources.getString(string.tuesday))
            adapter!!.addFragment(wednesdayFragment, resources.getString(string.wednesday))
            adapter!!.addFragment(thursdayFragment, resources.getString(string.thursday))
            if (showWeekend) {
                adapter!!.addFragment(fridayFragment, resources.getString(string.friday))
                adapter!!.addFragment(saturdayFragment, resources.getString(string.saturday))
            }
        }
        viewPager.setAdapter(adapter)
        val day = fragmentChoosingDay
        if (startOnSunday) {
            viewPager.setCurrentItem(day - 1, true)
        } else {
            viewPager.setCurrentItem(if (day == 1) 6 else day - 2, true)
        }
        tabLayout.setupWithViewPager(viewPager)
    }//Calender.Saturday
    //1 = Calendar.Sunday, 2 = Calendar.Monday etc.

    //If Saturday/Sunday are hidden, switch to Monday
    //If its after 20 o'clock, show the next day
    private val fragmentChoosingDay: Int
        private get() {
            val calendar = Calendar.getInstance()
            var day = calendar[Calendar.DAY_OF_WEEK]
            val hour = calendar[Calendar.HOUR_OF_DAY]

            //If its after 20 o'clock, show the next day
            if (hour >= showNextDayAfterSpecificHour) {
                day++
            }
            if (day > 7) { //Calender.Saturday
                day = day - 7 //1 = Calendar.Sunday, 2 = Calendar.Monday etc.
            }
            val startOnSunday = PreferenceUtil.isWeekStartOnSunday(this)
            val showWeekend = PreferenceUtil.isSevenDays(this)

            //If Saturday/Sunday are hidden, switch to Monday
            if (!startOnSunday && !showWeekend && (day == Calendar.SATURDAY || day == Calendar.SUNDAY)) {
                day = Calendar.MONDAY
            } else if (startOnSunday && !showWeekend && (day == Calendar.FRIDAY || day == Calendar.SATURDAY)) {
                day = Calendar.SUNDAY
            }
            return day
        }

    private fun setupCustomDialog() {
        val alertLayout = layoutInflater.inflate(R.layout.dialog_add_subject, null)
        AlertDialogsHelper.getAddSubjectDialog(
            DbHelper(this),
            this@MainActivity,
            alertLayout,
            adapter!!,
            viewPager!!
        )
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        }
        ProfileManagement.resetSelectedProfile()
        finishAffinity()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            val settings = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(settings)
            finish()
        } else if (item.itemId == R.id.action_backup) {
            backup()
        } else if (item.itemId == R.id.action_restore) {
            restore()
        } else if (item.itemId == R.id.action_remove_all) {
            deleteAll()
        } else if (item.itemId == R.id.action_about_libs) {
            LibsBuilder()
                .withActivityTitle(getString(string.about_libs_title))
                .withAboutIconShown(true)
                .withFields(string::class.java.fields)
                .withLicenseShown(true)
                .withAboutDescription(getString(string.nav_drawer_description))
                .withAboutAppName(getString(string.app_name))
                .start(this)
        } else if (item.itemId == R.id.action_profiles) {
            val intent = Intent(baseContext, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.exams) {
            val exams = Intent(this@MainActivity, ExamsActivity::class.java)
            startActivity(exams)
        } else if (itemId == R.id.homework) {
            val homework = Intent(this@MainActivity, HomeworkActivity::class.java)
            startActivity(homework)
        } else if (itemId == R.id.notes) {
            val note = Intent(this@MainActivity, NotesActivity::class.java)
            startActivity(note)
        } else if (itemId == R.id.settings) {
            val settings = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(settings)
            finish()
        } else if (itemId == R.id.schoolwebsitemenu) {
            val schoolWebsite = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(SettingsActivity.Companion.KEY_SCHOOL_WEBSITE_SETTING, null)
            if (!TextUtils.isEmpty(schoolWebsite)) {
                openUrlInChromeCustomTab(schoolWebsite)
            } else {
                ChocoBar.builder().setActivity(this)
                    .setText(getString(string.please_set_school_website_url))
                    .setDuration(ChocoBar.LENGTH_LONG)
                    .red()
                    .show()
            }
        } else if (itemId == R.id.teachers) {
            val teacher = Intent(this@MainActivity, TeachersActivity::class.java)
            startActivity(teacher)
        } else if (itemId == R.id.summary) {
            val teacher = Intent(this@MainActivity, SummaryActivity::class.java)
            startActivity(teacher)
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun backup() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission(Runnable { backup() }, SheriffPermission.STORAGE)
            return
        }
        val path =
            Environment.getExternalStoragePublicDirectory(if (Build.VERSION.SDK_INT >= 19) Environment.DIRECTORY_DOCUMENTS else Environment.DIRECTORY_DOWNLOADS)
                .toString()
        //        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd");
//        String filename = timeStampFormat.format(new Date());
        val activity: AppCompatActivity = this
        val folder = File(path)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val sqliteToExcel = SQLiteToExcel(
            this,
            DbHelper.Companion.getDBName(ProfileManagement.getSelectedProfilePosition()),
            path
        )
        sqliteToExcel.exportAllTables(backup_filename, object : ExportListener {
            override fun onStart() {}
            override fun onCompleted(filePath: String) {
                runOnUiThread {
                    ChocoBar.builder().setActivity(activity)
                        .setText(
                            getString(
                                string.backup_successful,
                                if (Build.VERSION.SDK_INT >= 19) getString(
                                    string.Documents
                                ) else getString(string.Downloads)
                            )
                        )
                        .setDuration(ChocoBar.LENGTH_LONG)
                        .setIcon(R.drawable.ic_baseline_save_24)
                        .green()
                        .show()
                }
            }

            override fun onError(e: Exception) {
                runOnUiThread {
                    ChocoBar.builder().setActivity(activity)
                        .setText(getString(string.backup_failed) + ": " + e.toString())
                        .setDuration(ChocoBar.LENGTH_LONG)
                        .red()
                        .show()
                }
            }
        })
    }

    fun restore() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission(Runnable { restore() }, SheriffPermission.STORAGE)
            return
        }
        val path =
            Environment.getExternalStoragePublicDirectory(if (Build.VERSION.SDK_INT >= 19) Environment.DIRECTORY_DOCUMENTS else Environment.DIRECTORY_DOWNLOADS)
                .toString() + File.separator + backup_filename
        val file = File(path)
        if (!file.exists()) {
            ChocoBar.builder().setActivity(this)
                .setText(
                    getString(
                        string.no_backup_found_in_downloads,
                        if (Build.VERSION.SDK_INT >= 19) getString(
                            string.Documents
                        ) else getString(string.Downloads)
                    )
                )
                .setDuration(ChocoBar.LENGTH_LONG)
                .red()
                .show()
            return
        }
        val activity: AppCompatActivity = this
        val dbHelper = DbHelper(this)
        dbHelper.deleteAll()
        val excelToSQLite = ExcelToSQLite(
            applicationContext,
            DbHelper.Companion.getDBName(ProfileManagement.getSelectedProfilePosition()),
            false
        )
        excelToSQLite.importFromFile(path, object : ImportListener {
            override fun onStart() {}
            override fun onCompleted(filePath: String) {
                runOnUiThread {
                    ChocoBar.builder().setActivity(activity)
                        .setText(getString(string.import_successful))
                        .setDuration(ChocoBar.LENGTH_LONG)
                        .setIcon(R.drawable.ic_baseline_settings_backup_restore_24)
                        .green()
                        .show()
                }
                initAll()
            }

            override fun onError(e: Exception) {
                runOnUiThread {
                    ChocoBar.builder().setActivity(activity)
                        .setText(getString(string.import_failed) + ": " + e.toString())
                        .setDuration(ChocoBar.LENGTH_LONG)
                        .red()
                        .show()
                }
            }
        })
    }

    fun deleteAll() {
        MaterialDialog.Builder(this)
            .title(getString(string.delete_everything))
            .content(getString(string.delete_everything_desc))
            .positiveText(getString(string.yes))
            .onPositive { dialog: MaterialDialog?, which: DialogAction? ->
                try {
                    val dbHelper = DbHelper(this)
                    dbHelper.deleteAll()
                    ChocoBar.builder().setActivity(this)
                        .setText(getString(string.successfully_deleted_everything))
                        .setDuration(ChocoBar.LENGTH_LONG)
                        .setIcon(R.drawable.ic_delete_forever_black_24dp)
                        .green()
                        .show()
                    initAll()
                } catch (e: Exception) {
                    ChocoBar.builder().setActivity(this)
                        .setText(getString(string.an_error_occurred))
                        .setDuration(ChocoBar.LENGTH_LONG)
                        .red()
                        .show()
                }
            }
            .onNegative { dialog: MaterialDialog, which: DialogAction? -> dialog.dismiss() }
            .negativeText(getString(string.no))
            .onNeutral { dialog: MaterialDialog, which: DialogAction? ->
                backup()
                dialog.dismiss()
            }
            .neutralText(string.backup)
            .show()
    }

    private fun openUrlInChromeCustomTab(url: String?) {
        val context: Context = this
        try {
            val customTabsIntent = CustomTabsIntent.Builder()
                .addDefaultShareMenuItem()
                .setToolbarColor(PreferenceUtil.getPrimaryColor(this))
                .setShowTitle(true)
                .build()

            // This is optional but recommended
            addKeepAliveExtra(context, customTabsIntent.intent)

            // This is where the magic happens...
            openCustomTab(
                context, customTabsIntent,
                Uri.parse(url),
                WebViewFallback()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Permissions
    private var sheriffPermission: Sheriff? = null
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        sheriffPermission!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    protected fun requestPermission(runAfter: Runnable?, vararg permissions: SheriffPermission?) {
        val pl: PermissionListener = MyPermissionListener(runAfter)
        sheriffPermission = Sheriff.Builder()
            .with(this)
            .requestCode(REQUEST_MULTIPLE_PERMISSION)
            .setPermissionResultCallback(pl)
            .askFor(*permissions)
            .rationalMessage(getString(string.permission_request_message))
            .build()
        sheriffPermission!!.requestPermissions()
    }

    private inner class MyPermissionListener internal constructor(val runAfter: Runnable?) :
        PermissionListener {
        override fun onPermissionsGranted(
            requestCode: Int,
            acceptedPermissionList: ArrayList<String>
        ) {
            if (runAfter == null) return
            try {
                runAfter.run()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onPermissionsDenied(
            requestCode: Int,
            deniedPermissionList: ArrayList<String>
        ) {
            // setup the alert builder
            val builder = MaterialDialog.Builder(this@MainActivity)
            builder.title(getString(string.permission_required))
            builder.content(getString(string.permission_required_description))

            // add the buttons
            builder.onPositive { dialog: MaterialDialog, which: DialogAction? ->
                openAppPermissionSettings()
                dialog.dismiss()
            }
            builder.positiveText(getString(string.permission_ok_button))
            builder.negativeText(getString(string.permission_cancel_button))
            builder.onNegative { dialog: MaterialDialog, which: DialogAction? -> dialog.dismiss() }

            // create and show the alert dialog
            val dialog = builder.build()
            dialog.show()
        }
    }

    private fun openAppPermissionSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", applicationContext.packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    companion object {
        private const val showNextDayAfterSpecificHour = 20
        private const val backup_filename = "Timetable_Backup.xls"
        private const val REQUEST_MULTIPLE_PERMISSION = 101
    }
}