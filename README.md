# Mosayq for Muzei

Auto-generated and minimalistic Wallpapers for Muzei

An open-source product, created at ODD Bureau as an artsy side-project.

Mosayq, a Muzei extension, enables you to choose from 17 algorithmic patterns and generate beautiful wallpapers based on your own choice of colors.

Main features:

-   17 patterns

-   Gallery

-   Color palettes customization

-   Wallpaper sharing

[<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" alt="Get it on Google Play" height="90px"/>](https://play.google.com/store/apps/details?id=com.oddbureau.mosayq)

Implemented using:

-   [Kotlin](https://kotlinlang.org/)

-   [Realm](https://realm.io/)



## Why?

Simple answer - Why not?

We did this because there was no Android wallpaper generator that we really liked at the time.

We ended with 17 backgrounds that look sharp in devices Moto G, Pixel, Samsung and One Plus.

Doing this helped us strengthen our Kotlin, Realm and Android development skills.

We really respect [@romannurik](https://github.com/romannurik) and [@ianhanniballake](https://github.com/ianhanniballake) work in lending their skills to all Android users by providing the [Muzei app](https://github.com/romannurik/muzei) for **free**.

The Android development community helped us and we are now giving it back to the community, by open-sourcing this Muzei plugin.



## Patterns

<img src="./media/001_lines_horizontal.png" height="200px" /><img src="./media/002_lines_vertical.png" height="200px" /><img src="./media/003_lines_diagonal.png" height="200px" />

<img src="./media/004_mosaics_squares.png" height="200px" /><img src="./media/005_triangles_rising-sun.png" height="200px" /><img src="./media/006_triangles_chaos.png" height="200px" />

<img src="./media/007_ripples_circle-multi-color.png" height="200px" /><img src="./media/008_lines_traces.png" height="200px" /><img src="./media/009_mosaics_arcs.png" height="200px" />

<img src="./media/010_mosaics_voronoi-triangles.png" height="200px" /><img src="./media/011_mosaics_voronoi-polygons.png" height="200px" /><img src="./media/012_mosaics_polygons.png" height="200px" />

<img src="./media/013_mosaics_pixels.png" height="200px" /><img src="./media/014_mosaics_triangles.png" height="200px" /><img src="./media/015_geometric_gradient-simple.png" height="200px" />

<img src="./media/016_geometric_gradient-spiral.png" height="200px" /><img src="./media/017_geometric_progressive.png" height="200px" />



## Add your own custom Pattern

1. Add entry to assets/patterns.json

   ```javascript
   {
   	"id": 18,
       "type": "geometric",
       "category": "progressive",
       "isSelected": true,
       "image": "img_example"
   }   
   ```

2. Add listing image as res/drawable/img\_example.png

3. Add the type to the switch case on images/ImageManager.kt

4. Create/Update a ImageCreator (e.g. ImageGeometricCreator.kt) and draw the background on the provided canvas

   

## App Screenshots

<img src="./media/screenshots/00-patterns.png" height="450px" /><img src="./media/screenshots/01-gallery.png" height="450px" /><img src="./media/screenshots/01b-gallery_detail.png" height="450px" />

<img src="./media/screenshots/02-palettes.png" height="450px" /><img src="./media/screenshots/02b-palettes_detail.png" height="450px" /><img src="./media/screenshots/03-settings.png" height="450px" />



## Known issues

-   No resolution independence, different resolutions may produce different backgrounds ( it's not a bug, it's a feature ;) )
-   Applying the above logic, the backgrounds may not be created as expected on landscape devices or devices that change their orientation very often



## Contributors

-   [@joelclaudio](https://github.com/jrrmt)
-   [@jrrmt](https://github.com/jrrmt)
-   @you?



## License

Copyright 2017 Joel Oliveira & José Teixeira

Licensed under the Apache License, Version 2.0 (the "License");

You may not use this file except in compliance with the License.

You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and limitations under the License.
