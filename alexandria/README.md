#Alexandria
- Books browsing app


#Changes
- Added Google Vision API for barcode reading, so no need for 3rd party barcode reader app
- Added new book functionality using barcode
- Updated Nav drawer
- Fixed issue of improper title when pressed back
- Fixed bug of improper fragments in stack in case of tablets
- Updated deprecated v4.ActionBarDrawerToggle to v7.ActionBarDrawerToggle
- Used Glide for async image loading & caching
- Fixed crash on searching for book without internet connection
- Used butterknife for elegant code + code formatting
- Used custom textview for book content with custom font
- Refactored UI layout, removed deprecated fill_parent attribute, removed relative layouts
- Added code for making app accessible to impaired
- Fixed bug where right container comes in place of main container on tablet rotation
- Added code for retaining detail book page on mobile/tablet devices.
- Added localization for DE
- Added toolbar and back navigation to settings activity
- Removed deprecated addPreferencesFromResource method from settings activity
- Removed un-necessary annoying softkeyboard coming up in fragments


##Acknowledgments
- [BarCodes APIs](https://search-codelabs.appspot.com/codelabs/bar-codes)
- [Barcode Reader](https://github.com/googlesamples/android-vision/tree/master/visionSamples/barcode-reader)
- [Capturing barcode](http://stackoverflow.com/questions/32021193/how-to-capture-barcode-values-using-the-new-barcode-api-in-google-play-services)