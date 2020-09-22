package com.dr.fintracker.ui.wallet

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dr.fintracker.R
import com.dr.fintracker.data.entities.MoneyEmission
import com.dr.fintracker.data.entities.Wallet
import com.dr.fintracker.other.IDefaultWalletProvider
import com.dr.fintracker.ui.moneyemissions.CreateEmissionActivity
import com.dr.fintracker.ui.moneyemissions.DeleteEmissionDialog
import com.dr.fintracker.viewmodel.WalletAdapter
import com.dr.fintracker.viewmodel.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_wallet.*
import javax.inject.Inject


@AndroidEntryPoint
class WalletFragment : Fragment() {

    private val viewModel : WalletViewModel by viewModels()
    lateinit var walletSelectorAdapter : WalletAdapter
    lateinit var moneyEmissionsAdapter : MoneyEmissionsAdapter

    @Inject
    lateinit var defaultWalletProvider : IDefaultWalletProvider

    private var selectedWallet : Wallet? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.wallet_menu_item);
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    private fun onEmissionsUpdates(selectedEmissions : List<MoneyEmission>){
        moneyEmissionsAdapter.emissions = selectedEmissions
        moneyEmissionsAdapter.notifyDataSetChanged()
    }

    private fun onWalletsUpdates(wallets : List<Wallet>){
        wallet_delete_icon.isVisible = wallets.count()>1
        if (wallets.count() == 0) {
            val defaultWallet = defaultWalletProvider.getWallet()
            viewModel.insertWallet(defaultWallet)
        } else {
            walletSelectorAdapter.wallets = wallets
            walletSelectorAdapter.notifyDataSetChanged()
        }
    }

    private fun onSelectedWalletUpdates(wallet : Wallet?){
        wallet?.let {
            selectedWallet = it
            wallet_balance_value.text = it.balance.toString()
            wallet_spinner.setSelection(walletSelectorAdapter.wallets.indexOf(it))
        }
    }

    private fun addNewEmission(){
        selectedWallet.let {
            val intent = Intent(context, CreateEmissionActivity::class.java)
            intent.putExtra("walletId", selectedWallet?.walletID)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_anim, 0);
        }
    }

    private fun createNewWallet(){
        activity?.supportFragmentManager?.let {
            val transaction = it.beginTransaction()
            val newFragment = AddEditWalletDialog(
                object : AddEditWalletDialog.AddNewWalletDialogListener{
                    override fun onWalletCreated(walletTitle: String, walletBalance: Int) {
                        val newWallet = Wallet(walletTitle, walletBalance)
                        viewModel.insertWallet(newWallet)
                    }
                }
            )
            newFragment.show(transaction, "dialog")
        }
    }

    private fun bindMoneyEmissionRecyclerView(){
        moneyEmissionsAdapter = MoneyEmissionsAdapter(listOf(), View.OnClickListener { addNewEmission() },viewModel)
        wallet_emissions_list_recycler_view.adapter = moneyEmissionsAdapter
        wallet_emissions_list_recycler_view.layoutManager = LinearLayoutManager(context)
    }

    private fun bindWalletSpinner(){
        walletSelectorAdapter = WalletAdapter(requireContext(), R.layout.dropdown_item, listOf())
        walletSelectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wallet_spinner.adapter = walletSelectorAdapter
        wallet_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selected = walletSelectorAdapter.getItem(p2)
                viewModel.setSelectedWallet(selected)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {     }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindMoneyEmissionRecyclerView()
        bindWalletSpinner()

        viewModel.getWallets().observe(viewLifecycleOwner, Observer { onWalletsUpdates(it) })
        viewModel.getSelectedWalletEmissions().observe(viewLifecycleOwner, Observer { onEmissionsUpdates(it) })
        viewModel.getSelectedWallet().observe(viewLifecycleOwner, Observer { onSelectedWalletUpdates(it) })

        add_new_wallet_button.setOnClickListener { createNewWallet() }
        wallet_edit_icon.setOnClickListener { editSelectedWallet() }
        wallet_delete_icon.setOnClickListener { deleteSelectedWallet() }

        val touchHelperCallback = EmissionListTouchHelperCallback(
            activity?.supportFragmentManager,
            { pos -> moneyEmissionsAdapter.onItemSwiped(pos) },
            { moneyEmissionsAdapter.notifyDataSetChanged() }
        )
        val touchHelper = ItemTouchHelper(touchHelperCallback)
        touchHelper.attachToRecyclerView(wallet_emissions_list_recycler_view)
    }

    private fun deleteSelectedWallet() {
        activity?.supportFragmentManager?.let {
            val transaction = it.beginTransaction()
            val newFragment = DeleteWalletDialog(
                DialogInterface.OnClickListener { _, _ ->
                    val index = walletSelectorAdapter.wallets.indexOf(selectedWallet)
                    if(index>0){
                        val previousWallet = walletSelectorAdapter.wallets[index-1]
                        selectedWallet?.let { it1 -> viewModel.deleteWallet(it1) }
                        viewModel.setSelectedWallet(previousWallet)
                    }

                },
                DialogInterface.OnClickListener { _, _ ->
                    Toast.makeText(requireContext(), "Delete wallet reverted", Toast.LENGTH_SHORT).show()
                }
            )
            newFragment.show(transaction, "dialog")
        }
    }

    private fun editSelectedWallet(){
        selectedWallet?.let {wallet ->
            activity?.supportFragmentManager?.let {
                val transaction = it.beginTransaction()
                val newFragment = AddEditWalletDialog(
                    object : AddEditWalletDialog.AddNewWalletDialogListener{
                        override fun onWalletCreated(walletTitle: String, walletBalance: Int) {
                            wallet.title = walletTitle
                            wallet.balance = walletBalance
                            viewModel.updateWallet(wallet)
                        }
                    },
                    wallet.title,
                    wallet.balance
                )
                newFragment.show(transaction, "dialog")
            }
        }
    }

}