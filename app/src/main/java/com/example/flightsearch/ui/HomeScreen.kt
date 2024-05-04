package com.example.flightsearch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flightsearch.R
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.ui.theme.AirportCardShape
import com.example.flightsearch.ui.theme.FlightSearchTheme

// TODO: airports in SearchBar LazyColumn should come from viewModel, matched to search term
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    airports: List<Airport>,
    searchBarActive: Boolean = false
) {
    Scaffold(topBar = {
        HomeScreenTopBar(stringResource(R.string.flight_search))
    }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            // TextField(value = "Enter departure airport", onValueChange = {})
            SearchBar(
                query = "Enter departure airport",
                onQueryChange = {}, // TODO: Update viewModel
                onSearch = {},
                active = searchBarActive,
                onActiveChange = {}
            ) {
                LazyColumn(
                    modifier = Modifier.padding(8.dp)
                ) {
                    items(airports) { airport ->
                        Row {
                            Text(text = airport.iataCode, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(text = airport.name)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(
    title: String, modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(title) }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ), modifier = modifier
    )
}

@Composable
fun DestinationAirportCard(
    startingAirport: Airport, destinationAirport: Airport, modifier: Modifier = Modifier
) {
    Card(
        shape = AirportCardShape, modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(9f)
            ) {
                Text(
                    text = "DEPART", style = MaterialTheme.typography.labelMedium
                )
                Row {
                    Text(text = startingAirport.iataCode, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = startingAirport.name)
                }
                Text(
                    text = "ARRIVE", style = MaterialTheme.typography.labelMedium
                )
                Row {
                    Text(text = destinationAirport.iataCode, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = destinationAirport.name)
                }
            }
            // TODO: Change Icon to filled when already favorited
            Icon(
                Icons.Outlined.Star, contentDescription = "Favorite", modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DestinationAirportsList(
    startingAirport: Airport,
    destinationAirports: List<Airport>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.flights_from, startingAirport.iataCode),
            style = MaterialTheme.typography.titleMedium
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(destinationAirports) { airport ->
                DestinationAirportCard(
                    startingAirport = startingAirport,
                    destinationAirport = airport
                )
            }
        }
    }
}

@Composable
fun FavoriteRouteCard(
    favorite: Favorite,
    modifier: Modifier = Modifier
) {
    Card(
        shape = AirportCardShape, modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(9f)
            ) {
                Text(text = "DEPART", style = MaterialTheme.typography.labelMedium)
                Text(text = favorite.departureCode, fontWeight = FontWeight.Bold)
                Text(text = "ARRIVE", style = MaterialTheme.typography.labelMedium)
                Text(text = favorite.destinationCode, fontWeight = FontWeight.Bold)
            }
            // TODO: Change Icon to filled when already favorited
            Icon(
                Icons.Filled.Star, contentDescription = "Favorite", modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun FavoriteRoutesList(
    favorites: List<Favorite>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(text = "Favorite routes", style = MaterialTheme.typography.titleMedium)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(favorites) { favorite ->
                FavoriteRouteCard(favorite = favorite)
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun DestinationAirportCardPreview() {
    FlightSearchTheme {
        DestinationAirportCard(
            startingAirport = Airport(
                0, "SEA", "Seattle-Tacoma International Airport", 5_000
            ), destinationAirport = Airport(
                0, "PDX", "Portland International Airport", 3_000
            )
        )
    }
}

@Preview
@Composable
private fun FavoriteRouteCardPreview() {
    FlightSearchTheme {
        FavoriteRouteCard(favorite = Favorite(0, "SEA", "PDX"))
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteRoutesListPreview() {
    FlightSearchTheme {
        FavoriteRoutesList(
            favorites = listOf(
                Favorite(0, "SEA", "PDX"),
                Favorite(1, "ATL", "SEA"),
                Favorite(2, "LGA", "ATL")
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DestinationAirportsListPreview() {
    FlightSearchTheme {
        DestinationAirportsList(
            startingAirport = Airport(
                0, "SEA", "Seattle-Tacoma International Airport", 5_000
            ),
            destinationAirports = listOf(
                Airport(1, "ATL", "Hartsfield-Jackson Atlanta International", 7_000),
                Airport(2, "LGA", "LaGuardia Airport", 5_000),
                Airport(3, "PDX", "Portland International Airport", 4_000)
            )
        )
    }
}

@Preview
@Composable
private fun HomeScreenInactiveSearchBarPreview() {
    FlightSearchTheme {
        HomeScreen(listOf(
            Airport(1, "ATL", "Hartsfield-Jackson Atlanta International", 7_000),
            Airport(2, "LGA", "LaGuardia Airport", 5_000),
            Airport(3, "PDX", "Portland International Airport", 4_000)
        ))
    }
}

@Preview
@Composable
private fun HomeScreenActiveSearchBarPreview() {
    FlightSearchTheme {
        HomeScreen(listOf(
            Airport(1, "ATL", "Hartsfield-Jackson Atlanta International", 7_000),
            Airport(2, "LGA", "LaGuardia Airport", 5_000),
            Airport(3, "PDX", "Portland International Airport", 4_000)
        ), searchBarActive = true)
    }
}