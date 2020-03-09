# BeerBox
![Android CI](https://github.com/mmzitarosa/BeerBox/workflows/Android%20CI/badge.svg)

![MainActivity](https://user-images.githubusercontent.com/50272177/76261726-e95d3580-625a-11ea-8dc4-71b629afa34f.png)
![BottomSheetDialog1](https://user-images.githubusercontent.com/50272177/76261728-e95d3580-625a-11ea-9bf9-1504abfae260.png)

## Favourite beers

![Bookmarks](https://user-images.githubusercontent.com/50272177/76261729-ea8e6280-625a-11ea-9fb9-0cc77a0fc9fb.png)
![BottomSheetDialog2](https://user-images.githubusercontent.com/50272177/76262062-b7000800-625b-11ea-9664-3c1295dbb10a.png)

## Caching system
BeerBox contains a caching system with the aim of saving mobile data.

All the images are download only the first time and saved (based on the URL) in the app cache. The following requests to the same URL retrieve the images from the cache.

Also the responses of PunkApi API are stored in the app cache, to be used only in case of unavailable  network or bad response.
