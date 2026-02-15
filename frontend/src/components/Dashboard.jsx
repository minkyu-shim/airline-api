import { useState } from "react";
import FlightService from "../services/FlightService";

const DEFAULT_TODAY = new Date().toLocaleDateString("en-CA");

const Dashboard = () => {
  const [flights, setFlights] = useState([]);
  const [loading, setLoading] = useState(false);
  const [hasSearched, setHasSearched] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const [formData, setFormData] = useState({
    departureCity: "Paris",
    arrivalCity: "London",
    date: DEFAULT_TODAY,
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSearchSpecific = async () => {
    setLoading(true);
    setErrorMessage("");
    const departureCity = formData.departureCity.trim();
    const arrivalCity = formData.arrivalCity.trim();

    if (!departureCity || !arrivalCity) {
      setErrorMessage("Departure and destination city are required.");
      setFlights([]);
      setHasSearched(true);
      setLoading(false);
      return;
    }

    try {
      const data = await FlightService.searchFlights(
        departureCity,
        arrivalCity,
        formData.date
      );
      setFlights(data);
      setHasSearched(true);
    } catch (error) {
      console.error("Search error:", error);
      setErrorMessage(error.message || "Search failed");
      setFlights([]);
      setHasSearched(true);
    } finally {
      setLoading(false);
    }
  };

  const handleSearchAll = async () => {
    setLoading(true);
    setErrorMessage("");

    try {
      const data = await FlightService.getAllFlights();
      setFlights(data);
      setHasSearched(true);
    } catch (error) {
      console.error("Could not fetch flights", error);
      setErrorMessage(error.message || "Could not fetch flights");
      setFlights([]);
      setHasSearched(true);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="pt-5 pb-10">
      <div className="w-full max-w-7xl mx-auto">
        <div className="bg-[#242424] rounded-lg shadow-sm border border-[#4a4a4a] py-5 mb-6">
          <h2 className="mb-4 font-mono">find a flight</h2>
          <div className="mx-8 gap-6 leading-relaxed">
            <form className="mx-auto px-4">
              <div className="w-full mb-10 mt-10">
                <label
                  htmlFor="departureCity"
                  className="text-sm font-bold text-gray-300 mb-3"
                >
                  Departure City
                </label>
                <br />
                <input
                  type="text"
                  id="departureCity"
                  name="departureCity"
                  value={formData.departureCity}
                  onChange={handleInputChange}
                  required
                  className="w-full px-3 py-2 bg-gray-600 border border-gray-700 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-shadow-white placeholder-gray-400"
                />
              </div>

              <div className="mb-10">
                <label
                  htmlFor="arrivalCity"
                  className="text-sm font-bold text-gray-300 mb-10"
                >
                  Destination City
                </label>
                <input
                  type="text"
                  id="arrivalCity"
                  name="arrivalCity"
                  value={formData.arrivalCity}
                  onChange={handleInputChange}
                  required
                  className="w-full px-3 py-2 bg-gray-600 border border-gray-700 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-shadow-white placeholder-gray-400"
                />
              </div>

              <div className="mb-10">
                <label htmlFor="departureDate" className="block text-sm font-bold text-gray-300 mb-1">
                  Date
                </label>
                <input
                  type="date"
                  id="departureDate"
                  name="date"
                  value={formData.date}
                  onChange={handleInputChange}
                  required
                  className="w-full px-3 py-2 bg-gray-600 border border-gray-700 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-shadow-white"
                />
              </div>

              <button
                type="button"
                className="w-full bg-[#007cc2] hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition duration-200 mt-4 mb-4 disabled:opacity-60"
                onClick={handleSearchSpecific}
                disabled={loading}
              >
                Search Flights
              </button>

              <hr />

              <button
                type="button"
                className="w-full bg-[#007cc2] hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition duration-200 mt-4 disabled:opacity-60"
                onClick={handleSearchAll}
                disabled={loading}
              >
                Search For All Flights
              </button>
            </form>
          </div>
        </div>

        {hasSearched && (
          <div className="bg-[#242424] rounded-lg shadow-sm border border-[#4a4a4a] p-5">
            <h3 className="text-white text-lg font-bold mb-4">Flight Results</h3>

            {errorMessage ? (
              <p className="text-red-400 text-center">{errorMessage}</p>
            ) : loading ? (
              <p className="text-gray-300 text-center">Loading flights...</p>
            ) : flights.length === 0 ? (
              <p className="text-gray-400 text-center">No flights found matching your criteria.</p>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full text-left text-gray-300">
                  <thead className="bg-gray-700 text-gray-100 uppercase text-xs">
                    <tr>
                      <th className="px-4 py-3">Flight #</th>
                      <th className="px-4 py-3">Route</th>
                      <th className="px-4 py-3">Airport Info</th>
                      <th className="px-4 py-3">Plane</th>
                      <th className="px-4 py-3">Date</th>
                      <th className="px-4 py-3 text-right">Price (Eco)</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-600">
                    {flights.map((flight) => (
                      <tr key={flight.flightId} className="hover:bg-gray-700 transition">
                        <td className="px-4 py-3 font-bold text-blue-400">{flight.flightNumber}</td>
                        <td className="px-4 py-3">
                          {flight.departureCity} <span className="text-gray-500">&rarr;</span> {flight.arrivalCity}
                        </td>
                        <td className="px-4 py-3 text-sm">
                          <div className="text-gray-400">Dep: {flight.departureAirport?.airportName}</div>
                          <div className="text-gray-400">Arr: {flight.arrivalAirport?.airportName}</div>
                        </td>
                        <td className="px-4 py-3">
                          {flight.plane?.planeBrand} {flight.plane?.planeModel}
                        </td>
                        <td className="px-4 py-3">
                          {new Date(flight.departureDate).toLocaleDateString()}
                        </td>
                        <td className="px-4 py-3 text-right font-mono text-[#007cc2]">
                          ${flight.economyPrice}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
