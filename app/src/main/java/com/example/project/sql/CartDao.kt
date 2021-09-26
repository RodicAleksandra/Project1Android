package com.example.project.sql
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import com.example.project.data.CartItem
import com.example.project.data.Product

class CartDao(val context: Context) {
    val db: SQLiteDatabase
    init {
        db = DBHelper(context).writableDatabase
    }

    fun getItems(): ArrayList<CartItem>{
        try {
            val itemsList = ArrayList<CartItem>()

            val cursor: Cursor = db.query("cart", null, null, null,
                null, null, null)
            while(cursor.moveToNext()){
                val itemId = cursor.getLong(0)
                val productId = cursor.getString(1)
                val productName = cursor.getString(2)
                val image = cursor.getString(3)
                val quantity = cursor.getInt(4)
                val price = cursor.getDouble(5)

                val cartItem = CartItem(itemId, productId, productName, image, quantity, price)
                itemsList.add(cartItem)
            }
            return itemsList
        }catch(e: SQLException){
            e.printStackTrace()
            return ArrayList<CartItem>()
        }
    }

    fun addItem(cartItem: CartItem): Boolean {
        try{
            val values: ContentValues = ContentValues()
            values.put("productId", cartItem.cartProductId)
            values.put("productName", cartItem.cartProductName)
            values.put("image", cartItem.cartImage)
            values.put("quantity", cartItem.cartQuantity)
            values.put("price", cartItem.cartPrice)
            val id:Long = db.insert("cart", null, values)
            return id != -1L
        }catch(e: SQLException){
            e.printStackTrace()
            return false
        }
    }

    fun deleteItem(itemId: Long): Boolean {
        val numberRowsDeleted = db.delete("cart", "itemId = $itemId", null)

        return numberRowsDeleted == 1
    }

    fun clearCart() {
        try {

            val cartList = getItems()
            cartList.forEach {
                deleteItem(it.cartItemId)
            }
        }catch(e: SQLException){
            e.printStackTrace()
            Toast.makeText(context, "An error occured while deleting cart items: ${e.message}",
                Toast.LENGTH_LONG).show()
        }
    }

    fun updateItem(cartItem: CartItem): Boolean {
        val values = ContentValues()

        values.put("itemId", cartItem.cartItemId)
        values.put("productId", cartItem.cartProductId)
        values.put("productName", cartItem.cartProductName)
        values.put("image", cartItem.cartImage)
        values.put("quantity", cartItem.cartQuantity)
        values.put("price", cartItem.cartPrice)
        val numberOfRowsUpdated = db.update("cart", values,"itemId=${cartItem.cartItemId}", null)
        return numberOfRowsUpdated == 1
    }

    fun hasItem(product: Product): Boolean {
        var cursor = db.rawQuery("""
            SELECT COUNT(itemId)
            FROM cart
            WHERE '${product.productId}' LIKE productId
        """.trimIndent(), null)

        cursor.moveToNext()
        val numberFound = cursor.getInt(0)
        return numberFound == 1
    }
}