package com.example.flightsearch

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FlightSearchDao
import com.example.flightsearch.data.FlightSearchDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FlightSearchDaoTest {
    private lateinit var flightSearchDao: FlightSearchDao
    private lateinit var flightSearchDatabase: FlightSearchDatabase

    private var airport1 = Airport(1, "SEA", "Seattle-Tacoma International", 5_000)
    private var airport2 = Airport(2, "ATL", "Hartsfield-Jackson Atlanta International", 7_000)
    private var airport3 = Airport(3, "PDX", "Portland International", 3_000)

    private var favorite1 = Favorite(1, "SEA", "PDX")
    private var favorite2 = Favorite(2, "PDX", "ATL")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        flightSearchDatabase =
            Room.inMemoryDatabaseBuilder(context, FlightSearchDatabase::class.java)
                .allowMainThreadQueries().build()
        flightSearchDao = flightSearchDatabase.flightSearchDao()
    }

    @After
    fun closeDb() {
        flightSearchDatabase.close()
    }

    @Test
    fun daoGetAllAirports_returnsAllAirportsInOrderFromDb() = runBlocking {
        addAllAirports()
        val allAirports = flightSearchDao.getAllAirports().first()
        assertEquals(3, allAirports.size)
        assertEquals(airport2, allAirports[0])
        assertEquals(airport1, allAirports[1])
        assertEquals(airport3, allAirports[2])
    }

    @Test
    fun daoGetAllFavorites_returnsAllFavorites() = runBlocking {
        addFavorites()
        val allFavorites = flightSearchDao.getAllFavorites().first()
        assertEquals(2, allFavorites.size)
        assertEquals(favorite1, allFavorites[0])
        assertEquals(favorite2, allFavorites[1])
    }

    @Test
    fun daoSaveFavorite_favoriteInserted() = runBlocking {
        addFavorite(favorite1)
        val savedFavorites = flightSearchDao.getAllFavorites().first()
        assertEquals(1, savedFavorites.size)
        assertEquals(favorite1, savedFavorites[0])
    }

    @Test
    fun daoGetAirportsLike_returnsNameMatchingAirports() = runBlocking {
        addAllAirports()
        // "Portland" and "Hartsfield" both contain "rt", which is not in the SEA name or code
        val matchingAirports = flightSearchDao.getAirportsLike("rt").first()
        assertEquals(2, matchingAirports.size)
        assertEquals(airport2, matchingAirports[0])
        assertEquals(airport3, matchingAirports[1])
    }

    @Test
    fun daoGetAirportsLike_returnsCodeMatchingAirports() = runBlocking {
        addAllAirports()
        val matchingAirports = flightSearchDao.getAirportsLike("DX").first()
        assertEquals(1, matchingAirports.size)
        assertEquals(airport3, matchingAirports[0])
    }

    @Test
    fun daoGetAirportsLike_returnsNoMatchingAirports() = runBlocking {
        addAllAirports()
        val matchingAirports = flightSearchDao.getAirportsLike("zzz").first()
        assertEquals(0, matchingAirports.size)
    }

    @Test
    fun daoGetDestinationsFromAirport_returnsRemainingAirports() = runBlocking {
        addAllAirports()
        val destinations = flightSearchDao.getDestinationsFromAirport(airport1.id).first()
        assertEquals(2, destinations.size)
        assertEquals(airport2, destinations[0])
        assertEquals(airport3, destinations[1])
    }

    private suspend fun addAllAirports() {
        flightSearchDao.insertAirport(airport1)
        flightSearchDao.insertAirport(airport2)
        flightSearchDao.insertAirport(airport3)
    }

    private suspend fun addFavorites() {
        flightSearchDao.saveFavorite(favorite1)
        flightSearchDao.saveFavorite(favorite2)
    }

    private suspend fun addFavorite(favorite: Favorite) {
        flightSearchDao.saveFavorite(favorite)
    }
}