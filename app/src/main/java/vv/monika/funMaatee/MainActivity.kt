package vv.monika.funMaatee

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import vv.monika.funMaatee.databinding.ActivityMainBinding
import vv.monika.funMaatee.fragment.HomeFragment
import vv.monika.funMaatee.fragment.ListFragment
import vv.monika.funMaatee.fragment.RecentFragment
import vv.monika.funMaatee.fragment.WalletFragment

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


        bottomNavigationView = binding.bottomNavigationView



//set home fragment as default
        supportFragmentManager.beginTransaction().replace(R.id.fragment_view, HomeFragment())
            .commit()
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_view,
                        HomeFragment()
                    ).commit()
                    true

                }

                R.id.recentFragment -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_view,
                        RecentFragment()
                    ).commit()
                    true
                }

                R.id.walletFragment -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_view,
                        WalletFragment()
                    ).commit()
                    true
                }

                R.id.listFragment -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_view, ListFragment()).commit()
//                open side bar
//                    drawerLayout.openDrawer(GravityCompat.START)
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
