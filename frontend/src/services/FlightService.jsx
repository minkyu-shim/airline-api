// src/services/FlightService.js

// Vite Proxy redirects this to http://localhost:8080/api/v1/flights
const API_URL = "/api/v1/flights";

const getAllFlights = async () => {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) {throw new Error("Failed to fetch flights");}
    return await response.json();
  } catch (error) {
    console.error(error);
    return []; // Return empty array on error to prevent app crash
  }
};

// NEW: Matches the Controller params
const searchFlights = async (departureCity, arrivalCity, date) => {
  try {
    // Construct URL: /api/v1/flights/search?departureCity=Paris&arrivalCity=London&date=2025-12-25
    const params = new URLSearchParams({
      departureCity: departureCity,
      arrivalCity: arrivalCity,
      date: date
    });

    const response = await fetch(`${API_URL}/search?${params.toString()}`);

    if (!response.ok) throw new Error("Search failed");
    return await response.json();
  } catch (error) {
    console.error(error);
    return [];
  }
};



const FlightService = {
  getAllFlights,
  searchFlights
};

export default FlightService;