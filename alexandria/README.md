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
- Used custom textview for book content with custom font
- Refactored UI layout, removed deprecated fill_parent attribute, removed relative layouts

#Todo
- Add Accessibility
- Crash on rotation [Done]
- Crash when error in string split [Done]
- Use Gilde for image loading/caching [Done]
- Use retrofit for rest api call
- Update layouts to enhance UI
- Add dynamic search with every char in listOfBooks fragment [Done]
- Remove un-necessary annoying keyboard

##Acknowledgments
- [BarCodes APIs](https://search-codelabs.appspot.com/codelabs/bar-codes)
- [Barcode Reader](https://github.com/googlesamples/android-vision/tree/master/visionSamples/barcode-reader)
- [Capturing barcode](http://stackoverflow.com/questions/32021193/how-to-capture-barcode-values-using-the-new-barcode-api-in-google-play-services)