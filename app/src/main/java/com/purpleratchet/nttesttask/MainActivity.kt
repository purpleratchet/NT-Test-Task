package com.purpleratchet.nttesttask

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var dealsAdapter: DealsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var sortSpinner: Spinner
    private lateinit var sortToggle: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация RecyclerView и адаптера
        recyclerView = findViewById(R.id.deals_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        dealsAdapter = DealsAdapter(listOf())
        recyclerView.adapter = dealsAdapter

        // Инициализация элементов управления сортировкой
        sortSpinner = findViewById(R.id.sort_spinner)
        sortToggle = findViewById(R.id.sort_toggle)

        setupSortControls()
        subscribeToServer()
    }

    private fun setupSortControls() {
        // Настройка Spinner для выбора поля сортировки
        val sortOptions = arrayOf("Дата изменения", "Имя инструмента", "Цена сделки", "Объем сделки", "Сторона сделки")
        sortSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sortOptions)

        // Обработчик выбора в Spinner
        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                sortDeals()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Обработчик переключения направления сортировки
        sortToggle.setOnCheckedChangeListener { _, _ ->
            sortDeals()
        }
    }

    private fun subscribeToServer() {
        val server = Server()
        server.subscribeToDeals { deals ->
            // Обновление списка сделок в основном потоке
            runOnUiThread {
                dealsAdapter.updateDeals(deals)
            }
        }
    }

    private fun sortDeals() {
        val selectedOption = sortSpinner.selectedItem.toString()
        val isDescending = sortToggle.isChecked
        val sortedDeals = when (selectedOption) {
            "Дата изменения" -> if (isDescending) dealsAdapter.deals.sortedByDescending { it.timeStamp } else dealsAdapter.deals.sortedBy { it.timeStamp }
            "Имя инструмента" -> if (isDescending) dealsAdapter.deals.sortedByDescending { it.instrumentName } else dealsAdapter.deals.sortedBy { it.instrumentName }
            "Цена сделки" -> if (isDescending) dealsAdapter.deals.sortedByDescending { it.price } else dealsAdapter.deals.sortedBy { it.price }
            "Объем сделки" -> if (isDescending) dealsAdapter.deals.sortedByDescending { it.amount } else dealsAdapter.deals.sortedBy { it.amount }
            "Сторона сделки" -> if (isDescending) dealsAdapter.deals.sortedByDescending { it.side } else dealsAdapter.deals.sortedBy { it.side }
            else -> dealsAdapter.deals
        }

        dealsAdapter.updateDeals(sortedDeals)
    }

    // ... Остальная часть класса
}