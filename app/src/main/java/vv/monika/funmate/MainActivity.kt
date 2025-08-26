package vv.monika.funmate

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.system.Os.close
import android.widget.Toast
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
import com.google.android.material.navigation.NavigationView
import vv.monika.funmate.databinding.ActivityMainBinding
import vv.monika.funmate.databinding.AppBlockedBinding
import vv.monika.funmate.databinding.MaintanenceAlertBinding
import vv.monika.funmate.databinding.SideNavigationBarBinding
import vv.monika.funmate.databinding.UpdateActivityBinding
import vv.monika.funmate.databinding.VpnDetectedBinding
import vv.monika.funmate.fragment.HomeFragment
import vv.monika.funmate.fragment.ListFragment
import vv.monika.funmate.fragment.RecentFragment
import vv.monika.funmate.fragment.walletFragment
import vv.monika.funmate.ui.theme.FunMateTheme

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navigationView: NavigationView
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        bottomNavigationView = binding.bottomNavigationView

      binding.sideNavView.setNavigationItemSelectedListener { menuItem ->
          when( menuItem.itemId) {
              R.id.nav_home -> {
                  supportFragmentManager.beginTransaction()
                      .replace(R.id.fragment_view, HomeFragment())
                      .commit()

              }
              R.id.nav_setting -> {
                  Toast.makeText(this, "Setting Clicked", Toast.LENGTH_SHORT).show()
                  true
              } R.id.nav_redeem -> {
             startActivity(Intent(this, RadeemCodeActivity::class.java))
              }
              R.id.nav_history -> {
                  supportFragmentManager.beginTransaction()
                      .replace(R.id.fragment_view, RecentFragment())
                      .commit()
              } R.id.nav_telegram -> {
              Toast.makeText(this, "telegram Clicked", Toast.LENGTH_SHORT).show()
                  true
              }
              R.id.nav_rating -> {
                  Toast.makeText(this, "Rating Clicked", Toast.LENGTH_SHORT).show()
                  true
              } R.id.nav_share -> {
             startActivity(Intent(this, InviteActivity::class.java))
              }
              R.id.nav_email -> {
                  Toast.makeText(this, "Email Clicked", Toast.LENGTH_SHORT).show()
                  true
              } R.id.nav_delete_acc -> {
              Toast.makeText(this, "Delete Clicked", Toast.LENGTH_SHORT).show()
              true
          }R.id.nav_logout -> {
              Toast.makeText(this, "Logout Clicked", Toast.LENGTH_SHORT).show()
              true
          }
              else -> false
      }
          drawerLayout.closeDrawer(GravityCompat.START)
          true

      }




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
//                open side bar
                drawerLayout.openDrawer(GravityCompat.START)

                true
            }

                else -> false

            }

        }

        // Open Drawer when clicking menu icon
//        binding.navDrawerButton.setOnClickListener {
//
//        }



    }



}
