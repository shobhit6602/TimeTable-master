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
import android.graphics.Color
import android.net.Uri
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
import android.view.View
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
import java.text.SimpleDateFormat
import java.util.*

/**
 * From https://github.com/SubhamTyagi/TimeTable
 */
class DayAppWidgetProvider : AppWidgetProvider() {
    var lastAppWidgetId = 0
    var lastAction: String? = null
    override fun onEnabled(context: Context) {
        registerNewDayBroadcast(context)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        if (isAlarmManagerNotSet(context)) {
            registerNewDayBroadcast(context)
        }
        for (appWidgetId in appWidgetIds) {
            val intent = Intent(context, DayAppWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            val currentTimeMillis = System.currentTimeMillis()
            AppWidgetDao.saveAppWidgetCurrentTime(appWidgetId, currentTimeMillis, context)
            val rv = RemoteViews(context.packageName, R.layout.day_appwidget)
            rv.setRemoteAdapter(R.id.lv_day_appwidget, intent)
            rv.setEmptyView(R.id.lv_day_appwidget, R.id.empty_view)
            rv.setTextViewText(R.id.tv_date, getDateText(currentTimeMillis, context))
            rv.setInt(
                R.id.fl_root,
                "setBackgroundColor",
                AppWidgetDao.getAppWidgetBackgroundColor(appWidgetId, Color.TRANSPARENT, context)
            )
            rv.setOnClickPendingIntent(
                R.id.imgBtn_restore,
                makePendingIntent(context, appWidgetId, ACTION_RESTORE)
            )
            rv.setOnClickPendingIntent(
                R.id.imgBtn_yesterday,
                makePendingIntent(context, appWidgetId, ACTION_YESTERDAY)
            )
            rv.setOnClickPendingIntent(
                R.id.imgBtn_tomorrow,
                makePendingIntent(context, appWidgetId, ACTION_TOMORROW)
            )
            val listviewClickIntent = Intent(context, MainActivity::class.java)
            listviewClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            listviewClickIntent.action = Intent.ACTION_VIEW
            val listviewPendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId,
                listviewClickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            rv.setPendingIntentTemplate(R.id.lv_day_appwidget, listviewPendingIntent)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_day_appwidget)
            appWidgetManager.updateAppWidget(appWidgetId, rv)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            AppWidgetDao.deleteAppWidget(appWidgetId, context)
        }
    }

    override fun onDisabled(context: Context) {
        unregisterNewDayBroadcast(context)
        AppWidgetDao.clear(context)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        onUpdate(context, appWidgetManager, intArrayOf(appWidgetId))
    }

    private fun makePendingIntent(
        context: Context,
        appWidgetId: Int,
        action: String
    ): PendingIntent {
        val intent = Intent(context, DayAppWidgetProvider::class.java)
        intent.action = action
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onReceive(context: Context, intent: Intent) {
        /*
         * Whenever a request is received, checks if anything changes before using the data
         */
        if (intent.action == lastAction && lastAppWidgetId == intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        ) {
            return
        }
        updateValues(intent)
        if (ACTION_NEW_DAY == lastAction) {
            notifyUpdate(context)
            return
        }
        if (ACTION_RESTORE == lastAction || ACTION_YESTERDAY == lastAction || ACTION_TOMORROW == lastAction) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val rv = RemoteViews(context.packageName, R.layout.day_appwidget)
            val currentTime: Long
            val newTime: Long
            if (ACTION_RESTORE == lastAction) {
                rv.setViewVisibility(R.id.imgBtn_restore, View.INVISIBLE)
                newTime = System.currentTimeMillis()
            } else if (ACTION_YESTERDAY == lastAction) {
                rv.setViewVisibility(R.id.imgBtn_restore, View.VISIBLE)
                currentTime = AppWidgetDao.getAppWidgetCurrentTime(
                    lastAppWidgetId,
                    System.currentTimeMillis(),
                    context
                )
                newTime = currentTime - ONE_DAY_MILLIS
            } else { //ACTION_TOMORROW
                rv.setViewVisibility(R.id.imgBtn_restore, View.VISIBLE)
                currentTime = AppWidgetDao.getAppWidgetCurrentTime(
                    lastAppWidgetId,
                    System.currentTimeMillis(),
                    context
                )
                newTime = currentTime + ONE_DAY_MILLIS
            }
            if (("" + newTime).substring(0, 7)
                    .equals(("" + System.currentTimeMillis()).substring(0, 7), ignoreCase = true)
            ) {
                rv.setViewVisibility(R.id.imgBtn_restore, View.INVISIBLE)
            }
            AppWidgetDao.saveAppWidgetCurrentTime(lastAppWidgetId, newTime, context)
            rv.setTextViewText(R.id.tv_date, getDateText(newTime, context))
            appWidgetManager.notifyAppWidgetViewDataChanged(lastAppWidgetId, R.id.lv_day_appwidget)
            appWidgetManager.partiallyUpdateAppWidget(lastAppWidgetId, rv)
        }
        super.onReceive(context, intent)
    }

