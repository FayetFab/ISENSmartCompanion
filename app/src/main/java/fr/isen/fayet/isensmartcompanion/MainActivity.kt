package fr.isen.fayet.isensmartcompanion

import android.content.Intent
import android.os.Bundle
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.fayet.isensmartcompanion.models.EventModel
import fr.isen.fayet.isensmartcompanion.screen.EventsScreen
import fr.isen.fayet.isensmartcompanion.screen.HistoryScreen
import fr.isen.fayet.isensmartcompanion.screen.MainScreen
import fr.isen.fayet.isensmartcompanion.screen.TabView
import fr.isen.fayet.isensmartcompanion.ui.theme.ISENSmartCompanionTheme

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
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
                                MainScreen(innerPadding)
                            }
                            composable(eventsTab.title) {
                                EventsScreen(
                                    innerPadding = innerPadding,
                                    eventHandler = { startEventDetailActivity(it) }
                                )
                            }
                            composable(historyTab.title) {
                                HistoryScreen(innerPadding)
                            }
                        }
                    }
                    }
            }
        }


    }
    fun startEventDetailActivity(event: EventModel) {
        val intent = Intent( this, EventDetailActivity::class.java).apply {
            putExtra(EventDetailActivity.eventExtraKey, event)
        }
        startActivity(intent)
    }
}


