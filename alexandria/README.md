#Alexandria
- Books browsing app


#Changes
- Added Google Vision API for barcode reading, so no need for 3rd party barcode reader app
- Added new book functionality using barcode
- Updated Nav drawer with latest material theme
- Fixed issue of improper title when pressed back
- Updated deprecated v4.ActionBarDrawerToggle to v7.ActionBarDrawerToggle
- Used Glide for async image loading & caching
- Fixed crash on searching for book without internet connection
- Used butterknife for elegant code + code formatting


#Todo
- Add Accessibility
- Crash on rotation
- Crash when error in string split
- Use Gilde for image loading/caching [Done]
- Use retrofit for rest api call


##Acknowledgments
- [BarCodes APIs](https://search-codelabs.appspot.com/codelabs/bar-codes)
- [Barcode Reader](https://github.com/googlesamples/android-vision/tree/master/visionSamples/barcode-reader)
- [Capturing barcode](http://stackoverflow.com/questions/32021193/how-to-capture-barcode-values-using-the-new-barcode-api-in-google-play-services)