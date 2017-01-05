# Nerdery Android NAT
This is the Base Project for The Nerdery's Android NAT.
It provides a number of key pieces to help you get started on the NAT.

Nothing that we provide should be interpreted as a mandatory component; if you want to remove, modify,
or replace any of the functionality we have provided with your own implementation, you are free to do so.

# Android verions
* App should target SDK 23
* App must support API versions 16 and up.

# APIs
The api package contains an ApiManager, which provides access to two API classes:
 * WeatherApi: This is the Weather Underground API. The only implemented endpoint is /conditions/hourly.
 * IconApi: This API is used to provide better icons than Weather Underground provides.

# Resources
We have set up a few resources for you already.
All of the icons you need should already be in the appropriate drawable folders.
The colors used to indicate that the current temperature is "warm" or "cool" are provided in colors.xml.
Take special note of metrics.md, which defines a number of metrics for styling the application.

# Forecast cards and the hourly grid
Note that the LayoutManagers provided in the support library currently do not support nested RecyclerViews 
that wrap_content or change sizes (e.g. by adding content) very well.

To help you get started working around this issue, we have provided a DynamicGridLayoutManager that can be used
on a RecyclerView nested within another RecyclerView to display a grid of items.

You do not need to use this class if it does not fit well with your implementation of this application; it is simply
offered as an option for implementing the hourly forecast grid.