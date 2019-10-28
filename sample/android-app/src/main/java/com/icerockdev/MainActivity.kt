package com.icerockdev

import android.os.Bundle
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icerockdev.library.ItemData
import com.icerockdev.library.Testing
import com.icerockdev.library.UnitSimple
import com.icerockdev.library.UnitSimpleDropdown
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.units.adapter.UnitsAdapter
import dev.icerock.moko.units.adapter.UnitsRecyclerViewAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val test = Testing(
            unitFactory = object : Testing.UnitFactory {
                override fun createSimpleUnit(
                    id: Long,
                    title: String,
                    itemData: ItemData?
                ): UnitItem {
                    return UnitSimpleDropdown().apply {
                        itemId = id
                        text = title
                        number = 9
                        obj1 = itemData
                        obj2 = itemData
                    }
                }
            }
        )

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val spinner: AppCompatSpinner = findViewById(R.id.spinner)

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = UnitsRecyclerViewAdapter().apply {
            units = test.getUnits()
        }

        spinner.adapter = UnitsAdapter().apply {
            units = test.getUnits()
        }
    }
}
