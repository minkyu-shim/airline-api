const Dashboard = () => {
  return (
    <div className= "pt-5 pb-10">
      <div className="w-full max-w-7xl mx-auto">
        <div className="bg-[#242424] rounded-lg shadow-sm border border-[#4a4a4a] py-5 mb-6">
          <h2 className="mb-4 font-mono">find a flight</h2>
          <div className="mx-8 gap-6 leading-relaxed">
            <form className="mx-auto px-4">
              <div className="w-full mb-10 mt-10">
                <label className="text-sm font-bold text-gray-300 mb-3">Departure City</label><br />
                <input type="text" value="Paris"
                       className="w-full px-3 py-2 bg-gray-600 border border-gray-700 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-shadow-white placeholder-gray-400"/>
              </div>

              <div className="mb-10">
                <label className="text-sm font-bold text-gray-300 mb-10">Destination City</label>
                <input type="text" value="London"
                       className="w-full px-3 py-2 bg-gray-600 border border-gray-700 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-shadow-white placeholder-gray-400"/>
              </div>

              <div className="mb-10">
                <label className="block text-sm font-bold text-gray-300 mb-1">Date</label>
                <input type="date" value="2025-12-25"
                       className="w-full px-3 py-2 bg-gray-600 border border-gray-700 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-shadow-white"/>
              </div>

              <button
                type="button"
                className="w-full bg-[#007cc2] hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition duration-200 mt-4"

              >
                Search Flights
              </button>
            </form>
          </div>
        </div>


      </div>
    </div>
  )
}
export default Dashboard
