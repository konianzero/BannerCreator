Banner Creator
--------------

_**Kotlin**_

Create banners from ascii font.

---

### Run

Build
```bash
./gradlew clean build
```
Run
```bash
java -jar build/libs/BannerCreator-2.0-SNAPSHOT.jar <options>
```

---

### Example

```bash
> java -jar build/libs/BannerCreator-2.1-SNAPSHOT.jar -h
Usage: Banner Creator options_list
Options: 
    --name, -n -> Project name (always required) { String }
    --description, -d -> Project short description (always required) { String }
    --singleFont, -sf [false] -> Use one font for name and description (name font for default) 
    --nameFontPath, -nf [font/roman.txt] -> Path to font file for Name { String }
    --descFontPath, -df [font/medium.txt] -> Path to font file for Description { String }
    --print, -p [false] -> Print banner to console 
    --border, -b [8] -> Border symbol { String }
    --output, -o [banner.txt] -> Output file { String }
    --help, -h -> Usage info 
```

```
> java -jar build/libs/BannerCreator-2.1-SNAPSHOT.jar -p -n "Name" -d "description"
8888888888888888888888888888888888888888888888888888888888888
88  ooooo      ooo                                         88
88  `888b.     `8'                                         88
88   8 `88b.    8   .oooo.   ooo. .oo.  .oo.    .ooooo.    88
88   8   `88b.  8  `P  )88b  `888P"Y88bP"Y88b  d88' `88b   88
88   8     `88b.8   .oP"888   888   888   888  888ooo888   88
88   8       `888  d8(  888   888   888   888  888    .o   88
88  o8o        `8  `Y888""8o o888o o888o o888o `Y8bod8P'   88
88                                                         88
88                                                         88
88                                                         88
88    ___  ____ ____ ____ ____ _ ___  ___ _ ____ _  _      88
88    |  \ |___ [__  |    |__/ | |__]  |  | |  | |\ |      88
88    |__/ |___ ___] |___ |  \ | |     |  | |__| | \|      88
8888888888888888888888888888888888888888888888888888888888888
```

```
> java -jar build/libs/BannerCreator-2.1-SNAPSHOT.jar -p -sf -b "+" -n "Name" -d "description"
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
++                          ooooo      ooo                                                                  ++
++                          `888b.     `8'                                                                  ++
++                           8 `88b.    8   .oooo.   ooo. .oo.  .oo.    .ooooo.                             ++
++                           8   `88b.  8  `P  )88b  `888P"Y88bP"Y88b  d88' `88b                            ++
++                           8     `88b.8   .oP"888   888   888   888  888ooo888                            ++
++                           8       `888  d8(  888   888   888   888  888    .o                            ++
++                          o8o        `8  `Y888""8o o888o o888o o888o `Y8bod8P'                            ++
++                                                                                                          ++
++                                                                                                          ++
++                                                                                                          ++
++        .o8                                         o8o                 .    o8o                          ++
++       "888                                         `"'               .o8    `"'                          ++
++   .oooo888   .ooooo.   .oooo.o  .ooooo.  oooo d8b oooo  oo.ooooo.  .o888oo oooo   .ooooo.  ooo. .oo.     ++
++  d88' `888  d88' `88b d88(  "8 d88' `"Y8 `888""8P `888   888' `88b   888   `888  d88' `88b `888P"Y88b    ++
++  888   888  888ooo888 `"Y88b.  888        888      888   888   888   888    888  888   888  888   888    ++
++  888   888  888    .o o.  )88b 888   .o8  888      888   888   888   888 .  888  888   888  888   888    ++
++  `Y8bod88P" `Y8bod8P' 8""888P' `Y8bod8P' d888b    o888o  888bod8P'   "888" o888o `Y8bod8P' o888o o888o   ++
++                                                          888                                             ++
++                                                         o888o                                            ++
++                                                                                                          ++
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

```