package com.quinox.mobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.amulyakhare.textdrawable.TextDrawable
import com.quinox.mobile.anotations.RequiresActivityViewModel
import com.quinox.mobile.base.BaseActivity
import com.quinox.mobile.viewModels.HomeVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.nav_header_home.*

@RequiresActivityViewModel(HomeVM.ViewModel::class)
class HomeActivity : BaseActivity<HomeVM.ViewModel>() {

    private val composite = CompositeDisposable()
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_profile, R.id.nav_lessons, R.id.nav_news,
                R.id.nav_exit
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel.inputs.onCreate()


        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_exit -> {
                    signOutDialog()
                }
                R.id.nav_home -> {
                    navController.navigate(R.id.nav_home)
                    drawerLayout.closeDrawers()
                }
                R.id.nav_profile -> {
                    navController.navigate(R.id.nav_profile)
                    drawerLayout.closeDrawers()
                }
                R.id.nav_news -> {
                    navController.navigate(R.id.nav_news)
                    drawerLayout.closeDrawers()
                }
                R.id.nav_lessons -> {
                    navController.navigate(R.id.nav_lessons)
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }

        composite.add(viewModel.outputs.signOutAction().observeOn(AndroidSchedulers.mainThread()).subscribe {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        })

        composite.add(viewModel.outputs.profileInfo().observeOn(AndroidSchedulers.mainThread()).subscribe {
            it.forEach { pair ->
                when(pair.first){
                    "name" -> {
                        Log.e("Info", pair.second)
                        home_header_name.text = pair.second
                        val initials = getUserInitials(pair.second)
                        val drawable = TextDrawable.builder()
                            .beginConfig()
                            .width(150)
                            .height(150)
                            .bold()
                            .endConfig()
                            .buildRound(initials,getColor(R.color.secondaryColor))
                        home_header_image.setImageDrawable(drawable)
                    }
                    "email" -> {
                       home_header_email.text = pair.second
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        composite.dispose()
        super.onDestroy()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
       signOutDialog()
    }

    private fun signOutDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cerrar Sesión")
        builder.setMessage("¿Deseas realmente cerrar la sesión?")
        builder.setPositiveButton("Aceptar"){
                _,_ ->
            this.viewModel.inputs.signOutButtonPressed()
        }
        builder.setNegativeButton("Cancelar", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun getUserInitials(username : String) : String {
        val splitedUsername = username.toUpperCase().split(" ")
        val response = ""
        if(splitedUsername.isEmpty()){
            return response
        }
        return if (splitedUsername.size <= 1) {
            splitedUsername[0].first().toString()
        } else {
            val firstInitial = splitedUsername[0].first()
            val secondInitial = splitedUsername[1].first()
            "$firstInitial$secondInitial"
        }
    }


}
