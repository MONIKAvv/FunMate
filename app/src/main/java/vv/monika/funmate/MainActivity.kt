package vv.monika.funmate

import android.os.Bundle
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.DialogTitle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import vv.monika.funmate.databinding.ActivityMainBinding
import vv.monika.funmate.databinding.SideNavigationBarBinding
import vv.monika.funmate.fragment.HomeFragment
import vv.monika.funmate.fragment.ListFragment
import vv.monika.funmate.fragment.RecentFragment
import vv.monika.funmate.fragment.walletFragment
import vv.monika.funmate.ui.theme.FunMateTheme

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        bottomNavigationView = binding.bottomNavigationView


//        show shome fragment by default
        supportFragmentManager.beginTransaction().replace(R.id.fragment_view, HomeFragment()).commit()
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_view,
                        HomeFragment()).commit()
                    true

                }R.id.recentFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_view,
                        RecentFragment()).commit()
                true
            }R.id.walletFragment -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_view,
                    walletFragment()).commit()
                true
            }R.id.listFragment -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_view, ListFragment()).commit()
                true
            }

                else -> false

            }

        }

        // Open Drawer when clicking menu icon
        binding.navDrawerButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }



    }



}
