package com.example.perpustakaanapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.perpustakaanapp.databinding.ActivityMainBinding
import com.example.perpustakaanapp.fragments.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val api by lazy {ApiRetrofit().apiEndPoint}

    private val favoriteFragment = FavoriteFragment()
    private val homeFragment = HomeFragment()
    private val profileFragment = ProfileFragment()
    private val searchFragment = SearchFragment()
    private val loginFragment = LoginFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Home"
        replaceFragment(homeFragment)
        setupListener()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun setupListener(){
        binding.navMenu.setOnNavigationItemSelectedListener {

            when(it.itemId){
                R.id.nav_home -> {
                    replaceFragment(homeFragment)
                    supportActionBar?.title = "Home"
                    supportActionBar?.show()
                }
                R.id.nav_search -> {
                    replaceFragment(searchFragment)
                    supportActionBar?.hide()
                }
                R.id.nav_favorite -> {
                    if(Method.login){
                        replaceFragment(favoriteFragment)
                        supportActionBar?.title = "Favorite"
                        supportActionBar?.show()
                    } else {
                        replaceFragment(loginFragment)
                    }
                }
                R.id.nav_profile -> {
                    if(Method.login){
                        replaceFragment(profileFragment)
                        supportActionBar?.title = "Profile"
                        supportActionBar?.show()
                    } else {
                        replaceFragment(loginFragment)
                    }
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.navPage, fragment)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
/*        val searchItem = menu!!.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                startActivity(Intent(applicationContext, SearchActivity::class.java).putExtra("search", p0))
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
        val logoutItem = menu.findItem(R.id.logout)
        logoutItem.setOnMenuItemClickListener {
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure logout this account?")
                .setPositiveButton("Yes"){s, t->
                    Method.id = null
                    Method.token = null

                }
                .setNegativeButton("No"){s, t->
                    onStart()
                }
            alertDialog.show()
            false
        }
        searchView.queryHint = "Search Book..."
        searchView.setMaxWidth(Integer.MAX_VALUE)
//        searchView.isIconifiedByDefault = false
        searchView.setOnCloseListener(object: SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                onStart()
                return false
            }
        })*/
        return super.onCreateOptionsMenu(menu)
    }



}