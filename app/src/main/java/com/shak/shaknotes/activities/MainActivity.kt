package com.shak.shaknotes.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.shak.shaknotes.R
import com.shak.shaknotes.databinding.ActivityMainBinding
import com.shak.shaknotes.fragments.FolderFragment
import com.shak.shaknotes.fragments.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Make status bar non-transparent
        WindowCompat.setDecorFitsSystemWindows(window, true)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }


        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolBar.title = "Shak Notes"
        binding.toolBar.subtitle = "Notes App"



        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> loadFragment(HomeFragment())

                R.id.folder -> loadFragment(FolderFragment())
            }
            true
        }


        //Default fragment to be loaded
        binding.bottomNav.selectedItemId = R.id.home


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.select -> {
                // Handle select
                return true
            }
            R.id.search -> {
                // Handle search
                return true
            }
            R.id.sync -> {
                // Handle sync
                return true
            }
            R.id.setup -> {
                // Handle setup
                return true
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun loadFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.containerFrameLay, fragment)
        ft.commit()
    }
}