package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightSearchDao {

    /**
     * Get a list of all Airports, ordered by number of yearly passengers (most to least)
     */
    @Query("SELECT * FROM airport ORDER BY passengers DESC")
    fun getAllAirports(): Flow<List<Airport>>

    /**
     * Get a list of all saved Favorites
     */
    @Query("SELECT * FROM favorite")
    fun getAllFavorites(): Flow<List<Favorite>>

    /**
     * Save a Favorite to the DB
     */
    @Insert
    suspend fun saveFavorite(favorite: Favorite)

    /**
     * Search for Airports by their IATA Code AND their full name
     */
    @Query("SELECT * FROM airport WHERE iata_code LIKE '%' || :searchTerm || '%' OR name LIKE '%' || :searchTerm || '%' ORDER BY passengers DESC")
    fun getAirportsLike(searchTerm: String): Flow<List<Airport>>

    /**
     * Get a list of possible destinations from an Airport (i.e., all Airports EXCEPT the specified one)
     */
    @Query("SELECT * FROM airport WHERE id != :airportId ORDER BY passengers DESC")
    fun getDestinationsFromAirport(airportId: Int): Flow<List<Airport>>

    /**
     * Add an Airport to the DB (used for testing purposes only)
     */
    @Insert
    suspend fun insertAirport(airport: Airport)
}