// src/services/FlightService.js

// Vite Proxy redirects this to http://localhost:8080/api/v1/flights
const API_URL = "/api/v1/flights";

const getAllFlights = async () => {
  const response = await fetch(API_URL);

  if (response.status === 204) {
    return [];
  }

  if (!response.ok) {
    const message = await safeText(response);
    throw new Error(message || "Failed to fetch flights");
  }

  return await response.json();
};

const searchFlights = async (departureCity, arrivalCity, date) => {
  if (!departureCity || !arrivalCity || !date) {
    throw new Error("Departure city, arrival city, and date are required");
  }

  const params = new URLSearchParams({
    from: departureCity,
    to: arrivalCity,
    date,
  });

  const response = await fetch(`${API_URL}/search?${params.toString()}`);

  if (response.status === 204) {
    return [];
  }

  if (!response.ok) {
    const message = await safeText(response);
    throw new Error(message || "Search failed");
  }

  return await response.json();
};

const safeText = async (response) => {
  try {
    return await response.text();
  } catch (error) {
    return "";
  }
};

const FlightService = {
  getAllFlights,
  searchFlights,
};

export default FlightService;
