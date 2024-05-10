package com.clothit.server.dao

interface ItemsToOutfitsDao {

    fun save(outfitId : Int, itemId : Int ) : Int

    fun getItemsIdByOutfitId(outfitId : Int) : List<Int>

    fun delete(outfitId: Int, itemId: Int)

}