    fun notifyUpdate(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(
                context,
                DayAppWidgetProvider::class.java
            )
        )
        onUpdate(context, appWidgetManager, appWidgetIds)
    }

    private fun registerNewDayBroadcast(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            ?: return
        val intent = Intent(context, DayAppWidgetProvider::class.java)
        intent.action = ACTION_NEW_DAY
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val midnight = Calendar.getInstance(Locale.getDefault())
        midnight[Calendar.HOUR_OF_DAY] = 0
        midnight[Calendar.MINUTE] = 0
        midnight[Calendar.SECOND] = 1 //
        midnight[Calendar.MILLISECOND] = 0
        midnight.add(Calendar.DAY_OF_YEAR, 1)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            midnight.timeInMillis,
            ONE_DAY_MILLIS.toLong(),
            pendingIntent
        )
    }

    private fun unregisterNewDayBroadcast(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            ?: return
        val intent = Intent(context, DayAppWidgetProvider::class.java)
        intent.action = ACTION_NEW_DAY
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE)
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    private fun isAlarmManagerNotSet(context: Context): Boolean {
        val intent = Intent(context, DayAppWidgetProvider::class.java)
        intent.action = ACTION_NEW_DAY
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE) == null
    }

    private fun updateValues(intent: Intent) {
        lastAction = intent.action
        lastAppWidgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
    }

    companion object {
        private const val ACTION_RESTORE = "com.ulan.timetable" + ".ACTION_RESTORE"
        private const val ACTION_YESTERDAY = "com.ulan.timetable" + ".ACTION_YESTERDAY"
        private const val ACTION_TOMORROW = "com.ulan.timetable" + ".ACTION_TOMORROW"
        private const val ACTION_NEW_DAY = "com.ulan.timetable" + ".ACTION_NEW_DAY"
        private const val ONE_DAY_MILLIS = 86400000
        private fun getDateText(currentTimeMillis: Long, context: Context): String {
            var date = SimpleDateFormat("E  d.M.", Locale.getDefault()).format(currentTimeMillis)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = currentTimeMillis
            if (PreferenceUtil.isTwoWeeksEnabled(context)) {
                date += " ("
                date += if (PreferenceUtil.isEvenWeek(
                        context,
                        calendar
                    )
                ) context.getString(string.even_week) else context.getString(
                    string.odd_week
                )
                date += ")"
            }
            return date
        }

        fun updateAppWidgetConfig(
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            backgroundColor: Int,
            timeStyle: Int,
            profile: Int,
            context: Context
        ) {
            AppWidgetDao.saveAppWidgetConfig(
                appWidgetId,
                backgroundColor,
                timeStyle,
                profile,
                context
            )
            val intent = Intent(context, DayAppWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            val views = RemoteViews(context.packageName, R.layout.day_appwidget)
            views.setRemoteAdapter(R.id.lv_day_appwidget, intent)
            views.setEmptyView(R.id.lv_day_appwidget, R.id.empty_view)
            views.setInt(R.id.fl_root, "setBackgroundColor", backgroundColor)
            views.setTextViewText(R.id.tv_date, getDateText(System.currentTimeMillis(), context))
            appWidgetManager.partiallyUpdateAppWidget(appWidgetId, views)
        }
    }
}