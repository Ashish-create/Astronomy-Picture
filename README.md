# Astronomy-Picture
Demo app for implementing Nasa Astronomy picture of the day query

When app launches it gives following options:

A. Select date to view Picture : 
    1.This will load the image according to the selected date.
    2.User can mark this image as favourite using favourite icon in the top right corner of the image.
    3.User can mark/unmark to favourite/unfavourite the image.
    4.User can expand the text too view full description of the Astronomy picture.

B. View and Manage Favourites: This will show those images which are marked as favourite by user 
   1.This shows the user favourite images.
   2.User can remove the images from favourite list using delete icon in the top right corner.
  
  
  
  Supported Features:
  
  1. App takes Dark/Night mode settings from the system and adjusts UI accordingly.
  2. Supports Landscape/Portrait mode.
  
  Components used:
  
  Architure: MVVM
  Image Loading Library : Glide
  Data Persistance: Room
  Network call: Retrofit.
  
  Note: ExpandableTextView has been used for managing read less/more feature
   
  dependency :  implementation ‘com.ms-square:expandableTextView:0.1.4’
  licence:  Apache License Version 2.0 : https://github.com/Manabu-GT/ExpandableTextView/blob/master/LICENSE
  
