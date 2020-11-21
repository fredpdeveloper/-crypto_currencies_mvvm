package com.example.cryptos.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cryptos.adapter.TickerListAdapter
import com.example.cryptos.databinding.FragmentTickerBinding
import com.example.cryptos.view.TickerDialog
import com.example.cryptos.viewmodel.CryptoViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_news.*


@AndroidEntryPoint
class Tickers : Fragment() {

    private lateinit var model: CryptoViewModel
    private lateinit var binding: FragmentTickerBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(this)[CryptoViewModel::class.java]
        binding.viewModel = model
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTickerBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.recyclerview.adapter = TickerListAdapter(
            TickerListAdapter.OnClickListener { ticker ->
                model.getTickersByMarker(ticker.market)
                    .observe(viewLifecycleOwner, Observer { tickers ->
                        // Update the cached copy of the tickers in the adapter.
                        tickers?.let {
                            val tickerDialog: TickerDialog =
                                TickerDialog(tickers, ticker).newInstance()
                            tickerDialog.show(
                                activity?.supportFragmentManager!!,
                                "ticker_dialog"
                            )
                        }
                    })
            }
        )

        binding.include.chipsGroupExchange.forEach { child ->
            (child as? Chip)?.setOnCheckedChangeListener { _, _ ->
                if (child.isChecked) {
                    val exchange: String =
                        binding.include.chipsGroupExchange.findViewById<Chip>(child.id).text.toString()
                    model.getTickers(exchange)
                } else {
                    model.getTickers()
                }


            }
        }


        return binding.root

    }

    /**
     * TODO remove shimmer
     */
    override fun onResume() {
        super.onResume()
        shimmerFrameLayout!!.startShimmerAnimation()
    }

    override fun onPause() {
        shimmerFrameLayout!!.stopShimmerAnimation()
        super.onPause()
    }


}