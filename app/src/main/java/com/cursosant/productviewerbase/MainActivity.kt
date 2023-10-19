package com.cursosant.productviewerbase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.text.HtmlCompat
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cursosant.productviewerbase.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var selectedProduct: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupProduct()
        setupButtons()
        setupTextField()
    }

    private fun setupProduct() {
        selectedProduct = getProductFromServer()

        binding.tvName.text = selectedProduct.name
        binding.tvDescription.text = selectedProduct.description
        setQuantity()
        setNewQuantity()

        Glide.with(this)
            .load(selectedProduct.imgUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(binding.imgProduct)
    }

    private fun setupButtons(){
        binding.ibSub.setOnClickListener {
            if (selectedProduct.newQuantity > 1){
                selectedProduct.newQuantity -= 1
                setNewQuantity()
            }
        }
        binding.ibSum.setOnClickListener {
            if (selectedProduct.newQuantity < selectedProduct.quantity){
                selectedProduct.newQuantity += 1
                setNewQuantity()
            }
        }
        binding.efab.setOnClickListener {
            selectedProduct.newQuantity = binding.etNewQuantity.text.toString().toInt()
            addToCart(selectedProduct)
        }
    }

    private fun setupTextField(){
        binding.etNewQuantity.addTextChangedListener {
            try {
                selectedProduct.newQuantity = it.toString().toInt()
            } catch (e: Exception) {
                selectedProduct.newQuantity = 1
                binding.etNewQuantity.setText("1")
            }
            binding.etNewQuantity.setSelection(it?.length ?: 0)
        }
    }

    private fun getProductFromServer(): Product = Product(
        "mjn23jkn42kjn",
        "Vino tinto",
        "Aroma frutal, con tonos semiamargos. Los vinos tintos chilenos están provistos de " +
                "una nota muy frutosa, pero también aromática y se caracterizan, en la mayoría de los " +
                "casos, más bien por un contenido bajo de taninos.",
        "https://get.pxhere.com/photo/stemware-wine-glass-champagne-stemware-glass-drinkware-" +
                "drink-bottle-red-wine-alcoholic-beverage-glass-bottle-wine-alcohol-Snifter-wine-bottle-" +
                "tableware-barware-water-dessert-wine-kir-liqueur-distilled-beverage-wine-cocktail-" +
                "Burgundy-wine-wine-tasting-liquid-kalimotxo-port-wine-1617348.jpg",
        343,
        1,
        74.99,
        "njk3n4jk3n")

    private fun setQuantity(){
        binding.tvQuantity.text = getString(R.string.detail_quantity, selectedProduct.quantity)
    }
    private fun setNewQuantity() {
        binding.etNewQuantity.setText(selectedProduct.newQuantity.toString())

        val newQuantityStr = getString(R.string.detail_total_price, selectedProduct.totalPrice(),
            selectedProduct.newQuantity, selectedProduct.price)
        binding.tvTotalPrice.text = HtmlCompat.fromHtml(newQuantityStr, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun addToCart(product: Product) {
        //save product in database...
        Snackbar.make(binding.root, getString(R.string.main_message_product_added, product.name),
            Snackbar.LENGTH_SHORT).show()
        selectedProduct.apply {
            quantity -= product.newQuantity
            newQuantity = 1
        }
        setQuantity()
        setNewQuantity()
    }
}