package com.example.knot

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.knot.adapter.CardAdapter
import com.example.knot.repository.ReminderRepository


class MainActivity : AppCompatActivity(), CardAdapter.CardViewHolder.OnReminderListener {

    lateinit var recyclerView: RecyclerView
    var repository: ReminderRepository

    init {
        this.repository = ReminderRepository()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.knot.R.layout.activity_main)

        recyclerView = findViewById(com.example.knot.R.id.recyclerview) as RecyclerView
    }

    override fun onResume() {
        super.onResume()
        var layoutManager = LinearLayoutManager(this)
        var rvAdapter = CardAdapter(repository,this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = rvAdapter
    }

    override fun onReminderClick(position: Int) {
        var bundle = Bundle()
        bundle.putSerializable("reminder", repository.getByIndex(position)?.id)
        bundle.putSerializable("repository", repository)

        var intent = Intent(this, ReminderActivity::class.java).apply {
            putExtras(bundle)
        }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.knot.R.menu.menu_layout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            com.example.knot.R.id.addReminderBtn -> {
                var bundle = Bundle()
                bundle.putSerializable("repository", repository)

                var intent = Intent(this, ReminderActivity::class.java).apply {
                    putExtras(bundle)
                }

                startActivity(intent)
                true
            }
            com.example.knot.R.id.quitBtn -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
