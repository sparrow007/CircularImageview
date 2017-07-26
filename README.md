![backcover](https://user-images.githubusercontent.com/22986571/27983736-23f27b9a-63e1-11e7-9dcb-da1f50bccef9.png)


# CircularImageview
This project allowing you to create circular and rounded corner imageview in android through simplest way.

In circular imageview It uses a BitmapShader and **does not**:
* create a copy of the original bitmap
* use a clipPath (which is neither hardware accelerated nor anti-aliased)
* use setXfermode to clip the bitmap (which means drawing twice to the canvas)

USAGE
-----
To make a circular ImageView add CircularImageView in your layout XML and add CircularImageView library in your project or you can also grab it through Gradle:

Gradle
------
```
dependencies {
    ...
    compile 'com.jackandphantom.android:circularimageview:1.2.0'
}
```
XML
-----

```xml
<!-- <a> circular imageview xml</a> -->
 <com.jackandphantom.circularimageview.CircleImage
        android:layout_width="270dp"
        android:layout_height="270dp"
        android:src="@drawable/circularImage"
        app:border_width="3dp"
        app:border_color="#ed2e2e"
        app:add_shadow="true"
        app:shadow_color="#a10909"
        app:shadow_radius="20"
        android:id="@+id/circleImage" />
        
<!-- <a> Rounded corner imageview xml </a> -->
 <com.jackandphantom.circularimageview.RoundedImage
        android:layout_width="320dp"
        android:layout_height="220dp"
        app:rounded_radius="50"
        android:src="@drawable/rounded_Image"
        android:id="@+id/roundedImage3" />

```
You may use the following properties in your XML to change your CircularImageView.


#####Properties:

To add shadow in your circular imageview you have to add property add_shadow= "true"

 /*  circular imageview xml */
*  app.border_width  (Dimension)  ->  default 0dp
*  app.border_color  (Color)      ->  default White
*  app.add_shadow    (boolean)    ->  default false
*  app.shadow_color  (Color)      ->  default Black
*  app.shadow_radius (float)      ->  default 10
*  app.shadow_radius (float)      ->  default 10

/* Rounded corner imageview xml */
* app.rounded_radius  (float)     ->  default 20

JAVA
-----

```java
 CircleImage circleImage = (CircleImage) findViewById(R.id.circleImage);
        circleImage.setBorderColor(Color.BLACK);
        circleImage.setBorderWidth(5);
        circleImage.setAddShadow(true);
        circleImage.setShadowRadius(20);
        circleImage.setShadowColor(Color.parseColor("#a10909"));
     
  RoundedImage roundedImage = (RoundedImage) findViewById(R.id.roundedImage);
        roundedImage.setRoundedRadius(50);
```
New Functionality 
---------
* Now you can load high resolution images without getting outOfmemory exception, all you need to do is call mehtod              * **loadHighResolutionImage** and pass the path of image as arguments

```java
 CircleImage circleImage = (CircleImage) findViewById(R.id.circleImage);
             circleImage.loadHighResolutionImage(String filePath);
 ```
 
 This method can be load image upto 2MB, 5MB, 10MB, i did'nt know the limitation that how much it can load.

This is my first library use it :) !!

Changelog
---------
* **1.2.0**
    * Now you can load high resolution images without getting outOfmemory exception  (minsdkVersion = 13)
    
* **1.1.0**
    * Add shadow effect (minsdkVersion = 19)

* **1.0.0**
    * initial release (minsdkVersion = 19)

SAMPLE APP LINK
-----
(https://www.dropbox.com/s/ifd07uaj2z4c9bi/sample.apk?dl=0)

LICENCE
-----

 Copyright 2017 Ankit kumar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 Thanks to stackoverflow and Henning Dodenhof
