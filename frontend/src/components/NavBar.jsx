import { PlaneTakeoff } from 'lucide-react';

const Navbar = () => {
  return (
    <nav
      className="fixed top-0 left-0 right-0 bg-[#242424] border-b border-gray-500 shadow-sm z-50 transition-transform duration-300"
    >
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center gap-6">
            {/*<button*/}
            {/*    // onClick={onToggleSidebar}*/}
            {/*    className="p-2 rounded-lg hover:bg-gray-100 transition-colors lg:hidden"*/}
            {/*>*/}
            {/*    /!*{sidebarOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}*!/*/}
            {/*</button>*/}

            <a className="flex flex-shrink-0 items-center mr-4" href="/">
              {/*<img*/}
              {/*  className="h-10 w-auto"*/}
              {/*  src={logo}*/}
              {/*  alt="React Jobs"*/}
              {/*/>*/}
              <PlaneTakeoff className="h-5 w-5 mr-2 text-gray-500" />
              <h2 className="text-gray-100 ml-1 hover:text-pink-100 font-mono">timeout airlines</h2>
            </a>
          </div>
        </div>
      </div>
    </nav>
  )
}
export default Navbar
