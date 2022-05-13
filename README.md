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
java -jar build/libs/BannerCreator-2.0-SNAPSHOT.jar
```

---

### Example

```bash
> java -jar build/libs/BannerCreator-2.0-SNAPSHOT.jar -h
Usage: Banner Creator options_list
Options: 
    --print, -p [false] -> print banner to console 
    --help, -h -> Usage info 
```

```
> java -jar build/libs/BannerCreator-1.0-SNAPSHOT.jar -p
Enter project name: Name
Enter short description: description
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