package fr.isen.fayet.isensmartcompanion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.google.ai.client.generativeai.GenerativeModel
import fr.isen.fayet.isensmartcompanion.models.EventModel
import fr.isen.fayet.isensmartcompanion.models.AppDatabase
import fr.isen.fayet.isensmartcompanion.screen.EventsScreen
import fr.isen.fayet.isensmartcompanion.screen.HistoryScreen
import fr.isen.fayet.isensmartcompanion.screen.MainScreen
import fr.isen.fayet.isensmartcompanion.screen.TabView
import fr.isen.fayet.isensmartcompanion.service.RetrofitInstance
import fr.isen.fayet.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

class MainActivity : ComponentActivity() {
    private val apiKey = BuildConfig.GOOGLE_API_KEY
    private val generativeModel = GenerativeModel(modelName = "gemini-1.5-flash", apiKey = apiKey)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        createNotificationChannel()
        fetchEvents()
        enableEdgeToEdge()
        setContent {
            val db = Room.databaseBuilder(
                LocalContext.current,
                AppDatabase::class.java,
                "historyDataBase"
            ).fallbackToDestructiveMigration().build()
            // setting up the individual tabs
            val homeTab = TabBarItem(title = "Home", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home)
            val eventsTab = TabBarItem(title = "Events", selectedIcon = Icons.Filled.Info, unselectedIcon = Icons.Outlined.Info)//jai enlevé badgeAmount
            val historyTab = TabBarItem(title = "History", selectedIcon = Icons.Filled.Menu, unselectedIcon = Icons.Outlined.Menu)

            // creating a list of all the tabs
            val tabBarItems = listOf(homeTab, eventsTab, historyTab)

            // creating our navController
            val navController = rememberNavController()

            ISENSmartCompanionTheme {
                Scaffold(bottomBar = { TabView(tabBarItems, navController) },
                    modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        NavHost(navController = navController, startDestination = homeTab.title) {
                            composable(homeTab.title) {
                                MainScreen(innerPadding, generativeModel, db)
                            }
                            composable(eventsTab.title) {
                                EventsScreen(
                                    innerPadding = innerPadding,
                                    eventHandler = { startEventDetailActivity(it) }
                                )
                            }
                            composable(historyTab.title) {
                                HistoryScreen(innerPadding, db)
                            }
                        }
                    }
                    }
            }
        }

    }
    fun fetchEvents(){
        val call = RetrofitInstance.api.getEvents()
        call.enqueue(object : Callback<List<EventModel>> {
            override fun onResponse(p0: Call<List<EventModel>>, p1: Response<List<EventModel>>) {
                p1.body()?.forEach {
                    Log.d("request1", "event")

                }
            }

            override fun onFailure(p0: Call<List<EventModel>>, p1: Throwable) {
                Log.e("request2", p1.message ?: "request failed")
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Failed to fetch events", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }
    fun startEventDetailActivity(event: EventModel) {
        val intent = Intent( this, EventDetailActivity::class.java).apply {
            putExtra(EventDetailActivity.eventExtraKey, event)
        }
        startActivity(intent)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "EventNotif"
            val descriptionText = "DescNotif"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun onRestart() {
        super.onRestart()
        Log.d("lifecycle", "MainActivity onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifecycle", "MainActivity onResume")
    }

    override fun onStop() {
        Log.d("lifecycle", "MainActivity onStop")
        super.onStop()
    }

    override fun onPause() {
        Log.d("lifecycle", "MainActivity onPause")
        super.onPause()
    }

    override fun onDestroy() {
        Log.d("lifecycle", "MainActivity onDestroy")
        super.onDestroy()
    }
}


